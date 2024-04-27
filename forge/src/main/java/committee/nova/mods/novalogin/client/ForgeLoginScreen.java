package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.net.ServerLoginActionPkt;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;

/**
 * ForgeLoginScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午1:05
 */
public class ForgeLoginScreen extends LoginScreen{
    @Override
    protected void onAdd() {
        NetWorkDispatcher.CHANNEL.sendToServer(new ServerLoginActionPkt(this.usernameField.getValue(), this.passwordField.getValue()));
    }
}
