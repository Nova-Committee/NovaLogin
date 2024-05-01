package committee.nova.mods.novalogin.utils;

import com.mojang.authlib.Environment;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import committee.nova.mods.novalogin.Const;

import java.net.Proxy;

/**
 * YggdrasilUtils
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/1 上午1:05
 */
public final class YggdrasilUtils {
    public static boolean isOtherEnable() {
        return Const.configHandler.config.getYggdrasil().isEnable();
    }

    public static String getOtherName(){
        return Const.configHandler.config.getYggdrasil().getApiName();
    }
    public static MinecraftSessionService getOtherSessionService() {
        return new YggdrasilAuthenticationService(Proxy.NO_PROXY,
                Environment.create(
                        Const.configHandler.config.getYggdrasil().getApiUrl() + "/authserver",
                        "https://api.mojang.com",
                        Const.configHandler.config.getYggdrasil().getApiUrl() + "/sessionserver",
                        "https://api.minecraftservices.com",
                        Const.configHandler.config.getYggdrasil().getApiName()
                )).createMinecraftSessionService();
    }

    public static MinecraftSessionService getMinecraftSessionService() {
        return new YggdrasilAuthenticationService(Proxy.NO_PROXY, Environment.create(
                "https://authserver.mojang.com",
                "https://api.mojang.com",
                "https://sessionserver.mojang.com",
                "https://api.minecraftservices.com",
                "Minecraft Official"
        )).createMinecraftSessionService();
    }
}
