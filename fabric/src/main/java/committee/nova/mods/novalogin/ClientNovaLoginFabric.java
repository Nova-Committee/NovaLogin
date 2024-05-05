package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.network.ClientNetWorkHandler;
import net.fabricmc.api.ClientModInitializer;

public class ClientNovaLoginFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientNetWorkHandler.clientInit();
        CommonClass.clientInit();
    }
}
