package committee.nova.mods.novalogin;


import committee.nova.mods.novalogin.save.LocalUserSave;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;

@Mod(Const.MOD_ID)
public class NovaLoginNeo {

    public NovaLoginNeo(IEventBus eventBus) {
        CommonClass.init(FMLPaths.GAMEDIR.get());
        eventBus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event){
        CommonClass.clientInit();
    }
}