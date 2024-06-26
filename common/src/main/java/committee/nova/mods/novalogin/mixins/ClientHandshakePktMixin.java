package committee.nova.mods.novalogin.mixins;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * ServerLoginPktMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/19 13:02
 */
@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientHandshakePktMixin {
    
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract MinecraftSessionService getMinecraftSessionService();

    @Inject(
            method = "authenticateServer",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void novalogin$authenticateServer(String serverId, CallbackInfoReturnable<Component> cir) {
        try {
            this.getMinecraftSessionService().joinServer(this.minecraft.getUser().getGameProfile(), this.minecraft.getUser().getAccessToken(), serverId);
            cir.setReturnValue(null);
        } catch (AuthenticationUnavailableException exception) {
            cir.setReturnValue(Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.serversUnavailable")));
        } catch (InvalidCredentialsException exception) {
            cir.setReturnValue(null);
        } catch (InsufficientPrivilegesException exception) {
            cir.setReturnValue(Component.translatable("disconnect.loginFailedInfo", Component.translatable("disconnect.loginFailedInfo.insufficientPrivileges")));
        } catch (AuthenticationException var6) {
            cir.setReturnValue(Component.translatable("disconnect.loginFailedInfo", var6.getMessage()));
        }
    }
}
