package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * OnPlayerLeave
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 下午12:56
 */
public class OnPlayerLeave {
    public static void listen(EntityPlayerMP player) {
        String name = player.getGameProfile().getName();
        User user = Const.playerCacheMap.get(name);
        if (OnPlayerPremium.canPremium(player)) user.setAuth(true);
        user.setLastIp(player.getPlayerIP());
        user.setLastLeaveTime(System.currentTimeMillis());

        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        if (playerLogin.login) playerLogin.setReLogin(true);
        playerLogin.setLogin(false);

        Const.mojangAccountNamesCache.remove(name);
    }
}
