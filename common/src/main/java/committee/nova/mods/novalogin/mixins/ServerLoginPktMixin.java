package committee.nova.mods.novalogin.mixins;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.MojangResponse;
import committee.nova.mods.novalogin.utils.HttpUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

import static committee.nova.mods.novalogin.Const.*;

/**
 * ServerLoginPktMixin
 *
 * @author cnlimiter
 * @version 1.0
 * @description from EasyAuth
 * @date 2024/3/19 13:02
 */
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPktMixin {

    @Shadow
    @Final
    MinecraftServer server;

    @Shadow
    ServerLoginPacketListenerImpl.State state;

    @Shadow @Nullable
    GameProfile gameProfile;

    @Shadow @Final public Connection connection;

    @Shadow @Final private byte[] nonce;

    @Inject(
            method = "handleHello",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;getGameProfile()Lcom/mojang/authlib/GameProfile;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void novalogin$handleHello(ServerboundHelloPacket pPacket, CallbackInfo ci) {
        try {
            this.gameProfile = pPacket.getGameProfile();
            String playerName = pPacket.getGameProfile().getName();
            Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]{6,25}$");
            Validate.validState(!pattern.matcher(playerName).matches(), "Invalid characters in username");

            String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

            var con = HttpUtils.connect(url, 5000, null);
            int code = con.getResponseCode();
            String msg = HttpUtils.getResponseMsg(con);
            con.disconnect();

            if (server.usesAuthentication()) {
                //mojangAccountNamesCache.forEach(Const.LOGGER::info);
                if (mojangAccountNamesCache.contains(playerName)) {
                    LOGGER.info("Player {} is cached as online player. Authentication continues as vanilla", playerName);
                    mojangAccountNamesCache.add(playerName);
                    this.state = ServerLoginPacketListenerImpl.State.KEY;
                    this.connection.send(new ClientboundHelloPacket("", this.server.getKeyPair().getPublic().getEncoded(), this.nonce));
                } else {
                    novaLogin$useOnlineProfile(code, msg, playerName);
                }
            } else {
                if (CONFIG.config.isUuidTrans()){
                    novaLogin$useOnlineProfile(code, msg, playerName);
                } else {
                    novaLogin$useOfflineProfile(playerName);
                }
            }
            ci.cancel();
        } catch (Exception e) {
            LOGGER.error("Check error {}", e.getMessage());
        }
    }


    @Unique
    private void novaLogin$useOfflineProfile(String playerName) {
        this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        this.gameProfile = novaLogin$createOfflineProfile(playerName);
    }

    @Unique
    private void novaLogin$useOnlineProfile(int code, String msg, String playerName) {
        GameProfile gameProfile = novaLogin$createOfflineProfile(playerName);
        if (code == HttpURLConnection.HTTP_OK) {
            var re = GSON.fromJson(JsonParser.parseString(msg), MojangResponse.class);
            StringBuilder uuid = new StringBuilder(re.getId());
            uuid.insert(8,"-");
            uuid.insert(12,"-");
            uuid.insert(16,"-");
            uuid.insert(20,"-");

            LOGGER.info("Player {} has a Mojang account, use online UUID {}", playerName, uuid);

            gameProfile = new GameProfile(UUID.fromString(uuid.toString()), playerName);
        }
        this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
        this.gameProfile = gameProfile;
    }


    @Unique
    protected GameProfile novaLogin$createOfflineProfile(String name) {
        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        return new GameProfile(uuid, name);
    }

}
