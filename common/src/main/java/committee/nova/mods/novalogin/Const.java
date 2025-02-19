package committee.nova.mods.novalogin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import committee.nova.mods.novalogin.config.ConfigHandler;
import committee.nova.mods.novalogin.models.User;
import committee.nova.mods.novalogin.save.JsonLoginSave;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

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
    public static final String MOD_ID = "novalogin";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static ResourceLocation rl(String name){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static final HashSet<String> yggdrasilNamesCache = new HashSet<>();
    public static final HashSet<String> mojangAccountNamesCache = new HashSet<>();
    public static final HashMap<String, User> playerStorageMap = new HashMap<>();


    public static ConfigHandler configHandler;
    public static JsonLoginSave loginSave;
    public static Path novaPath;
}
