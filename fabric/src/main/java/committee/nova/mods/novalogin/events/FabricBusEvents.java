package committee.nova.mods.novalogin.events;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.cmds.ChangePwdCmd;
import committee.nova.mods.novalogin.cmds.LoginCmd;
import committee.nova.mods.novalogin.cmds.RegisterCmd;
import committee.nova.mods.novalogin.events.callbacks.IEvents;
import committee.nova.mods.novalogin.handler.OnPlayerAction;
import committee.nova.mods.novalogin.handler.OnPlayerConnect;
import committee.nova.mods.novalogin.handler.OnPlayerLeave;
import committee.nova.mods.novalogin.network.ServerNetWorkHandler;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

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
        //onPlayerInteract1();
        onPlayerInteract2();
        onPlayerInteract3();
        onPlayerInteract4();
    }


    private static void onCmdRegister() {
        CommandRegistrationCallback.EVENT.register((dispatcher, r, selection) -> {
            LoginCmd.register(dispatcher);
            RegisterCmd.register(dispatcher);
            ChangePwdCmd.register(dispatcher);
        });
    }

    private static void onPlayerLoginIn() {
        ServerEntityEvents.ENTITY_LOAD.register((player, world) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                if (OnPlayerConnect.listen(serverPlayer)) ServerNetWorkHandler.sendLoginActionToClient(serverPlayer);
            }
        });
    }

    private static void onPlayerLoginOut() {
        ServerEntityEvents.ENTITY_UNLOAD.register((player, world) -> {
            if (player instanceof ServerPlayer serverPlayer) {
                OnPlayerLeave.listen(serverPlayer);
            }
        });
    }

    private static void onServerStarted() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

        });
    }

    private static void onServerStopped() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            Const.loginSave.save();
            Const.configHandler.save();
        });
    }

    private static void onPlayerInteract1() {
        IEvents.PLAYER_INTERACT.register((player, hand, target) -> InteractionResult.sidedSuccess(OnPlayerAction.canInteract((ServerPlayer) player)));
    }

    private static void onPlayerInteract2() {
        IEvents.LIVING_USE_ITEM_FINISH.register((entity, item, duration, result) -> {
            if (OnPlayerAction.canInteract((ServerPlayer) entity)) {
                return item;
            } else {
                return result;
            }
        });
    }

    private static void onPlayerInteract3() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> OnPlayerAction.canInteract((ServerPlayer) player));
    }

    private static void onPlayerInteract4() {
        IEvents.PLAYER_OPEN_MENU.register((player, menu) -> OnPlayerAction.canInteract((ServerPlayer) player));
    }

}
