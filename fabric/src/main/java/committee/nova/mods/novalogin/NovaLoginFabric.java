package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.events.FabricBusEvents;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;

public class NovaLoginFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.SAVE = new JsonLoginSave(FabricLoader.getInstance().getGameDir());
        try {
            CommonClass.SAVE.load();
        } catch (IOException e) {
            Const.LOGGER.error(e.getMessage());
        }
        CommonClass.init();
        FabricBusEvents.init();
        NetWorkDispatcher.init();
    }
}
