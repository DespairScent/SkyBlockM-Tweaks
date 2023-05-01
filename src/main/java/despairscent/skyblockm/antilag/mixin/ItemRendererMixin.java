package despairscent.skyblockm.antilag.mixin;

import despairscent.skyblockm.antilag.ModLoader;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Redirect(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;"))
    private BakedModel replaceGetModel(ItemRenderer instance, ItemStack stack, World world, LivingEntity entity, int seed) {
        if (ModLoader.ENABLED && stack.hasNbt() && stack.getNbt().contains("CustomModelData")) {
            Map<Integer, BakedModel> cache = ModLoader.modelsCache.computeIfAbsent(stack.getItem(), k -> new Int2ObjectOpenHashMap<>());
            int modelId =  stack.getNbt().getInt("CustomModelData");
            BakedModel model = cache.get(modelId);
            if (model == null) {
                model = instance.getModel(stack, world, entity, seed);
                cache.put(modelId, model);
            }
            return model;
        }
        return instance.getModel(stack, world, entity, seed);
    }

}