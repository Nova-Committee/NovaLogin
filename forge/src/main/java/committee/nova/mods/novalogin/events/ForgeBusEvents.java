package committee.nova.mods.novalogin.events;

import committee.nova.mods.novalogin.CommonClass;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.cmds.ChangePwdCmd;
import committee.nova.mods.novalogin.cmds.LoginCmd;
import committee.nova.mods.novalogin.cmds.RegisterCmd;
import committee.nova.mods.novalogin.handler.OnPlayerAction;
import committee.nova.mods.novalogin.handler.OnPlayerConnect;
import committee.nova.mods.novalogin.handler.OnPlayerLeave;
import committee.nova.mods.novalogin.net.ClientLoginActionPkt;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;

/**
 * ModBusEvents
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:11
 */
@Mod.EventBusSubscriber
public class ForgeBusEvents {

    @SubscribeEvent
    public static void onCmdRegister(RegisterCommandsEvent event){
        LoginCmd.register(event.getDispatcher());
        RegisterCmd.register(event.getDispatcher());
        ChangePwdCmd.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLoginIn(PlayerEvent.PlayerLoggedInEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (OnPlayerConnect.listen(serverPlayer)) NetWorkDispatcher.CHANNEL.send(PacketDistributor.ALL.noArg(), new ClientLoginActionPkt());
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
        Const.SAVE.save();
        Const.CONFIG.save();
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

    @SubscribeEvent
    public static void onPlayerInteract5(PlayerContainerEvent.Open event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

}
