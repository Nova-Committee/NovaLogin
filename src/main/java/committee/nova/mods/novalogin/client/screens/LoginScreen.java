package committee.nova.mods.novalogin.client.screens;

import committee.nova.mods.novalogin.network.pkt.server.ServerLoginModePkt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * LoginScreen
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 19:51
 */
@OnlyIn(Dist.CLIENT)
public class LoginScreen extends Screen {

    private final Screen previous;
    private ServerData serverData;
    public EditBox pwdIn;
    private Button loginButton;
    private Button registerButton;
    @Nullable
    protected Checkbox pwdSave;
    @Nullable
    protected Checkbox noRegister;

    private static final Component PWD_SAVE = new TranslatableComponent("novalogin.info.screen.login.pwd_save");
    private static final Component NO_REG = new TranslatableComponent("novalogin.info.screen.login.no_reg");
    private static final Component PWD = new TranslatableComponent("novalogin.info.screen.login.pwd");

    private static final Component REG_LABEL = new TranslatableComponent("novalogin.info.screen.login.label.reg");
    private static final Component LOG_LABEL = new TranslatableComponent("novalogin.info.screen.login.label.log");


    public LoginScreen(Screen previous, ServerData serverData) {
        super(new TranslatableComponent("novalogin.info.screen.login.title"));
        this.previous = previous;
        this.serverData = serverData;
    }

    @Override
    protected void init() {
        super.init();
        assert this.minecraft != null;
        assert this.minecraft.player != null;
        this.pwdIn = new EditBox(this.font, this.width / 2 - 100, 66, 240, 20, PWD);
        this.pwdIn.setValue("");
        this.pwdIn.setResponder(string -> this.updateAddButtonStatus());
        this.addWidget(this.pwdIn);

        this.pwdSave = this.addRenderableWidget(
                Checkbox.builder(PWD_SAVE, this.font)
                        .pos(this.width / 2 - 100, 76)
                        .build()
        );

        this.noRegister = this.addRenderableWidget(
                Checkbox.builder(NO_REG, this.font)
                        .pos(this.width / 2 - 130, 76)
                        .selected(false)
                        .onValueChange((pCheckbox, pValue) -> {
                                    if (pValue) this.registerButton.active = true;
                                }
                        ).build()
        );



        this.loginButton = this.addRenderableWidget(
                Button.builder(LOG_LABEL, button -> {
                            if (this.pwdSave.selected()) {

                            }
                            //this.minecraft.player.connection.send(new ServerLoginModePkt(this.pwdIn.getValue(), true));
                        }).bounds(this.width / 2 - 260, 100, 150, 20)
                        .build()
        );

        this.registerButton = this.addRenderableWidget(
                Button.builder(REG_LABEL, button -> {
                            //this.minecraft.player.connection.send(new ServerLoginModePkt(this.pwdIn.getValue(), false));
                        })
                        .bounds(this.width / 2 - 100, 100, 150, 20)
                        .build()
        );

        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_BACK, button -> this.minecraft.setScreen(this.previous))
                        .bounds(this.width / 2 - 100 + 160, 100, 150, 20)
                        .build()
        );

    }

    private void updateAddButtonStatus() {
        this.loginButton.active = !this.pwdIn.getValue().isEmpty();
    }

    @Override
    public void resize(@NotNull Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.pwdIn.getValue();
        this.init(pMinecraft, pWidth, pHeight);
        this.pwdIn.setValue(s);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTitle(pGuiGraphics);
    }

    protected void renderTitle(GuiGraphics pGuiGraphics) {
        pGuiGraphics.drawString(this.font, this.title, 25, 30, 16777215);
    }

}
