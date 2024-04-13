package committee.nova.mods.novalogin.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import committee.nova.mods.novalogin.CommonClass;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
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
public class RegisterCmd {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("register")
                .then(argument("newPassword", StringArgumentType.word())
                        .then(argument("confirmPassword", StringArgumentType.word())
                                .executes(ctx -> {
                                    String password = StringArgumentType.getString(ctx, "newPassword");
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    String username = player.getGameProfile().getName();

                                    if (CommonClass.SAVE.isReg(username)) {
                                        ctx.getSource().sendSuccess(new TranslatableComponent("info.novalogin.cmd.registered"), false);
                                        return 1;
                                    }
                                    if (!password.equals(StringArgumentType.getString(ctx, "confirmPassword"))) {
                                        ctx.getSource().sendSuccess(new TranslatableComponent("info.novalogin.cmd.pwd_strict"), false);
                                        return 1;
                                    }
                                    CommonClass.SAVE.reg(player, password);
                                    LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
                                    playerLogin.setLogin(true);
                                    player.setInvulnerable(false);
                                    player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING, SoundSource.MASTER, 100f, 0f);
                                    ctx.getSource().sendSuccess(new TranslatableComponent("info.novalogin.cmd.register_success"), false);
                                    return 1;
                                }))));
    }
}
