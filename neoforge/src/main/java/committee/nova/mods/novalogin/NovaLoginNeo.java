package committee.nova.mods.novalogin;


import committee.nova.mods.novalogin.proxy.ClientProxy;
import committee.nova.mods.novalogin.proxy.IProxy;
import committee.nova.mods.novalogin.proxy.ServerProxy;
import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;

@Mod(Const.MOD_ID)
public class NovaLoginNeo {
    public static final IProxy proxy = FMLEnvironment.dist == Dist.CLIENT ? new ClientProxy() : new ServerProxy();

    public NovaLoginNeo(IEventBus eventBus) throws IOException {
        CommonClass.SAVE = new JsonLoginSave(FMLPaths.GAMEDIR.get());
        CommonClass.SAVE.load();
        CommonClass.init();
    }
}