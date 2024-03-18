package committee.nova.mods.novalogin.network;


import com.mojang.serialization.Codec;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.network.pkt.ServerLoginPkt;
import committee.nova.mods.novalogin.network.pkt.ServerPwdChangePkt;
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
@Mod.EventBusSubscriber
public class NetWorkDispatcher {
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    @SubscribeEvent
    public static void register(RegisterPayloadHandlerEvent event) {
        registerPackets(event.registrar(Const.MOD_ID).versioned(PROTOCOL_VERSION));
    }


    @SuppressWarnings("Convert2MethodRef")
    public static void registerPackets(IPayloadRegistrar registrar) {
        registrar.common(ServerLoginPkt.ID, jsonReader(ServerLoginPkt.CODEC), handler -> handler.server(ServerPayloadHandler.getInstance()::handleLoginTaskPacket));
        registrar.common(ServerPwdChangePkt.ID, jsonReader(ServerPwdChangePkt.CODEC), handler -> handler.server(ServerPayloadHandler.getInstance()::handlePwdChangeTaskPacket));
    }


    protected static <T> FriendlyByteBuf.Reader<T> jsonReader(Codec<T> codec) {
        return buf -> buf.readJsonWithCodec(codec);
    }
}
