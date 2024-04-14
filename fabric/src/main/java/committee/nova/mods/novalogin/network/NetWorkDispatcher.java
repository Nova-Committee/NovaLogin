package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.Const;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * NetWorkDispatcher
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午8:02
 */
public class NetWorkDispatcher {
    public static ResourceLocation LOGIN_MODE = Const.rl("login_mode");

    public static void init(){
        receiveLoginModeToServer();
    }

    public static void sendLoginModeToServer(String name, int mode) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(name);
        buf.writeInt(mode);
        ClientPlayNetworking.send(LOGIN_MODE, buf);
    }

    private static void receiveLoginModeToServer() {
        ServerPlayNetworking.registerGlobalReceiver(LOGIN_MODE, (server, player, handler, buf, responseSender) -> {
            String name = buf.readUtf();
            int mode = buf.readInt();
            server.execute(() -> {
                switch (mode){
                    case 1, 2 -> Const.mojangAccountNamesCache.add(name);
                }
            });
        });
    }
}
