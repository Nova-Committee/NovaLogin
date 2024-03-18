package committee.nova.mods.novalogin.save;

import java.io.IOException;
import java.util.UUID;

/**
 * LoginSave
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:42
 */
public interface LoginSave {
    boolean checkPwd(UUID uuid, String password);

    void unReg(UUID uuid);

    boolean isReg(UUID uuid);

    void reg(UUID uuid, String password);

    void changePwd(UUID uuid, String newPassword);

    boolean dirty();

    void save() throws IOException;

}
