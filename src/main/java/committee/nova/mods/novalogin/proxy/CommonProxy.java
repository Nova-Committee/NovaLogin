package committee.nova.mods.novalogin.proxy;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * CommonProxy
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/8 上午12:56
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void sendChatMessageToAllPlayers(String message, Object... args) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
                .sendMessage(new TextComponentTranslation(message, args));
    }
}
