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
public class ServerLoginActionPkt {

    public String username;
    public String password;

    public ServerLoginActionPkt() {}

    public ServerLoginActionPkt(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ServerLoginActionPkt(FriendlyByteBuf buf) {
        this.username = buf.readUtf();
        this.password = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(username);
        buf.writeUtf(password);
    }

    public static boolean run(String username, String password, ServerPlayer player) {
        if (!Const.loginSave.isReg(username)) {
            player.sendMessage(Component.translatable("info.novalogin.cmd.unregister"), ChatType.SYSTEM, Util.NIL_UUID);
            return false;
        } else if (Const.loginSave.checkPwd(username, password)) {
            LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
            playerLogin.setLogin(true);
            player.sendMessage(Component.translatable("info.novalogin.cmd.login_success"), ChatType.SYSTEM, Util.NIL_UUID);
            if (!player.isCreative()) {
                player.setInvulnerable(false);
            }
            player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING, SoundSource.MASTER, 100f, 0f);
            return true;
        } else {
            player.playNotifySound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.MASTER, 100f, 0.5f);
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.pwd_wrong"), false);
            return false;
        }
    }
}
