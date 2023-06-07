package despairscent.skyblockm.tweaks.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static despairscent.skyblockm.tweaks.ModUtils.config;
import static despairscent.skyblockm.tweaks.ModUtils.modelsCache;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Redirect(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"))
    private BakedModel replaceGetModel(ItemRenderer instance, ItemStack stack, World world, LivingEntity entity, int seed) {
        if (config.general.optimize && config.optimize.modelsCaching &&
                stack.hasNbt() && stack.getNbt().contains("CustomModelData")) {
            int modelId = stack.getNbt().getInt("CustomModelData");
            return modelsCache
                    .computeIfAbsent(stack.getItem(), k -> new Int2ObjectOpenHashMap<>())
                    .computeIfAbsent(modelId, k -> instance.getModel(stack, world, entity, seed));
        }
        return instance.getModel(stack, world, entity, seed);
    }

}