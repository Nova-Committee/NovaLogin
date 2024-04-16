package committee.nova.mods.novalogin.events;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.handler.OnPlayerAction;
import committee.nova.mods.novalogin.handler.OnPlayerConnect;
import committee.nova.mods.novalogin.handler.OnPlayerLeave;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * ModBusEvents
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:11
 */
@Mod.EventBusSubscriber(modid = Const.MOD_ID)
public class ForgeBusEvents {

    @SubscribeEvent
    public static void onPlayerLoginIn(PlayerEvent.PlayerLoggedInEvent event){
        if (event.player instanceof EntityPlayerMP){
            EntityPlayerMP serverPlayer = (EntityPlayerMP) event.player;
            OnPlayerConnect.listen(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoginOut(PlayerEvent.PlayerLoggedOutEvent event){
        if (event.player instanceof EntityPlayerMP){
            EntityPlayerMP serverPlayer = (EntityPlayerMP) event.player;
            OnPlayerLeave.listen(serverPlayer);
        }

    }



    @SubscribeEvent
    public static void onPlayerInteract1(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() instanceof EntityPlayerMP){
            EntityPlayerMP serverPlayer = (EntityPlayerMP) event.getEntity();
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract2(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof EntityPlayerMP){
            EntityPlayerMP serverPlayer = (EntityPlayerMP) event.getEntity();
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract3(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof EntityPlayerMP){
            EntityPlayerMP serverPlayer = (EntityPlayerMP) event.getEntity();
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract4(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof EntityPlayerMP){
            EntityPlayerMP serverPlayer = (EntityPlayerMP) event.getEntity();
            if (!OnPlayerAction.canInteract(serverPlayer)) event.setCanceled(true);
        }
    }

}
