package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.client.ForgeLoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * ForgeClientLoginHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/1 下午1:29
 */
@OnlyIn(Dist.CLIENT)
public class ForgeClientPktHandler {
    public static void handleLogin(ForgeClientLoginActionPkt msg, CustomPayloadEvent.Context ctx) {
        Minecraft.getInstance().setScreen(new ForgeLoginScreen());
    }

    public static void handleCloseScreen(ForgeClientCloseScreenPkt msg, CustomPayloadEvent.Context ctx) {
        Minecraft.getInstance().setScreen(null);
    }
}
