package me.shardcoder.skyblockaddon.gui.buttons;

import me.shardcoder.skyblockaddon.SkyblockAddon;
import me.shardcoder.skyblockaddon.utils.ConfigColor;
import me.shardcoder.skyblockaddon.utils.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class ButtonModify extends ButtonText {
    private SkyblockAddon main;

    private Feature feature;

    /**
     * Create a button for adding or subtracting a number.
     */
    public ButtonModify(double x, double y, int width, int height, String buttonText, SkyblockAddon main, Feature feature) {
        super(0, (int)x, (int)y, buttonText, feature);
        this.main = main;
        this.feature = feature;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        int boxColor;
        int boxAlpha = 100;
        if (hovered && !hitMaximum()) {
            boxAlpha = 170;
        }
        if (hitMaximum()) {
            boxColor = ConfigColor.GRAY.getColor(boxAlpha);
        } else {
            if (feature == Feature.ADD) {
                boxColor = ConfigColor.GREEN.getColor(boxAlpha);
            } else {
                boxColor = ConfigColor.RED.getColor(boxAlpha);
            }
        }
        GlStateManager.enableBlend();
        int fontColor = new Color(224, 224, 224, 255).getRGB();
        if (hovered && !hitMaximum()) {
            fontColor = new Color(255, 255, 160, 255).getRGB();
        }
        drawButtonBoxAndText(mc, boxColor, 1, fontColor);
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        if (!hitMaximum()) {
            super.playPressSound(soundHandlerIn);
        }
    }

    private boolean hitMaximum() {
        return (feature == Feature.SUBTRACT && main.getConfigValues().getWarningSeconds() == 1) ||
            (feature == Feature.ADD && main.getConfigValues().getWarningSeconds() == 99);
    }

}
