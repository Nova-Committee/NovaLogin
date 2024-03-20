package committee.nova.mods.novalogin.network.pkt.server;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.NovaLogin;
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


    public void handleLoginPacket(ServerLoginPkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> context.player().ifPresent(player -> {
            String name = player.getGameProfile().getName();
            if (msg.login()){
                if (NovaLogin.SAVE.isReg(name) && NovaLogin.SAVE.checkPwd(name, msg.pwd())){
                    Const.LOGGER.info("Player " + name + "logged in!");
                } else {
                    Const.LOGGER.warn("Player " + name + "try to login with wrong password!");
                    context.packetHandler().disconnect(Component.translatable("novalogin.info.no_allow"));
                }
            } else {
                if (!NovaLogin.SAVE.isReg(name)) {
                    NovaLogin.SAVE.reg(name, msg.pwd());
                    Const.LOGGER.info("Player " + name + "registered!");
                } else {
                    Const.LOGGER.warn("Player " + name + "has registered!");
                    context.packetHandler().disconnect(Component.translatable("novalogin.info.no_allow"));
                }
            }
        }));
    }

    public void handlePwdChangePacket(ServerPwdChangePkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> context.player().ifPresent(player -> {
            String name = player.getGameProfile().getName();
            if (NovaLogin.SAVE.checkPwd(name, msg.from())){
                NovaLogin.SAVE.changePwd(name, msg.to());
                player.displayClientMessage(Component.translatable("novalogin.info.pwd_change_success"), false);
            } else {
                player.displayClientMessage(Component.translatable("novalogin.info.pwd_change_fail"), false);
            }
        }));
    }
}
