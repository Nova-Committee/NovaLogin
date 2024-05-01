package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.client.FabricLoginScreen;
import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.ServerRegisterActionPkt;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * NetWorkDispatcher
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午8:02
 */
public class NetWorkDispatcher {
    public static ResourceLocation LOGIN_ACTION_SERVER = Const.rl("login_action_server");
    public static ResourceLocation LOGIN_ACTION_CLIENT = Const.rl("login_action_client");
    public static ResourceLocation REGISTER_ACTION_SERVER = Const.rl("register_action_server");
    public static ResourceLocation CLOSE_SCREEN_CLIENT = Const.rl("close_screen_client");

    public static void init(){
        receiveLoginActionFromClient();
        receiveRegisterActionFromClient();
    }

    public static void clientInit(){
        receiveLoginActionFromServer();
        receiveCloseScreenFromServer();
    }

    @Environment(EnvType.CLIENT)
    public static void sendLoginActionToServer(String username, String password) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(username);
        buf.writeUtf(password);
        ClientPlayNetworking.send(LOGIN_ACTION_SERVER, buf);
    }

    @Environment(EnvType.CLIENT)
    public static void sendRegisterActionToServer(String username, String password, String confirmPassword) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(username);
        buf.writeUtf(password);
        buf.writeUtf(confirmPassword);
        ClientPlayNetworking.send(REGISTER_ACTION_SERVER, buf);
    }

    public static void sendLoginActionToClient(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        ServerPlayNetworking.send(player, LOGIN_ACTION_CLIENT, buf);
    }

    public static void sendCloseScreenToClient(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        ServerPlayNetworking.send(player, CLOSE_SCREEN_CLIENT, buf);
    }

    private static void receiveLoginActionFromClient() {
        ServerPlayNetworking.registerGlobalReceiver(LOGIN_ACTION_SERVER, (server, player, handler, buf, responseSender) -> {
            String username = buf.readUtf();
            String password = buf.readUtf();
            server.execute(() -> {
               if (ServerLoginActionPkt.run(username, password, player)) sendCloseScreenToClient(player);
            });
        });
    }

    private static void receiveRegisterActionFromClient() {
        ServerPlayNetworking.registerGlobalReceiver(REGISTER_ACTION_SERVER, (server, player, handler, buf, responseSender) -> {
            String username = buf.readUtf();
            String password = buf.readUtf();
            String confirmPassword = buf.readUtf();
            server.execute(() -> {
                if (ServerRegisterActionPkt.run(username, password, confirmPassword, player)) sendCloseScreenToClient(player);
            });
        });
    }


    @Environment(EnvType.CLIENT)
    private static void receiveLoginActionFromServer() {
        ClientPlayNetworking.registerGlobalReceiver(LOGIN_ACTION_CLIENT, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.setScreen(new FabricLoginScreen());
            });
        });
    }

    @Environment(EnvType.CLIENT)
    private static void receiveCloseScreenFromServer() {
        ClientPlayNetworking.registerGlobalReceiver(CLOSE_SCREEN_CLIENT, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.setScreen(null);
            });
        });
    }
}
