package committee.nova.mods.novalogin.network.pkt.server;

import committee.nova.mods.novalogin.Const;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * ServerPayloadHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 13:44
 */
public class ServerPayloadHandler {
    public static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();


    public void handleLoginPacket(ServerLoginModePkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> context.player().ifPresent(player -> {
            //Const.LOGGER.info(msg.toString());
            switch (msg.mode()){
                case 1, 2 -> Const.mojangAccountNamesCache.add(msg.name());
            }
        }));
    }
}
