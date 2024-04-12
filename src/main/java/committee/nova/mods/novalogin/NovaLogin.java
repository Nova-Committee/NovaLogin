package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.network.pkt.server.ServerLoginModePkt;
import committee.nova.mods.novalogin.proxy.ClientProxy;
import committee.nova.mods.novalogin.proxy.IProxy;
import committee.nova.mods.novalogin.proxy.ServerProxy;
import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;

import java.io.IOException;

@Mod(Const.MOD_ID)
public class NovaLogin
{

    public static final JsonLoginSave SAVE = new JsonLoginSave();
    public static Boolean ONLINE = false;
    public static final IProxy proxy = FMLEnvironment.dist == Dist.CLIENT ? new ClientProxy() : new ServerProxy();

    public NovaLogin(IEventBus bus) throws IOException {
        NovaLogin.SAVE.load();
        bus.addListener(this::clientSetup);
    }


    private void clientSetup(final FMLClientSetupEvent event){

    }
}
