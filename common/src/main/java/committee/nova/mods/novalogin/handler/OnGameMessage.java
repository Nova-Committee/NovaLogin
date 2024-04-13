package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerPlayer;

import static committee.nova.mods.novalogin.Const.mojangAccountNamesCache;

/**
 * OnGameMessage
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:39
 */
public class OnGameMessage {
    public static boolean canSendMessage(ServerPlayer player, ServerboundChatPacket packet) {
        if (OnPlayerPremium.canPremium(player)) return true;
        if (OnPlayerReLogin.canReLogin(player)) return true;
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        String message = packet.getMessage();
        if (!playerLogin.isLogin && (message.startsWith("/login") || message.startsWith("/register"))) {
            return true;
        }
        return playerLogin.isLogin;
    }
}
