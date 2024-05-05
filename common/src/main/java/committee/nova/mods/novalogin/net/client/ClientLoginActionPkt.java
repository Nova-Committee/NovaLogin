package committee.nova.mods.novalogin.net.client;

import committee.nova.mods.novalogin.Const;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

/**
 * ClientLoginActionPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/5 下午2:19
 */
public record ClientLoginActionPkt(String screenId) implements CustomPacketPayload {
    public static final Type<ClientLoginActionPkt> TYPE = new Type<>(Const.rl("client_login"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientLoginActionPkt> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ClientLoginActionPkt::screenId,
            ClientLoginActionPkt::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}