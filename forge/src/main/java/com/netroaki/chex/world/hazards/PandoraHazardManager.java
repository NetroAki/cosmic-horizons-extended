package com.netroaki.chex.world.hazards;

import com.netroaki.chex.config.PandoraHazardsConfig;
import com.netroaki.chex.registry.biomes.CHEXBiomes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PandoraHazardManager {

  private static final int HAZARD_CHECK_INTERVAL = 20; // Check every second
  private int tickCounter = 0;

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.level.isClientSide) {
      return;
    }

    Level level = event.level;
    if (level.getGameTime() % HAZARD_CHECK_INTERVAL != 0) {
      return;
    }

    // Process all players in the world
    for (Player player : level.players()) {
      if (player.isCreative() || player.isSpectator()) {
        continue;
      }

      Biome biome = level.getBiome(player.blockPosition()).value();
      BlockPos pos = player.blockPosition();

      // Check and apply hazards based on biome and config
      try {
        if (PandoraHazardsConfig.ENABLE_LEVITATION_UPDRAFTS.get()
            && biome == CHEXBiomes.PANDORA_FLOATING_MOUNTAINS.get()) {
          handleFloatingMountainsHazards(player, pos, level);
        } else if (PandoraHazardsConfig.ENABLE_HEAT_AURA.get()
            && biome == CHEXBiomes.PANDORA_VOLCANIC_WASTELAND.get()) {
          handleVolcanicWastelandHazards(player, pos, level);
        } else if (PandoraHazardsConfig.ENABLE_SPORE_BLINDNESS.get()
            && biome == CHEXBiomes.PANDORA_BIOLUMINESCENT_FOREST.get()) {
          handleBioluminescentForestHazards(player, pos, level);
        }
      } catch (IllegalStateException e) {
        // Config not loaded yet, skip hazard checks
        continue;
      }
    }
  }

  private static void handleFloatingMountainsHazards(Player player, BlockPos pos, Level level) {
    // Check if player is in an updraft area (near floating islands)
    if (isInUpdraftArea(pos, level)) {
      // Calculate effect strength based on config (1-10 scale mapped to 0-2 amplifier)
      int strength = Math.max(0, Math.min(2, PandoraHazardsConfig.UPDRAFT_STRENGTH.get() / 5));

      // Apply levitation effect with configurable strength
      player.addEffect(
          new MobEffectInstance(
              MobEffects.LEVITATION,
              HAZARD_CHECK_INTERVAL + 10, // Slightly longer than check interval
              strength, // Configurable strength
              false, // Not ambient
              PandoraHazardsConfig.ENABLE_PARTICLES.get(), // Show particles based on config
              true // Show icon
              ));

      // Add particle effects if enabled
      if (PandoraHazardsConfig.ENABLE_PARTICLES.get()) {
        spawnUpdraftParticles(level, pos);
      }

      // Play sound effect
      if (level.random.nextFloat() < 0.1f) { // 10% chance per check to play sound
        level.playSound(
            null,
            pos,
            CHEXSoundEvents.UPDRAFT_WHOOSH.get(),
            SoundSource.AMBIENT,
            0.7f,
            0.8f + level.random.nextFloat() * 0.4f);
      }
    }
  }

  private static void handleVolcanicWastelandHazards(Player player, BlockPos pos, Level level) {
    // Apply heat aura effect
    if (isInHeatAura(pos, level)) {
      // Only apply damage on the interval defined in config
      if (player.tickCount % PandoraHazardsConfig.HEAT_DAMAGE_INTERVAL.get() == 0) {
        player.setSecondsOnFire(2);

        // Play crackling sound
        if (level.random.nextFloat() < 0.3f) {
          level.playSound(
              null,
              pos,
              CHEXSoundEvents.HEAT_CRACKLE.get(),
              SoundSource.AMBIENT,
              0.6f,
              0.5f + level.random.nextFloat() * 0.5f);
        }
      }

      // Add particle effects if enabled
      if (PandoraHazardsConfig.ENABLE_PARTICLES.get()) {
        spawnHeatAuraParticles(level, pos);
      }
    }
  }

  private void handleBioluminescentForestHazards(Player player, BlockPos pos, Level level) {
    // Check if player is in a spore cloud
    if (isInSporeCloud(pos, level)) {
      // Apply blindness and slowness
      player.addEffect(
          new MobEffectInstance(
              MobEffects.BLINDNESS,
              100, // 5 seconds
              0,
              false,
              true,
              true));
      player.addEffect(
          new MobEffectInstance(
              MobEffects.MOVEMENT_SLOWDOWN,
              100, // 5 seconds
              1, // Amplifier 1 (slower)
              false,
              true,
              true));

      // Add particle effects
      spawnSporeParticles(level, pos);
    }
  }

  private boolean isInUpdraftArea(BlockPos pos, Level level) {
    // Check if there's air below the player (indicating they're near a cliff)
    return level.isEmptyBlock(pos.below())
        && level.isEmptyBlock(pos.below(2))
        && level.isEmptyBlock(pos.below(3));
  }

  private boolean isInHeatAura(BlockPos pos, Level level) {
    // Check if player is near lava or in a hot area
    return level.getBlockState(pos.below()).isBurning(level, pos.below())
        || level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, null) != null;
  }

  private boolean isInSporeCloud(BlockPos pos, Level level) {
    // Check if player is near spore-emitting plants
    AABB searchArea = new AABB(pos).inflate(3.0);
    List<LivingEntity> nearbyEntities =
        level.getEntitiesOfClass(
            LivingEntity.class,
            searchArea,
            e -> e.getType().getRegistryName().getPath().contains("spore"));

    return !nearbyEntities.isEmpty()
        && level.random.nextFloat() < 0.2f; // 20% chance when near spores
  }

  private void spawnUpdraftParticles(Level level, BlockPos pos) {
    if (level instanceof ServerLevel serverLevel) {
      // Spawn upward-moving particles
      for (int i = 0; i < 5; i++) {
        double x = pos.getX() + level.random.nextDouble();
        double z = pos.getZ() + level.random.nextDouble();
        serverLevel.sendParticles(
            net.minecraft.core.particles.ParticleTypes.CLOUD,
            x,
            pos.getY(),
            z,
            1, // Count
            0,
            0.1,
            0, // Delta movement (upward)
            0.1 // Speed
            );
      }
    }
  }

  private void spawnHeatAuraParticles(Level level, BlockPos pos) {
    if (level instanceof ServerLevel serverLevel) {
      // Spawn heat distortion and fire particles
      for (int i = 0; i < 3; i++) {
        double x = pos.getX() + level.random.nextDouble() * 2 - 1;
        double y = pos.getY() + level.random.nextDouble() * 2;
        double z = pos.getZ() + level.random.nextDouble() * 2 - 1;

        serverLevel.sendParticles(
            net.minecraft.core.particles.ParticleTypes.FLAME,
            x,
            y,
            z,
            1, // Count
            0,
            0,
            0, // No movement
            0.02 // Speed
            );

        serverLevel.sendParticles(
            net.minecraft.core.particles.ParticleTypes.SMOKE,
            x,
            y,
            z,
            0, // Count
            0.1,
            0.1,
            0.1, // Random movement
            0.05 // Speed
            );
      }
    }
  }

  private void spawnSporeParticles(Level level, BlockPos pos) {
    if (level instanceof ServerLevel serverLevel) {
      // Spawn spore particles in a cloud
      for (int i = 0; i < 10; i++) {
        double x = pos.getX() + level.random.nextDouble() * 4 - 2;
        double y = pos.getY() + level.random.nextDouble() * 2;
        double z = pos.getZ() + level.random.nextDouble() * 4 - 2;

        serverLevel.sendParticles(
            net.minecraft.core.particles.ParticleTypes.ASH,
            x,
            y,
            z,
            1, // Count
            0.1,
            0.1,
            0.1, // Random movement
            0.05 // Speed
            );

        // Add some glowing particles
        if (level.random.nextFloat() < 0.3f) {
          serverLevel.sendParticles(
              net.minecraft.core.particles.ParticleTypes.END_ROD,
              x,
              y,
              z,
              1, // Count
              0.05,
              0.05,
              0.05, // Slight movement
              0.01 // Speed
              );
        }
      }
    }
  }
}
