package despairscent.skyblockm.antilag.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Redirect(method = "isInvisibleTo",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z")
	)
	private boolean injected(PlayerEntity instance) {
		if (((Object)this) instanceof ArmorStandEntity) {
			return false;
		}
		return instance.isSpectator();
	}

}
