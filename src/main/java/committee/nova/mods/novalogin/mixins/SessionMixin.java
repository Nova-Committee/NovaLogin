package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.apis.ISessionAccessor;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * SessionMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/17 上午1:02
 */
@Mixin(Session.class)
public abstract class SessionMixin implements ISessionAccessor {
    @Unique
    private Session.Type novaLogin$sessionType;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void getSession(String p_i1098_1_, String p_i1098_2_, String s1, String type, CallbackInfo ci){
        Const.LOGGER.info(type);
        this.novaLogin$sessionType = type.equals("msa") ? Session.Type.MOJANG : Session.Type.setSessionType(type);
    }

    @Override
    public Session.Type novaLogin$getSessionType() {
        return novaLogin$sessionType;
    }
}
