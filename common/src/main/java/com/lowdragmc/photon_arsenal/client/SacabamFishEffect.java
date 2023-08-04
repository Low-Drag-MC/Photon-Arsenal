package com.lowdragmc.photon_arsenal.client;

import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.fx.IEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

/**
 * @author KilaBash
 * @date 2023/8/4
 * @implNote SacabamFishEffect
 */
@Environment(EnvType.CLIENT)
public class SacabamFishEffect implements IEffect {
    private final List<IParticleEmitter> emitters;
    private final int lifeTime;

    public SacabamFishEffect(List<IParticleEmitter> emitters, int lifeTime) {
        this.emitters = emitters;
        this.lifeTime = lifeTime;
    }

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
}
