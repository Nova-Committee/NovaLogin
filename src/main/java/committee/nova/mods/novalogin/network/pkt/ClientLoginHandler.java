package committee.nova.mods.novalogin.network.pkt;

import committee.nova.mods.novalogin.client.LoginScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ClientLoginHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/8 上午1:52
 */
public class ClientLoginHandler implements IMessageHandler<ClientLoginPkt, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(ClientLoginPkt pkt, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (pkt.screenId.equals("login")) {
                Minecraft.getMinecraft().displayGuiScreen(new LoginScreen());
            }
            else {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        });
        return null;
    }
}