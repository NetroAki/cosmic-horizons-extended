package com.netroaki.chex.client.handler;

import com.netroaki.chex.registry.CHEXParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class SporeParticleHandler {
  private static final float SPORE_DENSITY = 0.1f; // Lower = more dense
  private static final int MAX_PARTICLES_PER_TICK = 20;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    if (mc.level == null || mc.player == null) return;

    // Only spawn particles when the game is not paused
    if (mc.isPaused()) return;

    // Only spawn particles in the Sporehaze Thicket biome
    if (!isInSporeBiome(mc.level, mc.player.blockPosition())) return;

    // Limit the number of particles per tick for performance
    int particlesToSpawn =
        mc.options.particles().get().shouldSpawn()
            ? Math.min(1 + mc.level.random.nextInt(3), MAX_PARTICLES_PER_TICK)
            : 0;

    if (particlesToSpawn <= 0) return;

    // Get player position and view vector
    Vec3 pos = mc.player.position();
    Vec3 view = mc.player.getViewVector(1.0F);

    // Calculate area around player to spawn particles
    double range = 16.0;
    double minX = pos.x - range;
    double minY = pos.y - 2.0;
    double minZ = pos.z - range;
    double maxX = pos.x + range;
    double maxY = pos.y + 3.0;
    double maxZ = pos.z + range;

    // Spawn particles
    for (int i = 0; i < particlesToSpawn; i++) {
      // Random position in range
      double x = minX + mc.level.random.nextDouble() * (maxX - minX);
      double y = minY + mc.level.random.nextDouble() * (maxY - minY);
      double z = minZ + mc.level.random.nextDouble() * (maxZ - minZ);

      // Skip if too close to player's view to avoid visual clutter
      Vec3 particlePos = new Vec3(x, y, z);
      if (isInViewFrustum(mc, particlePos, view)) continue;

      // Skip if not in a suitable location (e.g., underwater, in a wall)
      if (!isValidParticleLocation(mc.level, new BlockPos((int) x, (int) y, (int) z))) continue;

      // Random motion
      double motionX = (mc.level.random.nextDouble() - 0.5) * 0.02;
      double motionY = (mc.level.random.nextDouble() - 0.5) * 0.02;
      double motionZ = (mc.level.random.nextDouble() - 0.5) * 0.02;

      // Randomly choose between spore particle types
      ParticleOptions particleType =
          mc.level.random.nextFloat() > 0.3f
              ? CHEXParticleTypes.SPORE_PARTICLE.get()
              : ParticleTypes.WHITE_ASH;

      // Add particle to level
      mc.level.addParticle(
          particleType,
          true, // Always render even if far away
          x,
          y,
          z, // Position
          motionX,
          motionY,
          motionZ // Motion
          );
    }
  }

  private static boolean isInSporeBiome(Level level, BlockPos pos) {
    // Check if the current biome is a Sporehaze Thicket or similar
    Biome biome = level.getBiome(pos).value();
    return biome.getBiomeCategory() == Biome.BiomeCategory.FOREST
        && biome.getPrecipitation() == Biome.Precipitation.RAIN
        && biome.getBaseTemperature() > 0.5f;
  }

  private static boolean isInViewFrustum(Minecraft mc, Vec3 pos, Vec3 view) {
    // Check if position is in the player's view frustum
    Vec3 toParticle = pos.subtract(mc.gameRenderer.getMainCamera().getPosition());
    double dot = toParticle.normalize().dot(view);
    return dot > 0.5 && toParticle.lengthSqr() < 256.0; // Within 16 blocks and in front
  }

  private static boolean isValidParticleLocation(Level level, BlockPos pos) {
    // Check if the position is suitable for spawning particles
    if (!level.isLoaded(pos)) return false;

    // Don't spawn particles inside blocks or underwater
    if (!level.getBlockState(pos).isAir()) return false;
    if (level.getBlockState(pos.below()).isAir()) return false;

    // Check light level (prefer darker areas)
    return level.getMaxLocalRawBrightness(pos) < 12;
  }
}
