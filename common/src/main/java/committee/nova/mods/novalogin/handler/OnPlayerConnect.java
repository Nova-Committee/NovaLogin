package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

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
    public static void listen(ServerPlayer player) {
        String name = player.getGameProfile().getName();
        User user = new User();
        user.setName(name);
        user.setLastIp(player.getIpAddress());
        if (OnPlayerPremium.canPremium(player)) {
            user.setAuth(true);
            player.sendMessage(new TranslatableComponent("info.novalogin.premium"), ChatType.SYSTEM, Util.NIL_UUID);
            playerCacheMap.put(name, user);
            return;
        }

        if (playerCacheMap.containsKey(name)) {
            user = playerCacheMap.get(name);
        }
        playerCacheMap.put(name, user);

        if (OnPlayerReLogin.canReLogin(player)) {
            player.sendMessage(new TranslatableComponent("info.novalogin.re_login"), ChatType.SYSTEM, Util.NIL_UUID);
            return;
        }
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(false);
        player.setInvulnerable(true);
        player.sendMessage(new TranslatableComponent("info.novalogin.welcome"), ChatType.SYSTEM, Util.NIL_UUID);
        player.connection.send(new ClientboundSetTitleTextPacket(new TranslatableComponent("info.novalogin.verify")));
    }
}
