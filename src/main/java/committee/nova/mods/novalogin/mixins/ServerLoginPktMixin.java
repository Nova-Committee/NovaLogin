package committee.nova.mods.novalogin.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static committee.nova.mods.novalogin.Const.LOGGER;
import static committee.nova.mods.novalogin.Const.mojangAccountNamesCache;

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

    @Shadow
    @Nullable
    public GameProfile authenticatedProfile;
    @Shadow
    @Final
    MinecraftServer server;
    @Shadow
    private volatile ServerLoginPacketListenerImpl.State state;

    @Shadow
    abstract void startClientVerification(GameProfile gameProfile);

    @Inject(
            method = "handleHello",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;usesAuthentication()Z",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void novalogin$handleHello(ServerboundHelloPacket pPacket, CallbackInfo ci) {
        try {
            String playerName = pPacket.name();
            Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]{6,16}$");//包含中文，至少6字符，最多16字符
            Matcher matcher = pattern.matcher(playerName);
            if (server.usesAuthentication()) {

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setConnectTimeout(5000);
                httpsURLConnection.setReadTimeout(5000);
                int response = httpsURLConnection.getResponseCode();//校验该用户名是否已经拥有正版
                if (response == HttpURLConnection.HTTP_OK) {
                    // 玩家有正版
                    httpsURLConnection.disconnect();
                    LOGGER.info("Player {} has a Mojang account", playerName);

                    // 缓存
                    mojangAccountNamesCache.add(playerName);
                } else if (response == HttpURLConnection.HTTP_NO_CONTENT || response == HttpURLConnection.HTTP_NOT_FOUND) {
                    // 玩家没有正版
                    httpsURLConnection.disconnect();
                    LOGGER.info("Player {} doesn't have a Mojang account, cached as offline player", playerName);
                    this.startClientVerification(UUIDUtil.createOfflineProfile(playerName));
                    ci.cancel();
                }

            } else {
                this.startClientVerification(UUIDUtil.createOfflineProfile(playerName));
                ci.cancel();
            }
        } catch (Exception e) {
            LOGGER.error("check error");
        }
    }


}
