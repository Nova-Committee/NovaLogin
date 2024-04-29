package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * ServerLoginModePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:36
 */
public class ForgeServerLoginActionPkt {
    public static void handle(ServerLoginActionPkt msg, CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(() -> {
            if(ctx.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerLoginActionPkt.run(msg.username, msg.password, ctx.getSender());
            }
        });

        ctx.setPacketHandled(true);
    }
}
