package me.shardcoder.skyblockaddon.gui.buttons;

import cc.hyperium.utils.ChatColor;
import java.util.ArrayList;
import java.util.List;
import me.shardcoder.skyblockaddon.SkyblockAddon;
import me.shardcoder.skyblockaddon.utils.ConfigColor;
import me.shardcoder.skyblockaddon.utils.Feature;
import me.shardcoder.skyblockaddon.utils.ItemDiff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ButtonLocation extends ButtonFeature {
    // So we know the latest hovered feature (used for arrow key movement).
    private static Feature lastHoveredFeature = null;

    private SkyblockAddon main;
    private int lastMouseX;
    private int lastMouseY;

    /**
     * Create a button that allows you to change the location of a GUI element.
     */
    public ButtonLocation(SkyblockAddon main, Feature feature) {
        super(-1, 0, 0, null, feature);
        this.main = main;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        // The scale of the GUI
        float scale = main.getUtils().denormalizeValue(main.getConfigValues().getGuiScale(), ButtonSlider.GUI_SCALE_MINIMUM, ButtonSlider.GUI_SCALE_MAXIMUM, ButtonSlider.GUI_SCALE_STEP);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        if (feature == Feature.MANA_BAR || feature == Feature.HEALTH_BAR) {
            main.getRenderListener().drawBar(feature, scale, mc, this);
        } else if (feature == Feature.SKELETON_BAR) {
            main.getRenderListener().drawSkeletonBar(scale, mc, this);
        } else if (feature == Feature.MANA_TEXT || feature == Feature.HEALTH_TEXT ||
            feature == Feature.DEFENCE_TEXT || feature == Feature.DEFENCE_PERCENTAGE ||
            feature == Feature.HEALTH_UPDATES || feature == Feature.DARK_AUCTION_TIMER ||
            feature == Feature.MAGMA_BOSS_TIMER) {
            main.getRenderListener().drawText(feature, scale, mc, this);
        } else if (feature == Feature.ITEM_PICKUP_LOG) {
            List<ItemDiff> collectionLog = new ArrayList<>();
            collectionLog.add(new ItemDiff(ChatColor.DARK_PURPLE+"Forceful Ember Chestplate", 1));
            collectionLog.add(new ItemDiff("Boat", -1));
            collectionLog.add(new ItemDiff(ChatColor.BLUE+"Aspect of the End", 1));
            main.getRenderListener().drawItemPickupLog(mc, scale, collectionLog, this);
        } else if (feature == Feature.DEFENCE_ICON) {
            scale *= 1.5;
            GlStateManager.scale(scale,scale,1);
            main.getRenderListener().drawIcon(scale, mc, this);
            scale /= 1.5;
            GlStateManager.scale(scale,scale,1);
        }
        if (hovered) {
            lastHoveredFeature = feature;
        }
        GlStateManager.popMatrix();
    }

    /**
     * This just updates the hovered status and draws the box around each feature. To avoid repetitive code.
     */
    public void checkHoveredAndDrawBox(int boxXOne, int boxXTwo, int boxYOne, int boxYTwo, float scale) {
        hovered = lastMouseX >= boxXOne * scale && lastMouseY >= boxYOne * scale && lastMouseX < boxXTwo * scale && lastMouseY < boxYTwo * scale;
        int boxAlpha = 100;
        if (hovered) {
            boxAlpha = 170;
        }
        int boxColor = ConfigColor.GRAY.getColor(boxAlpha);
        drawRect(boxXOne, boxYOne,
            boxXTwo, boxYTwo, boxColor);
    }

    /**
     * Because the box changes with the scale, have to override this.
     */
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && hovered;
    }

    public static Feature getLastHoveredFeature() {
        return lastHoveredFeature;
    }

    public int getLastMouseY() {
        return lastMouseY;
    }

    public int getLastMouseX() {
        return lastMouseX;
    }

}
