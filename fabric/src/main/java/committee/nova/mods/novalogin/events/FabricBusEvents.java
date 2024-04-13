package committee.nova.mods.novalogin.events;

import committee.nova.mods.novalogin.CommonClass;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.cmds.LoginCmd;
import committee.nova.mods.novalogin.cmds.RegisterCmd;
import committee.nova.mods.novalogin.handler.OnPlayerAction;
import committee.nova.mods.novalogin.handler.OnPlayerConnect;
import committee.nova.mods.novalogin.handler.OnPlayerLeave;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;

/**
 * ModBusEvents
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:11
 */
public class FabricBusEvents {
    public static void init() {
        onCmdRegister();
        onPlayerLoginIn();
        onPlayerLoginOut();
        onServerStarted();
        onServerStopped();
        onPlayerInteract3();
    }


    private static void onCmdRegister() {
        CommandRegistrationCallback.EVENT.register((dispatcher, r) -> {
            LoginCmd.register(dispatcher);
            RegisterCmd.register(dispatcher);
        });
    }

    private static void onPlayerLoginIn() {
        IEvents.PLAYER_LOGGED_IN.register((world, player) -> {
            OnPlayerConnect.listen(player);
        });
    }

    private static void onPlayerLoginOut() {
        IEvents.PLAYER_LOGGED_OUT.register((world, player) -> {
            OnPlayerLeave.listen(player);
        });
    }

    private static void onServerStarted() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

        });
    }

    private static void onServerStopped() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            try {
                CommonClass.SAVE.save();
            } catch (IOException e) {
                Const.LOGGER.error(e.getMessage());
            }
        });
    }


    private static void onPlayerInteract3() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> OnPlayerAction.canInteract((ServerPlayer) player));
    }

}
