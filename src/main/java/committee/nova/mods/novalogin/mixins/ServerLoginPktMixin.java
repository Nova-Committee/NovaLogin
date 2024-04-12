package committee.nova.mods.novalogin.mixins;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.MojangResponse;
import committee.nova.mods.novalogin.utils.HttpUtils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.slf4j.Logger;
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
import java.util.UUID;
import java.util.regex.Matcher;
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

    @Shadow @Final private static Logger LOGGER;

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

                String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

//                var con = HttpUtils.connect(url, 5000, null);
//                int code = con.getResponseCode();//校验该用户名是否已经拥有正版
                //mojangAccountNamesCache.forEach(Const.LOGGER::info);

                if (mojangAccountNamesCache.contains(playerName)) {
                    LOGGER.info("Player {} is cached as online player. Authentication continues as vanilla", playerName);
                    mojangAccountNamesCache.add(playerName);
                    return;
                }
                if (((playerCacheMap.containsKey(playerName) && !playerCacheMap.get(playerName).isAuth))) {
                    LOGGER.info("Player {} is cached as offline player", playerName);
                    this.startClientVerification(UUIDUtil.createOfflineProfile(playerName));
                    ci.cancel();
                } else {
                    LOGGER.info("Player {} doesn't have a Mojang account, cached as offline player", playerName);
                    this.startClientVerification(UUIDUtil.createOfflineProfile(playerName));
                    ci.cancel();
//                    if (code == HttpURLConnection.HTTP_OK) {
//                        var re = GSON.fromJson(JsonParser.parseString(HttpUtils.getResponseMsg(con)), MojangResponse.class);
//                        // 玩家有正版
//                        con.disconnect();
//                        LOGGER.info("Player {} has a Mojang account", playerName);
//
//                        // 缓存
//                        mojangAccountNamesCache.add(playerName);
//                        //构造正版玩家信息
////                    this.startClientVerification(new GameProfile(UUID.fromString(re.getId()), playerName));
////                    ci.cancel();
//                    } else if (code == HttpURLConnection.HTTP_NO_CONTENT || code == HttpURLConnection.HTTP_NOT_FOUND) {
//                        // 玩家没有正版
//                        con.disconnect();
//                        LOGGER.info("Player {} doesn't have a Mojang account, cached as offline player", playerName);
//                        this.startClientVerification(UUIDUtil.createOfflineProfile(playerName));
//                        ci.cancel();
//                    }
                }

            } else {
                this.startClientVerification(UUIDUtil.createOfflineProfile(playerName));
                ci.cancel();
            }
        } catch (Exception e) {
            LOGGER.error("check error {}", e.getMessage());
        }
    }


}
