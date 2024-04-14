package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import net.minecraft.server.level.ServerPlayer;

/**
 * OnPlayerPremium
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 下午9:14
 */
public class OnPlayerPremium {
    public static boolean canPremium(ServerPlayer player) {
        return Const.mojangAccountNamesCache.contains(player.getGameProfile().getName());
    }
}
