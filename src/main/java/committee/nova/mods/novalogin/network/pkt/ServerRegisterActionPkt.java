package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * ServerLoginModePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:36
 */
public class ServerRegisterActionPkt implements IMessage {
    public String username;
    public String password;
    public String confirmPassword;

    public ServerRegisterActionPkt(){
    }

    public ServerRegisterActionPkt(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public static class HandlerServer implements IMessageHandler<ServerRegisterActionPkt, IMessage> {
        @Override
        public IMessage onMessage(ServerRegisterActionPkt pkt, MessageContext ctx) {
            ctx.getServerHandler().player.server.addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Const.loginSave.isReg(pkt.username)) {
                    player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.registered"), ChatType.SYSTEM));
                    return ;
                }
                if (!pkt.password.equals(pkt.confirmPassword)) {
                    player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.pwd_strict"), ChatType.SYSTEM));
                    return ;
                }
                Const.loginSave.reg(player, pkt.password);
                LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
                playerLogin.setLogin(true);
                player.setEntityInvulnerable(false);
                player.playSound(SoundEvents.BLOCK_NOTE_PLING,100f, 0f);
                player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.register_success"), ChatType.SYSTEM));
                NetWorkDispatcher.INSTANCE.sendTo(new ClientLoginPkt(""), player);
            });
            return null;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.username = ByteBufUtils.readUTF8String(buf);
        this.password = ByteBufUtils.readUTF8String(buf);
        this.confirmPassword = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.username);
        ByteBufUtils.writeUTF8String(buf, this.password);
        ByteBufUtils.writeUTF8String(buf, this.confirmPassword);
    }
}
