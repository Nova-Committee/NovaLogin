package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.server.level.ServerPlayer;

/**
 * OnGameMessage
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:39
 */
public class OnGameMessage {
    public static boolean canSendMessage(ServerPlayer player, String message) {
        if (OnPlayerReLogin.canReLogin(player)) return true;
        if (OnPlayerPremium.canYggdrasil(player)) return true;
        if (OnPlayerPremium.canPremium(player)) return true;
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        if (!playerLogin.login && (message.startsWith("/login") || message.startsWith("/register"))) {
            return true;
        }
        return playerLogin.login;
    }
}
