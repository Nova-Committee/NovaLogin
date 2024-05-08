package committee.nova.mods.novalogin.network;


import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.network.pkt.ClientLoginHandler;
import committee.nova.mods.novalogin.network.pkt.ClientLoginPkt;
import committee.nova.mods.novalogin.network.pkt.ServerLoginActionPkt;
import committee.nova.mods.novalogin.network.pkt.ServerRegisterActionPkt;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * NetWorkDispatcher
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 13:43
 */
public class NetWorkDispatcher {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(ServerLoginActionPkt.HandlerServer.class, ServerLoginActionPkt.class, 0, Side.SERVER);
        INSTANCE.registerMessage(ServerRegisterActionPkt.HandlerServer.class, ServerRegisterActionPkt.class, 1, Side.SERVER);

        INSTANCE.registerMessage(ClientLoginHandler.class, ClientLoginPkt.class, 2, Side.CLIENT);
    }

}
