package com.netroaki.chex.particles;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleEffects {
  @OnlyIn(Dist.CLIENT)
  public static void spawnSpiceParticles(Level level, double x, double y, double z, int count) {
    if (level.isClientSide) {
      for (int i = 0; i < count; i++) {
        double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
        double offsetY = level.random.nextDouble() * 0.5;
        double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;

        double motionX = (level.random.nextDouble() - 0.5) * 0.02;
        double motionY = level.random.nextDouble() * 0.02 + 0.01;
        double motionZ = (level.random.nextDouble() - 0.5) * 0.02;

        level.addParticle(
            SpiceParticleType.SPICE_PARTICLE.get(),
            x + offsetX,
            y + offsetY,
            z + offsetZ,
            motionX,
            motionY,
            motionZ);
      }
    }
  }

  public static void spawnSpiceParticles(Level level, Vec3 pos, int count) {
    spawnSpiceParticles(level, pos.x, pos.y, pos.z, count);
  }
}
