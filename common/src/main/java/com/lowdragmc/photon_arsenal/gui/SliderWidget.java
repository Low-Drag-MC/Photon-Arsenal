package com.lowdragmc.photon_arsenal.gui;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/7/21
 * @implNote SliderWidget
 */
public class SliderWidget extends Widget {
    public static final ResourceTexture SLIDER_BACKGROUND = new ResourceBorderTexture("photon_arsenal:textures/gui/slider_background.png", 200, 20, 2, 2);
    public static final ResourceTexture SLIDER_ICON = new ResourceBorderTexture("photon_arsenal:textures/gui/slider.png", 8, 20, 2, 2);

    @Setter
    private int sliderWidth = 8;
    @Setter
    private IGuiTexture sliderIcon = SLIDER_ICON;
    @Setter
    private int textColor = 0xFFFFFF;
    @Setter
    private float min;
    @Setter
    private float max;
    @Setter
    private String displayName;
    @Setter
    private FloatConsumer responder;
    @Setter
    private Supplier<Float> provider;
    private float sliderPosition;
    public boolean isMouseDown;

    public SliderWidget(String displayName, int xPosition, int yPosition, int width, int height, float min, float max, @Nonnull Supplier<Float> provider, FloatConsumer responder) {
        super(new Position(xPosition, yPosition), new Size(width, height));
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
        this.displayName = displayName;
        this.provider = provider;
        this.responder = responder;
        this.sliderPosition = (this.provider.get() - this.min) / (this.max - this.min);
        this.setBackground(SLIDER_BACKGROUND);
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        this.sliderPosition = (provider.get() - min) / (max - min);
        buffer.writeFloat(sliderPosition);
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        this.sliderPosition = buffer.readFloat();
    }

    public float getSliderValue() {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    @Override
    public void detectAndSendChanges() {
        var newPosition = (provider.get() - min) / (max - min);
        if (newPosition != sliderPosition) {
            this.sliderPosition = newPosition;
            writeUpdateInfo(1, buffer -> buffer.writeFloat(sliderPosition));
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        Position pos = getPosition();
        Size size = getSize();

        var displayString = displayName.formatted(getSliderValue());
        var font = Minecraft.getInstance().font;
        graphics.drawString(font, displayString,
                pos.x + size.width / 2 - font.width(displayString) / 2,
                pos.y + size.height / 2 - font.lineHeight / 2, textColor, false);

        sliderIcon.draw(graphics, mouseX, mouseY, pos.x + (int) (this.sliderPosition * (float) (size.width - 8)), pos.y, sliderWidth, size.height);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.isMouseDown) {
            setSliderPosition(mouseX);

            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }


    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            setSliderPosition(mouseX);
            this.isMouseDown = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void setSliderPosition(double mouseX) {
        Position pos = getPosition();
        Size size = getSize();
        var newPosition = Mth.clamp((float) (mouseX - (pos.x + 4)) / (float) (size.width - 8), 0, 1);
        if (newPosition != sliderPosition) {
            this.sliderPosition = newPosition;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 3 * this.sliderPosition));
            writeClientAction(1, buffer -> buffer.writeFloat(sliderPosition));
            if (responder != null) {
                responder.accept(getSliderValue());
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.isMouseDown = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        if (id == 1) {
            this.sliderPosition = buffer.readFloat();
            this.sliderPosition = Mth.clamp(sliderPosition, 0.0f, 1.0f);
            if (responder != null) {
                responder.accept(getSliderValue());
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 1) {
            this.sliderPosition = buffer.readFloat();
            this.sliderPosition = Mth.clamp(sliderPosition, 0.0f, 1.0f);
        }
    }

}
