package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * ServerLoginModePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:36
 */
public class ForgeServerLoginActionPkt {
    public static void handle(ServerLoginActionPkt msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerLoginActionPkt.run(msg.username, msg.password, ctx.get().getSender());
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
