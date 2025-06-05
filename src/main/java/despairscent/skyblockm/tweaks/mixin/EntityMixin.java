package despairscent.skyblockm.tweaks.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow public abstract EntityType<?> getType();

	@Redirect(method = "isInvisibleTo",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"))
	private boolean redirectSpectatorCheck(PlayerEntity instance) {
		if (CONFIG.modules.hideHiddenArmorStands && this.getType() == EntityType.ARMOR_STAND) {
			return false;
		}
		return instance.isSpectator();
	}

}
