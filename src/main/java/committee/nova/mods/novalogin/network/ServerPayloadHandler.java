package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.NovaLogin;
import committee.nova.mods.novalogin.handler.PlayerLoginHandler;
import committee.nova.mods.novalogin.network.pkt.ServerLoginPkt;
import committee.nova.mods.novalogin.network.pkt.ServerPwdChangePkt;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

/**
 * ServerPayloadHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 13:44
 */
public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();
    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }



    public void handleLoginTaskPacket(ServerLoginPkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> context.player().ifPresent(player -> PlayerLoginHandler.INSTANCE.login(player.getGameProfile().getId(), msg.pwd())));
    }


    public void handlePwdChangeTaskPacket(ServerPwdChangePkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> context.player().ifPresent(player -> {
            UUID uuid = player.getGameProfile().getId();
            if (NovaLogin.SAVE.checkPwd(uuid, msg.from())){
                NovaLogin.SAVE.changePwd(uuid, msg.to());
                player.displayClientMessage(Component.translatable("novalogin.info.pwd_change_success"), false);
            } else {
                player.displayClientMessage(Component.translatable("novalogin.info.pwd_change_fail"), false);
            }
        }));
    }
}
