package com.netroaki.chex.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

/** Custom sky rendering effects for the Arrakis dimension (harsh desert planet). */
public final class ArrakisSkyEffects extends DimensionSpecialEffects {
  public static final DimensionSpecialEffects INSTANCE = new ArrakisSkyEffects();

  private ArrakisSkyEffects() {
    super(192.0F, true, SkyType.NORMAL, false, false);
  }

  @Override
  public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
    // Red-tinted desert sky with harsh sunlight
    float f = 0.5F;
    float f1 = (float) Math.cos(sunHeight * Math.PI * 2.0D) - 0.0F;
    float f2 = -0.0F;
    if (f1 >= f2) {
      if (f1 > 0.0F) {
        f2 = f1;
      } else {
        f2 = 0.0F;
      }

      if (f1 > 0.0F) {
        f2 = f1 / f;
        f2 = 1.0F - f2;
        f2 = (float) Math.sin(f2 * Math.PI);
        f2 = f2 * f2 * f2 * f2;
      }

      // Apply red tint for desert atmosphere
      f1 = 1.0F - f1 * 0.95F;
      f1 = f1 * f1 * f1 * f1;
      color = new Vec3(color.x * f1 * 1.2, color.y * f1 * 0.8, color.z * f1 * 0.6);
    }
    return color;
  }

  @Override
  public boolean isFoggyAt(int x, int z) {
    return false;
  }
}
