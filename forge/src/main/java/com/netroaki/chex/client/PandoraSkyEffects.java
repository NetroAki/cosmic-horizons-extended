package com.netroaki.chex.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public final class PandoraSkyEffects extends DimensionSpecialEffects {
  public static final DimensionSpecialEffects INSTANCE = new PandoraSkyEffects();

  private PandoraSkyEffects() {
    super(192.0F, true, SkyType.NORMAL, false, false);
  }

  @Override
  public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
    // Twilight gradient: bias toward purple/teal hues with gentle darkening
    double r = color.x * 0.85 + 0.08;
    double g = color.y * 0.80 + 0.07;
    double b = color.z * 0.95 + 0.10;
    return new Vec3(r, g, b);
  }

  @Override
  public boolean isFoggyAt(int x, int z) {
    return false;
  }
}
