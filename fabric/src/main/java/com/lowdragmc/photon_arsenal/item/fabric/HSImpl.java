package com.lowdragmc.photon_arsenal.item.fabric;

import com.lowdragmc.photon_arsenal.client.WeaponRenderer;
import com.lowdragmc.photon_arsenal.item.HS;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KilaBash
 * @date 2023/9/10
 * @implNote HSImpl
 */
public class HSImpl extends HS {
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    protected HSImpl(Item.Properties properties) {
        super(properties);
    }

    public static HS create(Item.Properties properties) {
        return new HSImpl(properties);
    }

    // Utilise our own render hook to define our custom renderer
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private WeaponRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new WeaponRenderer(BuiltInRegistries.ITEM.getKey(HSImpl.this).getPath());

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

}
