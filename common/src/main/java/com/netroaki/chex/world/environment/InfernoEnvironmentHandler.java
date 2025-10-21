package com.netroaki.chex.world.environment;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID)
public class InfernoEnvironmentHandler {
  private static final int HEAT_DAMAGE_INTERVAL = 80; // 4 seconds at 20 TPS
  private static final float HEAT_DAMAGE_AMOUNT = 3.0F;
  private static final int LAVA_RAIN_CHECK_INTERVAL = 100; // 5 seconds at 20 TPS
  private static final float LAVA_RAIN_CHANCE = 0.3F;
  private static final int LAVA_RAIN_MIN_DURATION = 400; // 20 seconds at 20 TPS
  private static final int LAVA_RAIN_MAX_DURATION = 1200; // 1 minute at 20 TPS
  private static final int EMBER_PARTICLE_RADIUS = 32;
  private static final int HEAT_DISTORTION_RADIUS = 16;

  private static int heatDamageCounter = 0;
  private static int lavaRainCheckCounter = 0;
  private static int lavaRainDuration = 0;
  private static boolean isLavaRainActive = false;
  private static final Random RANDOM = new Random();

  public static boolean isLavaRainActive() {
    return isLavaRainActive;
  }

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) {
      return;
    }

    Level level = event.level;

    // Only process in Inferno Prime dimension
    if (!isInfernoPrime(level)) {
      return;
    }

    // Handle heat damage
    if (++heatDamageCounter >= HEAT_DAMAGE_INTERVAL) {
      heatDamageCounter = 0;
      applyHeatEffects(level);
    }

    // Handle lava rain
    if (++lavaRainCheckCounter >= LAVA_RAIN_CHECK_INTERVAL) {
      lavaRainCheckCounter = 0;
      updateLavaRain(level);
    }

    // Update lava rain effects if active
    if (isLavaRainActive) {
      if (lavaRainDuration-- <= 0) {
        endLavaRain(level);
      } else {
        applyLavaRainEffects(level);
      }
    }

    // Always apply ambient effects
    applyAmbientEffects(level);
  }

  private static boolean isInfernoPrime(Level level) {
    return level
        .dimension()
        .location()
        .equals(new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "inferno_prime"));
  }

  private static void applyHeatEffects(Level level) {
    if (!(level instanceof ServerLevel serverLevel)) return;

    for (Player player : serverLevel.players()) {
      // Skip creative/spectator players
      if (player.isCreative() || player.isSpectator()) {
        continue;
      }

      // Check if player is exposed to the environment
      if (isExposedToEnvironment(level, player.blockPosition())) {
        // Apply damage if not protected
        if (!isPlayerProtectedFromHeat(player)) {
          player.hurt(level.damageSources().hotFloor(), HEAT_DAMAGE_AMOUNT);
          player.setSecondsOnFire(3);
        }

        // Apply debuffs
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 120, 1));

        // Random chance for additional effects
        if (RANDOM.nextFloat() < 0.1F) {
          player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
      }
    }
  }

  private static boolean isExposedToEnvironment(Level level, BlockPos pos) {
    // Check if there are blocks above the position
    return level.canSeeSky(pos)
        && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() <= pos.getY();
  }

  private static boolean isPlayerProtectedFromHeat(Player player) {
    // TODO: Implement suit protection check
    // This should check the player's suit tier against the required protection level
    return false;
  }

  private static void updateLavaRain(Level level) {
    if (!isLavaRainActive && RANDOM.nextFloat() < LAVA_RAIN_CHANCE) {
      startLavaRain(level);
    }
  }

  private static void startLavaRain(Level level) {
    isLavaRainActive = true;
    lavaRainDuration =
        LAVA_RAIN_MIN_DURATION + RANDOM.nextInt(LAVA_RAIN_MAX_DURATION - LAVA_RAIN_MIN_DURATION);

    // Play sound effect
    level.playSound(
        null,
        level.getSharedSpawnPos(),
        SoundEvents.LAVA_AMBIENT,
        SoundSource.WEATHER,
        1.0F,
        0.8F + RANDOM.nextFloat() * 0.4F);

    // Notify players
    if (level instanceof ServerLevel serverLevel) {
      for (Player player : serverLevel.players()) {
        if (player.distanceToSqr(
                level.getSharedSpawnPos().getX(), player.getY(), level.getSharedSpawnPos().getZ())
            < 2500) {
          player.displayClientMessage(
              Component.translatable("message.chex.inferno.lava_rain_start"), true);
        }
      }
    }
  }

  private static void endLavaRain(Level level) {
    isLavaRainActive = false;

    // Play sound effect
    level.playSound(
        null,
        level.getSharedSpawnPos(),
        SoundEvents.LAVA_EXTINGUISH,
        SoundSource.WEATHER,
        0.5F,
        1.0F);
  }

  private static void applyLavaRainEffects(Level level) {
    if (!(level instanceof ServerLevel serverLevel)) return;

    // Spawn lava rain particles and damage entities
    for (Player player : serverLevel.players()) {
      if (isExposedToEnvironment(level, player.blockPosition())) {
        // Spawn particles around the player
        for (int i = 0; i < 5; i++) {
          double x = player.getX() + (RANDOM.nextDouble() - 0.5) * 20;
          double z = player.getZ() + (RANDOM.nextDouble() - 0.5) * 20;
          double y = player.getY() + 10 + RANDOM.nextDouble() * 5;

          serverLevel.sendParticles(ParticleTypes.LAVA, x, y, z, 1, 0, 0, 0, 0);
          serverLevel.sendParticles(ParticleTypes.FLAME, x, y, z, 0, 0, -0.1, 0, 0.1);

          // Damage entities under the lava rain
          if (RANDOM.nextFloat() < 0.2F) {
            BlockPos pos = new BlockPos((int) x, (int) y - 1, (int) z);
            if (level.isEmptyBlock(pos)) {
              level.setBlockAndUpdate(
                  pos, net.minecraft.world.level.block.Blocks.FIRE.defaultBlockState());
            }
          }
        }

        // Apply damage to players not protected from heat
        if (!isPlayerProtectedFromHeat(player) && RANDOM.nextFloat() < 0.1F) {
          player.hurt(level.damageSources().lava(), 2.0F);
          player.setSecondsOnFire(5);
        }
      }
    }
  }

  private static void applyAmbientEffects(Level level) {
    if (level.isClientSide()) return;

    // Spawn ambient ember particles
    if (level.getGameTime() % 2 == 0) {
      for (Player player : level.players()) {
        if (isInfernoPrime(level)) {
          // Spawn ember particles in a radius around the player
          int particleCount = isLavaRainActive ? 20 : 10; // More particles during lava rain
          for (int i = 0; i < particleCount; i++) {
            double angle = RANDOM.nextDouble() * Math.PI * 2;
            double distance = RANDOM.nextDouble() * EMBER_PARTICLE_RADIUS;
            double x = player.getX() + Math.cos(angle) * distance;
            double z = player.getZ() + Math.sin(angle) * distance;
            double y = player.getY() + 1.5 + RANDOM.nextDouble() * 4;

            // Randomize particle type based on conditions
            float rand = RANDOM.nextFloat();
            if (rand < 0.1F) {
              // Lava drip particles
              level.addParticle(ParticleTypes.DRIPPING_LAVA, x, y, z, 0, -0.1, 0);
            } else if (rand < 0.4F) {
              // Smoke particles
              level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0.02, 0);
            } else if (rand < 0.7F) {
              // Flame particles with random movement
              level.addParticle(
                  ParticleTypes.FLAME,
                  x,
                  y,
                  z,
                  (RANDOM.nextDouble() - 0.5) * 0.2,
                  RANDOM.nextDouble() * 0.1,
                  (RANDOM.nextDouble() - 0.5) * 0.2);
            } else {
              // Custom ember particles with more dynamic movement
              double xSpeed = (RANDOM.nextDouble() - 0.5) * 0.1;
              double ySpeed = RANDOM.nextDouble() * 0.05 + 0.01;
              double zSpeed = (RANDOM.nextDouble() - 0.5) * 0.1;
              level.addParticle(ParticleTypes.FLAME, x, y, z, xSpeed, ySpeed, zSpeed);
            }
          }

          // Apply heat distortion effect to players
          if (level.getGameTime() % 20 == 0) {
            if (!isPlayerProtectedFromHeat(player)
                && isExposedToEnvironment(level, player.blockPosition())) {
              // Add more intense effects based on exposure
              int duration = isLavaRainActive ? 200 : 100;
              int amplifier = isLavaRainActive ? 1 : 0;
              player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, amplifier));

              // Occasionally apply slowness when in lava rain
              if (isLavaRainActive && RANDOM.nextFloat() < 0.3F) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
              }
            }
          }

          // Add heat distortion particles in the air
          if (level.getGameTime() % 5 == 0) {
            for (int i = 0; i < 5; i++) {
              double dist = RANDOM.nextDouble() * HEAT_DISTORTION_RADIUS;
              double angle = RANDOM.nextDouble() * Math.PI * 2;
              double x = player.getX() + Math.cos(angle) * dist;
              double z = player.getZ() + Math.sin(angle) * dist;
              double y = player.getY() + RANDOM.nextDouble() * 3;

              // Heat distortion effect
              level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.05, 0);
            }
          }
        }
      }
    }
  }
}
