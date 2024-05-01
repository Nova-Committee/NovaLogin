package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.net.ServerRegisterActionPkt;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import committee.nova.mods.novalogin.save.LocalUserSave;
import net.minecraft.client.gui.screens.Screen;

/**
 * ForgeRegisterScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午10:31
 */
public class ForgeRegisterScreen extends RegisterScreen{
    protected ForgeRegisterScreen(Screen parentScreen) {
        super(parentScreen);
    }

    @Override
    protected void onRegister() {
        NetWorkDispatcher.CHANNEL.sendToServer(new ServerRegisterActionPkt(this.usernameField.getValue(), this.passwordField.getValue(), this.confirmPasswordField.getValue()));
        super.onRegister();
    }
}
