package committee.nova.mods.novalogin.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/**
 * IEvents
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:47
 */
public final class IEvents {


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







    @FunctionalInterface
    public interface PlayerLoggedIn {
        void onPlayerLoggedIn(Level world, ServerPlayer player);
    }

    @FunctionalInterface
    public interface PlayerLoggedOut {
        void onPlayerLoggedOut(Level world, ServerPlayer player);
    }


}
