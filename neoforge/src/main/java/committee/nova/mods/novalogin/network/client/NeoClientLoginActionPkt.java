package committee.nova.mods.novalogin.network.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.network.server.NeoServerRegisterActionPkt;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * ForgeClientLoginActionPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午1:02
 */
public record NeoClientLoginActionPkt(String screenId) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Const.MOD_ID, "client_login");
    public static final Codec<NeoClientLoginActionPkt> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.fieldOf("screen_id").forGetter(NeoClientLoginActionPkt::screenId)
            ).apply(inst, NeoClientLoginActionPkt::new));

    @Override
    public void write(@NotNull FriendlyByteBuf pBuffer) {
        pBuffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
