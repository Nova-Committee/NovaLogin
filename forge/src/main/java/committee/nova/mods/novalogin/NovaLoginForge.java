package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.save.LocalUserSave;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Const.MOD_ID)
public class NovaLoginForge {
    
    public NovaLoginForge() {
        CommonClass.init(FMLPaths.GAMEDIR.get());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event){
        LocalUserSave.load();
    }
}