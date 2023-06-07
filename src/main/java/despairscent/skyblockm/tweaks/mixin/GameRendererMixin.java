package despairscent.skyblockm.tweaks.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

import static despairscent.skyblockm.tweaks.ModUtils.config;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Redirect(method = "updateTargetedEntity(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;")
    )
    private EntityHitResult injected(Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, double d) {
        if (config.qol.storageTargetingFix) {
            return ProjectileUtil.raycast(entity, min, max, box, e -> !(e instanceof ItemFrameEntity && e.isInvisible()) && predicate.test(e), d);
        }
        return ProjectileUtil.raycast(entity, min, max, box, predicate, d);
    }

}
