package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import committee.nova.mods.novalogin.utils.YggdrasilUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;

import static committee.nova.mods.novalogin.Const.playerStorageMap;

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
        if (LoginUsers.INSTANCE.containsKey(name) && LoginUsers.INSTANCE.get(player).login) {
            player.connection.disconnect(Component.translatable("info.novalogin.is_in"));
            return false;
        }
        User user = new User();
        user.setName(name);
        user.setLastIp(player.getIpAddress());
        if (OnPlayerPremium.canPremium(player)) {
            user.setPremium(true);
            player.sendSystemMessage(Component.translatable("info.novalogin.premium"), false);
            playerStorageMap.put(name, user);
            return false;
        }

        if (OnPlayerPremium.canYggdrasil(player)){
            user.setYggdrasil(true);
            player.sendSystemMessage(Component.translatable("info.novalogin.yggdrasil", YggdrasilUtils.getOtherName()), false);
            playerStorageMap.put(name, user);
            return false;
        }


        if (playerStorageMap.containsKey(name)) {
            user = playerStorageMap.get(name);
        }
        playerStorageMap.put(name, user);

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
