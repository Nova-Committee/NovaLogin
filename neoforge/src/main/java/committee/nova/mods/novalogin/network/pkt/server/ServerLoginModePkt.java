package committee.nova.mods.novalogin.network.pkt.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import committee.nova.mods.novalogin.Const;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * ServerLoginPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 13:26
 */
public record ServerLoginModePkt(String name, int mode) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Const.MOD_ID, "login_mode");

    public static final Codec<ServerLoginModePkt> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.fieldOf("name").forGetter(ServerLoginModePkt::name),
                    Codec.INT.fieldOf("mode").forGetter(ServerLoginModePkt::mode)
            ).apply(inst, ServerLoginModePkt::new));

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
