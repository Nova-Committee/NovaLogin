package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(Const.MOD_ID)
public class NovaLogin
{

    public static final JsonLoginSave SAVE = new JsonLoginSave();
    public static Boolean ONLINE = false;

    public NovaLogin(IEventBus bus){
        bus.addListener(this::clientSetup);
    }


    private void clientSetup(final FMLClientSetupEvent event){
        ONLINE = Minecraft.getInstance().getUser().getType() == User.Type.MSA;
    }
}
