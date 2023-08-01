package com.lowdragmc.photon_arsenal;

import com.lowdragmc.lowdraglib.Platform;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhotonArsenal {
    public static final String MOD_ID = "photon_arsenal";
    public static final String NAME = "Photon-Arsenal";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static void init() {
        LOGGER.info("{} is initializing on platform: {}", NAME, Platform.platformName());
    }

    public final static Registrate REGISTRATE = Registrate.create(MOD_ID);

    public static ResourceLocation id(String sababamFish) {
        return new ResourceLocation(MOD_ID, sababamFish);
    }
}

