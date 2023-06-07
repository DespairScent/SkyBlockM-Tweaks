package despairscent.skyblockm.tweaks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static despairscent.skyblockm.tweaks.ModUtils.config;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandMixin {

	@Inject(method = "tickCramming()V", at = @At("HEAD"), cancellable = true)
	private void injected(CallbackInfo ci) {
		if (config.general.optimize && config.optimize.armorStandCramming &&
				MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.isClient()) {
			ci.cancel();
		}
	}

}
