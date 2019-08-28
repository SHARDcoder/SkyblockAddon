package me.shardcoder.skyblockaddon.mixins;

import me.shardcoder.skyblockaddon.SkyblockAddon;
import me.shardcoder.skyblockaddon.utils.Feature;
import me.shardcoder.skyblockaddon.utils.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    private Item lastItem = null;
    private long lastDrop = System.currentTimeMillis();

    @Inject(method = "dropOneItem", at = @At(value = "HEAD"), cancellable = true)
    private void dropOneItemConfirmation(boolean dropAll, CallbackInfoReturnable<EntityItem> cir) {
        SkyblockAddon main = SkyblockAddon.getInstance();
        if (main.getConfigValues().isEnabled(Feature.DROP_CONFIRMATION) && (main.getUtils().isOnSkyblock() ||
            main.getConfigValues().isDisabled(Feature.DISABLE_DOUBLE_DROP_AUTOMATICALLY))) {
            ItemStack heldItemStack = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (heldItemStack != null) {
                Item heldItem = heldItemStack.getItem();
                if (lastItem != null && lastItem == heldItem && System.currentTimeMillis() - lastDrop < 3000) {
                    lastDrop = System.currentTimeMillis();
                } else {
                    SkyblockAddon.getInstance().getUtils().sendMessage(main.getConfigValues().getColor(
                        Feature.DROP_CONFIRMATION).getChatFormatting() +
                        Message.MESSAGE_DROP_CONFIRMATION.getMessage());
                    lastItem = heldItem;
                    lastDrop = System.currentTimeMillis();
                    cir.setReturnValue(null);
                }
            }
        }
    }
}
