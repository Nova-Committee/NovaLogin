package committee.nova.mods.novalogin.models;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

/**
 * LoginPlayer
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:06
 */
public class LoginPlayer {
    public final UUID uuid;
    public final String name;
    public final long time;
    public final double posX, posY, posZ;
    public final float rotX, rotY;

    public LoginPlayer (ServerPlayer player){
        this.uuid = player.getGameProfile().getId();
        this.name = player.getGameProfile().getName();
        this.time = System.currentTimeMillis();
        this.posX = player.getX();
        this.posY = player.getY();
        this.posZ = player.getZ();
        this.rotX = player.getXRot();
        this.rotY = player.getYRot();
    }
}
