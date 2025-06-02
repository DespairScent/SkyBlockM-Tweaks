package despairscent.skyblockm.tweaks.mixin;

import despairscent.skyblockm.tweaks.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static despairscent.skyblockm.tweaks.ModUtils.config;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @Final
    @Shadow
    private MatrixStack matrices;

    @Shadow @Final private VertexConsumerProvider.Immediate vertexConsumers;

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
            at = @At("HEAD"))
    private void drawItemInjectHead(LivingEntity entity, World world, ItemStack itemStack, int x, int y, int seed, int z, CallbackInfo ci) {
        if (!config.modules.renderItemInside ||
                !itemStack.contains(DataComponentTypes.CUSTOM_MODEL_DATA) ||
                !itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
            return;
        }

        int modelId = itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA).value();
        NbtCompound customData = itemStack.get(DataComponentTypes.CUSTOM_DATA).getNbt();

        int bgColor;
        if (itemStack.getItem() == Items.PAPER && modelId == 7301) {
            if (!customData.contains("ElectricStorage.RecipeResults") ||
                    !testRender(config.renderItemInside.esPattern)) {
                return;
            }
            bgColor = config.renderItemInside.esPattern.bgColor;
        } else if (itemStack.getItem() == Items.BARRIER && modelId >= 1010 && modelId <= 1013) {
            if (!customData.contains("ItemStack") ||
                    !testRender(config.renderItemInside.storage)) {
                return;
            }
            bgColor = config.renderItemInside.storage.bgColor;
        } else if (itemStack.getItem() == Items.IRON_HORSE_ARMOR && modelId == 2001) {
            if (!customData.contains("StoredItem") ||
                    !testRender(config.renderItemInside.crystalMemory)) {
                return;
            }
            bgColor = config.renderItemInside.crystalMemory.bgColor;
        } else {
            return;
        }

        if (bgColor >>> 24 != 0) {
            this.matrices.push();
            this.matrices.translate(x, y, 1);
            ((DrawContext) (Object) this).fill(RenderLayer.getGuiOverlay(),0, 0, 16, 16, bgColor);
            this.matrices.pop();
        }
    }

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"))
    private void drawItemInject(LivingEntity entity, World world, ItemStack itemStack, int x, int y, int seed, int z, CallbackInfo ci) {
        if (!config.modules.renderItemInside ||
                !itemStack.contains(DataComponentTypes.CUSTOM_MODEL_DATA) ||
                !itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
            return;
        }

        int modelId = itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA).value();
        NbtCompound customData = itemStack.get(DataComponentTypes.CUSTOM_DATA).getNbt();

        ItemStack itemInside;
        boolean drawOriginal;
        if (itemStack.getItem() == Items.PAPER && modelId == 7301) {
            if (!customData.contains("ElectricStorage.RecipeResults") ||
                    !testRender(config.renderItemInside.esPattern)) {
                return;
            }
            drawOriginal = config.renderItemInside.esPattern.drawOriginal;
            itemInside = itemStackFromNbtPre1_20_5(
                    customData.getList("ElectricStorage.RecipeResults", NbtElement.COMPOUND_TYPE)
                            .getCompound(0));
        } else if (itemStack.getItem() == Items.BARRIER && modelId >= 1010 && modelId <= 1013) {
            if (!customData.contains("ItemStack") ||
                    !testRender(config.renderItemInside.storage)) {
                return;
            }
            drawOriginal = config.renderItemInside.storage.drawOriginal;
            itemInside = itemStackFromNbtPre1_20_5(customData.getCompound("ItemStack"));
        } else if (itemStack.getItem() == Items.IRON_HORSE_ARMOR && modelId == 2001) {
            if (!testRender(config.renderItemInside.crystalMemory)) {
                return;
            }
            drawOriginal = config.renderItemInside.crystalMemory.drawOriginal;
            itemInside = itemStackFromCrystalMemory(customData);
            if (itemInside == null) {
                return;
            }
        } else {
            return;
        }

        BakedModel itemInsideModel = this.client.getItemRenderer().getModel(itemInside, world, entity, seed);

        if (itemInsideModel.isSideLit()) {
            DiffuseLighting.enableGuiDepthLighting();
        } else {
            DiffuseLighting.disableGuiDepthLighting();
        }

        this.client.getItemRenderer().renderItem(itemInside, ModelTransformationMode.GUI, false, this.matrices, this.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV, itemInsideModel);
        ((DrawContext) (Object) this).draw();

        if (drawOriginal) {
            this.matrices.translate(0.25f, -0.25f, 1f);
            this.matrices.scale(0.5f, 0.5f, 1f);

            if (this.client.getItemRenderer().getModel(itemStack, world, entity, seed).isSideLit()) {
                DiffuseLighting.enableGuiDepthLighting();
            } else {
                DiffuseLighting.disableGuiDepthLighting();
            }
        } else {
            this.matrices.scale(0, 0, 0);
        }
    }

    @Unique
    private static boolean testRender(Config.RenderItemInsideSub subConfig) {
        return subConfig.enabled &&
                (subConfig.renderAlways ||
                        (MinecraftClient.getInstance().currentScreen != null && Screen.hasShiftDown()));
    }

    @Unique
    private static ItemStack itemStackFromCrystalMemory(NbtCompound nbt) {
        Item item;
        int modelId;
        item_definition:
        {
            if (nbt.get("StoredItem_Display") instanceof NbtCompound nbtStoredItem &&
                    nbtStoredItem.contains("id", NbtElement.STRING_TYPE) &&
                    nbtStoredItem.contains("CustomModelData", NbtElement.NUMBER_TYPE)) {
                item = Registries.ITEM.get(Identifier.tryParse(nbtStoredItem.getString("id")));
                if (item != Items.AIR) {
                    modelId = nbtStoredItem.getInt("CustomModelData");
                    break item_definition;
                }
            }

            if (nbt.contains("StoredItem", NbtElement.STRING_TYPE)) {
                String identifierStr = nbt.getString("StoredItem");

                item = Registries.ITEM.get(Identifier.tryParse(identifierStr));
                if (item != Items.AIR) {
                    modelId = 0;
                    break item_definition;
                }

                // TODO: удалить [по ненадобности] после обновы сервера
                switch (identifierStr) {
                    case "general:tin_ingot":
                        item = Items.PAPER;
                        modelId = 204;
                        break item_definition;
                    case "general:bronze_ingot":
                        item = Items.PAPER;
                        modelId = 304;
                        break item_definition;
                    case "general:steel_ingot":
                        item = Items.PAPER;
                        modelId = 1104;
                        break item_definition;
                    case "general:lead_ingot":
                        item = Items.PAPER;
                        modelId = 1004;
                        break item_definition;
                    case "electricity:iridium_shard":
                        item = Items.PAPER;
                        modelId = 4015;
                        break item_definition;
                    case "electricity:sunnarium_shard":
                        item = Items.PAPER;
                        modelId = 4019;
                        break item_definition;
                    case "electricity:silica":
                        item = Items.PAPER;
                        modelId = 4010;
                        break item_definition;
                    case "quantum:enderium":
                        item = Items.PAPER;
                        modelId = 8002;
                        break item_definition;
                }
            }

            return null;
        }

        ItemStack stack = new ItemStack(item);
        if (modelId != 0) {
            stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(modelId));
        }
        return stack;
    }

    @Unique
    private static ItemStack itemStackFromNbtPre1_20_5(NbtCompound stackNbt) {
        ItemStack stack = ItemStack.EMPTY;
        try {
            Item item = Registries.ITEM.get(Identifier.tryParse(stackNbt.getString("id")));
            stack = new ItemStack(item);
            if (stackNbt.contains("tag", NbtElement.COMPOUND_TYPE)) {
                NbtCompound nbt = stackNbt.getCompound("tag");
                if (nbt.contains("CustomModelData", NbtElement.INT_TYPE)) {
                    stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(nbt.getInt("CustomModelData")));
                }
            }
        } catch (Exception e) {
        }
        return stack;
    }

}
