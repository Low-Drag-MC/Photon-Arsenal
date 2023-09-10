package com.lowdragmc.photon_arsenal.item.forge;

import com.lowdragmc.photon_arsenal.client.WeaponRenderer;
import com.lowdragmc.photon_arsenal.item.SRL;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

/**
 * @author KilaBash
 * @date 2023/9/10
 * @implNote SRLImpl
 */
public class SRLImpl extends SRL {
    protected SRLImpl(Properties properties) {
        super(properties);
    }

    public static SRL create(Item.Properties properties) {
        return new SRLImpl(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private WeaponRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new WeaponRenderer(ForgeRegistries.ITEMS.getKey(SRLImpl.this).getPath());

                return this.renderer;
            }
        });
    }
}
