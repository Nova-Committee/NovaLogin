package committee.nova.mods.novalogin.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import committee.nova.mods.novalogin.Const;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private boolean dirty = false;

    public JsonLoginSave(){
        this.path = FMLPaths.GAMEDIR.get().resolve("nova").resolve("login");
        try {
            load();
        }catch (IOException e){
            Const.LOGGER.error("加载出错，请检查文件", e);
        }
    }

    @Override
    public boolean checkPwd(String name, String password) {
        if (users.containsKey(name)) {
            return BCrypt.checkpw(password, users.get(name).pwd);
        }
        return false;
    }

    @Override
    public void unReg(String name) {
        dirty = true;
        users.remove(name);
    }

    @Override
    public boolean isReg(String name) {
        return users.containsKey(name);
    }

    @Override
    public void reg(String name, String password) {
        if (!users.containsKey(name)) {
            users.put(name, new User(name, BCrypt.hashpw(password, BCrypt.gensalt())));
            dirty = true;
        }
    }

    @Override
    public void changePwd(String name, String newPassword) {
        if (users.containsKey(name)) {
            dirty = true;
            users.put(name, new User(name, BCrypt.hashpw(newPassword, BCrypt.gensalt())));
        }
    }

    @Override
    public boolean dirty() {
        return dirty;
    }

    @Override
    public void save() throws IOException {
        users.forEach(((name, user) -> {
            try {
                if (!Files.exists(this.path.resolve(name + ".json"))) Files.createDirectories(path);
                Files.writeString(path, gson.toJson(user), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                Const.LOGGER.error("保存出错，请检查文件", e);
            }
        }));
    }

    @Override
    public void load() throws IOException {
        if (!Files.exists(this.path)) Files.createDirectories(path);
        File[] files = path.toFile().listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;
        for (File f : files){
            User user = gson.fromJson(Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8), User.class);
            users.put(user.name, user);
        }
    }

    public record User(String name, String pwd){
    }
}
