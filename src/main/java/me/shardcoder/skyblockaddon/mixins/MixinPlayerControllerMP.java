package me.shardcoder.skyblockaddon.mixins;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    private long lastMessage = -1;

    /**
     * Cancels stem breaks if holding an item, to avoid accidental breaking.
     */
    @Inject(method = "clickBlock", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void onPlayerDamageBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        SkyblockAddons main = SkyblockAddons.getInstance();
        if (main.getConfigValues().isEnabled(Feature.AVOID_BREAKING_STEMS)) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP p = mc.thePlayer;
            ItemStack heldItem = p.getHeldItem();
            Block block = mc.theWorld.getBlockState(loc).getBlock();
            if (heldItem != null && (block.equals(Blocks.melon_stem) || block.equals(Blocks.pumpkin_stem))) {
                cir.setReturnValue(false);
                if (System.currentTimeMillis()-lastMessage > 15000) {
                    lastMessage = System.currentTimeMillis();
                    main.getUtils().sendMessage(EnumChatFormatting.RED+Message.MESSAGE_CANCELLED_STEM_BREAK.getMessage());
                }
            }
        }
    }
}
