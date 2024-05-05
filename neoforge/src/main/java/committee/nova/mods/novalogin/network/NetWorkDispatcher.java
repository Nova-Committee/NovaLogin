package committee.nova.mods.novalogin.network;


import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.net.client.ClientLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerRegisterActionPkt;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * NetWorkDispatcher
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 13:43
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NetWorkDispatcher {
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        registerPackets(event.registrar(Const.MOD_ID).versioned(PROTOCOL_VERSION));
    }


    @SuppressWarnings("Convert2MethodRef")
    public static void registerPackets(PayloadRegistrar registrar) {
        registrar.playToClient(ClientLoginActionPkt.TYPE, ClientLoginActionPkt.CODEC, (msg, context) -> ClientPayloadHandler.handleClientLogin(msg, context));

        registrar.playToServer(ServerLoginActionPkt.TYPE, ServerLoginActionPkt.CODEC, (msg, context) -> ServerPayloadHandler.INSTANCE.handleServerLoginPacket(msg, context));
        registrar.playToServer(ServerRegisterActionPkt.TYPE, ServerRegisterActionPkt.CODEC, (msg, context) -> ServerPayloadHandler.INSTANCE.handleServerRegisterPacket(msg, context));
    }

}
