package committee.nova.mods.novalogin.proxy;

import committee.nova.mods.novalogin.CommonClass;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ClientProxy
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/8 上午12:57
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);
        CommonClass.clientInit();
    }

    @Override
    public void sendChatMessageToAllPlayers(String message, Object... args) {
        super.sendChatMessageToAllPlayers(message, args);
    }
}
