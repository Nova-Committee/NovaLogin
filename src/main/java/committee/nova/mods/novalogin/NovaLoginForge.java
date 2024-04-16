package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.cmds.LoginCmd;
import committee.nova.mods.novalogin.cmds.RegisterCmd;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import java.io.IOException;

@Mod(
        name = Const.MOD_NAME,
        modid = Const.MOD_ID,
        acceptedMinecraftVersions = "1.12.2",
        dependencies = Const.DEPENDENCIES
)
public class NovaLoginForge {
    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) throws IOException {
        CommonClass.init(event.getModConfigurationDirectory().getParentFile().toPath());
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) throws IOException {
        NetWorkDispatcher.instance = new NetWorkDispatcher();
    }

    @Mod.EventHandler
    public static void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new RegisterCmd());
        event.registerServerCommand(new LoginCmd());
    }

    @Mod.EventHandler
    public static void onServerStarted(FMLServerStartedEvent event) throws IOException {
    }

    @Mod.EventHandler
    public static void onServerStopped(FMLServerStoppedEvent event) throws IOException {
        CommonClass.SAVE.save();
    }
}