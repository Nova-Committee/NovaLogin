package committee.nova.mods.novalogin.mixins;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import committee.nova.mods.novalogin.models.MojangResponse;
import committee.nova.mods.novalogin.utils.HttpUtils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.net.HttpURLConnection;
import java.util.UUID;
import java.util.regex.Matcher;
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

    @Shadow @Nullable
    public GameProfile gameProfile;

    @Shadow
    public ServerLoginPacketListenerImpl.State state;

    @Inject(
            method = "handleHello",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;usesAuthentication()Z",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void novalogin$handleHello(ServerboundHelloPacket pPacket, CallbackInfo ci) {
        try {
            String playerName = pPacket.name();
            Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]{6,16}$");
            Matcher matcher = pattern.matcher(playerName);
            if (server.usesAuthentication()) {

                String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

                var con = HttpUtils.connect(url, 5000, null);
                int code = con.getResponseCode();
                String msg = HttpUtils.getResponseMsg(con);
                con.disconnect();
                //mojangAccountNamesCache.forEach(Const.LOGGER::info);

                if (mojangAccountNamesCache.contains(playerName)) {
                    LOGGER.info("Player {} is cached as online player. Authentication continues as vanilla", playerName);
                    mojangAccountNamesCache.add(playerName);
                    return;
                }

                GameProfile gameProfile = novaLogin$createOfflineProfile(playerName);
                if (code == HttpURLConnection.HTTP_OK) {
                    var re = GSON.fromJson(JsonParser.parseString(msg), MojangResponse.class);
                    StringBuilder uuid = new StringBuilder(re.getId());
                    uuid.insert(8,"-");
                    uuid.insert(12,"-");
                    uuid.insert(16,"-");
                    uuid.insert(20,"-");

                    LOGGER.info("Player {} {} has a Mojang account", playerName, uuid);

                    gameProfile = new GameProfile(UUID.fromString(uuid.toString()), playerName);
                }
                this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                this.gameProfile = gameProfile;
                ci.cancel();

            } else {
                this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
                this.gameProfile = novaLogin$createOfflineProfile(playerName);
                ci.cancel();
            }
        } catch (Exception e) {
            LOGGER.error("check error {}", e.getMessage());
        }
    }

    @Unique
    protected GameProfile novaLogin$createOfflineProfile(String name) {
        UUID uuid = UUIDUtil.createOfflinePlayerUUID(name);
        return new GameProfile(uuid, name);
    }
}
