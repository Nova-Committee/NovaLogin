package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.client.FabricLoginScreen;
import committee.nova.mods.novalogin.net.client.ClientLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.server.ServerRegisterActionPkt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;


/**
 * ClientNetWorkhandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/1 下午5:18
 */
public class ClientNetWorkHandler {
    public static void clientInit(){
        receiveLoginActionFromServer();
    }

    @Environment(EnvType.CLIENT)
    public static void sendLoginActionToServer(String username, String password) {
        ClientPlayNetworking.send(new ServerLoginActionPkt(username, password));
    }

    @Environment(EnvType.CLIENT)
    public static void sendRegisterActionToServer(String username, String password, String confirmPassword) {
        ClientPlayNetworking.send(new ServerRegisterActionPkt(username, password, confirmPassword));
    }


    @Environment(EnvType.CLIENT)
    private static void receiveLoginActionFromServer() {
        ClientPlayNetworking.registerGlobalReceiver(ClientLoginActionPkt.TYPE, (payload, context) -> {
            if (payload.screenId().equals("login")){
                context.client().setScreen(new FabricLoginScreen());
            } else context.client().setScreen(null);
        });
    }
}
