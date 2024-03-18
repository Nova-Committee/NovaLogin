package committee.nova.mods.novalogin.network.pkt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.utils.SecurityUtil;
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
public record ServerPwdChangePkt(String from , String to) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Const.MOD_ID, "pwd_change_task");

    public static final Codec<ServerPwdChangePkt> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.STRING.fieldOf("from").forGetter(ServerPwdChangePkt::from),
                    Codec.STRING.fieldOf("to").forGetter(ServerPwdChangePkt::to)
            ).apply(inst, (from, to) -> new ServerPwdChangePkt(SecurityUtil.getSHA256(from), SecurityUtil.getSHA256(to))));

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
