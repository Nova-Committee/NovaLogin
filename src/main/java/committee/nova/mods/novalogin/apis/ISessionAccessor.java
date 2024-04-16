package committee.nova.mods.novalogin.apis;

import net.minecraft.util.Session;

/**
 * ISessionAccessor
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/16 上午1:54
 */
public interface ISessionAccessor {
    Session.Type novaLogin$getSessionType();
}
