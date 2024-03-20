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
    boolean checkPwd(String name, String password);

    void unReg(String name);

    boolean isReg(String name);

    void reg(String name, String password);

    void changePwd(String name, String newPassword);

    boolean dirty();

    void save() throws IOException;

    void load() throws IOException;

}
