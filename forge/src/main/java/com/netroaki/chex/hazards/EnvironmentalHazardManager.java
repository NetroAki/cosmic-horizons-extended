package com.netroaki.chex.hazards;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;

public class EnvironmentalHazardManager {

  private static final Map<ResourceLocation, HazardType> BIOME_HAZARDS = new HashMap<>();
  private static final int HAZARD_CHECK_RADIUS = 16;
  private static final int HAZARD_CHECK_INTERVAL = 20; // 1 second

  static {
    // Initialize biome hazard mappings
    BIOME_HAZARDS.put(
        ResourceLocation.fromNamespaceAndPath(
            "cosmic_horizons_extended", "pandora_bioluminescent_forest"),
        HazardType.SPORE_BLINDNESS);
    BIOME_HAZARDS.put(
        ResourceLocation.fromNamespaceAndPath(
            "cosmic_horizons_extended", "pandora_floating_mountains"),
        HazardType.LEVITATION_UPDRAFT);
    BIOME_HAZARDS.put(
        ResourceLocation.fromNamespaceAndPath("cosmic_horizons_extended", "pandora_ocean_depths"),
        HazardType.PRESSURE);
    BIOME_HAZARDS.put(
        ResourceLocation.fromNamespaceAndPath(
            "cosmic_horizons_extended", "pandora_volcanic_wasteland"),
        HazardType.HEAT_AURA);
    BIOME_HAZARDS.put(
        ResourceLocation.fromNamespaceAndPath("cosmic_horizons_extended", "pandora_sky_islands"),
        HazardType.WIND);
  }

  public enum HazardType {
    NONE,
    LEVITATION_UPDRAFT,
    HEAT_AURA,
    SPORE_BLINDNESS,
    PRESSURE,
    WIND
  }

  public static void tick(Level level) {
    if (level.isClientSide) return;

    // Only process every HAZARD_CHECK_INTERVAL ticks
    if (level.getGameTime() % HAZARD_CHECK_INTERVAL != 0) return;

    // Process hazards for all players
    for (Player player : level.players()) {
      if (player != null && !player.isCreative() && !player.isSpectator()) {
        processPlayerHazards(level, player);
      }
    }
  }

  private static void processPlayerHazards(Level level, Player player) {
    BlockPos playerPos = player.blockPosition();
    Biome biome = level.getBiome(playerPos).value();
    ResourceLocation biomeId =
        level
            .registryAccess()
            .registryOrThrow(net.minecraft.core.registries.Registries.BIOME)
            .getKey(biome);

    if (biomeId == null) return;

    HazardType hazardType = BIOME_HAZARDS.get(biomeId);
    if (hazardType == HazardType.NONE) return;

    // Check for hazard sources in the area
    for (int x = -HAZARD_CHECK_RADIUS; x <= HAZARD_CHECK_RADIUS; x += 4) {
      for (int z = -HAZARD_CHECK_RADIUS; z <= HAZARD_CHECK_RADIUS; z += 4) {
        BlockPos checkPos = playerPos.offset(x, 0, z);

        if (shouldCreateHazard(level, checkPos, hazardType)) {
          applyHazard(level, checkPos, hazardType);
        }
      }
    }
  }

  private static boolean shouldCreateHazard(Level level, BlockPos pos, HazardType hazardType) {
    switch (hazardType) {
      case LEVITATION_UPDRAFT:
        return LevitationUpdraftHazard.shouldCreateUpdraft(level, pos);
      case HEAT_AURA:
        return HeatAuraHazard.shouldCreateHeatAura(level, pos);
      case SPORE_BLINDNESS:
        return SporeBlindnessHazard.shouldCreateSporeHazard(level, pos);
      case PRESSURE:
        return PressureHazard.shouldCreatePressureHazard(level, pos);
      case WIND:
        return WindHazard.shouldCreateWindHazard(level, pos);
      case NONE:
      default:
        return false;
    }
  }

  private static void applyHazard(Level level, BlockPos pos, HazardType hazardType) {
    switch (hazardType) {
      case LEVITATION_UPDRAFT:
        LevitationUpdraftHazard.tick(level, pos);
        break;
      case HEAT_AURA:
        HeatAuraHazard.tick(level, pos);
        break;
      case SPORE_BLINDNESS:
        SporeBlindnessHazard.tick(level, pos);
        break;
      case PRESSURE:
        PressureHazard.tick(level, pos);
        break;
      case WIND:
        WindHazard.tick(level, pos);
        break;
      case NONE:
      default:
        break;
    }
  }

