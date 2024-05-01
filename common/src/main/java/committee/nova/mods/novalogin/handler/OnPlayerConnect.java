package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
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
            user.setAuth(true);
            player.sendSystemMessage(Component.translatable("info.novalogin.premium"), false);
            playerStorageMap.put(name, user);
            return false;
        }

        if (OnPlayerPremium.canYggdrasil(player)){
            user.setYggdrasil(true);
            player.sendMessage(new TranslatableComponent("info.novalogin.yggdrasil", YggdrasilUtils.getOtherName()), ChatType.SYSTEM, Util.NIL_UUID);
            playerCacheMap.put(name, user);
            return false;
        }

        if (playerCacheMap.containsKey(name)) {
            user = playerCacheMap.get(name);
        }
        playerCacheMap.put(name, user);

        if (OnPlayerReLogin.canReLogin(player)) {
            player.sendMessage(new TranslatableComponent("info.novalogin.re_login"), ChatType.SYSTEM, Util.NIL_UUID);
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
