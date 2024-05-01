package committee.nova.mods.novalogin.net;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

/**
 * LoginPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午12:14
 */
public class ServerRegisterActionPkt {

    public String username;
    public String password;
    public String confirmPassword;

    public ServerRegisterActionPkt() {}

    public ServerRegisterActionPkt(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public ServerRegisterActionPkt(FriendlyByteBuf buf) {
        this.username = buf.readUtf();
        this.password = buf.readUtf();
        this.confirmPassword = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(username);
        buf.writeUtf(password);
        buf.writeUtf(confirmPassword);
    }

    public static void run(String username, String password, String confirmPassword, ServerPlayer player) {
        if (Const.loginSave.isReg(username)) {
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.registered"), false);
        }
        if (!password.equals(confirmPassword)) {
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.pwd_strict"), false);
        }
        Const.loginSave.reg(player, password);
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(true);
        player.setInvulnerable(false);
        player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.MASTER, 100f, 0f);
        player.sendSystemMessage(Component.translatable("info.novalogin.cmd.register_success"), false);
    }
}
