package com.lowdragmc.photon_arsenal.item;

import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.photon_arsenal.client.WeaponFX;
import com.lowdragmc.photon_arsenal.gui.FXSelectorWidget;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

/**
 * @author KilaBash
 * @date 2023/9/10
 * @implNote WeaponItem
 */
public abstract class WeaponItem extends Item implements GeoItem, HeldItemUIFactory.IHeldItemUIHolder {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected WeaponItem(Properties properties) {
        super(properties);
        // Register our item as server-side handled.
        // This enables both animation data syncing and server-side animation triggering
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    //////////////////////////////////////
    //********    Properties    ********//
    //////////////////////////////////////
    public static int getInterval(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var interval = 1;
        if (tag.contains("interval")) {
            interval = tag.getInt("interval");
        }
        return Math.max(interval, 1);
    }

    public static void setInterval(ItemStack itemStack, int interval) {
        var tag = itemStack.getOrCreateTag();
        tag.putInt("interval", Math.max(1, interval));
    }

    public static int getLifeTime(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var lifeTime = 5 * 20;
        if (tag.contains("lifeTime")) {
            lifeTime = tag.getInt("lifeTime");
        }
        return Math.max(lifeTime, 1);
    }

    public static void setLifeTime(ItemStack itemStack, int lifeTime) {
        var tag = itemStack.getOrCreateTag();
        tag.putInt("lifeTime", Math.max(1, lifeTime));
    }

    public static float getGravity(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var gravity = 0.98f;
        if (tag.contains("gravity")) {
            gravity = tag.getFloat("gravity");
        }
        return Math.max(gravity, 0);
    }

    public static void setGravity(ItemStack itemStack, float gravity) {
        var tag = itemStack.getOrCreateTag();
        tag.putFloat("gravity", Math.max(0, gravity));
    }

    public static float getBounceChance(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var bounceChance = 1f;
        if (tag.contains("bounceChance")) {
            bounceChance = tag.getFloat("bounceChance");
        }
        return Mth.clamp(bounceChance, 0, 1);
    }

    public static void setBounceChance(ItemStack itemStack, float bounceChance) {
        var tag = itemStack.getOrCreateTag();
        tag.putFloat("bounceChance", Mth.clamp(bounceChance, 0, 1));
    }

    public static float getBounceRate(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var bounceRate = 1f;
        if (tag.contains("bounceRate")) {
            bounceRate = tag.getFloat("bounceRate");
        }
        return Math.max(bounceRate, 0);
    }

    public static void setBounceRate(ItemStack itemStack, float bounceRate) {
        var tag = itemStack.getOrCreateTag();
        tag.putFloat("bounceRate", Math.max(0, bounceRate));
    }

    public static boolean hasPhysics(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var physics = true;
        if (tag.contains("physics")) {
            physics = tag.getBoolean("physics");
        }
        return physics;
    }

    public static void setPhysics(ItemStack itemStack, boolean physics) {
        var tag = itemStack.getOrCreateTag();
        tag.putBoolean("physics", physics);
    }

    public static boolean isMoveless(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var moveless = false;
        if (tag.contains("moveless")) {
            moveless = tag.getBoolean("moveless");
        }
        return moveless;
    }

    public static void setMoveless(ItemStack itemStack, boolean moveless) {
        var tag = itemStack.getOrCreateTag();
        tag.putBoolean("moveless", moveless);
    }

    public static float getInaccuracy(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var inaccuracy = 1f;
        if (tag.contains("inaccuracy")) {
            inaccuracy = tag.getFloat("inaccuracy");
        }
        return Math.max(inaccuracy, 0);
    }

    public static void setInaccuracy(ItemStack itemStack, float inaccuracy) {
        var tag = itemStack.getOrCreateTag();
        tag.putFloat("inaccuracy", Math.max(0, inaccuracy));
    }

    public static float getVelocity(ItemStack itemStack) {
        var tag = itemStack.getOrCreateTag();
        var velocity = 1f;
        if (tag.contains("velocity")) {
            velocity = tag.getFloat("velocity");
        }
        return Math.max(velocity, 0);
    }

    public static void setVelocity(ItemStack itemStack, float velocity) {
        var tag = itemStack.getOrCreateTag();
        tag.putFloat("velocity", Math.max(0, velocity));
    }

    @Nullable
    public static String getFXName(ItemStack item) {
        var tag = item.getOrCreateTag();
        if (tag.contains("name")) {
            return tag.getString("name");
        }
        return null;
    }

    @Nullable
    public static CompoundTag getFXCompound(ItemStack item) {
        var tag = item.getOrCreateTag();
        if (tag.contains("fx")) {
            return tag.getCompound("fx");
        }
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            if (player instanceof ServerPlayer serverPlayer) {
                HeldItemUIFactory.INSTANCE.openUI(serverPlayer, hand);
            }
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        player.startUsingItem(hand);
        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity shooter, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, shooter, stack, remainingUseDuration);
        if (shooter instanceof Player player) {
            int interval = getInterval(stack);
            if ((72000 - remainingUseDuration) % interval != 0) return;
            if (level.isClientSide) {
                WeaponFX.emitFX(level, stack, player);
            } else {
                triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel)level), "FIRE", "fire");
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    //////////////////////////////////////
    //*********       GUI      *********//
    //////////////////////////////////////
    @Override
    public ModularUI createUI(Player entityPlayer, HeldItemUIFactory.HeldItemHolder holder) {
        return new ModularUI(400, 200, holder, entityPlayer)
                .widget(new FXSelectorWidget(holder.getHeld()));
    }

    //////////////////////////////////////
    //********     Geckolib    *********//
    //////////////////////////////////////
    private final RawAnimation FIRE_ANIM = RawAnimation.begin().thenPlay("fire");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "FIRE", 0, state -> PlayState.STOP)
                .triggerableAnim("fire", FIRE_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
