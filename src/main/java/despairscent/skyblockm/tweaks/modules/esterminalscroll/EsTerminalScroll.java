package despairscent.skyblockm.tweaks.modules.esterminalscroll;

import despairscent.skyblockm.tweaks.ModUtils;
import despairscent.skyblockm.tweaks.config.Config;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.SlotActionType;

import static despairscent.skyblockm.tweaks.ModUtils.CLIENT;
import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;

public class EsTerminalScroll {

    private static int tick;
    private static int lastClickAt;
    private static int clickedPerTick;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (CONFIG.modules.esTerminalScroll) {
                if (CONFIG.esTerminalScroll.keyUp != Config.KEY_UNDEFINED &&
                        InputUtil.isKeyPressed(CLIENT.getWindow().getHandle(), CONFIG.esTerminalScroll.keyUp)) {
                    doScrollUp(false);
                } else if (CONFIG.esTerminalScroll.keyDown != Config.KEY_UNDEFINED &&
                        InputUtil.isKeyPressed(CLIENT.getWindow().getHandle(), CONFIG.esTerminalScroll.keyDown)) {
                    doScrollDown(false);
                }
            }

            ++tick;
        });
    }

    public static boolean doScrollDown(boolean wheel) {
        return sendClick(35, wheel);
    }

    public static boolean doScrollUp(boolean wheel) {
        return sendClick(8, wheel);
    }

    private static boolean sendClick(int slot, boolean wheel) {
        if (CLIENT.currentScreen instanceof HandledScreen<?> screen && ModUtils.testCustomScreen(screen, "electric_storage:interfaces", "\u1000")) {
            if (lastClickAt != tick) {
                clickedPerTick = 0;
            }
            int limit = wheel ? CONFIG.esTerminalScroll.actionLimitWheel : CONFIG.esTerminalScroll.actionLimitKey;
            if (limit > 0 ?
                    tick - lastClickAt > limit :
                    clickedPerTick < Math.max(1, -limit)) {
                CLIENT.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot, 0, SlotActionType.PICKUP, CLIENT.player);
                lastClickAt = tick;
                ++clickedPerTick;
            }
            return true;
        }
        return false;
    }

}
