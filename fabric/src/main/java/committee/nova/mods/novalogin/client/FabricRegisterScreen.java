package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.network.ClientNetWorkHandler;
import net.minecraft.client.gui.screens.Screen;

/**
 * FabricRegisterScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午10:29
 */
public class FabricRegisterScreen extends RegisterScreen{
    protected FabricRegisterScreen(Screen parentScreen) {
        super(parentScreen);
    }

    @Override
    protected void onRegister() {
        ClientNetWorkHandler.sendRegisterActionToServer(this.usernameField.getValue(), this.passwordField.getValue(), this.confirmPasswordField.getValue());
        super.onRegister();
    }
}
