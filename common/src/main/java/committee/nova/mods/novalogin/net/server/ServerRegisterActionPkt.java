package committee.nova.mods.novalogin.net.server;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.LoginUsers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

/**
 * ServerLoginModePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:36
 */
public record ServerRegisterActionPkt(String username, String pwd, String confirmPwd) implements CustomPacketPayload {
    public static final Type<ServerRegisterActionPkt> TYPE = new Type<>(Const.rl("server_register"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerRegisterActionPkt> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ServerRegisterActionPkt::username,
            ByteBufCodecs.STRING_UTF8,
            ServerRegisterActionPkt::pwd,
            ByteBufCodecs.STRING_UTF8,
            ServerRegisterActionPkt::confirmPwd,
            ServerRegisterActionPkt::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static boolean run(String username, String password, String confirmPassword, ServerPlayer player) {
        if (Const.loginSave.isReg(username)) {
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.registered"), false);
            return false;
        }
        if (!password.equals(confirmPassword)) {
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.pwd_strict"), false);
            return false;
        }
        Const.loginSave.reg(player, password);
        LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
        playerLogin.setLogin(true);
        player.setInvulnerable(false);
        player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.MASTER, 100f, 0f);
        player.sendSystemMessage(Component.translatable("info.novalogin.cmd.register_success"), false);
        return true;
    }
}
