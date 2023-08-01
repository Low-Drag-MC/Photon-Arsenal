package com.lowdragmc.photon_arsenal.item.fabric;

import com.lowdragmc.photon_arsenal.client.SacabamFishItemRenderer;
import com.lowdragmc.photon_arsenal.item.SacabamFishItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.lowdragmc.photon_arsenal.PhotonArsenal.REGISTRATE;

/**
 * @author KilaBash
 * @date 2023/7/17
 * @implNote SababamFishItemImpl
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SacabamFishItemImpl extends SacabamFishItem {
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    protected SacabamFishItemImpl(Properties properties) {
        super(properties);
        REGISTRATE.register();
    }

    public static SacabamFishItem create(Item.Properties properties) {
        return new SacabamFishItemImpl(properties);
    }

    // Utilise our own render hook to define our custom renderer
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private SacabamFishItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new SacabamFishItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

}
