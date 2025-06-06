package despairscent.skyblockm.tweaks.modules.esterminalscroll;

import despairscent.skyblockm.tweaks.ModUtils;
import despairscent.skyblockm.tweaks.config.Config;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.SlotActionType;

import static despairscent.skyblockm.tweaks.ModUtils.CLIENT;
import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;

public class EsTerminalScroll {

    private static int tick;
    private static int lastClickAt;
    private static int clickedPerTick;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (CONFIG.esTerminalScroll.enabled) {
                if (ModUtils.isKeyPressed(CONFIG.esTerminalScroll.keyUp)) {
                    doScrollUp(false);
                } else if (ModUtils.isKeyPressed(CONFIG.esTerminalScroll.keyDown)) {
                    doScrollDown(false);
                }
            }

            ++tick;
        });
    }

    public static boolean doScrollDown(boolean wheel) {
        return sendClick(false, wheel);
    }

    public static boolean doScrollUp(boolean wheel) {
        return sendClick(true, wheel);
    }

    private static boolean sendClick(boolean up, boolean wheel) {
        if (!(CLIENT.currentScreen instanceof HandledScreen<?> screen)) {
            return false;
        }

        int slot;
        if (ModUtils.testCustomScreen(screen, "electric_storage:interfaces", "\u1000") ||
                ModUtils.testCustomScreen(screen, "electric_storage:interfaces", "\u1020") ||
                ModUtils.testCustomScreen(screen, "electric_storage:interfaces", "\u1030") ||
                ModUtils.testCustomScreen(screen, "electric_storage:interfaces", "\u2000")) {
            slot = up ? 8 : 35;
        } else if (ModUtils.testCustomScreen(screen, "electric_storage:interfaces", "\u1010")) {
            slot = up ? 11 : 38;
        } else {
            return false;
        }

        if (lastClickAt != tick) {
            clickedPerTick = 0;
        }
        int limit = wheel ? CONFIG.esTerminalScroll.actionLimitWheel : CONFIG.esTerminalScroll.actionLimitKey;
        if (limit > 0 ?
                tick - lastClickAt > limit :
                clickedPerTick < Math.max(1, -limit)) {
            boolean boost = ModUtils.isKeyPressed(CONFIG.esTerminalScroll.boostKey) ^ (CONFIG.esTerminalScroll.boostKeyType == Config.EsTerminalScrollBoostKeyType.DEACTIVATE);
            CLIENT.interactionManager.clickSlot(screen.getScreenHandler().syncId, slot, 0, boost ? SlotActionType.QUICK_MOVE : SlotActionType.PICKUP, CLIENT.player);
            lastClickAt = tick;
            ++clickedPerTick;
        }

        return true;
    }

}
