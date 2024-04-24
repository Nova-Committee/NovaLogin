//package committee.nova.mods.novalogin.mixins;
//
//import committee.nova.mods.novalogin.network.NetWorkDispatcher;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientPacketListener;
//import net.minecraft.network.protocol.game.ClientboundLoginPacket;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
///**
// * ClientPacketListenerMixin
// *
// * @author cnlimiter
// * @version 1.0
// * @description
// * @date 2024/3/18 18:54
// */
//@Mixin(ClientPacketListener.class)
//public abstract class ClientPacketListenerMixin {
//
//    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
//    public void novalogin$handleLogin(ClientboundLoginPacket arg, CallbackInfo ci){
//        int mode;
//        switch (Minecraft.getInstance().getUser().getType()){
//            case MSA -> mode = 2;
//            case MOJANG -> mode = 1;
//            case LEGACY -> mode = 0;
//            default -> mode = -1;
//        }
//        NetWorkDispatcher.sendLoginModeToServer(Minecraft.getInstance().getUser().getName(), mode);
//    }
//
//}
