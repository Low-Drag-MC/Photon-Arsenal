package com.lowdragmc.photon_arsenal.item;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * @author KilaBash
 * @date 2023/9/10
 * @implNote GSR
 */
public abstract class HS extends WeaponItem {

    protected HS(Properties properties) {
        super(properties);
    }

    @ExpectPlatform
    public static HS create(Properties properties) {
        throw new AssertionError();
    }
}
