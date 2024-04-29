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
public record NeoServerRegisterActionPkt(String username, String pwd, String confirmPwd) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Const.MOD_ID, "server_register");

    public static final Codec<NeoServerRegisterActionPkt> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.fieldOf("username").forGetter(NeoServerRegisterActionPkt::username),
                    Codec.STRING.fieldOf("password").forGetter(NeoServerRegisterActionPkt::pwd),
                    Codec.STRING.fieldOf("confirmPassword").forGetter(NeoServerRegisterActionPkt::confirmPwd)
            ).apply(inst, NeoServerRegisterActionPkt::new));

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
