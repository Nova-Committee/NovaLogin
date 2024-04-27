package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.network.NetWorkDispatcher;

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
}
