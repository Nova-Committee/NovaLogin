package committee.nova.mods.novalogin.client.widgets;

/**
 * CycleButton
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/6 上午1:30
 */

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.TooltipAccessor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class CycleButton extends AbstractButton implements TooltipAccessor {
    static final BooleanSupplier DEFAULT_ALT_LIST_SELECTOR = Screen::hasAltDown;
    private static final List<Boolean> BOOLEAN_OPTIONS;
    private final Component name;
    private int index;
    @Getter
    private Boolean value;
    private final ValueListSupplier values;
    private final Function<Boolean, Component> valueStringifier;
    private final OnValueChange  onValueChange;
    private final TooltipSupplier  tooltipSupplier;
    private final boolean displayOnlyValue;

    CycleButton(int $$0, int $$1, int $$2, int $$3, Component $$4, Component $$5, int $$6, Boolean $$7, ValueListSupplier $$8, Function<Boolean, Component> $$9, OnValueChange  $$11, TooltipSupplier  $$12, boolean $$13) {
        super($$0, $$1, $$2, $$3, $$4);
        this.name = $$5;
        this.index = $$6;
        this.value = $$7;
        this.values = $$8;
        this.valueStringifier = $$9;
        this.onValueChange = $$11;
        this.tooltipSupplier = $$12;
        this.displayOnlyValue = $$13;
    }

    public void onPress() {
        if (Screen.hasShiftDown()) {
            this.cycleValue(-1);
        } else {
            this.cycleValue(1);
        }

    }

    private void cycleValue(int $$0) {
        List<Boolean> $$1 = this.values.getSelectedList();
        this.index = Mth.positiveModulo(this.index + $$0, $$1.size());
        Boolean $$2 = $$1.get(this.index);
        this.updateValue($$2);
        this.onValueChange.onValueChange(this, $$2);
    }

    private Boolean getCycledValue(int $$0) {
        List<Boolean> $$1 = this.values.getSelectedList();
        return $$1.get(Mth.positiveModulo(this.index + $$0, $$1.size()));
    }

    public boolean mouseScrolled(double $$0, double $$1, double $$2) {
        if ($$2 > 0.0) {
            this.cycleValue(-1);
        } else if ($$2 < 0.0) {
            this.cycleValue(1);
        }

        return true;
    }

    public void setValue(Boolean $$0) {
        List<Boolean> $$1 = this.values.getSelectedList();
        int $$2 = $$1.indexOf($$0);
        if ($$2 != -1) {
            this.index = $$2;
        }

        this.updateValue($$0);
    }

    private void updateValue(Boolean $$0) {
        this.setMessage(this.valueStringifier.apply($$0));
        this.value = $$0;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return new TextComponent("");
    }


    @Override
    public Optional<List<FormattedCharSequence>> getTooltip() {
        return Optional.ofNullable(this.tooltipSupplier.apply(this.value));
    }

    public static Builder  builder(Function<Boolean, Component> $$0) {
        return new Builder($$0);
    }

    public static Builder  booleanBuilder(Component $$0, Component $$1) {
        return (new Builder(($$2) -> {
            return $$2 ? $$0 : $$1;
        })).withValues(BOOLEAN_OPTIONS);
    }

    static {
        BOOLEAN_OPTIONS = ImmutableList.of(Boolean.TRUE, Boolean.FALSE);
    }

    @OnlyIn(Dist.CLIENT)
    private interface ValueListSupplier {
        List<java.lang.Boolean> getSelectedList();

        List<java.lang.Boolean> getDefaultList();

        static ValueListSupplier create(List<Boolean> $$0) {
            final List<Boolean> $$1 = ImmutableList.copyOf($$0);
            return new ValueListSupplier() {
                public List<java.lang.Boolean> getSelectedList() {
                    return $$1;
                }

                public List<java.lang.Boolean> getDefaultList() {
                    return $$1;
                }
            };
        }

        static <Boolean> ValueListSupplier create(final BooleanSupplier $$0, List<java.lang.Boolean> $$1, List<java.lang.Boolean> $$2) {
            final List<java.lang.Boolean> $$3 = ImmutableList.copyOf($$1);
            final List<java.lang.Boolean> $$4 = ImmutableList.copyOf($$2);
            return new ValueListSupplier() {
                public List<java.lang.Boolean> getSelectedList() {
                    return $$0.getAsBoolean() ? $$4 : $$3;
                }

                public List<java.lang.Boolean> getDefaultList() {
                    return $$3;
                }
            };
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnValueChange{
        void onValueChange(CycleButton var1, Boolean var2);
    }

    @FunctionalInterface
    @OnlyIn(Dist.CLIENT)
    public interface TooltipSupplier extends Function<Boolean, List<FormattedCharSequence>> {
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder{
        private int initialIndex;
        @Nullable
        private Boolean initialValue;
        private final Function<Boolean, Component> valueStringifier;
        private TooltipSupplier tooltipSupplier = ($$0x) -> {
            return ImmutableList.of();
        };
        private ValueListSupplier values = CycleButton.ValueListSupplier.create(ImmutableList.of());
        private boolean displayOnlyValue;

        public Builder(Function<Boolean, Component> $$0) {
            this.valueStringifier = $$0;
        }

        public Builder withValues(List<Boolean> $$0) {
            this.values = CycleButton.ValueListSupplier.create($$0);
            return this;
        }

        @SafeVarargs
        public final Builder withValues(Boolean... $$0) {
            return this.withValues(ImmutableList.copyOf($$0));
        }

        public Builder withValues(List<Boolean> $$0, List<Boolean> $$1) {
            this.values = CycleButton.ValueListSupplier.create(CycleButton.DEFAULT_ALT_LIST_SELECTOR, $$0, $$1);
            return this;
        }

        public Builder withValues(BooleanSupplier $$0, List<Boolean> $$1, List<Boolean> $$2) {
            this.values = CycleButton.ValueListSupplier.create($$0, $$1, $$2);
            return this;
        }

        public Builder withTooltip(TooltipSupplier $$0) {
            this.tooltipSupplier = $$0;
            return this;
        }

        public Builder withInitialValue(Boolean $$0) {
            this.initialValue = $$0;
            int $$1 = this.values.getDefaultList().indexOf($$0);
            if ($$1 != -1) {
                this.initialIndex = $$1;
            }

            return this;
        }

        public Builder  displayOnlyValue() {
            this.displayOnlyValue = true;
            return this;
        }

        public CycleButton  create(int $$0, int $$1, int $$2, int $$3, Component $$4) {
            return this.create($$0, $$1, $$2, $$3, $$4, ($$0x, $$1x) -> {
            });
        }

        public CycleButton  create(int $$0, int $$1, int $$2, int $$3, Component $$4, OnValueChange  $$5) {
            List<Boolean> $$6 = this.values.getDefaultList();
            if ($$6.isEmpty()) {
                throw new IllegalStateException("No values for cycle button");
            } else {
                Boolean $$7 = this.initialValue != null ? this.initialValue : $$6.get(this.initialIndex);
                Component $$8 = (Component)this.valueStringifier.apply($$7);
                Component $$9 = this.displayOnlyValue ? $$8 : CommonComponents.optionStatus($$4, false);
                return new CycleButton($$0, $$1, $$2, $$3, $$9, $$4, this.initialIndex, $$7, this.values, this.valueStringifier,  $$5, this.tooltipSupplier, this.displayOnlyValue);
            }
        }
    }
}
