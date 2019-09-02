package me.shardcoder.skyblockaddon.mixins;

import me.shardcoder.skyblockaddon.SkyblockAddon;
import me.shardcoder.skyblockaddon.utils.Feature;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class MixinItem {

    @Redirect(method = "showDurabilityBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemDamaged()Z", ordinal = 0))
    private boolean showDurabilityBar(ItemStack stack) { //Item item, ItemStack stack
        if (SkyblockAddon.getInstance().getUtils().isOnSkyblock() && !SkyblockAddon.getInstance().getConfigValues().isDisabled(
            Feature.HIDE_DURABILITY)) {
            return false;
        }
        return stack.isItemDamaged();
    }
}
