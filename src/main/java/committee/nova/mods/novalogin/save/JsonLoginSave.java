package committee.nova.mods.novalogin.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import committee.nova.mods.novalogin.Const;
import net.neoforged.fml.loading.FMLPaths;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JsonLoginSave
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:41
 */
public class JsonLoginSave implements LoginSave{
    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final Path path;
    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private boolean dirty = false;

    public JsonLoginSave(){
        this.path = FMLPaths.GAMEDIR.get().resolve("nova").resolve("login");
        try {
            if (Files.exists(this.path)){
                User[] buf = gson.fromJson(Files.newBufferedReader(path, StandardCharsets.UTF_8), User[].class);
                if (buf != null) {
                    Arrays.stream(buf).forEach(e -> users.put(e.uuid, e));
                }
            }
            else {
                if (!Files.exists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }
                Files.createFile(path);
            }
        }catch (IOException ignored){}
    }

    @Override
    public boolean checkPwd(UUID uuid, String password) {
        if (users.containsKey(uuid)) {
            return BCrypt.checkpw(password, users.get(uuid).pwd);
        }
        return false;
    }

    @Override
    public void unReg(UUID uuid) {
        dirty = true;
        users.remove(uuid);
    }

    @Override
    public boolean isReg(UUID uuid) {
        return users.containsKey(uuid);
    }

    @Override
    public void reg(UUID uuid, String password) {
        if (!users.containsKey(uuid)) {
            users.put(uuid, new User(uuid, BCrypt.hashpw(password, BCrypt.gensalt())));
            dirty = true;
        }
    }

    @Override
    public void changePwd(UUID uuid, String newPassword) {
        if (users.containsKey(uuid)) {
            dirty = true;
            users.put(uuid, new User(uuid, BCrypt.hashpw(newPassword, BCrypt.gensalt())));
        }
    }

    @Override
    public boolean dirty() {
        return dirty;
    }

    @Override
    public void save() throws IOException {
        try {
            Files.writeString(path, gson.toJson(users.values().toArray()), StandardOpenOption.TRUNCATE_EXISTING);
            dirty = false;
        } catch (IOException ex) {
            Const.LOGGER.error("Unable to save users", ex);
            throw ex;
        }
    }

    public record User(UUID uuid, String pwd){
    }
}
