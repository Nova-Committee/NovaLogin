package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * OnPlayerAction
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:39
 */
public class OnPlayerAction {
    public static boolean canInteract(ServerGamePacketListenerImpl networkHandler) {
        ServerPlayer player = networkHandler.player;
        if (Const.mojangAccountNamesCache.contains(player.getGameProfile().getName())) return true;
        if (OnPlayerReLogin.canReLogin(player)) return true;
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        return playerLogin.isLogin;
    }
}
