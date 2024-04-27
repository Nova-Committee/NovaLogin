package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.events.FabricBusEvents;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class NovaLoginFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        NetWorkDispatcher.init();
        CommonClass.init(FabricLoader.getInstance().getGameDir());
        FabricBusEvents.init();
    }
}
