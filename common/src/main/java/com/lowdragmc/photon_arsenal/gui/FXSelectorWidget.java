package com.lowdragmc.photon_arsenal.gui;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.lowdragmc.photon_arsenal.item.SacabamFishItem.*;

/**
 * @author KilaBash
 * @date 2023/7/21
 * @implNote FXSelectorWidget
 */
public class FXSelectorWidget extends WidgetGroup {
    public static final ResourceBorderTexture BUTTON = new ResourceBorderTexture("photon_arsenal:textures/gui/button.png", 32, 32, 2, 2);
    public static final ResourceBorderTexture BACKGROUND = new ResourceBorderTexture("photon_arsenal:textures/gui/background.png", 16, 16, 4, 4);
    public static final ResourceBorderTexture BACKGROUND_INVERSE = new ResourceBorderTexture("photon_arsenal:textures/gui/background_inverse.png", 16, 16, 4, 4);

    private final ItemStack held;

    public FXSelectorWidget(ItemStack held) {
        super(0, 0, 400, 200);
        this.held = held;

    }

    @Override
    public void initWidget() {
        super.initWidget();
        this.setBackground(BACKGROUND);
        this.initConfigurator();
        if (isRemote()) {
            var size = 200;
            this.addWidget(new ImageWidget(5, 5, size - 10, 15, new GuiTextureGroup(
                    ColorPattern.BLACK.rectTexture(),
                    ColorPattern.WHITE.borderTexture(-1),
                    new TextTexture("").setSupplier(() -> getFXName(held) == null ? "" : getFXName(held)).setWidth(size - 15).setType(TextTexture.TextType.ROLL_ALWAYS)
            )));
            this.addWidget(new ImageWidget(4, 22, size - 8, size - 22 - 4, BACKGROUND_INVERSE));
            var list = new DraggableScrollableWidgetGroup(8, 26, size - 16, size - 26 - 8);
            list.setYScrollBarWidth(4);
            list.setYBarStyle(IGuiTexture.EMPTY, ColorPattern.T_WHITE.rectTexture());
            this.addWidget(list);
            for (var entry : Minecraft.getInstance().getResourceManager().listResources("fx", (arg) -> arg.getPath().endsWith(".fx")).entrySet()) {
                var selectable = new SelectableWidgetGroup(0, 2 + list.getAllWidgetSize() * 12, size - 16 - 4, 10);
                var selectableSize = selectable.getSize();
                var fx = entry.getKey();
                var simple = new ResourceLocation(fx.getNamespace(), fx.getPath().substring(3, fx.getPath().length() - 3));
                selectable.addWidget(new ImageWidget(0, 0, selectableSize.width, selectableSize.height,
                        new TextTexture(simple.toString()).setWidth(selectableSize.width)));
                selectable.setOnSelected(s -> {
                    var tag = held.getOrCreateTag();
                    tag.putString("name", simple.toString());
                    try (var inputStream = entry.getValue().open()) {
                        var data = NbtIo.readCompressed(inputStream);
                        tag.put("fx", data);
                    } catch (Exception ignored) {
                        return;
                    }
                    writeClientAction(-33, buf -> buf.writeNbt(tag));
                });
                list.addWidget(selectable);
            }
        }
    }

    private void initConfigurator() {
        int yOffset = 4;
        this.addWidget(new SwitchWidget(204, yOffset, 40, 20, (cd, pressed) -> setMoveless(held, pressed)).setSupplier(() -> isMoveless(held)).setTexture(new GuiTextureGroup(BUTTON, new TextTexture("movable")), new GuiTextureGroup(BUTTON, new TextTexture("static"))));
        this.addWidget(new SwitchWidget(204 + 45, yOffset, 40, 20, (cd, pressed) -> setPhysics(held, pressed)).setSupplier(() -> hasPhysics(held)).setTexture(new GuiTextureGroup(BUTTON, new TextTexture("no physics")), new GuiTextureGroup(BUTTON, new TextTexture("physics"))));
        this.addWidget(new SliderWidget("Interval (%.0f tick)", 204, yOffset += 24, 192, 20, 1, 100, () -> (float) getInterval(held), f -> setInterval(held, (int) f)));
        this.addWidget(new SliderWidget("Lifetime (%.0f tick)", 204, yOffset += 24, 192, 20, 1, 2000, () -> (float) getLifeTime(held), f -> setLifeTime(held, (int)f)));
        this.addWidget(new SliderWidget("Gravity (%.2f)", 204, yOffset += 24, 192, 20, 0, 2, () -> getGravity(held), f -> setGravity(held, f)));
        this.addWidget(new SliderWidget("BounceChance (%.2f)", 204, yOffset += 24, 192, 20, 0, 1, () -> getBounceChance(held), f -> setBounceChance(held, f)));
        this.addWidget(new SliderWidget("BounceRate (%.2f)", 204, yOffset += 24, 192, 20, 0, 2, () -> getBounceRate(held), f -> setBounceRate(held, f)));
        this.addWidget(new SliderWidget("Inaccuracy (%.2f)", 204, yOffset += 24, 192, 20, 0, 5, () -> getInaccuracy(held), f -> setInaccuracy(held, f)));
        this.addWidget(new SliderWidget("Velocity (%.2f)", 204, yOffset += 24, 192, 20, 0, 5, () -> getVelocity(held), f -> setVelocity(held, f)));
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        if (id == -33) {
            var tag = buffer.readNbt();
            if (tag != null) {
                held.setTag(tag);
            }
        } else {
            super.handleClientAction(id, buffer);
        }
    }
}
