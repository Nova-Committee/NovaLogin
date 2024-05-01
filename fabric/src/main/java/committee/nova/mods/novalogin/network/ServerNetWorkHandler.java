package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import committee.nova.mods.novalogin.net.ServerRegisterActionPkt;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
    public static ResourceLocation LOGIN_ACTION_SERVER = Const.rl("login_action_server");
    public static ResourceLocation REGISTER_ACTION_SERVER = Const.rl("register_action_server");
    public static ResourceLocation LOGIN_ACTION_CLIENT = Const.rl("login_action_client");
    public static ResourceLocation CLOSE_SCREEN_CLIENT = Const.rl("close_screen_client");

    public static void init(){
        receiveLoginActionFromClient();
        receiveRegisterActionFromClient();
    }

    private static void receiveLoginActionFromClient() {
        ServerPlayNetworking.registerGlobalReceiver(LOGIN_ACTION_SERVER, (server, player, handler, buf, responseSender) -> {
            String username = buf.readUtf();
            String password = buf.readUtf();
            server.execute(() -> {
                if (ServerLoginActionPkt.run(username, password, player)) responseSender.sendPacket(CLOSE_SCREEN_CLIENT, buf);
            });
        });
    }

    private static void receiveRegisterActionFromClient() {
        ServerPlayNetworking.registerGlobalReceiver(REGISTER_ACTION_SERVER, (server, player, handler, buf, responseSender) -> {
            String username = buf.readUtf();
            String password = buf.readUtf();
            String confirmPassword = buf.readUtf();
            server.execute(() -> {
                if (ServerRegisterActionPkt.run(username, password, confirmPassword, player)) responseSender.sendPacket(CLOSE_SCREEN_CLIENT, buf);
            });
        });
    }

    public static void sendLoginActionToClient(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf("");
        ServerPlayNetworking.send(player, LOGIN_ACTION_CLIENT, buf);
    }

}
