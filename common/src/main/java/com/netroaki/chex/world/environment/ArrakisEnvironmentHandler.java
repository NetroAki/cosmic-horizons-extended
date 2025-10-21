package com.netroaki.chex.world.environment;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArrakisEnvironmentHandler {
  private static final int HEAT_DAMAGE_INTERVAL = 100; // 5 seconds at 20 TPS
  private static final float HEAT_DAMAGE_AMOUNT = 2.0F;
  private static final int DUST_STORM_CHECK_INTERVAL = 200; // 10 seconds at 20 TPS
  private static final float DUST_STORM_CHANCE = 0.2F;
  private static final int DUST_STORM_MIN_DURATION = 1200; // 1 minute at 20 TPS
  private static final int DUST_STORM_MAX_DURATION = 3600; // 3 minutes at 20 TPS

  private static int heatDamageCounter = 0;
  private static int dustStormCheckCounter = 0;
  private static int dustStormDuration = 0;
  private static boolean isDustStormActive = false;

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) {
      return;
    }

    Level level = event.level;

    // Only process in Arrakis dimension
    if (!level.dimension().location().getPath().equals("arrakis")) {
      return;
    }

    // Handle heat damage
    if (++heatDamageCounter >= HEAT_DAMAGE_INTERVAL) {
      heatDamageCounter = 0;
      applyHeatEffects(level);
    }

    // Handle dust storms
    if (++dustStormCheckCounter >= DUST_STORM_CHECK_INTERVAL) {
      dustStormCheckCounter = 0;
      updateDustStorm(level);
    }

    // Update dust storm effects if active
    if (isDustStormActive) {
      if (dustStormDuration-- <= 0) {
        endDustStorm(level);
      } else {
        applyDustStormEffects(level);
      }
    }
  }

  private static void applyHeatEffects(Level level) {
    if (!(level instanceof ServerLevel serverLevel)) return;

    for (Player player : serverLevel.players()) {
      // Skip creative/spectator players
      if (player.isCreative() || player.isSpectator()) {
        continue;
      }

      // Check if player is exposed to the sky (no blocks above)
      BlockPos headPos = player.blockPosition().above();
      if (level.canSeeSky(headPos)) {
        // Apply heat effects based on time of day
        float time = level.getTimeOfDay(1.0F);
        float heatMultiplier = getHeatMultiplier(time);

        // Apply damage if not protected
        if (!isPlayerProtectedFromHeat(player)) {
          player.hurt(level.damageSources().hotFloor(), HEAT_DAMAGE_AMOUNT * heatMultiplier);
          player.setSecondsOnFire(2);
        }

        // Apply slowness and mining fatigue in extreme heat
        if (heatMultiplier > 0.7F) {
          player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
          player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0));
        }
      }
    }
  }

  private static float getHeatMultiplier(float time) {
    // Midday is hottest, midnight is coolest
    float timeOfDay = (time + 0.25F) % 1.0F; // Offset so 0.0 is sunrise
    float heat = 1.0F - Math.abs(timeOfDay - 0.5F) * 2.0F; // 0.0 at night, 1.0 at midday
    return heat * heat; // Square to make the peak more pronounced
  }

  private static boolean isPlayerProtectedFromHeat(Player player) {
    // TODO: Check for heat protection gear
    // This should check player's armor and other protective items
    return false;
  }

  private static void updateDustStorm(Level level) {
    if (isDustStormActive || level.random.nextFloat() >= DUST_STORM_CHANCE) {
      return;
    }

    // Only start a dust storm during the day
    float time = level.getTimeOfDay(1.0F);
    if (time > 0.25F && time < 0.75F) { // Between sunrise and sunset
      startDustStorm(level);
    }
  }

  private static void startDustStorm(Level level) {
    isDustStormActive = true;
    dustStormDuration =
        level.random.nextInt(DUST_STORM_MAX_DURATION - DUST_STORM_MIN_DURATION)
            + DUST_STORM_MIN_DURATION;

    // Play storm start sound globally in the dimension
    level.playSound(
        null,
        new BlockPos(0, 64, 0),
        SoundEvents.ENDER_DRAGON_GROWL,
        SoundSource.AMBIENT,
        1.0F,
        0.5F);

    // TODO: Add storm start particles and visual effects
  }

  private static void endDustStorm(Level level) {
    isDustStormActive = false;

    // Play storm end sound
    level.playSound(
        null,
        new BlockPos(0, 64, 0),
        SoundEvents.WEATHER_RAIN_STOP,
        SoundSource.AMBIENT,
        1.0F,
        0.8F);
  }

  private static void applyDustStormEffects(Level level) {
    // Apply effects to all entities in the storm
    AABB stormArea = new AABB(-1000, 0, -1000, 1000, 256, 1000);

    for (Entity entity : level.getEntities(null, stormArea)) {
      if (entity instanceof LivingEntity livingEntity) {
        // Apply blindness and slowness to entities in the storm
        livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));

        // Play wind sound near entities
        if (level.random.nextFloat() < 0.02F) {
          level.playSound(
              null,
              entity.getX(),
              entity.getY(),
              entity.getZ(),
              SoundEvents.WIND_BURST,
              SoundSource.AMBIENT,
              1.0F,
              0.8F + level.random.nextFloat() * 0.4F);
        }
      }
    }

    // Spawn dust particles (handled client-side)
    // TODO: Add client-side particle effects
  }

  public static boolean isDustStormActive() {
    return isDustStormActive;
  }

  public static float getDustStormIntensity(Level level, double x, double y, double z) {
    if (!isDustStormActive) {
      return 0.0F;
    }

    // Calculate storm intensity based on height (stronger higher up)
    float heightFactor = (float) (y / 256.0);
    float timeFactor =
        (float) Math.sin((dustStormDuration / (float) DUST_STORM_MAX_DURATION) * Math.PI);

    return Math.min(1.0F, heightFactor * timeFactor * 1.5F);
  }
}
