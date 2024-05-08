package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.cmds.ChangePwdCmd;
import committee.nova.mods.novalogin.cmds.LoginCmd;
import committee.nova.mods.novalogin.cmds.RegisterCmd;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import committee.nova.mods.novalogin.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(
        name = Const.MOD_NAME,
        modid = Const.MOD_ID,
        acceptedMinecraftVersions = "[1.12,]",
        dependencies = Const.DEPENDENCIES
)
public class NovaLoginForge {

    @SidedProxy(
            clientSide = "committee.nova.mods.novalogin.proxy.ClientProxy",
            serverSide = "committee.nova.mods.novalogin.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        CommonClass.init(event.getModConfigurationDirectory().getParentFile().toPath());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        NetWorkDispatcher.init();
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public static void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new RegisterCmd());
        event.registerServerCommand(new LoginCmd());
        event.registerServerCommand(new ChangePwdCmd());
    }

    @Mod.EventHandler
    public static void onServerStarted(FMLServerStartedEvent event) {
    }

    @Mod.EventHandler
    public static void onServerStopped(FMLServerStoppedEvent event) {
        Const.loginSave.save();
        Const.configHandler.save();
    }
}