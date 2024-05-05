package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.client.NeoLoginScreen;
import committee.nova.mods.novalogin.net.client.ClientLoginActionPkt;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * ClientPayloadHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/30 上午1:31
 */
public class ClientPayloadHandler {
    public static void handleClientLogin(ClientLoginActionPkt msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (msg.screenId().equals("login")) {
                Minecraft.getInstance().setScreen(new NeoLoginScreen());
            } else {
                Minecraft.getInstance().setScreen(null);
            }
        });
    }

}
