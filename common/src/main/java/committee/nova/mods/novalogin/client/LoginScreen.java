package committee.nova.mods.novalogin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.RealmsMainScreen;
import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.save.LocalUserSave;
import lombok.Data;
import net.minecraft.client.Minecraft;
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
public abstract class LoginScreen extends Screen {
    private Button loginButton;
    private CycleButton<Boolean> pwdFormattedButton;
    private boolean pwdVisible = false;

    public EditBox usernameField;
    public EditBox passwordField;
    private static final Component USERNAME_LABEL = Component.translatable("info.novalogin.gui.username");
    private static final Component PASSWORD_LABEL = Component.translatable("info.novalogin.gui.password");

    protected LoginScreen() {
        super(Component.translatable("info.novalogin.gui.login"));
    }

    @Override
    public void tick() {
        this.usernameField.tick();
        this.passwordField.tick();
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        String username = this.minecraft.player.getGameProfile().getName();

        this.usernameField = new EditBox(this.font, this.width / 2 - 100, 66, 200, 20, USERNAME_LABEL);
        this.usernameField.setEditable(false);
        this.usernameField.setMaxLength(50);
        this.usernameField.setValue(username);
        this.usernameField.setResponder(string -> this.updateAddButtonStatus());
        this.addWidget(this.usernameField);

        this.pwdFormattedButton = CycleButton.booleanBuilder(Component.translatable("info.novalogin.gui.pwd_visible"), Component.translatable("info.novalogin.gui.pwd_invisible"))
                .displayOnlyValue()
                .withInitialValue(pwdVisible)
                .create(this.width / 2 + 100, 106, 20, 20, Component.translatable("info.novalogin.gui.pwd_visible_s"), (cycleButton, aBoolean) -> this.pwdVisible = aBoolean);
        this.addRenderableWidget(this.pwdFormattedButton);

        this.passwordField = new EditBox(this.font, this.width / 2 - 100, 106, 200, 20, PASSWORD_LABEL);
        this.passwordField.setMaxLength(50);
        if (Const.configHandler.config.getCommon().isLoadLocalPwd()){
            if (LocalUserSave.containsUser(username)) this.passwordField.setValue(LocalUserSave.getUserPwd(username) != null ? LocalUserSave.getUserPwd(username) : "password");
            else this.passwordField.setValue("password");
        } else {
            this.passwordField.setValue("password");
        }
        this.passwordField.setFormatter((s, integer) -> {
            if (this.pwdVisible) {
               return FormattedCharSequence.forward(s, Style.EMPTY);
            } else {
                return FormattedCharSequence.forward("•".repeat(s.length()), Style.EMPTY);
            }
        });
        this.usernameField.setFocus(true);
        this.passwordField.setResponder(string -> this.updateAddButtonStatus());
        this.addWidget(this.passwordField);

        this.loginButton = this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20,
                Component.translatable("info.novalogin.gui.login"), button -> this.onAdd()));

        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 120 + 18, 100, 20,
                Component.translatable("info.novalogin.gui.register"), button -> this.onRegister(minecraft)
        ));

        this.addRenderableWidget(new Button(this.width / 2, this.height / 4 + 120 + 18, 100, 20,
                CommonComponents.GUI_CANCEL, button -> {
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
        }));
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int i, int j) {
        String string = this.usernameField.getValue();
        String string2 = this.passwordField.getValue();
        this.init(minecraft, i, j);
        this.usernameField.setValue(string);
        this.passwordField.setValue(string2);
    }

    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 17, 0xFFFFFF);
        drawString(poseStack, this.font, USERNAME_LABEL, this.width / 2 - 100, 53, 0xA0A0A0);
        drawString(poseStack, this.font, PASSWORD_LABEL, this.width / 2 - 100, 94, 0xA0A0A0);
        this.usernameField.render(poseStack, i, j, f);
        this.passwordField.render(poseStack, i, j, f);
        super.render(poseStack, i, j, f);
    }

    private void updateAddButtonStatus() {
        this.loginButton.active = !this.passwordField.getValue().isEmpty();
    }

    protected void onAdd() {

    }

    protected void onRegister(Minecraft minecraft) {

    }
}
