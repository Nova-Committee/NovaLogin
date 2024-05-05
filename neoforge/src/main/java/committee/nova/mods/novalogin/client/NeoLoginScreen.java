package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.net.server.ServerLoginActionPkt;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * ForgeLoginScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午1:05
 */
@OnlyIn(Dist.CLIENT)
public class NeoLoginScreen extends LoginScreen{
    @Override
    protected void onAdd() {
        PacketDistributor.sendToServer(new ServerLoginActionPkt(this.usernameField.getValue(), this.passwordField.getValue()));
    }

    @Override
    protected void onRegister(Minecraft minecraft) {
        minecraft.setScreen(new NeoRegisterScreen(this));
    }
}
