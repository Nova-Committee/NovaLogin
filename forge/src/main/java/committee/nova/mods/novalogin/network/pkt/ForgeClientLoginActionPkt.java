package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.client.ForgeLoginScreen;
import committee.nova.mods.novalogin.net.ClientLoginActionPkt;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkDirection;
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
public class ForgeClientLoginActionPkt {
    public static void handle(ClientLoginActionPkt msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                Minecraft.getInstance().setScreen(new ForgeLoginScreen());
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
