package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

/**
 * ForgeLoginScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/28 上午1:05
 */
@OnlyIn(Dist.CLIENT)
public class ForgeLoginScreen extends LoginScreen{
    @Override
    protected void onAdd() {
        NetWorkDispatcher.CHANNEL.send(new ServerLoginActionPkt(this.usernameField.getValue(), this.passwordField.getValue()), PacketDistributor.SERVER.noArg());
    }

    @Override
    protected void onRegister(Minecraft minecraft) {
        minecraft.setScreen(new ForgeRegisterScreen(this));
    }
}
