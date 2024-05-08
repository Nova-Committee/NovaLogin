package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.handler.OnGameMessage;
import committee.nova.mods.novalogin.handler.OnPlayerMove;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ServerGamePacketListenerImplMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:33
 */
@Mixin(NetHandlerPlayServer.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Unique
    NetHandlerPlayServer novaLogin$play =  (NetHandlerPlayServer) (Object) this;

    @Inject(method = "processPlayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V",
            shift = At.Shift.AFTER
    ), cancellable = true)
    public void onPlayerMove(CPacketPlayer packetIn, CallbackInfo ci) {
        if (!OnPlayerMove.canMove(novaLogin$play.player)) {
            ci.cancel();
        }
    }


    @Inject(method = "processChatMessage", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void onGameMessage(CPacketChatMessage packet, CallbackInfo ci) {
        if (!OnGameMessage.canSendMessage(novaLogin$play.player, packet.getMessage())) {
            ci.cancel();
        }
    }
}
