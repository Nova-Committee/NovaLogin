package committee.nova.mods.novalogin.network;


import com.mojang.serialization.Codec;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.network.client.ClientPayloadHandler;
import committee.nova.mods.novalogin.network.client.NeoClientLoginActionPkt;
import committee.nova.mods.novalogin.network.server.NeoServerLoginActionPkt;
import committee.nova.mods.novalogin.network.server.NeoServerRegisterActionPkt;
import committee.nova.mods.novalogin.network.server.ServerPayloadHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

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
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    @SubscribeEvent
    public static void register(RegisterPayloadHandlerEvent event) {
        registerPackets(event.registrar(Const.MOD_ID).versioned(PROTOCOL_VERSION));
    }


    @SuppressWarnings("Convert2MethodRef")
    public static void registerPackets(IPayloadRegistrar registrar) {
        registrar.play(NeoClientLoginActionPkt.ID, jsonReader(NeoClientLoginActionPkt.CODEC), handler -> handler.client((msg, context) -> ClientPayloadHandler.handleClientLogin(msg, context)));

        registrar.common(NeoServerLoginActionPkt.ID, jsonReader(NeoServerLoginActionPkt.CODEC), handler -> handler.server(ServerPayloadHandler.INSTANCE::handleServerLoginPacket));
        registrar.common(NeoServerRegisterActionPkt.ID, jsonReader(NeoServerRegisterActionPkt.CODEC), handler -> handler.server(ServerPayloadHandler.INSTANCE::handleServerRegisterPacket));
    }


    protected static <T> FriendlyByteBuf.Reader<T> jsonReader(Codec<T> codec) {
        return buf -> buf.readJsonWithCodec(codec);
    }
}
