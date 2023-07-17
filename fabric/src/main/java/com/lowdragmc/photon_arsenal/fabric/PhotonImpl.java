package com.lowdragmc.photon_arsenal.fabric;

import com.lowdragmc.photon_arsenal.PhotonArsenal;
import com.lowdragmc.photon_arsenal.PhotonCommonProxy;
import net.fabricmc.api.ModInitializer;

public class PhotonImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        PhotonArsenal.init();
        PhotonCommonProxy.init();
    }

}
