package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.handler.OnGameMessage;
import committee.nova.mods.novalogin.handler.OnPlayerMove;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
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
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Unique
    ServerGamePacketListenerImpl novaLogin$play =  (ServerGamePacketListenerImpl) (Object) this;

    @Inject(method = "handleMovePlayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V",
            shift = At.Shift.AFTER
    ), cancellable = true)
    public void onPlayerMove(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (!OnPlayerMove.canMove(novaLogin$play.player)) {
            ci.cancel();
        }
    }


    @Inject(method = "handleChat(Ljava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void onGameMessage(String string, CallbackInfo ci) {
        if (!OnGameMessage.canSendMessage(novaLogin$play.player, string)) {
            ci.cancel();
        }
    }
}
