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
public record ServerLoginPkt(String pwd, boolean login) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Const.MOD_ID, "login_task");

    public static final Codec<ServerLoginPkt> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.fieldOf("pwd").forGetter(ServerLoginPkt::pwd),
                    Codec.BOOL.fieldOf("login").forGetter(ServerLoginPkt::login)
            ).apply(inst, ServerLoginPkt::new));

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
