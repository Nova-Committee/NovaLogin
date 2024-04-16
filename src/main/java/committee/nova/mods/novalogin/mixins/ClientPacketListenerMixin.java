package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.apis.ISessionAccessor;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import committee.nova.mods.novalogin.network.pkt.ServerLoginModePkt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketJoinGame;
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
@Mixin(NetHandlerPlayClient.class)
public abstract class ClientPacketListenerMixin {

    @Inject(method = "handleJoinGame", at = @At(value = "TAIL"))
    public void novalogin$handleLogin(SPacketJoinGame sPacketJoinGame, CallbackInfo ci){
        ServerLoginModePkt pkt;
        switch (((ISessionAccessor)Minecraft.getMinecraft().getSession()).novaLogin$getSessionType()){
            case MOJANG : {
                pkt = new ServerLoginModePkt(Minecraft.getMinecraft().getSession().getUsername(), 1);
                break;
            }
            case LEGACY : {
                pkt = new ServerLoginModePkt(Minecraft.getMinecraft().getSession().getUsername(), 0);
                break;
            }
            default : {
                pkt = new ServerLoginModePkt(Minecraft.getMinecraft().getSession().getUsername(), -1);
                break;
            }
        }
        NetWorkDispatcher.instance.sendToServer(pkt);
    }

}
