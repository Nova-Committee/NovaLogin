package committee.nova.mods.novalogin.network.pkt;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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

    public static void handle(ForgeClientCloseScreenPkt msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeClientPktHandler.handleCloseScreen(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }
}
