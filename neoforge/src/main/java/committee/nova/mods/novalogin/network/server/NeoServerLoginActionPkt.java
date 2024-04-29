package committee.nova.mods.novalogin.network.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import committee.nova.mods.novalogin.Const;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * ServerLoginModePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:36
 */
public record NeoServerLoginActionPkt(String username, String pwd) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Const.MOD_ID, "server_login");

    public static final Codec<NeoServerLoginActionPkt> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.fieldOf("username").forGetter(NeoServerLoginActionPkt::username),
                    Codec.STRING.fieldOf("password").forGetter(NeoServerLoginActionPkt::pwd)
            ).apply(inst, NeoServerLoginActionPkt::new));

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
