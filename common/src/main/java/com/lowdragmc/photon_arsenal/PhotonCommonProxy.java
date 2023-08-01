package com.lowdragmc.photon_arsenal;


import com.lowdragmc.photon_arsenal.item.SacabamFishItem;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTabs;

import static com.lowdragmc.photon_arsenal.PhotonArsenal.REGISTRATE;

public class PhotonCommonProxy {

    public static RegistryEntry<SacabamFishItem> SACABM_FISH = REGISTRATE.item("sacabam_fish", SacabamFishItem::create)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register();

    public static void init() {
    }

}
