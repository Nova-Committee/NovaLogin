package committee.nova.mods.novalogin.utils;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.utils.yggdrasil.YggdrasilAuthenticationService;

import java.net.Proxy;
import java.util.UUID;

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
                Const.configHandler.config.getYggdrasil().getApiUrl() + "/sessionserver/",
                Const.configHandler.config.getYggdrasil().getApiUrl() + "/authserver/",
                UUID.randomUUID().toString()
        ).createMinecraftSessionService();
    }

    public static MinecraftSessionService getMinecraftSessionService() {
        return new YggdrasilAuthenticationService(Proxy.NO_PROXY,
                "https://authserver.mojang.com",
                "https://sessionserver.mojang.com",
                UUID.randomUUID().toString()
        ).createMinecraftSessionService();
    }
}
