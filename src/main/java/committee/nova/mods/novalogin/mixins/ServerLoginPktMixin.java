package committee.nova.mods.novalogin.mixins;

import com.google.gson.reflect.TypeToken;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.NovaLogin;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    @Shadow String requestedUsername;
    @Shadow @Final MinecraftServer server;
    @Shadow @Final static Logger LOGGER = LogUtils.getLogger();
    @Unique String novalogin$s;
    @Shadow @Final Connection connection;
    @Shadow private volatile ServerLoginPacketListenerImpl.State state;

    @Shadow void startClientVerification(GameProfile pAuthenticatedProfile){}
    @Shadow public void disconnect(Component pReason){}


    @Shadow @Nullable public GameProfile authenticatedProfile;

    @Inject(
            method = "handleHello",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;usesAuthentication()Z")
            ,
            cancellable = true
    )
    public void novalogin$handleHello(ServerboundHelloPacket pPacket, CallbackInfo ci){
        try {
            String playerName = pPacket.name();

            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(5000);
            httpsURLConnection.setReadTimeout(5000);
            int response = httpsURLConnection.getResponseCode();

            //在连接服务器是发登录包，检测玩家登陆账号类型，如果是正版则从服务器注册库中删除，如果不是正版登录则执行注册检测


            //此处是检测客户端不是正版登录但是客户端输入的离线用户名可能已经注册过正版
            if (NovaLogin.SAVE.isReg(playerName)){//如果玩家在服务器已经注册过则开始检测玩家名是否同时拥有正版
                state = ServerLoginPacketListenerImpl.State.VERIFYING;//至已验证服务器正版开启状态
                if (response == HttpURLConnection.HTTP_OK) {//通过mojang的api验证该玩家是否有正版账号，如果有则使用正版uuid
                    Const.mojangAccountNamesCache.add(playerName);//添加正版玩家缓存
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder sbf = new StringBuilder();
                    String temp;
                    while ((temp = br.readLine()) != null) {
                        sbf.append(temp);
                        sbf.append("\r\n");
                    }
                    authenticatedProfile = new GameProfile(Const.GSON.fromJson(sbf.toString(), new TypeToken<>(){}), playerName);
                } else if (response == HttpURLConnection.HTTP_NO_CONTENT || response == HttpURLConnection.HTTP_NOT_FOUND) {//没有正版账号则使用离线uuid生成
                    authenticatedProfile = UUIDUtil.createOfflineProfile(playerName);
                }
                ci.cancel();
            } //没注册过则按照原版验证逻辑继续执行handleKey
        } catch (IOException ignored){

        }
    }


    @Inject(method = "handleKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;setEncryptionKey(Ljavax/crypto/Cipher;Ljavax/crypto/Cipher;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public void novalogin$handleKey1(ServerboundKeyPacket pPacket, CallbackInfo ci, @Local String arg2){
        this.novalogin$s = arg2;//获取连接密钥
        //Const.LOGGER.info(arg2);
    }

}
