package com.lowdragmc.photon_arsenal.client;

import com.lowdragmc.photon_arsenal.PhotonArsenal;
import com.lowdragmc.photon_arsenal.item.SacabamFishItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author KilaBash
 * @date 2023/7/17
 * @implNote SacabamFishItemRenderer
 */
public class SacabamFishItemRenderer extends GeoItemRenderer<SacabamFishItem> {

    public SacabamFishItemRenderer() {
        super(new DefaultedItemGeoModel<>(PhotonArsenal.id("sacabam_fish")));
    }

}
