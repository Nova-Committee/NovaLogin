package committee.nova.mods.novalogin.network.pkt;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

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

    public static void handle(ForgeClientLoginActionPkt msg, CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeClientLoginHandler.handlePacket(msg, ctx));
        });
        ctx.setPacketHandled(true);
    }
}
