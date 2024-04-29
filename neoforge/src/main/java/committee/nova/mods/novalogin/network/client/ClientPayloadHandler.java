package committee.nova.mods.novalogin.network.client;

import committee.nova.mods.novalogin.client.NeoLoginScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

/**
 * ClientPayloadHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/30 上午1:31
 */
public class ClientPayloadHandler {
    public static void handleClientLogin(NeoClientLoginActionPkt msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> Minecraft.getInstance().setScreen(new NeoLoginScreen()));
    }
}
