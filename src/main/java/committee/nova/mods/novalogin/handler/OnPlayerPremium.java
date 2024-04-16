package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * OnPlayerPremium
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 下午9:14
 */
public class OnPlayerPremium {
    public static boolean canPremium(EntityPlayerMP player) {
        return Const.mojangAccountNamesCache.contains(player.getGameProfile().getName());
    }
}
