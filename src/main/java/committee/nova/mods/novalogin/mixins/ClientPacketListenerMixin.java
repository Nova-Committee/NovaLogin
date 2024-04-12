package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.NovaLogin;
import committee.nova.mods.novalogin.network.pkt.server.ServerLoginModePkt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ClientPacketListenerMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 18:54
 */
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
    public void novalogin$handleLogin(ClientboundLoginPacket arg, CallbackInfo ci){
        ServerLoginModePkt pkt;
        switch (Minecraft.getInstance().getUser().getType()){
            case MSA -> pkt = new ServerLoginModePkt(Minecraft.getInstance().getUser().getName(), 2);
            case MOJANG -> pkt = new ServerLoginModePkt(Minecraft.getInstance().getUser().getName(), 1);
            case LEGACY -> pkt = new ServerLoginModePkt(Minecraft.getInstance().getUser().getName(), 0);
            default -> pkt = new ServerLoginModePkt(Minecraft.getInstance().getUser().getName(), -1);
        }
        NovaLogin.proxy.sendToServer(pkt);
    }

}
