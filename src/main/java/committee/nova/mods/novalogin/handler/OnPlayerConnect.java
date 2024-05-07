package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.models.User;
import committee.nova.mods.novalogin.utils.YggdrasilUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentTranslation;

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
    public static boolean listen(EntityPlayerMP player) {
        String name = player.getGameProfile().getName();
        if (LoginUsers.INSTANCE.containsKey(name) && LoginUsers.INSTANCE.get(player).login) {
            player.connection.disconnect(new TextComponentTranslation("info.novalogin.is_in"));
            return false;
        }
        User user = new User();
        user.setName(name);
        user.setLastIp(player.getPlayerIP());
        if (OnPlayerPremium.canPremium(player)) {
            user.setPremium(true);
            player.sendMessage(new TextComponentTranslation("info.novalogin.premium"));
            playerCacheMap.put(name, user);
            return false;
        }

        if (OnPlayerPremium.canYggdrasil(player)){
            user.setYggdrasil(true);
            player.sendMessage(new TextComponentTranslation("info.novalogin.yggdrasil", YggdrasilUtils.getOtherName()));
            playerCacheMap.put(name, user);
            return false;
        }

        if (playerCacheMap.containsKey(name)) {
            user = playerCacheMap.get(name);
        }
        playerCacheMap.put(name, user);

        if (OnPlayerReLogin.canReLogin(player)) {
            player.sendMessage(new TextComponentTranslation("info.novalogin.re_login"));
            return false;
        }
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(false);
        player.setEntityInvulnerable(true);
        player.sendMessage(new TextComponentTranslation("info.novalogin.welcome"));
        player.connection.sendPacket(new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentTranslation("info.novalogin.verify")));
        return true;
    }
}
