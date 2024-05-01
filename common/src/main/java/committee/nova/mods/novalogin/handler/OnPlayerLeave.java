package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.server.level.ServerPlayer;

import static committee.nova.mods.novalogin.Const.mojangAccountNamesCache;
import static committee.nova.mods.novalogin.Const.yggdrasilNamesCache;

/**
 * OnPlayerLeave
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 下午12:56
 */
public class OnPlayerLeave {
    public static void listen(ServerPlayer player) {
        String name = player.getGameProfile().getName();
        User user = Const.playerCacheMap.get(name);
        if (OnPlayerPremium.canYggdrasil(player)) {
            user.setYggdrasil(true);
            yggdrasilNamesCache.remove(name);
        }
        if (OnPlayerPremium.canPremium(player)) {
            user.setPremium(true);
            mojangAccountNamesCache.remove(name);
        }
        user.setLastIp(player.getIpAddress());
        user.setLastLeaveTime(System.currentTimeMillis());

        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        if (playerLogin.login) playerLogin.setReLogin(true);
        playerLogin.setLogin(false);
    }
}
