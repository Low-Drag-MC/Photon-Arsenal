package com.lowdragmc.photon_arsenal.item.forge;

import com.lowdragmc.photon_arsenal.client.SacabamFishItemRenderer;
import com.lowdragmc.photon_arsenal.item.SacabamFishItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * @author KilaBash
 * @date 2023/7/17
 * @implNote SababamFishItemImpl
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SacabamFishItemImpl extends SacabamFishItem {
    protected SacabamFishItemImpl(Properties properties) {
        super(properties);
    }

    public static SacabamFishItem create(Item.Properties properties) {
        return new SacabamFishItemImpl(properties);
    }

    // Utilise the existing forge hook to define our custom renderer
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SacabamFishItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new SacabamFishItemRenderer();

                return this.renderer;
            }
        });
    }

}
