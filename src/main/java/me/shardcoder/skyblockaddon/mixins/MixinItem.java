package me.shardcoder.skyblockaddon.mixins;

@Mixin(Item.class)
public class MixinItem {

    @Redirect(method = "showDurabilityBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemDamaged()Z", ordinal = 0))
    private boolean showDurabilityBar(ItemStack stack) { //Item item, ItemStack stack
        if (SkyblockAddons.getInstance().getUtils().isOnSkyblock() && !SkyblockAddons.getInstance().getConfigValues().isDisabled(Feature.HIDE_DURABILITY)) {
            return false;
        }
        return stack.isItemDamaged();
    }
}
