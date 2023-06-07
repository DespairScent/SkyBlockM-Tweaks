package despairscent.skyblockm.tweaks.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

import static despairscent.skyblockm.tweaks.ModUtils.modelsCache;

@Mixin(MinecraftClient.class)
public abstract class ResourceReloadMixin {

    @Inject(
            method = "reloadResources()Ljava/util/concurrent/CompletableFuture;",
            at = @At("TAIL")
    )
    private void reloadInject(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        modelsCache.clear();
    }

}
