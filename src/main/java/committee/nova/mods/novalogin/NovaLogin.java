package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Const.MOD_ID)
public class NovaLogin
{

    public static final JsonLoginSave SAVE = new JsonLoginSave();

    public NovaLogin(IEventBus bus){
    }
}
