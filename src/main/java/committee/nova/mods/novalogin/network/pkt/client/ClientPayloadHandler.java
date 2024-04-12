package committee.nova.mods.novalogin.network.pkt.client;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * ClientPayloadHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 20:55
 */
public class ClientPayloadHandler {
    public static void handleLoginResponse(ClientLoginPkt msg, IPayloadContext context) {
        context.workHandler().execute(() ->{

        });
    }
}
