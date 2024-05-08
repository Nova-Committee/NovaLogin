package committee.nova.mods.novalogin.mixins;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import committee.nova.mods.novalogin.models.MojangResponse;
import committee.nova.mods.novalogin.utils.HttpUtils;
import committee.nova.mods.novalogin.utils.YggdrasilUtils;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.CryptManager;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
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

import static committee.nova.mods.novalogin.Const.*;

/**
 * ServerLoginPktMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description from EasyAuth
 * @date 2024/3/19 13:02
 */
@Mixin(NetHandlerLoginServer.class)
public abstract class ServerLoginPktMixin {
    @Final
    @Shadow
    private static AtomicInteger AUTHENTICATOR_THREAD_ID;

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow private NetHandlerLoginServer.LoginState currentLoginState;

    @Shadow private GameProfile loginGameProfile;

    @Shadow @Final public NetworkManager networkManager;

    @Shadow @Final private byte[] verifyToken;

    @Shadow private SecretKey secretKey;

    @Inject(
            method = "processLoginStart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;isServerInOnlineMode()Z",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void novalogin$handleHello(CPacketLoginStart packetIn, CallbackInfo ci) {
        Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.HELLO, "Unexpected hello packet");
        this.loginGameProfile = packetIn.getProfile();
        String playerName = packetIn.getProfile().getName();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = NetHandlerLoginServer.LoginState.KEY;
            this.networkManager.sendPacket(new SPacketEncryptionRequest("", this.server.getKeyPair().getPublic(), this.verifyToken));
        } else {
            if (configHandler.config.getCommon().isUuidTrans()){
                novaLogin$useOnlineProfile(playerName);
            } else {
                novaLogin$useOfflineProfile(playerName);
            }
        }
        ci.cancel();
    }

    @Unique
    private void novaLogin$useOfflineProfile(String playerName) {
        LOGGER.info("Username '{}' tried to join with an offline UUID", playerName);
        this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
        this.loginGameProfile = novaLogin$createOfflineProfile(playerName);
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
                MojangResponse re = GSON.fromJson(new JsonParser().parse(msg), MojangResponse.class);
                StringBuilder uuid = new StringBuilder(re.getId());
                uuid.insert(8,"-");
                uuid.insert(12,"-");
                uuid.insert(16,"-");
                uuid.insert(20,"-");

                LOGGER.info("Player {} has a Mojang account, use online UUID {}", playerName, uuid);

                gameProfile = new GameProfile(UUID.fromString(uuid.toString()), playerName);
            }
            this.currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
            this.loginGameProfile = gameProfile;
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
            method = "processEncryptionResponse",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void novalogin$handleKey(CPacketEncryptionResponse packet, CallbackInfo ci) {
        Validate.validState(this.currentLoginState == NetHandlerLoginServer.LoginState.KEY, "Unexpected key packet");
        PrivateKey privatekey = this.server.getKeyPair().getPrivate();

        if (!Arrays.equals(verifyToken, packet.getVerifyToken(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        } else {
            this.secretKey = packet.getSecretKey(privatekey);
            this.currentLoginState = NetHandlerLoginServer.LoginState.AUTHENTICATING;
            this.networkManager.enableEncryption(this.secretKey);
            (new Thread(SidedThreadGroups.SERVER, "Nova User Authenticator #" + AUTHENTICATOR_THREAD_ID.incrementAndGet()) {
                public void run() {
                    GameProfile gameprofile = loginGameProfile;
                    String playerName = gameprofile.getName();

                    try {
                        String session = (new BigInteger(CryptManager.getServerIdHash("", server.getKeyPair().getPublic(), secretKey))).toString(16);
                        GameProfile minecraftAuth = server
                                .getMinecraftSessionService()
                                .hasJoinedServer(new GameProfile(null, playerName), session, this.getAddress());
                        GameProfile yggdrasilAuth = YggdrasilUtils
                                .getOtherSessionService()
                                .hasJoinedServer(new GameProfile(null, playerName), session, this.getAddress());
                        if (minecraftAuth != null) {
                            loginGameProfile = minecraftAuth;
                            LOGGER.info("UUID of player {} is {}", minecraftAuth.getName(), minecraftAuth.getId());
                            mojangAccountNamesCache.add(minecraftAuth.getName());
                            currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        } else if (YggdrasilUtils.isOtherEnable() && yggdrasilAuth != null) {
                            loginGameProfile = yggdrasilAuth;
                            LOGGER
                                    .info("Other Auths, UUID of player {} is {}", yggdrasilAuth.getName(), yggdrasilAuth.getId());
                            yggdrasilNamesCache.add(yggdrasilAuth.getName());
                            currentLoginState = NetHandlerLoginServer.LoginState.READY_TO_ACCEPT;
                        } else {
                            if (configHandler.config.getCommon().isUuidTrans()){
                                novaLogin$useOnlineProfile(playerName);
                            } else {
                                novaLogin$useOfflineProfile(playerName);
                            }
                        }
                    } catch (AuthenticationUnavailableException var3) {
                        if (configHandler.config.getCommon().isUuidTrans()){
                            novaLogin$useOnlineProfile(playerName);
                        } else {
                            novaLogin$useOfflineProfile(playerName);
                        }
                    }

                }

                @Nullable
                private InetAddress getAddress() {
                    SocketAddress socketaddress = networkManager.getRemoteAddress();
                    return server.getPreventProxyConnections() && socketaddress instanceof InetSocketAddress ? ((InetSocketAddress)socketaddress).getAddress() : null;
                }
            }).start();
            ci.cancel();
        }
    }
}
