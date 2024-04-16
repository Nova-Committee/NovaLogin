package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

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
    public static void listen(EntityPlayerMP player) {
        String name = player.getGameProfile().getName();
        User user = new User();
        user.setName(name);
        user.setLastIp(player.getPlayerIP());
        if (OnPlayerPremium.canPremium(player)) {
            user.setAuth(true);
            player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.premium"), ChatType.SYSTEM));
            playerCacheMap.put(name, user);
            return;
        }

        if (playerCacheMap.containsKey(name)) {
            user = playerCacheMap.get(name);
        }
        playerCacheMap.put(name, user);

        if (OnPlayerReLogin.canReLogin(player)) {
            player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.re_login"), ChatType.SYSTEM));
            return;
        }
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(false);
        player.setEntityInvulnerable(true);
        player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.welcome"), ChatType.SYSTEM));
        player.connection.sendPacket(new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentTranslation("info.novalogin.verify")));
    }
}
