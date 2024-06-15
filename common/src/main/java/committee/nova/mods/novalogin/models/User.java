package committee.nova.mods.novalogin.models;

import lombok.Setter;
/**
 * LoginPlayer
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:06
 */
@Setter
public class User {
    public String name = "";
    public String pwd = "";
    public boolean isPremium = false;
    public boolean isYggdrasil = false;
    public boolean isRegister = false;
    public long lastLeaveTime = 0L;
    public String lastIp = "";
}
