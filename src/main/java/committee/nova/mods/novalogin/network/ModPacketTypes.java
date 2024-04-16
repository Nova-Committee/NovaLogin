package committee.nova.mods.novalogin.network;

import committee.nova.mods.novalogin.network.pkt.ModPacket;
import committee.nova.mods.novalogin.network.pkt.ServerLoginModePkt;

import java.util.HashMap;

/**
 * ModPacketTypes
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/16 下午10:21
 */
public enum ModPacketTypes {
    ACCOUNT( ServerLoginModePkt.class );
    private final Class<? extends ModPacket> packetClass;

    ModPacketTypes(
            final Class<? extends ModPacket> clz )
    {
        packetClass = clz;
    }

    private static HashMap<Class<? extends ModPacket>, Integer> fromClassToId = new HashMap<>();
    private static HashMap<Integer, Class<? extends ModPacket>> fromIdToClass = new HashMap<>();

    public static void init()
    {
        for ( final ModPacketTypes p : ModPacketTypes.values() )
        {
            fromClassToId.put( p.packetClass, p.ordinal() );
            fromIdToClass.put( p.ordinal(), p.packetClass );
        }
    }

    public static int getID(
            final Class<? extends ModPacket> clz )
    {
        return fromClassToId.get( clz );
    }

    public static ModPacket constructByID(
            final int id ) throws InstantiationException, IllegalAccessException
    {
        return fromIdToClass.get( id ).newInstance();
    }
}
