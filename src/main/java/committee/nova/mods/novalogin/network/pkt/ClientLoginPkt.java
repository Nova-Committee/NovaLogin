package committee.nova.mods.novalogin.network.pkt;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * ClientLoginPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/7 下午6:58
 */
public class ClientLoginPkt implements IMessage {
    public String screenId;

    public ClientLoginPkt(){
    }

    public ClientLoginPkt(String screenId) {
        this.screenId = screenId;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        this.screenId = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.screenId);
    }
}
