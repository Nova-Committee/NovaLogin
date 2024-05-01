package committee.nova.mods.novalogin.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * LoginCmd
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:46
 */
public class ChangePwdCmd {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("changepwd")
                .then(argument("newPassword", StringArgumentType.word())
                        .then(argument("confirmNewPassword", StringArgumentType.word())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            String newPassword = StringArgumentType.getString(ctx, "newPassword");
                            String username = player.getGameProfile().getName();

                            if (!Const.loginSave.isReg(username)) {
                                ctx.getSource().sendSuccess(new TranslatableComponent("info.novalogin.cmd.unregister"), false);
                                return 1;
                            }

                            if (!newPassword.equals(StringArgumentType.getString(ctx, "confirmNewPassword"))) {
                                ctx.getSource().sendSuccess(new TranslatableComponent("info.novalogin.cmd.pwd_strict"), false);
                                return 1;
                            }

                            LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
                            if (playerLogin.login) {
                                Const.loginSave.changePwd(player, newPassword);
                                player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING, SoundSource.MASTER, 100f, 0f);
                                ctx.getSource().sendSuccess(new TranslatableComponent("info.novalogin.cmd.change_pwd_success"), false);
                            } else {
                                player.playNotifySound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.MASTER, 100f, 0.5f);
                                ctx.getSource().sendSuccess(new TranslatableComponent("info.novalogin.cmd.change_pwd_failed"), false);
                            }
                            return 1;
                        }))));
    }
}
