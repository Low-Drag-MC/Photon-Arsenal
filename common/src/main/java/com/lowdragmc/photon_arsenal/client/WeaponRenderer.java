package com.lowdragmc.photon_arsenal.client;

import com.lowdragmc.photon_arsenal.PhotonArsenal;
import com.lowdragmc.photon_arsenal.item.WeaponItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author KilaBash
 * @date 2023/7/17
 * @implNote SacabamFishItemRenderer
 */
public class WeaponRenderer extends GeoItemRenderer<WeaponItem> {

    public WeaponRenderer(String path) {
        super(new DefaultedItemGeoModel<>(PhotonArsenal.id(path)));
    }

}
