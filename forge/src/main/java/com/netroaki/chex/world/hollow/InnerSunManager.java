package com.netroaki.chex.world.hollow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class InnerSunManager {
  private static final int DAY_LENGTH = 24000; // 20 minutes (Minecraft default)
  private static final int SUNRISE_START = 0; // 6:00 AM
  private static final int SUNRISE_END = 1000; // 7:00 AM
  private static final int SUNSET_START = 12000; // 6:00 PM
  private static final int SUNSET_END = 13000; // 7:00 PM
  private static final int NIGHT_START = 13188; // 7:06 PM
  private static final int NIGHT_END = 22812; // 4:54 AM

  // Sun properties
  private static final float MAX_SUN_INTENSITY = 1.0f;
  private static final float MIN_SUN_INTENSITY = 0.1f;
  private static final float HEAT_DAMAGE_THRESHOLD = 0.8f;
  private static final float HEAT_DAMAGE_AMOUNT = 1.0f;

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.level.isClientSide) {
      return;
    }

    ServerLevel level = (ServerLevel) event.level;
    if (level.dimension() != HollowWorldDimension.HOLLOW_WORLD) {
      return;
    }

    // Update sun intensity based on time of day
    updateSunIntensity(level);
  }

  @SubscribeEvent
  public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
    LivingEntity entity = event.getEntity();
    Level level = entity.level();

    if (level.isClientSide || level.dimension() != HollowWorldDimension.HOLLOW_WORLD) {
      return;
    }

    // Only process players and living entities that can be affected by heat
    if (entity instanceof Player || entity.isAffectedByPotions()) {
      updateEntityHeatEffects(entity, level);
    }
  }

  private static void updateSunIntensity(ServerLevel level) {
    long dayTime = level.getDayTime() % DAY_LENGTH;
    float sunIntensity = calculateSunIntensity(dayTime);

    // Update light levels based on sun intensity
    // This is a simplified implementation - in a real mod, you'd need to handle light updates more
    // carefully
    level
        .getServer()
        .getPlayerList()
        .getPlayers()
        .forEach(
            player -> {
              if (player.level().dimension() == HollowWorldDimension.HOLLOW_WORLD) {
                updatePlayerLightLevels(player, sunIntensity);
              }
            });
  }

  private static float calculateSunIntensity(long dayTime) {
    if (dayTime < SUNRISE_START) {
      // Night time (before sunrise)
      return MIN_SUN_INTENSITY;
    } else if (dayTime < SUNRISE_END) {
      // Sunrise
      return MIN_SUN_INTENSITY
          + (MAX_SUN_INTENSITY - MIN_SUN_INTENSITY)
              * ((float) (dayTime - SUNRISE_START) / (SUNRISE_END - SUNRISE_START));
    } else if (dayTime < SUNSET_START) {
      // Day time
      return MAX_SUN_INTENSITY;
    } else if (dayTime < SUNSET_END) {
      // Sunset
      return MAX_SUN_INTENSITY
          - (MAX_SUN_INTENSITY - MIN_SUN_INTENSITY)
              * ((float) (dayTime - SUNSET_START) / (SUNSET_END - SUNSET_START));
    } else {
      // Night time (after sunset)
      return MIN_SUN_INTENSITY;
    }
  }

  private static void updatePlayerLightLevels(Player player, float sunIntensity) {
    // Calculate distance from the center (Hollow World is a sphere with center at 0,0,0)
    double distance = player.position().length();
    double normalizedDistance = Mth.clamp(distance / 1000.0, 0.0, 1.0);

    // Calculate light level based on sun intensity and distance from center
    int lightLevel = (int) (15 * sunIntensity * (1.0 - normalizedDistance));

    // Update the light level for the player's position
    BlockPos pos = player.blockPosition();
    player.level().getChunkSource().getLightEngine().checkBlock(pos);
  }

  private static void updateEntityHeatEffects(LivingEntity entity, Level level) {
    if (level.isClientSide) {
      return;
    }

    // Calculate distance from the center
    double distance = entity.position().length();
    double normalizedDistance = Mth.clamp(distance / 1000.0, 0.0, 1.0);

    // Calculate heat intensity based on time of day and distance
    long dayTime = level.getDayTime() % DAY_LENGTH;
    float sunIntensity = calculateSunIntensity(dayTime);
    float heatIntensity = (float) (sunIntensity * (1.0 - normalizedDistance));

    // Apply heat effects
    if (heatIntensity > HEAT_DAMAGE_THRESHOLD
        && !entity.isInvulnerableTo(level.damageSources().onFire())) {
      // Apply fire damage if intensity is above threshold
      entity.setSecondsOnFire(1);
      entity.hurt(level.damageSources().onFire(), HEAT_DAMAGE_AMOUNT);
    } else if (heatIntensity > HEAT_DAMAGE_THRESHOLD * 0.7) {
      // Apply slowness and mining fatigue at high heat
      entity.addEffect(
          new MobEffectInstance(
              MobEffects.MOVEMENT_SLOWDOWN,
              100,
              (int) ((heatIntensity - 0.5) * 2),
              false,
              false,
              true));

      entity.addEffect(
          new MobEffectInstance(
              MobEffects.DIG_SLOWDOWN, 100, (int) ((heatIntensity - 0.5) * 2), false, false, true));
    }
  }

  public static float getSunIntensity(Level level, BlockPos pos) {
    if (level.dimension() != HollowWorldDimension.HOLLOW_WORLD) {
      return 0.0f;
    }

    long dayTime = level.getDayTime() % DAY_LENGTH;
    float sunIntensity = calculateSunIntensity(dayTime);

    // Calculate distance from the center
    double distance =
        Math.sqrt(pos.getX() * pos.getX() + pos.getY() * pos.getY() + pos.getZ() * pos.getZ());
    double normalizedDistance = Mth.clamp(distance / 1000.0, 0.0, 1.0);

    return (float) (sunIntensity * (1.0 - normalizedDistance));
  }
}
