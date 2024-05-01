package committee.nova.mods.novalogin.mixins;

import com.mojang.authlib.GameProfile;
import committee.nova.mods.novalogin.events.callbacks.IEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

/**
 * ServerPlayerMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/19 下午7:15
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {


    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "openMenu",
            at = @At(
                    value = "RETURN",
                    opcode = 2
            ),
            cancellable = true
    )
    public void novalogin$openMenu(MenuProvider menuProvider, CallbackInfoReturnable<OptionalInt> cir) {
        if (!IEvents.PLAYER_OPEN_MENU.invoker().onOpenMenu((ServerPlayer) (Object) this, this.containerMenu)){
            cir.setReturnValue(OptionalInt.empty());
        }
    }
}
