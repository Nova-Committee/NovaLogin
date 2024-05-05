package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.net.client.ClientLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerRegisterActionPkt;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;


/**
 * ClientNetWorkhandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/1 下午5:18
 */
public class ServerNetWorkHandler {
    public static void init(){
        PayloadTypeRegistry.playC2S().register(ServerLoginActionPkt.TYPE, ServerLoginActionPkt.CODEC);
        PayloadTypeRegistry.playC2S().register(ServerRegisterActionPkt.TYPE, ServerRegisterActionPkt.CODEC);
        PayloadTypeRegistry.playS2C().register(ClientLoginActionPkt.TYPE, ClientLoginActionPkt.CODEC);
        receiveLoginActionFromClient();
        receiveRegisterActionFromClient();
    }

    private static void receiveLoginActionFromClient() {
        ServerPlayNetworking.registerGlobalReceiver(ServerLoginActionPkt.TYPE, (payload, context) -> {
            if (ServerLoginActionPkt.run(payload.username(), payload.pwd(), context.player())) context.responseSender().sendPacket(new ClientLoginActionPkt(""));
        });
    }

    private static void receiveRegisterActionFromClient() {
        ServerPlayNetworking.registerGlobalReceiver(ServerRegisterActionPkt.TYPE, (payload, context) -> {
            if (ServerRegisterActionPkt.run(payload.username(), payload.pwd(), payload.confirmPwd(), context.player())) context.responseSender().sendPacket(new ClientLoginActionPkt(""));
        });
    }

    public static void sendLoginActionToClient(ServerPlayer player) {
        ServerPlayNetworking.send(player, new ClientLoginActionPkt("login"));
    }

}
