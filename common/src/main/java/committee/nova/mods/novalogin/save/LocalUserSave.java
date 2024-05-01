package committee.nova.mods.novalogin.save;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;
import committee.nova.mods.novalogin.Const;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static committee.nova.mods.novalogin.Const.GSON;

/**
 * LocalSavePwd
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/1 下午1:45
 */
public class LocalUserSave {
    private static Map<String, String> map = new HashMap<>();
    private static final Path saveFile = Const.novaPath.resolve("localusers.json");
    private static final Logger LOGGER = LogManager.getLogger(LocalUserSave.class);
    private static final Marker LOCALUSER = MarkerManager.getMarker("LOCALUSERSAVE");

    public LocalUserSave() {
    }

    public static void setUser(String name, String pwd) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(pwd);
        if (!pwd.equals(map.get(name))) {
            map.put(name, pwd);
            save();
        }
    }

    public static boolean removeUser(String name) {
        Objects.requireNonNull(name);
        if (map.remove(name) != null) {
            save();
            return true;
        } else {
            return false;
        }
    }

    public static String getUserPwd(String name) {
        Objects.requireNonNull(name);
        return map.get(name);
    }

    public static boolean containsUser(String name) {
        Objects.requireNonNull(name);
        return map.containsKey(name);
    }

    public static Map<String, String> getMap() {
        return ImmutableMap.copyOf(map);
    }

    public static void save() {
        (new SaveThread(GSON.toJson(map))).start();
    }

    public static void load() {
        if (Files.exists(saveFile)) {
            try {
                BufferedReader reader = Files.newBufferedReader(saveFile, Charsets.UTF_8);

                try {
                    map = GSON.fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
                } catch (Throwable throwable) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var10) {
                            throwable.addSuppressed(var10);
                        }
                    }

                    throw throwable;
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (IOException | JsonSyntaxException exception1) {
                LOGGER.error(LOCALUSER, "Could not parse username cache file as valid json, deleting file {}", saveFile, (Exception) exception1);

                try {
                    Files.delete(saveFile);
                } catch (IOException exception) {
                    LOGGER.error(LOCALUSER, "Could not delete file {}", saveFile.toString());
                }
            } finally {
                if (map == null) {
                    map = new HashMap<>();
                }

            }

        }
    }

    private static class SaveThread extends Thread {
        private final String data;

        public SaveThread(String data) {
            this.data = data;
        }

        public void run() {
            try {
                synchronized(LocalUserSave.saveFile) {
                    Files.writeString(LocalUserSave.saveFile, this.data);
                }
            } catch (IOException var4) {
                LocalUserSave.LOGGER.error(LocalUserSave.LOCALUSER, "Failed to save username cache to file!", var4);
            }

        }
    }
}
