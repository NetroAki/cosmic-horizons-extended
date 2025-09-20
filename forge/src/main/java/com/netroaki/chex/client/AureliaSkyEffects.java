package com.netroaki.chex.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public final class AureliaSkyEffects extends DimensionSpecialEffects {
    public static final DimensionSpecialEffects INSTANCE = new AureliaSkyEffects();

    private AureliaSkyEffects() {
        super(192.0F, true, SkyType.NORMAL, false, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
        // Slight teal tint
        return new Vec3(color.x * 0.9 + 0.05, color.y * 0.95 + 0.07, color.z * 1.0);
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }
}
