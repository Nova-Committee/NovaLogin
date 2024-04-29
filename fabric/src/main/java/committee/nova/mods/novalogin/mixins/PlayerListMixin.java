package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.events.callbacks.IEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * PlayerListMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:49
 */
@Mixin(value = PlayerList.class, priority = 1001)
public abstract class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At(value = "TAIL"))
    public void novalogin$placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        IEvents.PLAYER_LOGGED_IN.invoker().onPlayerLoggedIn(serverPlayer.getCommandSenderWorld(), serverPlayer);
    }
    @Inject(method = "remove", at = @At(value = "HEAD"))
    public void novalogin$removePlayer(ServerPlayer player, CallbackInfo ci) {
        IEvents.PLAYER_LOGGED_OUT.invoker().onPlayerLoggedOut(player.getCommandSenderWorld(), player);
    }
}