  public static void createHazardBurst(
      Level level, BlockPos pos, HazardType hazardType, int intensity) {
    if (level.isClientSide) return;

    switch (hazardType) {
      case LEVITATION_UPDRAFT:
        createLevitationBurst(level, pos, intensity);
        break;
      case HEAT_AURA:
        createHeatBurst(level, pos, intensity);
        break;
      case SPORE_BLINDNESS:
        SporeBlindnessHazard.createSporeBurst(level, pos, intensity);
        break;
      case PRESSURE:
        createPressureBurst(level, pos, intensity);
        break;
      case WIND:
        WindHazard.createWindGust(level, pos, intensity);
        break;
      case NONE:
      default:
        break;
    }
  }

  private static void createLevitationBurst(Level level, BlockPos pos, int intensity) {
    AABB burstArea =
        new AABB(
            pos.getX() - 12,
            pos.getY(),
            pos.getZ() - 12,
            pos.getX() + 12,
            pos.getY() + 20,
            pos.getZ() + 12);

    for (Player player : level.getEntitiesOfClass(Player.class, burstArea)) {
      if (player != null) {
        // Apply intense levitation effect
        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 300, 0, false, false));

        // Play wind sound
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.WEATHER_RAIN_ABOVE,
            SoundSource.AMBIENT,
            0.8F,
            0.5F);
      }
    }

    // Create burst particles
    RandomSource random = level.random;
    for (int i = 0; i < 50; i++) {
      double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 24;
      double y = pos.getY() + random.nextDouble() * 20;
      double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 24;

      level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0D, 0.1D, 0.0D);
    }
  }

  private static void createHeatBurst(Level level, BlockPos pos, int intensity) {
    AABB burstArea =
        new AABB(
            pos.getX() - 15,
            pos.getY(),
            pos.getZ() - 15,
            pos.getX() + 15,
            pos.getY() + 10,
            pos.getZ() + 15);

    for (Player player : level.getEntitiesOfClass(Player.class, burstArea)) {
      if (player != null) {
        // Apply intense heat effects
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, 1, false, false));

        // Apply heat damage
        player.hurt(level.damageSources().onFire(), 2.0F * intensity);

        // Play fire sound
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.FIRE_AMBIENT,
            SoundSource.AMBIENT,
            1.0F,
            0.5F);
      }
    }

    // Create burst particles
    RandomSource random = level.random;
    for (int i = 0; i < 80; i++) {
      double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 30;
      double y = pos.getY() + random.nextDouble() * 10;
      double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 30;

      level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
      level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
    }
  }

  private static void createPressureBurst(Level level, BlockPos pos, int intensity) {
    AABB burstArea =
        new AABB(
            pos.getX() - 10,
            pos.getY(),
            pos.getZ() - 10,
            pos.getX() + 10,
            pos.getY() + 15,
            pos.getZ() + 10);

    for (Player player : level.getEntitiesOfClass(Player.class, burstArea)) {
      if (player != null) {
        // Apply intense pressure effects
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 300, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 250, 2, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 180, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 0, false, false));

        // Apply pressure damage
        player.hurt(level.damageSources().drown(), 1.5F * intensity);

        // Play pressure sound
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.ELDER_GUARDIAN_AMBIENT,
            SoundSource.AMBIENT,
            0.8F,
            0.5F);
      }
    }

    // Create burst particles
    RandomSource random = level.random;
    for (int i = 0; i < 60; i++) {
      double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 20;
      double y = pos.getY() + random.nextDouble() * 15;
      double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 20;

      level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0.0D, 0.1D, 0.0D);
      level.addParticle(ParticleTypes.SPLASH, x, y, z, 0.0D, 0.0D, 0.0D);
    }
  }

  public static HazardType getHazardTypeForBiome(ResourceLocation biomeId) {
    return BIOME_HAZARDS.getOrDefault(biomeId, HazardType.NONE);
  }

  public static boolean hasHazard(ResourceLocation biomeId) {
    return BIOME_HAZARDS.containsKey(biomeId) && BIOME_HAZARDS.get(biomeId) != HazardType.NONE;
  }
}
