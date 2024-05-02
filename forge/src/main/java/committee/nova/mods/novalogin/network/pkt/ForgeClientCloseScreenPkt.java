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
public class ForgeClientCloseScreenPkt {
    public ForgeClientCloseScreenPkt() {}


    public ForgeClientCloseScreenPkt(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public static void handle(ForgeClientCloseScreenPkt msg, CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeClientPktHandler.handleCloseScreen(msg, ctx));
        });
        ctx.setPacketHandled(true);
    }
}
