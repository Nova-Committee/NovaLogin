package committee.nova.mods.novalogin;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

/**
 * Const
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:29
 */
public class Const {
    public static final String MOD_ID = "novalogin";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final MinecraftServer SERVER = ServerLifecycleHooks.getCurrentServer();
    public static ResourceLocation rl(String name){
        return new ResourceLocation(MOD_ID, name);
    }
}
