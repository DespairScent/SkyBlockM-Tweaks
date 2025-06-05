package despairscent.skyblockm.tweaks.mixin;

import despairscent.skyblockm.tweaks.mixininner.IAnvilScreenMixin;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;
import static despairscent.skyblockm.tweaks.ModUtils.testCustomScreen;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin implements IAnvilScreenMixin {

    @Unique boolean fixInputLag = false;

    @Unique boolean fixInputLagScreenChainMode = false;

    @Unique boolean inputStarted = false;

    @Unique TextFieldWidget nameFieldPrevious = null;

    @Unique boolean nameFieldInherited = false;

    @Shadow private TextFieldWidget nameField;

    @Shadow protected abstract void onRenamed(String name);

    @Override
    public void skyblockm_tweaks$handlePrevious(AnvilScreen screen) {
        this.nameFieldPrevious = ((AnvilScreenMixin) (Object) screen).nameField;
    }

    @Inject(method = "setup",
            at = @At("TAIL"))
    private void setupInject(CallbackInfo ci) {
        if (!CONFIG.textInputLagFix.enabled) {
            return;
        }
        if (testCustomScreen((AnvilScreen) (Object) this, "recipeviewer:interfaces", "\u0002")) {
            if (CONFIG.textInputLagFix.recipesSearch) {
                this.fixInputLag = true;
            }
        } else if (testCustomScreen((AnvilScreen) (Object) this, "electric_storage:interfaces", "\u1010")) {
            if (CONFIG.textInputLagFix.esTerminalSearch) {
                this.fixInputLag = true;
                this.fixInputLagScreenChainMode = true;
            }
        }

        if (this.nameFieldPrevious != null) {
            if (this.fixInputLagScreenChainMode) {
                this.nameField.setText(this.nameFieldPrevious.getText());
                this.nameField.setCursor(this.nameFieldPrevious.getCursor(), false);
                this.nameFieldInherited = true;
            }
            this.nameFieldPrevious = null;
        }
    }

    @Inject(method = "onSlotUpdate",
            at = @At("HEAD"),
            cancellable = true)
    private void onSlotUpdateInject(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
        if (this.fixInputLag && slotId == 0 && !stack.isEmpty() && !stack.getName().getString().isEmpty()) {
            if (this.inputStarted) {
                ci.cancel();
            } else {
                this.inputStarted = true;

                if (this.nameFieldInherited) {
                    // мы не хотим сброс курсора через setText()
                    ci.cancel();
                    this.nameField.setEditable(true);
                    ((AnvilScreen) (Object) this).setFocused(this.nameField);

                    this.onRenamed(this.nameField.getText());
                }
            }
        }
    }

}
