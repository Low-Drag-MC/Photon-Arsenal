package com.lowdragmc.photon_arsenal;


import com.lowdragmc.photon_arsenal.item.*;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTabs;

import static com.lowdragmc.photon_arsenal.PhotonArsenal.REGISTRATE;

public class PhotonCommonProxy {

    public static RegistryEntry<SacabamFishItem> SACABM_FISH = REGISTRATE.item("sacabam_fish", SacabamFishItem::create)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register();

    public static RegistryEntry<GSR> GAUSS_SNIER_RIFLE = REGISTRATE.item("gauss_sniper_rifle", GSR::create)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register();

    public static RegistryEntry<HS> HEILSTORM_SHOTGUN = REGISTRATE.item("heilstrom_shotgun", HS::create)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register();

    public static RegistryEntry<IM> ION_MORTAR = REGISTRATE.item("ion_mortar", IM::create)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register();

    public static RegistryEntry<SRL> SWRAN_ROCKET_LAUNCHER = REGISTRATE.item("swran_rocket_launcher", SRL::create)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register();

    public static RegistryEntry<VR> VOLT_RIFLE = REGISTRATE.item("volt_rifle", VR::create)
            .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register();

    public static void init() {
    }

}
