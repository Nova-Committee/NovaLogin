package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.net.client.ClientLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerRegisterActionPkt;
import net.minecraft.server.level.ServerPlayer;
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


    public void handleServerLoginPacket(ServerLoginActionPkt msg, IPayloadContext context) {
        context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        if (ServerLoginActionPkt.run(msg.username(), msg.pwd(), serverPlayer))
                            serverPlayer.connection.send(new ClientLoginActionPkt(""));
                    }
                }
        );
    }

    public void handleServerRegisterPacket(ServerRegisterActionPkt msg, IPayloadContext context) {
        context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        if (ServerRegisterActionPkt.run(msg.username(), msg.pwd(), msg.confirmPwd(), serverPlayer))
                            serverPlayer.connection.send(new ClientLoginActionPkt(""));
                    }
                }
        );
    }
}
