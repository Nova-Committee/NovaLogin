package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.client.FabricLoginScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * ForgeClientLoginHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/29 下午11:39
 */
@Environment(EnvType.CLIENT)
public class FabricClientLoginHandler {
    public static void handlePacket(Minecraft minecraft) {
        minecraft.setScreen(new FabricLoginScreen());
    }
}