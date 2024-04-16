package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.Const;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

/**
 * ServerLoginModePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 下午7:36
 */
public class ServerLoginModePkt extends ModPacket{
    public String name;
    public int mode;

    public ServerLoginModePkt(){
    }

    public ServerLoginModePkt(String name, int mode) {
        this.name = name;
        this.mode = mode;
    }

    @Override
    public void server(EntityPlayerMP playerEntity) {
        if (mode == 1) {
            Const.mojangAccountNamesCache.add(name);
        }
    }

    @Override
    public void getPayload(PacketBuffer buffer) {
        buffer.writeInt(this.mode);
        byte[] data = this.name.getBytes(StandardCharsets.UTF_8);
        buffer.writeShort(data.length);
        buffer.writeBytes(data);
    }

    @Override
    public void readPayload(PacketBuffer buffer) {
        this.mode = buffer.readInt();
        byte[] data = new byte[buffer.readShort()];
        buffer.readBytes(data);
        this.name = new String(data, StandardCharsets.UTF_8);
    }

}
