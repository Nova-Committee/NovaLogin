package committee.nova.mods.novalogin.mixins;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import committee.nova.mods.novalogin.models.MojangResponse;
import committee.nova.mods.novalogin.utils.HttpUtils;
import committee.nova.mods.novalogin.utils.YggdrasilUtils;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static committee.nova.mods.novalogin.Const.*;

/**
 * ServerLoginPktMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
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
    public ServerLoginPacketListenerImpl.State state;

    @Shadow @Nullable
    public GameProfile gameProfile;

    @Shadow @Final
    Connection connection;

    @Shadow public abstract void disconnect(Component $$0);

    @Shadow @Final private byte[] challenge;

    @ModifyConstant(
            method = "tick",
            constant = @Constant(intValue = 600)
    )
    public int novalogin$tick(int constant){
        return configHandler.config.getCommon().getOutTime();
    }


    @Inject(
            method = "handleHello",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void novalogin$handleHello(ServerboundHelloPacket pPacket, CallbackInfo ci) {
        Validate.validState(this.state == ServerLoginPacketListenerImpl.State.HELLO, "Unexpected hello packet");
        String playerName = pPacket.name();
        Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9_]{3,20}$");
        if (!pattern.matcher(playerName).matches()){
            disconnect(Component.translatable("info.novalogin.disconnect.invalid_name"));
        }
        GameProfile profile = this.server.getSingleplayerProfile();
        if (profile != null && pPacket.name().equalsIgnoreCase(profile.getName())) {
            this.gameProfile = profile;
            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        } else {
            this.gameProfile = new GameProfile(null, pPacket.name());
            if (this.server.usesAuthentication() && !this.connection.isMemoryConnection()) {
                this.state = ServerLoginPacketListenerImpl.State.KEY;
                this.connection.send(new ClientboundHelloPacket("", this.server.getKeyPair().getPublic().getEncoded(), this.challenge));
            } else {
                if (configHandler.config.getCommon().isUuidTrans()){
                    novaLogin$useOnlineProfile(playerName);
                } else {
                    novaLogin$useOfflineProfile(playerName);
                }
            }
        }
        ci.cancel();
    }


    @Unique
    private void novaLogin$useOfflineProfile(String playerName) {
        LOGGER.info("Username '{}' tried to join with an offline UUID", playerName);
        this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        this.gameProfile = novaLogin$createOfflineProfile(playerName);
    }

    @Unique
    private void novaLogin$useOnlineProfile(String playerName) {
        GameProfile gameProfile = novaLogin$createOfflineProfile(playerName);
        String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
        try {
            HttpURLConnection  con = HttpUtils.connect(url, 5000, null);
            int  code = con.getResponseCode();
            String  msg = HttpUtils.getResponseMsg(con);
            con.disconnect();
            if (code == HttpURLConnection.HTTP_OK) {
                var re = GSON.fromJson(JsonParser.parseString(msg), MojangResponse.class);
                StringBuilder uuid = new StringBuilder(re.getId());
                uuid.insert(20,"-");
                uuid.insert(16,"-");
                uuid.insert(12,"-");
                uuid.insert(8,"-");


                LOGGER.info("Player {} has a Mojang account, use online UUID {}", playerName, uuid);

                gameProfile = new GameProfile(UUID.fromString(uuid.toString()), playerName);
            }
            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            this.gameProfile = gameProfile;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
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

        final String session;
        try {
            PrivateKey privateKey = this.server.getKeyPair().getPrivate();
            if (!packet.isChallengeValid(this.challenge, privateKey)) {
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

                try {
                    GameProfile minecraftAuth = YggdrasilUtils
                            .getMinecraftSessionService()
                            .hasJoinedServer(new GameProfile(null, playerName), session, this.getAddress());
                    GameProfile yggdrasilAuth = YggdrasilUtils
                            .getOtherSessionService()
                            .hasJoinedServer(new GameProfile(null, playerName), session, this.getAddress());
                    if (minecraftAuth != null) {
                        gameProfile = minecraftAuth;
                        LOGGER
                                .info(
                                        "UUID of player {} is {}",
                                        gameProfile.getName(),
                                        gameProfile.getId()
                                );
                        mojangAccountNamesCache.add(gameProfile.getName());
                        state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                    } else if (YggdrasilUtils.isOtherEnable() && yggdrasilAuth != null) {
                        gameProfile = yggdrasilAuth;
                        LOGGER
                                .info("Other Auths, UUID of player {} is {}", gameProfile.getName(), gameProfile.getId());
                        yggdrasilNamesCache.add(gameProfile.getName());
                        state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                    } else {
                        if (configHandler.config.getCommon().isUuidTrans()){
                            novaLogin$useOnlineProfile(playerName);
                        } else {
                            novaLogin$useOfflineProfile(playerName);
                        }
                    }
                } catch (AuthenticationUnavailableException e) {
                    if (configHandler.config.getCommon().isUuidTrans()){
                        novaLogin$useOnlineProfile(playerName);
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
