package committee.nova.mods.novalogin.proxy;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * IProxy
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/13 上午12:18
 */
public interface IProxy {
    default void sendToServer(CustomPacketPayload packetPayload) {

    }
}
