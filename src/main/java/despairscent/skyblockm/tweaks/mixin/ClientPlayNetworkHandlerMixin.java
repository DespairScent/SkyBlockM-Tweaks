package despairscent.skyblockm.tweaks.mixin;

import despairscent.skyblockm.tweaks.modules.inventorydesyncfix.InventoryDesyncFixModule;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static despairscent.skyblockm.tweaks.ModUtils.CLIENT;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onUpdateSelectedSlot",
            at = @At("TAIL"))
    private void handlePacketInject(UpdateSelectedSlotS2CPacket packet, CallbackInfo ci) {
        if (CLIENT.isOnThread()) {
            InventoryDesyncFixModule.handleSelectedSlotUpdate();
        }
    }

}
