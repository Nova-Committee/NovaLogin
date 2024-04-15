package committee.nova.mods.novalogin;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;

@Mod(Const.MOD_ID)
public class NovaLoginForge {
    
    public NovaLoginForge() throws IOException {
        CommonClass.init(FMLPaths.GAMEDIR.get());
    }
}