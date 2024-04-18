package committee.nova.mods.novalogin.mixins;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import committee.nova.mods.novalogin.models.MojangResponse;
import committee.nova.mods.novalogin.utils.HttpUtils;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static committee.nova.mods.novalogin.Const.*;

/**
 * ServerLoginPktMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description from EasyAuth
 * @date 2024/3/19 13:02
 */
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPktMixin {

    @Final
    @Shadow
    private static AtomicInteger UNIQUE_THREAD_ID;

    @Shadow
    @Final
    MinecraftServer server;

    @Shadow
    ServerLoginPacketListenerImpl.State state;

    @Shadow @Nullable
    GameProfile gameProfile;

    @Shadow @Final public Connection connection;

    @Shadow @Final private byte[] nonce;

    @Shadow public abstract void disconnect(Component $$0);

    @ModifyConstant(
            method = "tick",
            constant = @Constant(intValue = 600)
    )
    public int novalogin$tick(int constant){
        return CONFIG.config.getOutTime();
    }


    @Inject(
            method = "handleHello",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;getGameProfile()Lcom/mojang/authlib/GameProfile;",
                    shift = At.Shift.BEFORE
            )
    )
    public void novalogin$handleHello(ServerboundHelloPacket pPacket, CallbackInfo ci) {
        this.gameProfile = pPacket.getGameProfile();
        String playerName = pPacket.getGameProfile().getName();
        Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]{6,25}$");
        Validate.validState(!pattern.matcher(playerName).matches(), "Invalid characters in username");
    }


    @Unique
    private void novaLogin$useOfflineProfile(String playerName) {
        this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        this.gameProfile = novaLogin$createOfflineProfile(playerName);
        LOGGER.info("Username '{}' tried to join with an offline UUID", playerName);
    }

    @Unique
    private void novaLogin$useOnlineProfile(int code, String msg, String playerName) {
        GameProfile gameProfile = novaLogin$createOfflineProfile(playerName);
        if (code == HttpURLConnection.HTTP_OK) {
            var re = GSON.fromJson(JsonParser.parseString(msg), MojangResponse.class);
            StringBuilder uuid = new StringBuilder(re.getId());
            uuid.insert(8,"-");
            uuid.insert(12,"-");
            uuid.insert(16,"-");
            uuid.insert(20,"-");

            LOGGER.info("Player {} has a Mojang account, use online UUID {}", playerName, uuid);

            gameProfile = new GameProfile(UUID.fromString(uuid.toString()), playerName);
        }
        this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        this.gameProfile = gameProfile;
    }

    @Unique
    protected GameProfile novaLogin$createFakeProfile(GameProfile gameProfile) {
        return novaLogin$createOfflineProfile(gameProfile.getName());
    }

    @Unique
    protected GameProfile novaLogin$createOfflineProfile(String name) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        return new GameProfile(uuid, name);
    }

    @Inject(
            method = "handleKey",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void novalogin$handleKey(ServerboundKeyPacket packet, CallbackInfo ci) {
        Validate.validState(this.state == ServerLoginPacketListenerImpl.State.KEY, "Unexpected key packet");
        PrivateKey privateKey = this.server.getKeyPair().getPrivate();

        final String session;
        try {
            if (!Arrays.equals(this.nonce, packet.getNonce(privateKey))) {
                throw new IllegalStateException("Protocol error");
            }

            SecretKey secretKey = packet.getSecretKey(privateKey);
            Cipher cipher = Crypt.getCipher(2, secretKey);
            Cipher cipher1 = Crypt.getCipher(1, secretKey);
            session = new BigInteger(Crypt.digestData("", this.server.getKeyPair().getPublic(), secretKey)).toString(16);
            this.state = ServerLoginPacketListenerImpl.State.AUTHENTICATING;
            this.connection.setEncryptionKey(cipher, cipher1);
        } catch (CryptException var7) {
            throw new IllegalStateException("Protocol error", var7);
        }

        Thread thread = new Thread("Nova User Authenticator #" + UNIQUE_THREAD_ID.incrementAndGet()) {
            @Override
            public void run() {
                assert gameProfile != null;
                String playerName = gameProfile.getName();
                UUID id = gameProfile.getId();

                String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

                HttpURLConnection con = null;
                int code = 0;
                String msg = "";
                try {
                    con = HttpUtils.connect(url, 5000, null);
                    code = con.getResponseCode();
                    msg = HttpUtils.getResponseMsg(con);
                    con.disconnect();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }

                try {
                    gameProfile = server
                            .getSessionService()
                            .hasJoinedServer(new GameProfile(null, playerName), session, this.getAddress());
                    if (gameProfile != null) {
                        LOGGER
                                .info(
                                        "UUID of player {} is {}",
                                        playerName,
                                        id
                                );
                        mojangAccountNamesCache.add(playerName);
                        state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                    } else if (server.isSingleplayer()){
                        if (CONFIG.config.isUuidTrans()){
                            novaLogin$useOnlineProfile(code, msg, playerName);
                        } else {
                            novaLogin$useOfflineProfile(playerName);
                        }
                    } else {
                        disconnect(new TranslatableComponent("multiplayer.disconnect.unverified_username"));
                        LOGGER.error("Username '{}' tried to join with an invalid session", playerName);
                    }
                } catch (AuthenticationUnavailableException e) {
                    if (CONFIG.config.isUuidTrans()){
                        novaLogin$useOnlineProfile(code, msg, playerName);
                    } else {
                        novaLogin$useOfflineProfile(playerName);
                    }
                }
            }

            @Nullable
            private InetAddress getAddress() {
                SocketAddress remoteAddress = connection.getRemoteAddress();
                return server.getPreventProxyConnections() && remoteAddress instanceof InetSocketAddress inetSocketAddress
                        ? inetSocketAddress.getAddress()
                        : null;
            }
        };
        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        thread.start();
        ci.cancel();
    }

}
