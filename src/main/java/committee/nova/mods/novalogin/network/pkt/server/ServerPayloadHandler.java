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


    public void handleLoginPacket(ServerLoginModePkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> context.player().ifPresent(player -> {
            //Const.LOGGER.info(msg.toString());
            switch (msg.mode()){
                case 1, 2 -> Const.mojangAccountNamesCache.add(msg.name());
            }
        }));
    }

    public void handlePwdChangePacket(ServerPwdChangePkt msg, IPayloadContext context) {
        context.workHandler().execute(() -> context.player().ifPresent(player -> {
            String name = player.getGameProfile().getName();
            if (NovaLogin.SAVE.checkPwd(name, msg.from())){
                //NovaLogin.SAVE.changePwd(name, msg.to());
                player.displayClientMessage(new TranslatableComponent("novalogin.info.pwd_change_success"), false);
            } else {
                player.displayClientMessage(new TranslatableComponent("novalogin.info.pwd_change_fail"), false);
            }
        }));
    }
}
