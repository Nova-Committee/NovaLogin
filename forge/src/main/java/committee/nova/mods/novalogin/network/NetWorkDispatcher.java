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
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;

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
    private static final int PROTOCOL_VERSION = 1;
    public static final SimpleChannel CHANNEL = ChannelBuilder.named(Const.rl("main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .acceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .simpleChannel();

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        CHANNEL.messageBuilder(ServerRegisterActionPkt.class, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerRegisterActionPkt::new)
                .encoder(ServerRegisterActionPkt::toBytes)
                .consumerMainThread(ForgeServerRegisterActionPkt::handle)
                .add()
        ;
        CHANNEL.messageBuilder(ServerLoginActionPkt.class, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerLoginActionPkt::new)
                .encoder(ServerLoginActionPkt::toBytes)
                .consumerMainThread(ForgeServerLoginActionPkt::handle)
                .add()
        ;
        CHANNEL.messageBuilder(ForgeClientLoginActionPkt.class, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeClientLoginActionPkt::new)
                .encoder(ForgeClientLoginActionPkt::toBytes)
                .consumerMainThread(ForgeClientLoginActionPkt::handle)
                .add()
        ;
        CHANNEL.messageBuilder(ForgeClientCloseScreenPkt.class, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ForgeClientCloseScreenPkt::new)
                .encoder(ForgeClientCloseScreenPkt::toBytes)
                .consumerMainThread(ForgeClientCloseScreenPkt::handle)
                .add()
        ;
    }
}
