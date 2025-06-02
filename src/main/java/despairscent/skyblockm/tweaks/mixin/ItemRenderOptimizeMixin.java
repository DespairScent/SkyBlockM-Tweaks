package despairscent.skyblockm.tweaks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import despairscent.skyblockm.tweaks.modules.modelscaching.CustomModelOverrideList;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ItemModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(value = ItemModel.class)
public class ItemRenderOptimizeMixin {

    @Shadow @Final private Identifier id;

    @Inject(
            method = "bake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/ItemModel$BakedItemModel;<init>(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/client/render/model/json/ModelOverrideList;)V"
            )
    )
    private void compileOverridesInject(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, CallbackInfoReturnable<BakedModel> cir, @Local LocalRef<ModelOverrideList> modelOverrideList) {
        var optimized = new CustomModelOverrideList(baker, modelOverrideList.get());
        if (optimized.hasCache()) {
            modelOverrideList.set(optimized);
        }
    }

}
