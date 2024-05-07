package committee.nova.mods.novalogin.save;

import net.minecraft.entity.player.EntityPlayerMP;

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

    void reg(EntityPlayerMP player, String password);

    void changePwd(EntityPlayerMP player, String newPassword);

    boolean dirty();

    void save();

    void load();

}
