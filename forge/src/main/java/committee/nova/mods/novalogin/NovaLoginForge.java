package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;

@Mod(Const.MOD_ID)
public class NovaLoginForge {
    
    public NovaLoginForge() throws IOException {
        CommonClass.SAVE = new JsonLoginSave(FMLPaths.GAMEDIR.get());
        CommonClass.SAVE.load();
        CommonClass.init();
    }
}