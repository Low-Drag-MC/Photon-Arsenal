package com.lowdragmc.photon_arsenal.forge;

import com.lowdragmc.photon_arsenal.PhotonArsenal;
import com.lowdragmc.photon_arsenal.client.forge.ClientProxyImpl;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(PhotonArsenal.MOD_ID)
public class PhotonArsenalImpl {
    public PhotonArsenalImpl() {
        PhotonArsenal.init();
        DistExecutor.unsafeRunForDist(() -> ClientProxyImpl::new, () -> CommonProxyImpl::new);
    }

}
