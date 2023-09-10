package com.lowdragmc.photon_arsenal.client;

import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.emitter.beam.BeamEmitter;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lowdragmc.photon_arsenal.item.SacabamFishItem.*;

/**
 * @author KilaBash
 * @date 2023/8/4
 * @implNote SacabamFishFX
 */
public class WeaponFX {
    private final static Map<ResourceLocation, FX> CACHE = new HashMap<>();


    public static void emitFX(Level level, ItemStack stack, Player player) {
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
                    emitter.emmitToLevel(new SacabamFishEffect(emitters, lifeTime), level, dest.x, dest.y, dest.z, 0, 0, 0);
                }
            }
        }
    }

}
