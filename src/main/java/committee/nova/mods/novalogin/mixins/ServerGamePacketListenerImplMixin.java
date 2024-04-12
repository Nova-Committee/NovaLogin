package committee.nova.mods.novalogin.mixins;

import committee.nova.mods.novalogin.handler.OnGameMessage;
import committee.nova.mods.novalogin.handler.OnPlayerAction;
import committee.nova.mods.novalogin.handler.OnPlayerMove;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
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

    @Inject(method = "handleMovePlayer", at = @At("HEAD"), cancellable = true)
    public void onPlayerMove(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (!OnPlayerMove.canMove(novaLogin$play)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleInteract", at = @At("HEAD"), cancellable = true)
    public void onPlayerAction1(ServerboundInteractPacket packet, CallbackInfo ci) {
        if (!OnPlayerAction.canInteract(novaLogin$play)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleUseItem", at = @At("HEAD"), cancellable = true)
    public void onPlayerAction2(ServerboundUseItemPacket arg, CallbackInfo ci) {
        if (!OnPlayerAction.canInteract(novaLogin$play)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleUseItemOn", at = @At("HEAD"), cancellable = true)
    public void onPlayerAction3(ServerboundUseItemOnPacket arg, CallbackInfo ci) {
        if (!OnPlayerAction.canInteract(novaLogin$play)) {
            ci.cancel();
        }
    }

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    public void onPlayerAction4(ServerboundPlayerActionPacket arg, CallbackInfo ci) {
        if (!OnPlayerAction.canInteract(novaLogin$play)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleContainerClick", at = @At("HEAD"), cancellable = true)
    public void onPlayerAction5(ServerboundContainerClickPacket arg, CallbackInfo ci) {
        if (!OnPlayerAction.canInteract(novaLogin$play)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(ServerboundChatPacket packet, CallbackInfo ci) {
        if (!OnGameMessage.canSendMessage(novaLogin$play, packet)) {
            ci.cancel();
        }
    }
}
