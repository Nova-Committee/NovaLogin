package committee.nova.mods.novalogin.client.widgets;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * ModCheckbox
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/1 下午2:17
 */
public class ModCheckbox extends Checkbox {
    public ModCheckbox(int x, int y, int w, int h, Component component, boolean select) {
        super(x, y, w, h, component, select);
    }

    public void updateNarration(NarrationElementOutput $$0) {
        $$0.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                $$0.add(NarratedElementType.USAGE, new TranslatableComponent("narration.checkbox.usage.focused"));
            } else {
                $$0.add(NarratedElementType.USAGE, new TranslatableComponent("narration.checkbox.usage.hovered"));
            }
        }

    }
}
