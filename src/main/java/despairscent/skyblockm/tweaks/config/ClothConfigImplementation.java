package despairscent.skyblockm.tweaks.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;

import static despairscent.skyblockm.tweaks.ModUtils.config;
import static despairscent.skyblockm.tweaks.ModUtils.translatableSelf;

public class ClothConfigImplementation {

    static Screen generate(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(translatableSelf("config.title"));

        builder.getOrCreateCategory(translatableSelf("config.general"))
                .addEntry(builder.entryBuilder().startBooleanToggle(translatableSelf("config.general.optimize"), config.general.optimize)
                        .setDefaultValue(Config.DEFAULT.general.optimize)
                        .setSaveConsumer(value -> config.general.optimize = value)
                        .build());

        builder.getOrCreateCategory(translatableSelf("config.optimize"))
                .addEntry(builder.entryBuilder().startBooleanToggle(translatableSelf("config.optimize.armorstandcramming"), config.optimize.armorStandCramming)
                        .setDefaultValue(Config.DEFAULT.optimize.armorStandCramming)
                        .setSaveConsumer(value -> config.optimize.armorStandCramming = value)
                        .build())
                .addEntry(builder.entryBuilder().startBooleanToggle(translatableSelf("config.optimize.modelscaching"), config.optimize.modelsCaching)
                        .setDefaultValue(Config.DEFAULT.optimize.modelsCaching)
                        .setSaveConsumer(value -> config.optimize.modelsCaching = value)
                        .build())
                .addEntry(builder.entryBuilder().startBooleanToggle(translatableSelf("config.optimize.spectatorarmorstands"), config.optimize.spectatorArmorStands)
                        .setDefaultValue(Config.DEFAULT.optimize.spectatorArmorStands)
                        .setSaveConsumer(value -> config.optimize.spectatorArmorStands = value)
                        .build());

        builder.getOrCreateCategory(translatableSelf("config.qol"))
                .addEntry(builder.entryBuilder().startBooleanToggle(translatableSelf("config.qol.storagetargetingfix"), config.qol.storageTargetingFix)
                        .setDefaultValue(Config.DEFAULT.qol.storageTargetingFix)
                        .setSaveConsumer(value -> config.qol.storageTargetingFix = value)
                        .build())
                .addEntry(builder.entryBuilder().startBooleanToggle(translatableSelf("config.qol.moretooltipinfo"), config.qol.moreTooltipInfo)
                        .setTooltip(translatableSelf("config.qol.moretooltipinfo.desc"))
                        .setDefaultValue(Config.DEFAULT.qol.moreTooltipInfo)
                        .setSaveConsumer(value -> config.qol.moreTooltipInfo = value)
                        .build());

        builder.setSavingRunnable(() -> config.save());

        return builder.build();
    }

}
