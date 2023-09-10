package com.lowdragmc.photon_arsenal.item;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * @author KilaBash
 * @date 2023/9/10
 * @implNote GSR
 */
public abstract class SRL extends WeaponItem {

    protected SRL(Properties properties) {
        super(properties);
    }

    @ExpectPlatform
    public static SRL create(Properties properties) {
        throw new AssertionError();
    }
}
