package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.minecraft.client.Minecraft;

/**
 * FabricLoginScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午12:54
 */
public class FabricLoginScreen extends LoginScreen{

    @Override
    protected void onAdd() {
        NetWorkDispatcher.sendLoginActionToServer(this.usernameField.getValue(), this.passwordField.getValue());
    }

    @Override
    protected void onRegister(Minecraft minecraft) {
        minecraft.setScreen(new FabricRegisterScreen(this));
    }
}
