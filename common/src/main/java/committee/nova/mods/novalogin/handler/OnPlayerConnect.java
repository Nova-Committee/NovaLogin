package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;

import static committee.nova.mods.novalogin.Const.playerCacheMap;

/**
 * OnPlayerConnect
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 下午12:56
 */
public class OnPlayerConnect {
    public static boolean listen(ServerPlayer player) {
        String name = player.getGameProfile().getName();
        User user = new User();
        user.setName(name);
        user.setLastIp(player.getIpAddress());
        if (OnPlayerPremium.canPremium(player)) {
            user.setAuth(true);
            player.sendSystemMessage(Component.translatable("info.novalogin.premium"), false);
            playerCacheMap.put(name, user);
            return false;
        }

        if (playerCacheMap.containsKey(name)) {
            user = playerCacheMap.get(name);
        }
        playerCacheMap.put(name, user);

        if (OnPlayerReLogin.canReLogin(player)) {
            player.sendSystemMessage(Component.translatable("info.novalogin.re_login"), false);
            return false;
        }
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(false);
        player.setInvulnerable(true);
        player.sendSystemMessage(Component.translatable("info.novalogin.welcome"), false);
        player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("info.novalogin.verify")));
        return true;
    }
}
