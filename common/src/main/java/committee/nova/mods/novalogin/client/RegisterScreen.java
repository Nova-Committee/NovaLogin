package committee.nova.mods.novalogin.client;

import com.mojang.realmsclient.RealmsMainScreen;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.save.LocalUserSave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

/**
 * LoginScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/27 上午12:07
 */
public abstract class RegisterScreen extends Screen {
    private Button registerButton;
    private CycleButton<Boolean> pwdFormattedButton;
    private boolean pwdVisible = false;

    public Checkbox rememberPassword;
    public EditBox usernameField;
    public EditBox passwordField;
    public EditBox confirmPasswordField;
    private static final Component USERNAME_LABEL = Component.translatable("info.novalogin.gui.username");
    private static final Component PASSWORD_LABEL = Component.translatable("info.novalogin.gui.password");
    private static final Component CONFIRM_PASSWORD_LABEL = Component.translatable("info.novalogin.gui.confirm_password");

    private Screen parentScreen;
    protected RegisterScreen(Screen parentScreen) {
        super(Component.translatable("info.novalogin.gui.register"));
        this.parentScreen = parentScreen;
    }

    @Override
    public void tick() {
        this.usernameField.tick();
        this.passwordField.tick();
        this.confirmPasswordField.tick();
    }

    @Override
    protected void init() {
        String username = this.minecraft.player.getGameProfile().getName();

        this.usernameField = new EditBox(this.font, this.width / 2 - 100, 60, 200, 20, USERNAME_LABEL);
        this.usernameField.setEditable(false);
        this.usernameField.setMaxLength(50);
        this.usernameField.setValue(username);
        this.usernameField.setResponder(string -> this.updateAddButtonStatus());
        this.addWidget(this.usernameField);

        this.passwordField = new EditBox(this.font, this.width / 2 - 100, 100, 200, 20, PASSWORD_LABEL);
        this.passwordField.setMaxLength(50);
        this.passwordField.setFormatter((s, integer) -> {
            if (this.pwdVisible) {
               return FormattedCharSequence.forward(s, Style.EMPTY);
            } else {
                return FormattedCharSequence.forward("•".repeat(s.length()), Style.EMPTY);
            }
        });
        this.usernameField.setFocused(true);
        this.passwordField.setValue("");
        this.passwordField.setResponder(string -> this.updateAddButtonStatus());
        this.addWidget(this.passwordField);

        this.confirmPasswordField = new EditBox(this.font, this.width / 2 - 100, 140, 200, 20, CONFIRM_PASSWORD_LABEL);
        this.confirmPasswordField.setMaxLength(50);
        this.confirmPasswordField.setFormatter((s, integer) -> {
            if (this.pwdVisible) {
                return FormattedCharSequence.forward(s, Style.EMPTY);
            } else {
                return FormattedCharSequence.forward("•".repeat(s.length()), Style.EMPTY);
            }
        });
        this.confirmPasswordField.setValue("");
        this.confirmPasswordField.setResponder(string -> this.updateAddButtonStatus());
        this.addWidget(this.confirmPasswordField);


        this.pwdFormattedButton = CycleButton.booleanBuilder(Component.translatable("info.novalogin.gui.pwd_visible"), Component.translatable("info.novalogin.gui.pwd_invisible"))
                .displayOnlyValue()
                .withInitialValue(pwdVisible)
                .create(this.width / 2 + 100, 140, 20, 20, Component.translatable("info.novalogin.gui.pwd_visible_s"), (cycleButton, aBoolean) -> this.pwdVisible = aBoolean);
        this.addRenderableWidget(this.pwdFormattedButton);


        this.registerButton = this.addRenderableWidget(
                Button.builder(Component.translatable("info.novalogin.gui.register_login"), button -> this.onRegister())
                        .bounds(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20)
                        .build());

        this.rememberPassword = new Checkbox(this.width / 2 + 100, this.height / 4 + 96 + 18, 20, 20, Component.translatable("info.novalogin.gui.remember_password"), true);
        this.addRenderableWidget(this.rememberPassword);
        Const.configHandler.config.getCommon().setLoadLocalPwd(this.rememberPassword.selected());

        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_BACK, button -> this.minecraft.setScreen(this.parentScreen))
                        .bounds(this.width / 2 - 100, this.height / 4 + 120 + 18, 100, 20)
                        .build());

        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
                    boolean bl = this.minecraft.isLocalServer();
                    boolean bl2 = this.minecraft.isConnectedToRealms();
                    button.active = false;
                    this.minecraft.level.disconnect();
                    if (bl) {
                        this.minecraft.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
                    } else {
                        this.minecraft.clearLevel();
                    }
                    TitleScreen titleScreen = new TitleScreen();
                    if (bl) {
                        this.minecraft.setScreen(titleScreen);
                    } else if (bl2) {
                        this.minecraft.setScreen(new RealmsMainScreen(titleScreen));
                    } else {
                        this.minecraft.setScreen(new JoinMultiplayerScreen(titleScreen));
                    }
                }).bounds(this.width / 2, this.height / 4 + 120 + 18, 100, 20).build());
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int i, int j) {
        String string = this.usernameField.getValue();
        String string2 = this.passwordField.getValue();
        String string3 = this.confirmPasswordField.getValue();
        this.init(minecraft, i, j);
        this.usernameField.setValue(string);
        this.passwordField.setValue(string2);
        this.confirmPasswordField.setValue(string3);
    }

    @Override
    public void render(@NotNull GuiGraphics poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        poseStack.drawCenteredString(this.font, this.title, this.width / 2, 17, 0xFFFFFF);
        poseStack.drawString(this.font, USERNAME_LABEL, this.width / 2 - 100, 47, 0xA0A0A0);
        poseStack.drawString(this.font, PASSWORD_LABEL, this.width / 2 - 100, 88, 0xA0A0A0);
        poseStack.drawString(this.font, CONFIRM_PASSWORD_LABEL, this.width / 2 - 100, 129, 0xA0A0A0);
        this.usernameField.render(poseStack, i, j, f);
        this.passwordField.render(poseStack, i, j, f);
        this.confirmPasswordField.render(poseStack, i, j, f);
        super.render(poseStack, i, j, f);
    }

    private void updateAddButtonStatus() {
        this.registerButton.active = !this.passwordField.getValue().isEmpty();
    }

    protected void onRegister() {
        if (this.rememberPassword.selected() && !LocalUserSave.containsUser(this.usernameField.getValue())) {
            LocalUserSave.setUser(this.usernameField.getValue(), this.confirmPasswordField.getValue());
            LocalUserSave.save();
        }
    }
}
