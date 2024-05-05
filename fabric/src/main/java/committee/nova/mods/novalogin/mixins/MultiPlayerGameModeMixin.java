package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.events.callbacks.IEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MultiPlayerGameModeMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/5 下午4:12
 */
@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    @Shadow private GameType localPlayerMode;

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;createInteractionPacket(Lnet/minecraft/world/entity/Entity;ZLnet/minecraft/world/InteractionHand;)Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;"
            )
            ,
            cancellable = true)
    public void port_lib$onEntityInteract(Player player, Entity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.localPlayerMode != GameType.SPECTATOR) { // don't fire for spectators to match non-specific EntityInteract
            InteractionResult cancelResult = IEvents.PLAYER_INTERACT.invoker().onEntityInteract(player, hand, target);
            if (cancelResult != null) cir.setReturnValue(cancelResult);
        }
    }
}
