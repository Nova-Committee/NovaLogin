package committee.nova.mods.novalogin;


import committee.nova.mods.novalogin.proxy.ClientProxy;
import committee.nova.mods.novalogin.proxy.IProxy;
import committee.nova.mods.novalogin.proxy.ServerProxy;
import committee.nova.mods.novalogin.save.LocalUserSave;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

@Mod(Const.MOD_ID)
public class NovaLoginNeo {
    public static final IProxy proxy = FMLEnvironment.dist == Dist.CLIENT ? new ClientProxy() : new ServerProxy();

    public NovaLoginNeo(IEventBus eventBus) {
        CommonClass.init(FMLPaths.GAMEDIR.get());
        eventBus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event){
        LocalUserSave.load();
    }
}