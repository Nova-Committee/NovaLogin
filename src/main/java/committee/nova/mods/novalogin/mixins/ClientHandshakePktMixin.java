package committee.nova.mods.novalogin.mixins;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.util.CryptManager;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.PublicKey;


/**
 * ClientPacketListenerMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 18:54
 */
@Mixin(NetHandlerLoginClient.class)
public abstract class ClientHandshakePktMixin {

    @Shadow @Final private Minecraft mc;

    @Shadow protected abstract MinecraftSessionService getSessionService();

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private NetworkManager networkManager;

    @Inject(method = "handleEncryptionRequest", at = @At(value = "HEAD"),
            cancellable = true)
    public void novalogin$handleLogin(SPacketEncryptionRequest packet, CallbackInfo ci){
        final SecretKey secretkey = CryptManager.createNewSharedKey();
        String s = packet.getServerId();
        PublicKey publickey = packet.getPublicKey();
        String s1 = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);
        if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().isOnLAN()) {
            try {
                this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), s1);
            } catch (AuthenticationException var10) {
                LOGGER.warn("Couldn't connect to auth servers but will continue to join LAN");
            }
        } else {
            try {
                this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(), s1);
            } catch (AuthenticationUnavailableException var7) {
                this.networkManager.closeChannel(new TextComponentTranslation("disconnect.loginFailedInfo", new TextComponentTranslation("disconnect.loginFailedInfo.serversUnavailable", new Object[0])));
                return;
            } catch (InvalidCredentialsException ignored) {
            } catch (AuthenticationException var9) {
                this.networkManager.closeChannel(new TextComponentTranslation("disconnect.loginFailedInfo", var9.getMessage()));
                return;
            }
        }

        this.networkManager.sendPacket(new CPacketEncryptionResponse(secretkey, publickey, packet.getVerifyToken()), p_operationComplete_1_ -> this.networkManager.enableEncryption(secretkey));
        ci.cancel();
    }

}
