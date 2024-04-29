package committee.nova.mods.novalogin.network.pkt;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * ForgeClientLoginActionPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午1:02
 */
public class ForgeClientLoginActionPkt{
    public ForgeClientLoginActionPkt() {}


    public ForgeClientLoginActionPkt(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public static void handle(ForgeClientLoginActionPkt msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeClientLoginHandler.handlePacket(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
}
