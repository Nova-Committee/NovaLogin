package committee.nova.mods.novalogin.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * IEvents
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:47
 */
public final class IEvents {

    public static final Event<PlayerInteract> PLAYER_INTERACT = EventFactory.createArrayBacked(PlayerInteract.class, callbacks -> ((player, hand, target) -> {
        for(PlayerInteract e : callbacks) {
            InteractionResult result = e.onEntityInteract(player, hand, target);
            if(result != null)
                return result;
        }
        return null;
    }));

    public static final Event<LivingUseItemFinish> LIVING_USE_ITEM_FINISH = EventFactory.createArrayBacked(LivingUseItemFinish.class, callbacks -> (entity, item, duration, result) -> {
        for(LivingUseItemFinish e : callbacks) {
            ItemStack itemStack = e.onUseItem(entity, item, duration, result);
            if(itemStack != null)
                return itemStack;
        }
        return null;
    });

    public static final Event<PlayerLoggedIn> PLAYER_LOGGED_IN = EventFactory.createArrayBacked(PlayerLoggedIn.class, callbacks -> (world, player) -> {
        for (PlayerLoggedIn callback : callbacks) {
            callback.onPlayerLoggedIn(world, player);
        }
    });

    public static final Event<PlayerLoggedOut> PLAYER_LOGGED_OUT = EventFactory.createArrayBacked(PlayerLoggedOut.class, callbacks -> (world, player) -> {
        for (PlayerLoggedOut callback : callbacks) {
            callback.onPlayerLoggedOut(world, player);
        }
    });

    public static final Event<PlayerOpenMenu> PLAYER_OPEN_MENU = EventFactory.createArrayBacked(PlayerOpenMenu.class, callbacks -> (player, menu) -> {
        for (PlayerOpenMenu callback : callbacks) {
            return callback.onOpenMenu(player, menu);
        }
        return false;
    });

    @FunctionalInterface
    public interface PlayerLoggedIn {
        void onPlayerLoggedIn(Level world, ServerPlayer player);
    }

    @FunctionalInterface
    public interface PlayerLoggedOut {
        void onPlayerLoggedOut(Level world, ServerPlayer player);
    }

    @FunctionalInterface
    public interface LivingUseItemFinish {
        ItemStack onUseItem(LivingEntity entity, @NotNull ItemStack item, int duration, @NotNull ItemStack result);
    }

    @FunctionalInterface
    public interface PlayerInteract {
        InteractionResult onEntityInteract(Player player, InteractionHand hand, Entity target);
    }

    @FunctionalInterface
    public interface PlayerOpenMenu {
        boolean onOpenMenu(ServerPlayer player, AbstractContainerMenu container);
    }

}
