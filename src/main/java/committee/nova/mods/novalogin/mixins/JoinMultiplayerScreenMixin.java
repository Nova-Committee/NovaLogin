package committee.nova.mods.novalogin.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import committee.nova.mods.novalogin.client.screens.LoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.server.LanServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * JoinMultiplayerScreenMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 18:54
 */
@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin {
    @Unique
    private final JoinMultiplayerScreen novaLogin$joinMultiplayerScreen = (JoinMultiplayerScreen) (Object) this;

    @Inject(method = "joinSelectedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList$NetworkServerEntry;getServerData()Lnet/minecraft/client/server/LanServer;", shift = At.Shift.AFTER), cancellable = true)
    public void novalogin$joinSelectedServer(CallbackInfo ci, @Local ServerSelectionList.Entry arg2){
        LanServer lanserver = ((ServerSelectionList.NetworkServerEntry)arg2).getServerData();
        ServerData serverData = new ServerData(lanserver.getMotd(), lanserver.getAddress(), ServerData.Type.LAN);
        Minecraft.getInstance().setScreen(new LoginScreen(novaLogin$joinMultiplayerScreen, serverData));
        ci.cancel();
    }

}
