package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.net.ServerRegisterActionPkt;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

/**
 * ForgeRegisterScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午10:31
 */
@OnlyIn(Dist.CLIENT)
public class ForgeRegisterScreen extends RegisterScreen{
    protected ForgeRegisterScreen(Screen parentScreen) {
        super(parentScreen);
    }

    @Override
    protected void onRegister() {
        NetWorkDispatcher.CHANNEL.send(new ServerRegisterActionPkt(this.usernameField.getValue(), this.passwordField.getValue(), this.confirmPasswordField.getValue()), PacketDistributor.SERVER.noArg());
        super.onRegister();
    }
}
