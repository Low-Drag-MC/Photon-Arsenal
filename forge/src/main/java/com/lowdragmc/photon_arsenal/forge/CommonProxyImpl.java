package com.lowdragmc.photon_arsenal.forge;

import com.lowdragmc.photon_arsenal.PhotonCommonProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonProxyImpl {

    public CommonProxyImpl() {
        // used for forge events (ClientProxy + CommonProxy)
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(this);
        // init common features
        PhotonCommonProxy.init();
    }

}
