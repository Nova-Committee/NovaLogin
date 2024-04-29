package committee.nova.mods.novalogin.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
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
public class LoginCmd {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("login")
                .then(argument("password", StringArgumentType.word())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            String password = StringArgumentType.getString(ctx, "password");
                            String username = player.getGameProfile().getName();

                            if (!Const.SAVE.isReg(username)) {
                                ctx.getSource().sendSuccess(() ->Component.translatable("info.novalogin.cmd.unregister"), false);
                            } else if (Const.SAVE.checkPwd(username, password)) {
                                LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
                                playerLogin.setLogin(true);
                                ctx.getSource().sendSuccess(() ->Component.translatable("info.novalogin.cmd.login_success"), false);
                                if (!player.isCreative()) {
                                    player.setInvulnerable(false);
                                }
                                player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.MASTER, 100f, 0f);
                            } else {
                                player.playNotifySound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.MASTER, 100f, 0.5f);
                                ctx.getSource().sendSuccess(() ->Component.translatable("info.novalogin.cmd.pwd_wrong"), false);
                            }
                            return 1;
                        })));
    }
}
