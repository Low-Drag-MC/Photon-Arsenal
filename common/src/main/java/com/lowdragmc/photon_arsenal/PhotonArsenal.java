package com.lowdragmc.photon_arsenal;

import com.lowdragmc.lowdraglib.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhotonArsenal {
    public static final String MOD_ID = "photon_arsenal";
    public static final String NAME = "Photon-Arsenal";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static void init() {
        LOGGER.info("{} is initializing on platform: {}", NAME, Platform.platformName());
    }

}

