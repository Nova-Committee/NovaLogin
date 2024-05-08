package committee.nova.mods.novalogin.client;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.network.NetWorkDispatcher;
import committee.nova.mods.novalogin.network.pkt.ServerRegisterActionPkt;
import committee.nova.mods.novalogin.save.LocalUserSave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * RegisterScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/7 下午11:12
 */
@SideOnly(Side.CLIENT)
public class RegisterScreen extends GuiScreen {
    private GuiButton registerButton;
    private GuiCheckBox pwdFormattedButton;
    private boolean pwdVisible = false;

    public GuiCheckBox rememberPassword;
    public GuiTextField usernameField;
    public GuiTextField passwordField;
    public GuiTextField confirmPasswordField;
    private static final String USERNAME_LABEL = I18n.format("info.novalogin.gui.username");
    private static final String PASSWORD_LABEL = I18n.format("info.novalogin.gui.password");
    private static final String CONFIRM_PASSWORD_LABEL = I18n.format("info.novalogin.gui.confirm_password");

    private final GuiScreen parentScreen;
    public RegisterScreen(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }


    @Override
    public void updateScreen()
    {
        this.usernameField.updateCursorCounter();
        this.passwordField.updateCursorCounter();
        this.confirmPasswordField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        String username = this.mc.player.getGameProfile().getName();
        this.buttonList.clear();

        this.usernameField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        this.usernameField.setMaxStringLength(50);
        this.usernameField.setText(username);

        this.passwordField = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, 100, 200, 20);
        this.passwordField.setMaxStringLength(50);
        this.passwordField.setText(pwdVisible ? "password" : StringUtils.repeat("•", "password".length()));
        this.passwordField.setFocused(true);

        this.confirmPasswordField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 100, 140, 200, 20);
        this.confirmPasswordField.setMaxStringLength(50);
        this.confirmPasswordField.setText(pwdVisible ? "password" : StringUtils.repeat("•", "password".length()));

        this.pwdFormattedButton = this.addButton(new GuiCheckBox(4, this.width / 2 + 100, 140, I18n.format("info.novalogin.gui.pwd_visible_s"), true));

        this.registerButton = this.addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20, I18n.format("info.novalogin.gui.register_login")));

        this.rememberPassword = this.addButton(new GuiCheckBox(1, this.width / 2 + 100, this.height / 4 + 96 + 18,  I18n.format("info.novalogin.gui.remember_password"), true));

        this.addButton(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120 + 18, 100, 20, I18n.format("gui.back")));

        this.addButton(new GuiButton(3, this.width / 2, this.height / 4 + 120 + 18, 100, 20, I18n.format("gui.cancel")));

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                NetWorkDispatcher.INSTANCE.sendToServer(new ServerRegisterActionPkt(this.usernameField.getText(), this.passwordField.getText(), this.confirmPasswordField.getText()));
                if (this.rememberPassword.isChecked() && !LocalUserSave.containsUser(this.usernameField.getText())) {
                    LocalUserSave.setUser(this.usernameField.getText(), this.confirmPasswordField.getText());
                    LocalUserSave.save();
                }
            }
            else if (button.id == 1)
            {
                Const.configHandler.config.getCommon().setLoadLocalPwd(!this.rememberPassword.isChecked());
                this.rememberPassword.setIsChecked(!this.rememberPassword.isChecked());
            }
            else if (button.id == 4)
            {
                this.pwdVisible = this.pwdFormattedButton.isChecked();
            }
            else if (button.id == 2)
            {
                this.mc.displayGuiScreen(parentScreen);
            }
            else if (button.id == 3)
            {
                boolean flag = this.mc.isIntegratedServerRunning();
                boolean flag1 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.world.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);

                if (flag)
                {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                }
                else if (flag1)
                {
                    RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new GuiMainMenu());
                }
                else
                {
                    this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                }
            }
        }
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onResize(Minecraft minecraft, int x, int y) {
        String string = this.usernameField.getText();
        String string2 = this.passwordField.getText();
        String string3 = this.confirmPasswordField.getText();
        super.onResize(minecraft, x, y);
        this.usernameField.setText(string);
        this.passwordField.setText(string2);
        this.confirmPasswordField.setText(string3);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("info.novalogin.gui.register"), this.width / 2, 17, 0xFFFFFF);
        this.drawString(this.fontRenderer, USERNAME_LABEL, this.width /  2 - 100, 47, 0xA0A0A0);
        this.drawString(this.fontRenderer, PASSWORD_LABEL, this.width / 2 - 100, 88, 0xA0A0A0);
        this.drawString(this.fontRenderer, CONFIRM_PASSWORD_LABEL, this.width / 2 - 100, 129, 0xA0A0A0);
        this.usernameField.drawTextBox();
        this.passwordField.drawTextBox();
        this.confirmPasswordField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
