package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
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
    public static void listen(ServerPlayer player) {
        String name = player.getGameProfile().getName();
        User user = new User();
        user.setName(name);
        user.setLastIp(player.getIpAddress());
        if (Const.mojangAccountNamesCache.contains(name)) {
            user.setAuth(true);
            playerCacheMap.put(name, user);
            return;
        }
        playerCacheMap.put(name, user);
        if (OnPlayerReLogin.canReLogin(player)) {
            player.sendSystemMessage(Component.literal("§9您已经在短时间内登陆过，为您跳过验证"), false);
            return;
        }
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(false);
        player.setInvulnerable(true);
        player.sendSystemMessage(Component.literal("§9欢迎来到服务器，请先登录。\n§e请使用 /login 登录或者使用 /register 注册"), false);
        player.connection.send(new ClientboundSetTitleTextPacket(Component.literal("§a验证你的身份!")));
    }
}
