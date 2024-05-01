package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.network.ClientNetWorkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientNovaLoginFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientNetWorkHandler.clientInit();
        CommonClass.clientInit();
    }
}
