package despairscent.skyblockm.tweaks;

import despairscent.skyblockm.tweaks.config.Config;
import despairscent.skyblockm.tweaks.modules.compactgenome.CompactGenomeModule;
import net.fabricmc.api.ClientModInitializer;

import static despairscent.skyblockm.tweaks.ModUtils.config;

public class ModLoader implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        config = Config.load();
        config.save();

        CompactGenomeModule.init();
    }

}
