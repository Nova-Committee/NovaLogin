package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.handler.OnGameMessage;
import committee.nova.mods.novalogin.handler.OnPlayerMove;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static committee.nova.mods.novalogin.Const.mojangAccountNamesCache;

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


    @Inject(method = "handleChat(Lnet/minecraft/network/protocol/game/ServerboundChatPacket;)V", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(ServerboundChatPacket packet, CallbackInfo ci) {
        if (!OnGameMessage.canSendMessage(novaLogin$play.player, packet)) {
            ci.cancel();
        }
    }
}
