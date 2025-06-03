package despairscent.skyblockm.tweaks.mixin;

import despairscent.skyblockm.tweaks.mixininner.IAnvilScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "setScreen",
            at = @At("HEAD"))
    private void setScreenInject(Screen screen, CallbackInfo ci) {
        Screen previousScreen = MinecraftClient.getInstance().currentScreen;
        if (previousScreen instanceof AnvilScreen previous && screen instanceof AnvilScreen) {
            ((IAnvilScreenMixin) screen).skyblockm_tweaks$handlePrevious(previous);
        }
    }

}
