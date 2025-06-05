package despairscent.skyblockm.tweaks.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;
import static despairscent.skyblockm.tweaks.ModUtils.i18n;

public class ClothConfigImplementation {

    static Screen generate(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(i18n("config.title"));
        ConfigCategory base = builder.getOrCreateCategory(Text.empty());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.storageTargetingFix"), CONFIG.modules.storageTargetingFix)
                .setTooltip(i18n("config.modules.storageTargetingFix.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.storageTargetingFix)
                .setSaveConsumer(value -> CONFIG.modules.storageTargetingFix = value)
                .build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.moreTooltipInfo"), CONFIG.modules.moreTooltipInfo)
                .setTooltip(i18n("config.modules.moreTooltipInfo.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.moreTooltipInfo)
                .setSaveConsumer(value -> CONFIG.modules.moreTooltipInfo = value)
                .build());
        base.addEntry(builder.entryBuilder().startSubCategory(i18n("config.subcategory"), Arrays.asList(
                builder.entryBuilder().startBooleanToggle(i18n("config.moreTooltipInfo.storage"), CONFIG.moreTooltipInfo.storage)
                        .setTooltip(i18n("config.moreTooltipInfo.storage.tooltip"))
                        .setDefaultValue(Config.DEFAULT.moreTooltipInfo.storage)
                        .setSaveConsumer(value -> CONFIG.moreTooltipInfo.storage = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.moreTooltipInfo.fluidStorage"), CONFIG.moreTooltipInfo.fluidStorage)
                        .setTooltip(i18n("config.moreTooltipInfo.fluidStorage.tooltip"))
                        .setDefaultValue(Config.DEFAULT.moreTooltipInfo.fluidStorage)
                        .setSaveConsumer(value -> CONFIG.moreTooltipInfo.fluidStorage = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.moreTooltipInfo.crystalMemory"), CONFIG.moreTooltipInfo.crystalMemory)
                        .setTooltip(i18n("config.moreTooltipInfo.crystalMemory.tooltip"))
                        .setDefaultValue(Config.DEFAULT.moreTooltipInfo.crystalMemory)
                        .setSaveConsumer(value -> CONFIG.moreTooltipInfo.crystalMemory = value)
                        .build()
        )).build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.renderItemInside"), CONFIG.modules.renderItemInside)
                .setTooltip(i18n("config.modules.renderItemInside.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.renderItemInside)
                .setSaveConsumer(value -> CONFIG.modules.renderItemInside = value)
                .build());
        base.addEntry(builder.entryBuilder().startSubCategory(i18n("config.subcategory"), Arrays.asList(
                builder.entryBuilder().startSubCategory(i18n("config.renderItemInside.item.esPattern"),
                        prepareRenderItemInsideSetup(builder, config -> config.renderItemInside.esPattern)).build(),
                builder.entryBuilder().startSubCategory(i18n("config.renderItemInside.item.storage"),
                        prepareRenderItemInsideSetup(builder, config -> config.renderItemInside.storage)).build(),
                builder.entryBuilder().startSubCategory(i18n("config.renderItemInside.item.crystalMemory"),
                        prepareRenderItemInsideSetup(builder, config -> config.renderItemInside.crystalMemory)).build()
        )).build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.inputLagFix"), CONFIG.modules.inputLagFix)
                .setDefaultValue(Config.DEFAULT.modules.inputLagFix)
                .setSaveConsumer(value -> CONFIG.modules.inputLagFix = value)
                .build());
        base.addEntry(builder.entryBuilder().startSubCategory(i18n("config.subcategory"), Arrays.asList(
                builder.entryBuilder().startBooleanToggle(i18n("config.inputLagFix.recipesSearch"), CONFIG.inputLagFix.recipesSearch)
                        .setDefaultValue(Config.DEFAULT.inputLagFix.recipesSearch)
                        .setSaveConsumer(value -> CONFIG.inputLagFix.recipesSearch = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.inputLagFix.esTerminalSearch"), CONFIG.inputLagFix.esTerminalSearch)
                        .setDefaultValue(Config.DEFAULT.inputLagFix.esTerminalSearch)
                        .setSaveConsumer(value -> CONFIG.inputLagFix.esTerminalSearch = value)
                        .build()
        )).build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.compactGenome"), config.modules.compactGenome)
                .setTooltip(i18n("config.modules.compactGenome.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.compactGenome)
                .setSaveConsumer(value -> CONFIG.modules.compactGenome = value)
                .build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.hideHiddenArmorStands"), CONFIG.modules.hideHiddenArmorStands)
                .setTooltip(i18n("config.modules.hideHiddenArmorStands.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.hideHiddenArmorStands)
                .setSaveConsumer(value -> CONFIG.modules.hideHiddenArmorStands = value)
                .build());

        builder.setSavingRunnable(() -> CONFIG.save());

        return builder.build();
    }

    private static List<AbstractConfigListEntry> prepareRenderItemInsideSetup(ConfigBuilder builder, Function<Config, Config.RenderItemInsideSub> subGetter) {
        return Arrays.asList(
                builder.entryBuilder().startBooleanToggle(i18n("config.renderItemInside.enabled"), subGetter.apply(CONFIG).enabled)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).enabled)
                        .setSaveConsumer(value -> subGetter.apply(CONFIG).enabled = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.renderItemInside.renderAlways"), subGetter.apply(CONFIG).renderAlways)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).renderAlways)
                        .setSaveConsumer(value -> subGetter.apply(CONFIG).renderAlways = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.renderItemInside.drawOriginal"), subGetter.apply(CONFIG).drawOriginal)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).drawOriginal)
                        .setSaveConsumer(value -> subGetter.apply(CONFIG).drawOriginal = value)
                        .build(),
                builder.entryBuilder().startAlphaColorField(i18n("config.renderItemInside.bgColor"), subGetter.apply(CONFIG).bgColor)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).bgColor)
                        .setSaveConsumer(value -> subGetter.apply(CONFIG).bgColor = value)
                        .build()
        );
    }

}
