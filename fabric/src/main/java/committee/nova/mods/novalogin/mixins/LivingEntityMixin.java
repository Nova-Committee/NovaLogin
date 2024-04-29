package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.events.callbacks.IEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * LivingEntityMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/19 下午7:23
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract ItemStack getUseItem();

    @Shadow public abstract int getUseItemRemainingTicks();

    public LivingEntityMixin(EntityType<?> variant, Level world) {
        super(variant, world);
    }

    @Inject(method = "completeUsingItem", at = @At(value = "INVOKE", shift = At.Shift.BY, by = 2, target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void novalogin$onFinishUsing(CallbackInfo ci, InteractionHand hand, ItemStack result) {
        IEvents.LIVING_USE_ITEM_FINISH.invoker().onUseItem((LivingEntity) (Object) this, this.getUseItem().copy(), getUseItemRemainingTicks(), result);
    }
}
