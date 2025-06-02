package despairscent.skyblockm.tweaks.modules.modelscaching;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.TreeMap;

import static despairscent.skyblockm.tweaks.ModUtils.config;

public class CustomModelOverrideList extends ModelOverrideList {

    private static final Identifier TYPE_CUSTOM_MODEL = Identifier.ofVanilla("custom_model_data");

    private final ModelOverrideList original;

    private final TreeMap<Integer, BakedModel> cache;

    private final BakedModel fallbackModel;

    public CustomModelOverrideList(Baker baker, ModelOverrideList original) {
        super(baker, Collections.emptyList());
        this.original = original;

        if (original.conditionTypes.length != 1 || !original.conditionTypes[0].equals(TYPE_CUSTOM_MODEL)) {
            this.cache = null;
            this.fallbackModel = null;
            return;
        }

        this.cache = new TreeMap<>();

        Integer modelIdPrev = null;
        // Список является перевернутым в отношении .json
        for (var override : original.overrides) {
            // Предмет автоматически попадает под условия, прерывая цепочку
            if (override.conditions.length == 0) {
                this.fallbackModel = override.model;
                return;
            }

            int modelId = MathHelper.ceil(override.conditions[0].threshold());
            // Игнорируем ID вне порядка
            if (modelIdPrev == null || modelId < modelIdPrev) {
                this.cache.put(modelId, override.model);
                modelIdPrev = modelId;
            }
        }

        this.fallbackModel = null;
    }

    public boolean hasCache() {
        return this.cache != null;
    }

    @Override
    public BakedModel getModel(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        if (this.cache != null && config.modules.fpsOptimize && config.fpsOptimize.modelsCaching) {
            if (stack.get(DataComponentTypes.CUSTOM_MODEL_DATA) instanceof CustomModelDataComponent(int modelId)) {
                var entry = this.cache.floorEntry(modelId);
                if (entry != null) {
                    return entry.getValue();
                }
            }
            return this.fallbackModel;
        }
        return this.original.getModel(stack, world, entity, seed);
    }

}
