package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.client.ForgeLoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * ForgeClientLoginHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/29 下午11:39
 */
@OnlyIn(Dist.CLIENT)
public class ForgeClientLoginHandler {
    public static void handlePacket(ForgeClientLoginActionPkt msg, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().setScreen(new ForgeLoginScreen());
    }
}