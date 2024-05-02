package committee.nova.mods.novalogin.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * ClientProxy
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 上午12:19
 */
public class ClientProxy implements IProxy {

    @Override
    public void sendToServer(CustomPacketPayload packetPayload) {
        Minecraft.getInstance().getConnection().send(packetPayload);
    }
}
