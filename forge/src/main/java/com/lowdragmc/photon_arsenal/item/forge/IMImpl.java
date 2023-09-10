package com.lowdragmc.photon_arsenal.item.forge;

import com.lowdragmc.photon_arsenal.client.WeaponRenderer;
import com.lowdragmc.photon_arsenal.item.HS;
import com.lowdragmc.photon_arsenal.item.IM;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

/**
 * @author KilaBash
 * @date 2023/9/10
 * @implNote IMImpl
 */
public class IMImpl extends IM {
    protected IMImpl(Properties properties) {
        super(properties);
    }

    public static IM create(Item.Properties properties) {
        return new IMImpl(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private WeaponRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new WeaponRenderer(ForgeRegistries.ITEMS.getKey(IMImpl.this).getPath());

                return this.renderer;
            }
        });
    }
}
