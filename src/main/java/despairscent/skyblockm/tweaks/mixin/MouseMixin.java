package despairscent.skyblockm.tweaks.mixin;

import despairscent.skyblockm.tweaks.ModUtils;
import despairscent.skyblockm.tweaks.config.Config;
import despairscent.skyblockm.tweaks.modules.esterminalscroll.EsTerminalScroll;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static despairscent.skyblockm.tweaks.ModUtils.CLIENT;
import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll",
            at = @At("HEAD"),
            order = 999, // Для выполнения перед IPN
            cancellable = true)
    private void onMouseScrollInject(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (!CONFIG.esTerminalScroll.enabled || !CONFIG.esTerminalScroll.wheel) {
            return;
        }
        if (window != CLIENT.getWindow().getHandle() || !CLIENT.isOnThread()) {
            return;
        }
        if (!ModUtils.isKeyPressedOrUndefined(CONFIG.esTerminalScroll.wheelModifier)) {
            return;
        }

        vertical = (CLIENT.options.getDiscreteMouseScroll().getValue() ? Math.signum(horizontal) : vertical) *
                CLIENT.options.getMouseWheelSensitivity().getValue();

        if (vertical >= 1) {
            if (EsTerminalScroll.doScrollUp(true)) {
                ci.cancel();
            }
        } else if (vertical <= -1) {
            if (EsTerminalScroll.doScrollDown(true)) {
                ci.cancel();
            }
        }
    }

}
