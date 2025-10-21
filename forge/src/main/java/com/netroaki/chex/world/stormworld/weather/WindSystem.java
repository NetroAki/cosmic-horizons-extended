package com.netroaki.chex.world.stormworld.weather;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WindSystem {
  private static final float MAX_WIND_STRENGTH = 0.5f;
  private static final float WIND_CHANGE_RATE = 0.01f;
  private static final float WIND_NOISE_SCALE = 0.01f;

  private static float windAngle = 0f;
  private static float targetWindAngle = 0f;
  private static float windStrength = 0f;
  private static float targetWindStrength = 0f;
  private static int windChangeTimer = 0;

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END
        || event.level.isClientSide
        || !isStormworldDimension(event.level)) {
      return;
    }

    Level level = event.level;

    // Update wind direction and strength
    if (windChangeTimer <= 0) {
      // Set new target wind direction and strength
      Random random = level.random;
      targetWindAngle = random.nextFloat() * (float) Math.PI * 2;
      targetWindStrength = random.nextFloat() * MAX_WIND_STRENGTH;

      // More intense wind during storms
      if (StormworldWeatherManager.isStormActive()) {
        targetWindStrength *= (1.0f + StormworldWeatherManager.getStormIntensity() * 2);
        targetWindStrength = Math.min(targetWindStrength, MAX_WIND_STRENGTH * 2);
      }

      // Set time until next wind change (longer during calm weather)
      windChangeTimer = 100 + random.nextInt(StormworldWeatherManager.isStormActive() ? 100 : 400);
    } else {
      windChangeTimer--;
    }

    // Smoothly interpolate wind direction and strength
    windAngle = lerpAngle(windAngle, targetWindAngle, WIND_CHANGE_RATE);
    windStrength += (targetWindStrength - windStrength) * 0.05f;

    // Apply wind to entities
    if (level.getGameTime() % 5 == 0) { // Only check every 5 ticks for performance
      for (Entity entity : ((ServerLevel) level).getAllEntities()) {
        if (shouldBeAffectedByWind(entity)) {
          applyWindToEntity(entity);
        }
      }
    }
  }

  @SubscribeEvent
  public static void onEntitySpawn(EntityJoinLevelEvent event) {
    if (!event.getLevel().isClientSide()
        && isStormworldDimension(event.getLevel())
        && shouldBeAffectedByWind(event.getEntity())) {
      // Apply initial wind force to new entities
      applyWindToEntity(event.getEntity());
    }
  }

  private static boolean shouldBeAffectedByWind(Entity entity) {
    // Only affect certain types of entities
    return (entity instanceof LivingEntity || entity instanceof Projectile)
        && !entity.isOnGround()
        && !entity.isInWaterOrBubble()
        && !entity.isInLava();
  }

  private static void applyWindToEntity(Entity entity) {
    if (windStrength < 0.01f) return;

    // Get wind vector at entity's position (add some noise for variation)
    Vec3 windVec = getWindVector(entity.position());

    // Apply wind force based on entity type
    if (entity instanceof LivingEntity) {
      // Weaker effect on living entities for better gameplay
      double scale = 0.1 * windStrength * (entity.isInWaterOrBubble() ? 0.2 : 1.0);
      entity.setDeltaMovement(
          entity
              .getDeltaMovement()
              .add(
                  windVec.x * scale,
                  windVec.y * 0.2 * scale, // Less vertical movement
                  windVec.z * scale));
    } else if (entity instanceof Projectile) {
      // Stronger effect on projectiles
      double scale = 0.2 * windStrength;
      entity.setDeltaMovement(
          entity
              .getDeltaMovement()
              .add(windVec.x * scale, windVec.y * 0.1 * scale, windVec.z * scale));
    }
  }

  public static Vec3 getWindVector(Vec3 position) {
    // Add some noise to make wind more dynamic
    double noiseX =
        Mth.noise(
                (float) position.x * WIND_NOISE_SCALE,
                (float) position.y * WIND_NOISE_SCALE,
                (float) position.z * WIND_NOISE_SCALE + System.currentTimeMillis() * 0.0001)
            * 0.5;

    double noiseZ =
        Mth.noise(
                (float) position.x * WIND_NOISE_SCALE + 1000,
                (float) position.y * WIND_NOISE_SCALE + 1000,
                (float) position.z * WIND_NOISE_SCALE + System.currentTimeMillis() * 0.0001 + 1000)
            * 0.5;

    // Calculate wind vector with noise
    double windX = Math.cos(windAngle + noiseX) * windStrength;
    double windZ = Math.sin(windAngle + noiseZ) * windStrength;

    // Add some vertical movement
    double windY =
        (Mth.noise(
                    (float) position.x * 0.1f,
                    (float) position.y * 0.1f,
                    (float) position.z * 0.1f + System.currentTimeMillis() * 0.0001)
                - 0.5)
            * 0.1
            * windStrength;

    return new Vec3(windX, windY, windZ);
  }

  public static float getWindStrength() {
    return windStrength;
  }

  public static float getWindAngle() {
    return windAngle;
  }

  public static float getWindParticleDensity() {
    // Higher density during storms
    float density = windStrength / MAX_WIND_STRENGTH;
    if (StormworldWeatherManager.isStormActive()) {
      density *= (1.0f + StormworldWeatherManager.getStormIntensity());
    }
    return density;
  }

  private static float lerpAngle(float a, float b, float t) {
    // Normalize angles
    float diff =
        ((b - a) % (2 * (float) Math.PI) + 3 * (float) Math.PI) % (2 * (float) Math.PI)
            - (float) Math.PI;
    return a + diff * t;
  }

  private static boolean isStormworldDimension(Level level) {
    // TODO: Replace with actual dimension check
    return level.dimension().location().getPath().equals("stormworld");
  }
}
