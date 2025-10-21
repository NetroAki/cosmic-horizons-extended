package com.netroaki.chex.world.arrakis;

import com.netroaki.chex.particle.ArrakisParticleTypes;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArrakisParticleSpawner {
  private static final Random RANDOM = new Random();
  private static final int SAND_PARTICLE_RANGE = 32;
  private static final int DUST_WHIRL_RANGE = 64;
  private static final int HEAT_HAZE_RANGE = 16;

  @SubscribeEvent
  public static void onClientTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.side.isServer()) {
      return;
    }

    Level level = event.level;

    // Only run in Arrakis dimension
    if (!level.dimension().location().getPath().equals("arrakis")) {
      return;
    }

    // Only run for players
    if (level.players().isEmpty()) {
      return;
    }

    // Get a random player to center effects around
    Player player = level.players().get(level.random.nextInt(level.players().size()));
    if (player == null) {
      return;
    }

    // Don't spam particles if the game is paused
    if (level.isClientSide && level.getGameTime() % 2 != 0) {
      return;
    }

    // Get biome data
    BlockPos playerPos = player.blockPosition();
    Biome biome = level.getBiome(playerPos).value();
    float temperature = biome.getBaseTemperature();

    // Only spawn particles in hot biomes
    if (temperature < 1.5f) {
      return;
    }

    // Spawn sand particles (more frequent in sandstorms)
    if (level.random.nextFloat() < 0.3f) {
      spawnSandParticles(level, player);
    }

    // Spawn dust whirls (less frequent)
    if (level.random.nextFloat() < 0.05f) {
      spawnDustWhirl(level, player);
    }

    // Spawn heat haze (more frequent in extreme heat)
    if (level.random.nextFloat() < 0.2f) {
      spawnHeatHaze(level, player, temperature);
    }
  }

  private static void spawnSandParticles(Level level, Player player) {
    RandomSource random = level.random;
    Vec3 playerPos = player.position();

    // Calculate wind direction (can be based on time or other factors)
    double windAngle = level.getGameTime() * 0.01 % (2 * Math.PI);
    double windX = Math.cos(windAngle) * 0.1;
    double windZ = Math.sin(windAngle) * 0.1;

    // Spawn multiple particles in a cone in front of the player
    for (int i = 0; i < 3; i++) {
      double offsetX = (random.nextDouble() - 0.5) * SAND_PARTICLE_RANGE * 2;
      double offsetY = random.nextDouble() * 4.0 - 1.0;
      double offsetZ = (random.nextDouble() - 0.5) * SAND_PARTICLE_RANGE * 2;

      // Skip if too far from player
      if (offsetX * offsetX + offsetZ * offsetZ > SAND_PARTICLE_RANGE * SAND_PARTICLE_RANGE) {
        continue;
      }

      // Calculate position
      double x = playerPos.x + offsetX;
      double y = playerPos.y + 1.5 + offsetY;
      double z = playerPos.z + offsetZ;

      // Add wind effect
      double motionX = windX + (random.nextDouble() - 0.5) * 0.1;
      double motionY = (random.nextDouble() - 0.5) * 0.05;
      double motionZ = windZ + (random.nextDouble() - 0.5) * 0.1;

      // Spawn particle
      level.addParticle(
          ArrakisParticleTypes.SAND_PARTICLE.get(), true, x, y, z, motionX, motionY, motionZ);
    }
  }

  private static void spawnDustWhirl(Level level, Player player) {
    RandomSource random = level.random;
    Vec3 playerPos = player.position();

    // Choose a random position near the player
    double angle = random.nextDouble() * 2 * Math.PI;
    double distance = 5.0 + random.nextDouble() * (DUST_WHIRL_RANGE - 5.0);

    double x = playerPos.x + Math.cos(angle) * distance;
    double y = playerPos.y + random.nextDouble() * 4.0;
    double z = playerPos.z + Math.sin(angle) * distance;

    // Check if the position is valid (not inside blocks)
    BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
    if (!level.getBlockState(pos).isAir()) {
      return;
    }

    // Spawn dust whirl
    level.addParticle(ArrakisParticleTypes.DUST_WHIRL.get(), true, x, y, z, 0, 0, 0);
  }

  private static void spawnHeatHaze(Level level, Player player, float temperature) {
    if (temperature < 1.8f) {
      return; // Only in very hot areas
    }

    RandomSource random = level.random;
    Vec3 playerPos = player.position();

    // Spawn near the ground in hot biomes
    double x = playerPos.x + (random.nextDouble() - 0.5) * HEAT_HAZE_RANGE * 2;
    double z = playerPos.z + (random.nextDouble() - 0.5) * HEAT_HAZE_RANGE * 2;

    // Find ground level
    int groundY = -1;
    for (int y = (int) playerPos.y + 5; y > playerPos.y - 5; y--) {
      if (!level.getBlockState(new BlockPos((int) x, y, (int) z)).isAir()) {
        groundY = y + 1;
        break;
      }
    }

    if (groundY == -1) {
      return;
    }

    // Spawn heat haze just above ground
    level.addParticle(ArrakisParticleTypes.HEAT_HAZE.get(), true, x, groundY + 0.1, z, 0, 0, 0);
  }
}
