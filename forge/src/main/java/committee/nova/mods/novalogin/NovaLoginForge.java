package committee.nova.mods.novalogin;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Const.MOD_ID)
public class NovaLoginForge {
    
    public NovaLoginForge(){
        CommonClass.init(FMLPaths.GAMEDIR.get());
    }
}