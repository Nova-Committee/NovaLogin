package committee.nova.mods.novalogin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import committee.nova.mods.novalogin.config.ConfigHandler;
import committee.nova.mods.novalogin.models.User;
import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Const
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:29
 */
public class Const {
    public static final String MOD_NAME = "NovaLogin";
    public static final String MOD_ID = "novalogin";
    public static final String VERSION = "0.1.0";
    public static final String DEPENDENCIES = "required-after:mixinbooter";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static ResourceLocation rl(String name){
        return new ResourceLocation(MOD_ID, name);
    }

    public static final HashSet<String> yggdrasilNamesCache = new HashSet<>();
    public static final HashSet<String> mojangAccountNamesCache = new HashSet<>();
    public static final HashMap<String, User> playerCacheMap = new HashMap<>();


    public static ConfigHandler configHandler;
    public static JsonLoginSave loginSave;
    public static Path novaPath;

}
