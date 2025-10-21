package com.netroaki.chex.world.stormworld.weather;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StormworldWeatherManager {
  private static final int STORM_DURATION = 12000; // 10 minutes in ticks
  private static final int MIN_CALM_DURATION = 6000; // 5 minutes in ticks
  private static final int MAX_CALM_DURATION = 18000; // 15 minutes in ticks

  private static int stormTimer = 0;
  private static boolean isStormActive = false;
  private static float stormIntensity = 0f;
  private static float targetIntensity = 0f;
  private static int timeUntilStorm = 0;
  private static int stormSeverity = 0; // 0-2: minor, 3-5: normal, 6-8: severe, 9: supercell

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END
        || event.level.isClientSide
        || !isStormworldDimension(event.level)) {
      return;
    }

    ServerLevel level = (ServerLevel) event.level;

    if (isStormActive) {
      // Update active storm
      stormTimer--;

      // Gradually adjust intensity towards target
      if (stormIntensity < targetIntensity) {
        stormIntensity = Math.min(targetIntensity, stormIntensity + 0.001f);
      } else if (stormIntensity > targetIntensity) {
        stormIntensity = Math.max(targetIntensity, stormIntensity - 0.001f);
      }

      // Randomly adjust target intensity during storm
      if (level.random.nextInt(100) == 0) {
        targetIntensity =
            Mth.clamp(targetIntensity + (level.random.nextFloat() - 0.5f) * 0.2f, 0.3f, 1.0f);
      }

      // Check if storm should end
      if (stormTimer <= 0) {
        endStorm(level);
      } else {
        // Spawn lightning occasionally
        if (level.random.nextFloat() < (0.01f * stormIntensity * (stormSeverity + 1))) {
          spawnLightning(level);
        }
      }
    } else {
      // Count down to next storm
      if (timeUntilStorm > 0) {
        timeUntilStorm--;
      } else {
        startStorm(level);
      }
    }
  }

  private static void startStorm(ServerLevel level) {
    isStormActive = true;
    stormIntensity = 0f;
    targetIntensity = 0.3f + level.random.nextFloat() * 0.7f;

    // Random storm duration with severity affecting length
    stormSeverity = level.random.nextInt(10);
    float severityModifier = 1.0f + (stormSeverity * 0.2f);
    stormTimer = (int) (STORM_DURATION * severityModifier);

    // Broadcast storm start to players
    // TODO: Add storm start packet
  }

  private static void endStorm(ServerLevel level) {
    isStormActive = false;
    stormIntensity = 0f;
    targetIntensity = 0f;

    // Random calm duration before next storm
    timeUntilStorm =
        MIN_CALM_DURATION + level.random.nextInt(MAX_CALM_DURATION - MIN_CALM_DURATION);

    // Broadcast storm end to players
    // TODO: Add storm end packet
  }

  private static void spawnLightning(ServerLevel level) {
    // Find a random position within the world border
    int range = 64;
    BlockPos.MutableBlockPos pos =
        new BlockPos.MutableBlockPos(
            level.random.nextInt(range * 2) - range, 0, level.random.nextInt(range * 2) - range);

    // Find the surface
    pos.setY(level.getHeight(level.getMinBuildHeight(), pos.getX(), pos.getZ()));

    // Only spawn lightning above solid blocks
    if (!level.getBlockState(pos.below()).isAir()) {
      // TODO: Spawn custom lightning entity
      // level.addFreshEntity(new StormworldLightningBolt(level, pos.getX(), pos.getY(),
      // pos.getZ()));

      // For now, use vanilla lightning
      level.strikeLightning(pos);
    }
  }

  public static boolean isStormActive() {
    return isStormActive;
  }

  public static float getStormIntensity() {
    return stormIntensity;
  }

  public static int getStormSeverity() {
    return stormSeverity;
  }

  public static Vec3 getWindDirection(Level level, BlockPos pos) {
    if (!isStormActive || !isStormworldDimension(level)) {
      return Vec3.ZERO;
    }

    // Simple wind direction based on position and time
    double time = level.getGameTime() * 0.01;
    double angle = Math.sin(time * 0.1) * Math.PI * 0.25;

    // Add some randomness based on position
    double noiseX = Mth.noise(pos.getX() * 0.01, 0, pos.getZ() * 0.01 + time) * 0.2;
    double noiseZ = Mth.noise(pos.getX() * 0.01 + 1000, 0, pos.getZ() * 0.01 + time + 1000) * 0.2;

    // Calculate wind vector
    double windX = Math.sin(angle + noiseX) * stormIntensity;
    double windZ = Math.cos(angle + noiseZ) * stormIntensity;

    return new Vec3(windX, 0, windZ).scale(0.1 * (1 + stormSeverity * 0.2));
  }

  private static boolean isStormworldDimension(Level level) {
    return level.dimension().location().getPath().equals("stormworld")
        || level.dimension().location().getPath().contains("storm");
  }
}
