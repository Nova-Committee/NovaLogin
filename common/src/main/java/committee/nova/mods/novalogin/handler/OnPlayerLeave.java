package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.server.level.ServerPlayer;

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
        if (OnPlayerPremium.canPremium(player)) user.setAuth(true);
        user.setLastIp(player.getIpAddress());
        user.setLastLeaveTime(System.currentTimeMillis());

        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(false);
    }
}
