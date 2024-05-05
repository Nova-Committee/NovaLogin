package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

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
                if (ServerLoginActionPkt.run(msg.username, msg.password, ctx.get().getSender())){
                    NetWorkDispatcher.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new ForgeClientCloseScreenPkt());
                }

            }
        });

        ctx.get().setPacketHandled(true);
    }
}
