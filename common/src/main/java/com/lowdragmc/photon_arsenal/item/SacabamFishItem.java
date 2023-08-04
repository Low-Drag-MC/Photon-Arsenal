package com.lowdragmc.photon_arsenal.item;

import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.emitter.beam.BeamEmitter;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.lowdragmc.photon.client.fx.IEffect;
import com.lowdragmc.photon.client.particle.BeamParticle;
import com.lowdragmc.photon_arsenal.gui.FXSelectorWidget;
import dev.architectury.injectables.annotations.ExpectPlatform;
import lombok.val;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author KilaBash
 * @date 2023/7/17
 * @implNote SacabamFishItem
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SacabamFishItem extends Item implements GeoItem, HeldItemUIFactory.IHeldItemUIHolder {
    private final static Map<ResourceLocation, FX> CACHE = new HashMap<>();

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("idol_swimming");
    private static final RawAnimation FIRE_ANIM = RawAnimation.begin().thenPlay("recharging");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected SacabamFishItem(Properties properties) {
        super(properties);
        // Register our item as server-side handled.
        // This enables both animation data syncing and server-side animation triggering
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @ExpectPlatform
    public static SacabamFishItem create(Properties properties) {
        throw new AssertionError();
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
            if (level.isClientSide) {
                if ((72000 - remainingUseDuration) % interval != 0) return;;
                var fxData = getFXCompound(stack);
                var fxName = getFXName(stack);
                if (fxData != null && fxName != null) {
                    FX fx = CACHE.computeIfAbsent(new ResourceLocation(fxName),
                            (name) -> new FX(name, FXHelper.getEmitters(fxData), fxData));
                    var x = -Mth.sin(player.getYRot() * 0.017453292F) * Mth.cos(player.getXRot() * 0.017453292F);
                    var y = -Mth.sin((player.getXRot()) * 0.017453292F);
                    var z = Mth.cos(player.getYRot() * 0.017453292F) * Mth.cos(player.getXRot() * 0.017453292F);
                    var inaccuracy = getInaccuracy(stack);
                    var velocity = getVelocity(stack);
                    var speed = new Vec3(x, y, z).normalize().add(
                            level.random.triangle(0.0, 0.0172275 * inaccuracy),
                            level.random.triangle(0.0, 0.0172275 * inaccuracy),
                            level.random.triangle(0.0, 0.0172275 * inaccuracy))
                            .scale(velocity).toVector3f();
                    var hasPhysics = hasPhysics(stack);
                    var isMoveless = isMoveless(stack);
                    var gravity = getGravity(stack);
                    var bounceChance = getBounceChance(stack);
                    var bounceRate = getBounceRate(stack);
                    val lifeTime = getLifeTime(stack);
                    // start pos
                    var mc = Minecraft.getInstance();
                    Vec3 dest;
                    if (mc.options.getCameraType().isFirstPerson() && player == mc.player) {
                        float f = player.getAttackAnim(mc.getDeltaFrameTime());
                        float f1 = Mth.sin(Mth.sqrt(f) * (float)Math.PI);
                        double d7 = 960.0D / mc.options.fov().get();
                        Vec3 vec3 = mc.gameRenderer.getMainCamera().getNearPlane().getPointOnPlane(0.6F, 0F);
                        vec3 = vec3.scale(d7);
                        vec3 = vec3.yRot(f1 * 0.5F);
                        vec3 = vec3.xRot(-f1 * 0.7F);
                        dest = vec3.add(player.getEyePosition(mc.getDeltaFrameTime()));
                    } else {
                        double d0 = 0.22D * (player.getMainArm() == HumanoidArm.RIGHT ? -1.0D : 1.0D);
                        float f1 = Mth.lerp(0, player.yBodyRotO, player.yBodyRot) * ((float)Math.PI / 180F);
                        double d5 = player.getBoundingBox().getYsize() - 1.0D;
                        double d6 = player.isCrouching() ? -0.2D : 0.07D;
                        if (player.isCrouching()) {
                            dest = player.getPosition(mc.getDeltaFrameTime()).add((new Vec3(d0 - 0.2, d5 - 0.1, d6 + 0.75)).yRot(-f1));
                        } else {
                            dest = player.getPosition(mc.getDeltaFrameTime()).add((new Vec3(d0 - 0.15, d5 + 0.2, d6 + 0.7)).yRot(-f1));
                        }
                    }

                    
                    var lookAngle = player.getLookAngle();
                    List<IParticleEmitter> emitters = new ArrayList<>(fx.generateEmitters());
                    for (IParticleEmitter emitter : emitters) {
                        if (!emitter.isSubEmitter()) {
                            emitter.reset();
                            var particle = emitter.self();
                            // setup properties
                            if (particle instanceof BeamEmitter beam) {
                                var length = beam.getEnd().length();
                                beam.setBeam(beam.getPos(), lookAngle.toVector3f().mul(length));
                            } else {
                                particle.setMoveless(isMoveless);
                                particle.setSpeed(speed);
                                particle.setPhysics(hasPhysics);
                                particle.setGravity(gravity);
                                particle.setBounceChance(bounceChance);
                                particle.setBounceRate(bounceRate);
                            }
                            emitter.emmitToLevel(new IEffect() {
                                @Override
                                public List<IParticleEmitter> getEmitters() {
                                    return emitters;
                                }

                                @Override
                                public boolean updateEmitter(IParticleEmitter emitter) {
                                    if (emitter.self().getAge() > lifeTime) {
                                        emitter.remove(false);
                                    }
                                    return false;
                                }
                            }, level, dest.x, dest.y, dest.z, 0, 0, 0);
                        }
                    }
                }
            } else {
                triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel)level), "FIRE", "fire");
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
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
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "SWIMMING", 0, state -> {
            state.setAnimation(IDLE_ANIM);
            return PlayState.CONTINUE;
        }));
        controllers.add(new AnimationController<>(this, "FIRE", 0, state -> PlayState.STOP)
                .triggerableAnim("fire", FIRE_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
