package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.server.level.ServerPlayer;

/**
 * OnPlayerMove
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:35
 */
public class OnPlayerMove {
    public static boolean canMove(ServerPlayer player) {
        if (OnPlayerReLogin.canReLogin(player)) return true;
        if (OnPlayerPremium.canYggdrasil(player)) return true;
        if (OnPlayerPremium.canPremium(player)) return true;
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        boolean isLoggedIn = playerLogin.login;
        if (!isLoggedIn) {
            player.teleportTo(player.getX(), player.getY(), player.getZ()); // 同步
        }
        return isLoggedIn;
    }
}
