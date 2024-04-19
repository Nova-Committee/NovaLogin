package committee.nova.mods.novalogin.network;


import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.network.pkt.ServerLoginModePkt;
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
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetWorkDispatcher {
    public static int id = 0;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Const.rl("main"), () -> {
        return "1.0";
    }, (s) -> {
        return true;
    }, (s) -> {
        return true;
    });;

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        CHANNEL.registerMessage(id++, ServerLoginModePkt.class, ServerLoginModePkt::toBytes, ServerLoginModePkt::new, ServerLoginModePkt::handle);
    }
}
