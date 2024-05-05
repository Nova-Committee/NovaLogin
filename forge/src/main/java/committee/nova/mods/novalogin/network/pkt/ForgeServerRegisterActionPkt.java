package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.PacketDistributor;

/**
 * ServerLoginModePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:36
 */
public class ForgeServerRegisterActionPkt {
    public static void handle(ServerRegisterActionPkt msg, CustomPayloadEvent.Context ctx){
        ctx.enqueueWork(() -> {
            if(ctx.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
               if (ServerRegisterActionPkt.run(msg.username, msg.password, msg.confirmPassword, ctx.getSender()))
                   NetWorkDispatcher.CHANNEL.send(new ForgeClientCloseScreenPkt(), PacketDistributor.PLAYER.with(ctx.getSender()));
            }
        });

        ctx.setPacketHandled(true);
    }
}
