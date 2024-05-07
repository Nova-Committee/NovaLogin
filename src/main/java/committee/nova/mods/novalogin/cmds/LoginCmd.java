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
public class LoginCmd extends CommandBase {

    @Override
    @Nonnull
    public String getName() {
        return "login";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/login";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String password = args[0];
            String username = sender.getName();

            if (!Const.loginSave.isReg(username)) {
                player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.unregister"), ChatType.SYSTEM));
            } else if (Const.loginSave.checkPwd(username, password)) {
                LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
                playerLogin.setLogin(true);
                player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.login_success"), ChatType.SYSTEM));
                if (!player.isCreative()) {
                    player.setEntityInvulnerable(false);
                }
                player.playSound(SoundEvents.BLOCK_NOTE_PLING, 100f, 0f);
            } else {
                player.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 100f, 0.5f);
                player.connection.sendPacket(new SPacketChat(new TextComponentTranslation("info.novalogin.cmd.pwd_wrong"), ChatType.SYSTEM));
            }
        }
    }
}
