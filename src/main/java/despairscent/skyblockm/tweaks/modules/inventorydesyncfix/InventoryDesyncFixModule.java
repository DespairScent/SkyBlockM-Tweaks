package despairscent.skyblockm.tweaks.modules.inventorydesyncfix;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.screen.slot.SlotActionType;

import static despairscent.skyblockm.tweaks.ModUtils.*;

public class InventoryDesyncFixModule {

    private static boolean fixingInventoryDesync;
    private static boolean fixingSelectedSlotDesync;
    private static int previousSelectedSlot;

    public static void init() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (CONFIG.inventoryDesyncFix.enabled && !fixingInventoryDesync && isRecipeViewer(screen)) {
                fixingInventoryDesync = true;
                fixingSelectedSlotDesync = false;
                previousSelectedSlot = client.player.getInventory().selectedSlot;
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!fixingInventoryDesync || isRecipeViewer(client.currentScreen)) {
                return;
            }
            fixingInventoryDesync = false;

            if (CONFIG.inventoryDesyncFix.inventoryUpdates) {
                // "Забираем" и "кладем обратно" в первый слот крафт-таблицы
                if (client.currentScreen == null) {
                    for (int i = 0; i < 2; i++) {
                        client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, 1, 0, SlotActionType.PICKUP, client.player);
                    }
                } else if (client.currentScreen instanceof CraftingScreen screen) {
                    for (int i = 0; i < 2; i++) {
                        client.interactionManager.clickSlot(screen.getScreenHandler().syncId, 1, 0, SlotActionType.PICKUP, client.player);
                    }
                }
            }

            if (CONFIG.inventoryDesyncFix.selectedSlot) {
                CLIENT.player.getInventory().selectedSlot = previousSelectedSlot;
                if (client.currentScreen == null) {
                    fixingSelectedSlotDesync = true;
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((networkHandler, client) -> {
            fixingInventoryDesync = false;
            fixingSelectedSlotDesync = false;
        });
    }

    private static boolean isRecipeViewer(Screen screen) {
        return testCustomScreen(screen, "recipeviewer:interfaces") || testCustomScreen(screen, "recipeviewer:interfaces_generated");
    }

    public static void handleSelectedSlotUpdate() {
        if (fixingSelectedSlotDesync) {
            fixingSelectedSlotDesync = false;
            if (CLIENT.player != null) {
                CLIENT.interactionManager.syncSelectedSlot(); // Окончательно применяем отправленный сервером слот
                CLIENT.player.getInventory().selectedSlot = previousSelectedSlot;
            }
        }
    }

}
