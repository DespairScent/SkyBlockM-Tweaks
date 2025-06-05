package despairscent.skyblockm.tweaks;

import despairscent.skyblockm.tweaks.config.Config;
import despairscent.skyblockm.tweaks.modules.compactgenome.CompactGenomeModule;
import despairscent.skyblockm.tweaks.modules.esterminalscroll.EsTerminalScroll;
import net.fabricmc.api.ClientModInitializer;

import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;

public class ModLoader implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CONFIG = Config.load();
        CONFIG.save();

        CompactGenomeModule.init();
        EsTerminalScroll.init();
    }

}
