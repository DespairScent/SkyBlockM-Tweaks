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

        // KeyBinding keybindingSwitchOptimize = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        //         "skyblockm-tweaks.keys.optimize",
        //         InputUtil.Type.KEYSYM,
        //         InputUtil.UNKNOWN_KEY.getCode(),
        //         "skyblockm-tweaks.keys"
        // ));
        // ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
        //     while (keybindingSwitchOptimize.wasPressed()) {
        //         if (client.world == null || client.player == null) {
        //             return;
        //         }
        //         config.modules.fpsOptimize = !config.modules.fpsOptimize;
        //         client.inGameHud.getChatHud().addMessage(i18n("message.optimizeSwitch").append(
        //                 config.modules.fpsOptimize ?
        //                         Text.literal("on").styled(style -> style.withColor(Formatting.GREEN)) :
        //                         Text.literal("off").styled(style -> style.withColor(Formatting.RED))));
        //     }
        // });

        CompactGenomeModule.init();
    }

}
