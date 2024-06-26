package despairscent.skyblockm.tweaks;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static despairscent.skyblockm.tweaks.ModUtils.config;

public class ModelOverrideListOptimized extends ModelOverrideList {

    private final ModelOverrideList original;

    // Модель могут попытаться достать с разных потоков одновременно
    private final Map<Integer, BakedModel> cache = new ConcurrentHashMap<>();

    public ModelOverrideListOptimized(Baker baker, JsonUnbakedModel parent, ModelOverrideList original) {
        super(baker, parent, Collections.emptyList());
        this.original = original;
    }

    @Override
    public BakedModel apply(BakedModel model, ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        if (config.modules.fpsOptimize && config.fpsOptimize.modelsCaching &&
                stack.hasNbt() && stack.getNbt().getType("CustomModelData") == NbtElement.INT_TYPE) {
            int modelId = stack.getNbt().getInt("CustomModelData");
            return cache.computeIfAbsent(modelId, k -> this.original.apply(model, stack, world, entity, seed));
        }
        return this.original.apply(model, stack, world, entity, seed);
    }

}
