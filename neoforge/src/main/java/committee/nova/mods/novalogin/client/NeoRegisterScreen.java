package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.net.server.ServerRegisterActionPkt;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * ForgeRegisterScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午10:31
 */
@OnlyIn(Dist.CLIENT)
public class NeoRegisterScreen extends RegisterScreen{
    protected NeoRegisterScreen(Screen parentScreen) {
        super(parentScreen);
    }

    @Override
    protected void onRegister() {
        PacketDistributor.sendToServer(new ServerRegisterActionPkt(this.usernameField.getValue(), this.passwordField.getValue(), this.confirmPasswordField.getValue()));
        super.onRegister();
    }
}
