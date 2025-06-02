package despairscent.skyblockm.tweaks.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static despairscent.skyblockm.tweaks.ModUtils.config;
import static despairscent.skyblockm.tweaks.ModUtils.i18n;

public class ClothConfigImplementation {

    static Screen generate(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(i18n("config.title"));
        ConfigCategory base = builder.getOrCreateCategory(Text.empty());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.fpsOptimize"), config.modules.fpsOptimize)
                        .setDefaultValue(Config.DEFAULT.modules.fpsOptimize)
                        .setSaveConsumer(value -> config.modules.fpsOptimize = value)
                        .build());
        base.addEntry(builder.entryBuilder().startSubCategory(i18n("config.subcategory"), Arrays.asList(
                builder.entryBuilder().startBooleanToggle(i18n("config.fpsOptimize.modelsCaching"), config.fpsOptimize.modelsCaching)
                        .setTooltip(i18n("config.fpsOptimize.modelsCaching.tooltip"))
                        .setDefaultValue(Config.DEFAULT.fpsOptimize.modelsCaching)
                        .setSaveConsumer(value -> config.fpsOptimize.modelsCaching = value)
                        .build()
        )).build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.storageTargetingFix"), config.modules.storageTargetingFix)
                .setTooltip(i18n("config.modules.storageTargetingFix.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.storageTargetingFix)
                .setSaveConsumer(value -> config.modules.storageTargetingFix = value)
                .build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.moreTooltipInfo"), config.modules.moreTooltipInfo)
                .setTooltip(i18n("config.modules.moreTooltipInfo.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.moreTooltipInfo)
                .setSaveConsumer(value -> config.modules.moreTooltipInfo = value)
                .build());
        base.addEntry(builder.entryBuilder().startSubCategory(i18n("config.subcategory"), Arrays.asList(
                builder.entryBuilder().startBooleanToggle(i18n("config.moreTooltipInfo.storage"), config.moreTooltipInfo.storage)
                        .setTooltip(i18n("config.moreTooltipInfo.storage.tooltip"))
                        .setDefaultValue(Config.DEFAULT.moreTooltipInfo.storage)
                        .setSaveConsumer(value -> config.moreTooltipInfo.storage = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.moreTooltipInfo.crystalMemory"), config.moreTooltipInfo.crystalMemory)
                        .setTooltip(i18n("config.moreTooltipInfo.crystalMemory.tooltip"))
                        .setDefaultValue(Config.DEFAULT.moreTooltipInfo.crystalMemory)
                        .setSaveConsumer(value -> config.moreTooltipInfo.crystalMemory = value)
                        .build()
        )).build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.renderItemInside"), config.modules.renderItemInside)
                .setTooltip(i18n("config.modules.renderItemInside.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.renderItemInside)
                .setSaveConsumer(value -> config.modules.renderItemInside = value)
                .build());
        base.addEntry(builder.entryBuilder().startSubCategory(i18n("config.subcategory"), Arrays.asList(
                builder.entryBuilder().startSubCategory(i18n("config.renderItemInside.item.esPattern"),
                        prepareRenderItemInsideSetup(builder, config -> config.renderItemInside.esPattern)).build(),
                builder.entryBuilder().startSubCategory(i18n("config.renderItemInside.item.storage"),
                        prepareRenderItemInsideSetup(builder, config -> config.renderItemInside.storage)).build(),
                builder.entryBuilder().startSubCategory(i18n("config.renderItemInside.item.crystalMemory"),
                        prepareRenderItemInsideSetup(builder, config -> config.renderItemInside.crystalMemory)).build()
        )).build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.inputLagFix"), config.modules.inputLagFix)
                .setDefaultValue(Config.DEFAULT.modules.inputLagFix)
                .setSaveConsumer(value -> config.modules.inputLagFix = value)
                .build());
        base.addEntry(builder.entryBuilder().startSubCategory(i18n("config.subcategory"), Arrays.asList(
                builder.entryBuilder().startBooleanToggle(i18n("config.inputLagFix.recipesSearch"), config.inputLagFix.recipesSearch)
                        .setDefaultValue(Config.DEFAULT.inputLagFix.recipesSearch)
                        .setSaveConsumer(value -> config.inputLagFix.recipesSearch = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.inputLagFix.esTerminalSearch"), config.inputLagFix.esTerminalSearch)
                        .setDefaultValue(Config.DEFAULT.inputLagFix.esTerminalSearch)
                        .setSaveConsumer(value -> config.inputLagFix.esTerminalSearch = value)
                        .build()
        )).build());

        base.addEntry(builder.entryBuilder().startBooleanToggle(i18n("config.modules.compactGenome"), config.modules.compactGenome)
                .setTooltip(i18n("config.modules.compactGenome.tooltip"))
                .setDefaultValue(Config.DEFAULT.modules.compactGenome)
                .setSaveConsumer(value -> config.modules.compactGenome = value)
                .build());

        builder.setSavingRunnable(() -> config.save());

        return builder.build();
    }

    private static List<AbstractConfigListEntry> prepareRenderItemInsideSetup(ConfigBuilder builder, Function<Config, Config.RenderItemInsideSub> subGetter) {
        return Arrays.asList(
                builder.entryBuilder().startBooleanToggle(i18n("config.renderItemInside.enabled"), subGetter.apply(config).enabled)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).enabled)
                        .setSaveConsumer(value -> subGetter.apply(config).enabled = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.renderItemInside.renderAlways"), subGetter.apply(config).renderAlways)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).renderAlways)
                        .setSaveConsumer(value -> subGetter.apply(config).renderAlways = value)
                        .build(),
                builder.entryBuilder().startBooleanToggle(i18n("config.renderItemInside.drawOriginal"), subGetter.apply(config).drawOriginal)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).drawOriginal)
                        .setSaveConsumer(value -> subGetter.apply(config).drawOriginal = value)
                        .build(),
                builder.entryBuilder().startAlphaColorField(i18n("config.renderItemInside.bgColor"), subGetter.apply(config).bgColor)
                        .setDefaultValue(subGetter.apply(Config.DEFAULT).bgColor)
                        .setSaveConsumer(value -> subGetter.apply(config).bgColor = value)
                        .build()
        );
    }

}
