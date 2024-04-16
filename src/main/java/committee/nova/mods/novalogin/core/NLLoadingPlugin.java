package committee.nova.mods.novalogin.core;

import committee.nova.mods.novalogin.Const;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * NLLoadingPlugin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/17 上午1:19
 */
@IFMLLoadingPlugin.Name("NovaLoginCore")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE)
public class NLLoadingPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    static
    {
        Const.LOGGER.info("NovaLoginCore Initializing...");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return NLContainer.class.getName();
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return "";
    }

    @Override
    public List<String> getMixinConfigs() {
        List<String> mixins = new ArrayList<>();
        mixins.add("novalogin.mixins.json");
        return mixins;
    }
}
