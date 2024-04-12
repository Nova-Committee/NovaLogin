package committee.nova.mods.novalogin.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import committee.nova.mods.novalogin.NovaLogin;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

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
                                    ServerPlayer player = ctx.getSource().getPlayer();
                                    String username = player.getGameProfile().getName();

                                    if (NovaLogin.SAVE.isReg(username)) {
                                        ctx.getSource().sendSuccess(() -> Component.literal("§cYou're already registered! Use /login instead."), false);
                                        return 1;
                                    }
                                    if (!password.equals(StringArgumentType.getString(ctx, "confirmPassword"))) {
                                        ctx.getSource().sendSuccess(() -> Component.literal("§cPasswords don't match! Repeat it correctly."), false);
                                        return 1;
                                    }
                                    NovaLogin.SAVE.reg(player, password);
                                    LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
                                    playerLogin.setLogin(true);
                                    player.setInvulnerable(false);
                                    ctx.getSource().sendSuccess(() -> Component.literal("§aSuccessfully registered."), false);
                                    return 1;
                                }))));
    }
}
