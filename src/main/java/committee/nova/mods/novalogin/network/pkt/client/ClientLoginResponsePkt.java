package committee.nova.mods.novalogin.network.pkt.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import committee.nova.mods.novalogin.Const;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * LoginResponsePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 20:52
 */
public record ClientLoginResponsePkt(boolean success) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Const.MOD_ID, "login_response");
    public static final Codec<ClientLoginResponsePkt> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.BOOL.fieldOf("success").forGetter(ClientLoginResponsePkt::success)
            ).apply(inst, ClientLoginResponsePkt::new));

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
