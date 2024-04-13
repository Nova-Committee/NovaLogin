package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.Const;
import net.minecraft.network.FriendlyByteBuf;
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
public class ServerLoginModePkt {
    public String name;
    public int mode;

    public ServerLoginModePkt(String name, int mode) {
        this.name = name;
        this.mode = mode;
    }

    public ServerLoginModePkt(FriendlyByteBuf pb) {
        this.name = pb.readUtf();
        this.mode = pb.readInt();
    }

    public void toBytes(FriendlyByteBuf pb) {
        pb.writeUtf(this.name);
        pb.writeInt(this.mode);
    }

    public static void handle(ServerLoginModePkt msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                switch (msg.mode){
                    case 1, 2 -> Const.mojangAccountNamesCache.add(msg.name);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
