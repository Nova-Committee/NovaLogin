package committee.nova.mods.novalogin.network;


import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.ServerRegisterActionPkt;
import committee.nova.mods.novalogin.network.pkt.ForgeClientCloseScreenPkt;
import committee.nova.mods.novalogin.network.pkt.ForgeClientLoginActionPkt;
import committee.nova.mods.novalogin.network.pkt.ForgeServerLoginActionPkt;
import committee.nova.mods.novalogin.network.pkt.ForgeServerRegisterActionPkt;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * NetWorkDispatcher
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 13:43
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetWorkDispatcher {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Const.rl("main"), () -> "1.0", (s) -> true, (s) -> true);;

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        CHANNEL.registerMessage(0, ServerRegisterActionPkt.class, ServerRegisterActionPkt::toBytes, ServerRegisterActionPkt::new, ForgeServerRegisterActionPkt::handle);
        CHANNEL.registerMessage(1, ServerLoginActionPkt.class, ServerLoginActionPkt::toBytes, ServerLoginActionPkt::new, ForgeServerLoginActionPkt::handle);
        CHANNEL.registerMessage(2, ForgeClientLoginActionPkt.class, ForgeClientLoginActionPkt::toBytes, ForgeClientLoginActionPkt::new, ForgeClientLoginActionPkt::handle);
        CHANNEL.registerMessage(3, ForgeClientCloseScreenPkt.class, ForgeClientCloseScreenPkt::toBytes, ForgeClientCloseScreenPkt::new, ForgeClientCloseScreenPkt::handle);
    }

}
