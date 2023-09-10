package com.lowdragmc.photon_arsenal.item;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/7/17
 * @implNote SacabamFishItem
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SacabamFishItem extends WeaponItem {

    private final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("idol_swimming");
    private final RawAnimation FIRE_ANIM = RawAnimation.begin().thenPlay("recharging");

    protected SacabamFishItem(Properties properties) {
        super(properties);
    }

    @ExpectPlatform
    public static SacabamFishItem create(Properties properties) {
        throw new AssertionError();
    }

    //////////////////////////////////////
    //********     Geckolib    *********//
    //////////////////////////////////////
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "SWIMMING", 0, state -> {
            state.setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }));
        controllers.add(new AnimationController<>(this, "FIRE", 0, state -> PlayState.STOP)
                .triggerableAnim("fire", FIRE_ANIM));
    }
}
