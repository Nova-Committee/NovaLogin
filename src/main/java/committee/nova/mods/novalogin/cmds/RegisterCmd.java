package committee.nova.mods.novalogin.cmds;

import committee.nova.mods.novalogin.CommonClass;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

/**
 * LoginCmd
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:46
 */
public class RegisterCmd extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "register";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/register";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            String password = args[0];
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String username = player.getGameProfile().getName();

            if (Const.loginSave.isReg(username)) {
                player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.registered"), ChatType.SYSTEM));
                return ;
            }
            if (!password.equals(args[1])) {
                player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.pwd_strict"), ChatType.SYSTEM));
                return ;
            }
            Const.loginSave.reg(player, password);
            LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
            playerLogin.setLogin(true);
            player.setEntityInvulnerable(false);
            player.playSound(SoundEvents.BLOCK_NOTE_PLING,100f, 0f);
            player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.register_success"), ChatType.SYSTEM));
        }

    }
}
