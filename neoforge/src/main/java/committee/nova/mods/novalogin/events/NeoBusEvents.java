package committee.nova.mods.novalogin.events;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.cmds.ChangePwdCmd;
import committee.nova.mods.novalogin.cmds.LoginCmd;
import committee.nova.mods.novalogin.cmds.RegisterCmd;
import committee.nova.mods.novalogin.handler.OnPlayerAction;
import committee.nova.mods.novalogin.handler.OnPlayerConnect;
import committee.nova.mods.novalogin.handler.OnPlayerLeave;
import committee.nova.mods.novalogin.net.client.ClientLoginActionPkt;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.IOException;

/**
 * ModBusEvents
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:11
 */
@EventBusSubscriber
public class NeoBusEvents {

    @SubscribeEvent
    public static void onCmdRegister(RegisterCommandsEvent event){
        LoginCmd.register(event.getDispatcher());
        RegisterCmd.register(event.getDispatcher());
        ChangePwdCmd.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLoginIn(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (OnPlayerConnect.listen(serverPlayer)) PacketDistributor.sendToPlayer(serverPlayer, new ClientLoginActionPkt("login"));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            OnPlayerLeave.listen(serverPlayer);
        }

    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) throws IOException {
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) throws IOException {
        Const.loginSave.save();
        Const.configHandler.save();
    }

    @SubscribeEvent
    public static void onPlayerInteract1(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract2(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract3(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract4(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

}
