package me.shardcoder.skyblockaddon.gui.buttons;

import java.math.BigDecimal;
import me.shardcoder.skyblockaddon.SkyblockAddon;
import me.shardcoder.skyblockaddon.utils.ConfigColor;
import me.shardcoder.skyblockaddon.utils.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class ButtonSlider extends GuiButton {
    public final static float GUI_SCALE_MINIMUM = 0.5F;
    public final static float GUI_SCALE_MAXIMUM = 5;
    public final static float GUI_SCALE_STEP = 0.1F;

    private float sliderValue;
    private boolean dragging;

    private SkyblockAddon main;

    /**
     * Right now this slider button is made specifically for changing the GUI scale.
     * Edited version of {@link net.minecraft.client.gui.GuiOptionSlider}
     */
    public ButtonSlider(int buttonID, double x, double y, int width, int height, SkyblockAddon main) {
        super(buttonID, (int)x, (int)y, width, height, "");
        this.sliderValue = main.getConfigValues().getGuiScale();
        this.displayString = Message.SETTING_GUI_SCALE.getMessage(String.valueOf(getRoundedValue(main.getUtils().denormalizeValue(sliderValue, GUI_SCALE_MINIMUM, GUI_SCALE_MAXIMUM, GUI_SCALE_STEP))));
        this.main = main;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        FontRenderer fontrenderer = mc.fontRendererObj;
        mc.getTextureManager().bindTexture(buttonTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        int boxAlpha = 100;
        if (hovered) {
            boxAlpha = 170;
        }
        drawRect(this.xPosition, this.yPosition, this.xPosition+this.width, this.yPosition+this.height, main.getUtils().getDefaultColor(boxAlpha));
        this.mouseDragged(mc, mouseX, mouseY);
        int j = 14737632;
        /*if (packedFGColour != 0) {
            j = packedFGColour;
        } else */if (!this.enabled) {
            j = 10526880;
        } else if (this.hovered) {
            j = 16777120;
        }
        this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
    }

    protected int getHoverState(boolean mouseOver) {
        return 0;
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                float scaleValue = main.getUtils().denormalizeValue(this.sliderValue, GUI_SCALE_MINIMUM, GUI_SCALE_MAXIMUM, GUI_SCALE_STEP);
                this.sliderValue = main.getUtils().normalizeValue(scaleValue, GUI_SCALE_MINIMUM, GUI_SCALE_MAXIMUM, GUI_SCALE_STEP);
                main.getConfigValues().setGuiScale(sliderValue);
                this.displayString = Message.SETTING_GUI_SCALE.getMessage(String.valueOf(getRoundedValue(main.getUtils().denormalizeValue(sliderValue, GUI_SCALE_MINIMUM, GUI_SCALE_MAXIMUM, GUI_SCALE_STEP))));
            }

            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8))+1, this.yPosition, this.xPosition + (int) (this.sliderValue * (float) (this.width - 8))+7, this.yPosition + 20, ConfigColor.WHITE.getColor(255));
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            main.getConfigValues().setGuiScale(sliderValue);
            this.displayString =Message.SETTING_GUI_SCALE.getMessage(String.valueOf(getRoundedValue(main.getUtils().denormalizeValue(sliderValue, GUI_SCALE_MINIMUM, GUI_SCALE_MAXIMUM, GUI_SCALE_STEP))));
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    private float getRoundedValue(float value) {
        return new BigDecimal(String.valueOf(value)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
