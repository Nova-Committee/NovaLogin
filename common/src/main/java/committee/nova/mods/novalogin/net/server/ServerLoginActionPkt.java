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
public record ServerLoginActionPkt(String username, String pwd) implements CustomPacketPayload {
    public static final Type<ServerLoginActionPkt> TYPE = new Type<>(Const.rl("server_login"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerLoginActionPkt> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ServerLoginActionPkt::username,
            ByteBufCodecs.STRING_UTF8,
            ServerLoginActionPkt::pwd,
            ServerLoginActionPkt::new
    );


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static boolean run(String username, String password, ServerPlayer player) {
        if (!Const.loginSave.isReg(username)) {
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.unregister"), false);
            return false;
        } else if (Const.loginSave.checkPwd(username, password)) {
            LoginUsers.LoginUser playerLogin = LoginUsers.INSTANCE.get(player);
            playerLogin.setLogin(true);
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.login_success"), false);
            if (!player.isCreative()) {
                player.setInvulnerable(false);
            }
            player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.MASTER, 100f, 0f);
            return true;
        } else {
            player.playNotifySound(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.MASTER, 100f, 0.5f);
            player.sendSystemMessage(Component.translatable("info.novalogin.cmd.pwd_wrong"), false);
            return false;
        }
    }
}
