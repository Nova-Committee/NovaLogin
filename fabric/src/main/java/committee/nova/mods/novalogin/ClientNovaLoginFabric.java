package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.fabricmc.api.ClientModInitializer;

public class ClientNovaLoginFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetWorkDispatcher.clientInit();
    }
}
