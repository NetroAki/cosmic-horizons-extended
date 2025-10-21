package com.netroaki.chex.world.stormworld.weather;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WeatherCycleManager {
  private static final int CYCLE_LENGTH = 24000; // One Minecraft day in ticks
  private static final int STORM_CHANCE = 30; // Percentage chance of a storm in a given cycle
  private static final int MIN_STORMS_PER_CYCLE = 0;
  private static final int MAX_STORMS_PER_CYCLE = 3;

  private static int currentCycleTime = 0;
  private static int stormsThisCycle = 0;
  private static int stormStartTime = -1;
  private static int stormDuration = 0;
  private static float stormIntensity = 0f;

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END
        || event.level.isClientSide
        || !isStormworldDimension(event.level)) {
      return;
    }

    ServerLevel level = (ServerLevel) event.level;
    currentCycleTime = (currentCycleTime + 1) % CYCLE_LENGTH;

    // Start of a new cycle
    if (currentCycleTime == 0) {
      startNewCycle(level);
    }

    // Check if it's time to start a storm
    if (stormsThisCycle > 0 && stormStartTime == currentCycleTime) {
      startStorm(level);
    }

    // Update current storm if active
    if (StormworldWeatherManager.isStormActive()) {
      updateStorm(level);
    }

    // Update weather effects based on time of day
    updateTimeBasedEffects(level);
  }

  private static void startNewCycle(ServerLevel level) {
    Random random = level.random;

    // Determine number of storms in this cycle
    stormsThisCycle =
        random.nextInt(MAX_STORMS_PER_CYCLE - MIN_STORMS_PER_CYCLE + 1) + MIN_STORMS_PER_CYCLE;

    // If we're having storms, schedule them
    if (stormsThisCycle > 0) {
      // Distribute storms throughout the cycle
      int stormInterval = CYCLE_LENGTH / (stormsThisCycle + 1);
      for (int i = 0; i < stormsThisCycle; i++) {
        int stormTime =
            stormInterval * (i + 1) + random.nextInt(stormInterval / 2) - (stormInterval / 4);
        stormTime = Mth.clamp(stormTime, 0, CYCLE_LENGTH - 1);

        // Only set the first storm's time, others will be set after previous storm ends
        if (i == 0) {
          stormStartTime = stormTime;
        }
      }
    }

    // Reset cycle tracking
    currentCycleTime = 0;
  }

  private static void startStorm(ServerLevel level) {
    Random random = level.random;

    // Determine storm intensity (0.3 to 1.0)
    stormIntensity = 0.3f + random.nextFloat() * 0.7f;

    // Determine storm duration (30 seconds to 5 minutes)
    float durationFactor = stormIntensity * stormIntensity; // More intense storms last longer
    stormDuration = (int) (600 + (random.nextFloat() * 2400) * durationFactor);

    // Start the storm
    StormworldWeatherManager.startStorm(level, stormIntensity);

    // Schedule next storm if there are more this cycle
    stormsThisCycle--;
    if (stormsThisCycle > 0) {
      int nextStormIn = CYCLE_LENGTH / (stormsThisCycle + 1);
      stormStartTime = (currentCycleTime + nextStormIn) % CYCLE_LENGTH;
    } else {
      stormStartTime = -1;
    }
  }

  private static void updateStorm(ServerLevel level) {
    // Gradually change storm intensity
    if (level.random.nextInt(100) == 0) {
      // Slight random variation in intensity
      float intensityChange = (level.random.nextFloat() - 0.5f) * 0.1f;
      stormIntensity = Mth.clamp(stormIntensity + intensityChange, 0.3f, 1.0f);
      StormworldWeatherManager.setStormIntensity(stormIntensity);
    }

    // Check if storm should end
    stormDuration--;
    if (stormDuration <= 0) {
      StormworldWeatherManager.endStorm(level);
    }
  }

  private static void updateTimeBasedEffects(ServerLevel level) {
    // Make storms more likely at night
    float timeOfDay = (float) level.getDayTime() % 24000 / 24000.0f;
    float nightFactor = 1.0f;

    if (timeOfDay > 0.25f && timeOfDay < 0.75f) {
      // Daytime - reduce storm intensity
      nightFactor = 0.7f;
    } else if (timeOfDay > 0.2f && timeOfDay < 0.8f) {
      // Dusk/dawn - normal intensity
      nightFactor = 1.0f;
    } else {
      // Nighttime - increase storm intensity
      nightFactor = 1.3f;
    }

    // Apply time-based intensity adjustment
    if (StormworldWeatherManager.isStormActive()) {
      StormworldWeatherManager.setTimeOfDayFactor(nightFactor);
    }
  }

  public static float getStormIntensity() {
    return stormIntensity;
  }

  public static int getTimeUntilNextStorm() {
    if (stormStartTime < 0) return -1;
    return (stormStartTime - currentCycleTime + CYCLE_LENGTH) % CYCLE_LENGTH;
  }

  public static boolean isStormImminent() {
    if (stormStartTime < 0) return false;
    int timeUntil = getTimeUntilNextStorm();
    return timeUntil >= 0 && timeUntil < 200; // Warning 10 seconds before storm
  }

  private static boolean isStormworldDimension(Level level) {
    // TODO: Replace with actual dimension check
    return level.dimension().location().getPath().equals("stormworld");
  }
}
