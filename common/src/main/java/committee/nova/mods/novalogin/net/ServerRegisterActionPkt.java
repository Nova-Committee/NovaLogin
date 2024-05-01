package committee.nova.mods.novalogin.net;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
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

    public static boolean run(String username, String password, String confirmPassword, ServerPlayer player) {
        if (Const.loginSave.isReg(username)) {
            player.sendMessage(new TranslatableComponent("info.novalogin.cmd.registered"), ChatType.SYSTEM, Util.NIL_UUID);
            return false;
        }
        if (!password.equals(confirmPassword)) {
            player.sendMessage(new TranslatableComponent("info.novalogin.cmd.pwd_strict"), ChatType.SYSTEM, Util.NIL_UUID);
            return false;
        }
        Const.loginSave.reg(player, password);
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(true);
        player.setInvulnerable(false);
        player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING, SoundSource.MASTER, 100f, 0f);
        player.sendMessage(new TranslatableComponent("info.novalogin.cmd.register_success"), ChatType.SYSTEM, Util.NIL_UUID);
        return true;
    }
}
