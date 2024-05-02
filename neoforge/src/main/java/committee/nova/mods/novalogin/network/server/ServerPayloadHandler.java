package committee.nova.mods.novalogin.network.server;

import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.ServerRegisterActionPkt;
import committee.nova.mods.novalogin.network.client.NeoClientLoginActionPkt;
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


    public void handleServerLoginPacket(NeoServerLoginActionPkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> {
            context.player().ifPresent(player -> {
                if (player instanceof ServerPlayer serverPlayer){
                    if (ServerLoginActionPkt.run(msg.username(), msg.pwd(), serverPlayer))
                        serverPlayer.connection.send(new NeoClientLoginActionPkt(""));
                }
            });
            }
        );
    }

    public void handleServerRegisterPacket(NeoServerRegisterActionPkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> {
                    context.player().ifPresent(player -> {
                        if (player instanceof ServerPlayer serverPlayer){
                            if (ServerRegisterActionPkt.run(msg.username(), msg.pwd(), msg.confirmPwd(), serverPlayer))
                                serverPlayer.connection.send(new NeoClientLoginActionPkt(""));
                        }
                    });
                }
        );
    }
}
