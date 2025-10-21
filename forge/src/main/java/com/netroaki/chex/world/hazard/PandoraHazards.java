package com.netroaki.chex.world.hazard;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.capability.PlayerSuitCapability;
import com.netroaki.chex.config.PandoraHazardsConfig;
import com.netroaki.chex.sound.CHEXSoundEvents;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class PandoraHazards {
  private static final Random RANDOM = new Random();

  // Configuration values
  private static int getHazardCheckInterval() {
    return PandoraHazardsConfig.getHazardCheckInterval();
  }

  private static float getLevitationStrength() {
    return PandoraHazardsConfig.getLevitationStrength();
  }

  private static int getLevitationDuration() {
    return PandoraHazardsConfig.getLevitationDuration();
  }

  private static int getLevitationImmuneTier() {
    return PandoraHazardsConfig.getLevitationImmuneTier();
  }

  private static int getHeatDamageInterval() {
    return PandoraHazardsConfig.getHeatDamageInterval();
  }

  private static float getHeatDamageAmount() {
    return PandoraHazardsConfig.getHeatDamageAmount();
  }

  private static int getHeatImmuneTier() {
    return PandoraHazardsConfig.getHeatImmuneTier();
  }

  private static int getBlindnessDuration() {
    return PandoraHazardsConfig.getBlindnessDuration();
  }

  private static int getBlindnessImmuneTier() {
    return PandoraHazardsConfig.getBlindnessImmuneTier();
  }

  private static boolean isLevitationEnabled() {
    return PandoraHazardsConfig.isLevitationEnabled();
  }

  private static boolean isHeatAuraEnabled() {
    return PandoraHazardsConfig.isHeatAuraEnabled();
  }

  private static boolean isSporeBlindnessEnabled() {
    return PandoraHazardsConfig.isSporeBlindnessEnabled();
  }

  private static boolean isAmbientAudioEnabled() {
    return PandoraHazardsConfig.isAmbientAudioEnabled();
  }

  private static int getAmbientSoundInterval() {
    return PandoraHazardsConfig.getAmbientSoundInterval();
  }

  private static float getAmbientSoundVolume() {
    return PandoraHazardsConfig.getAmbientSoundVolume();
  }

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
      return;
    }

    Player player = event.player;
    Level level = player.level();

    // Only check hazards at certain intervals for performance
    if (level.getGameTime() % getHazardCheckInterval() != 0) {
      return;
    }

    // Check if player is in a Pandora biome
    if (!isInPandoraBiome(level, player.blockPosition())) {
      return;
    }

    // Check player's suit tier for hazard protection
    int suitTier =
        player.getCapability(PlayerSuitCapability.INSTANCE).map(cap -> cap.getSuitTier()).orElse(0);

    // Apply hazards based on player's position and suit tier
    checkLevitationUpdrafts(player, level, suitTier);
    checkHeatAura(player, level, suitTier);
    checkSporeBlindness(player, level, suitTier);
  }

  private static boolean isInPandoraBiome(Level level, BlockPos pos) {
    // Check if the current biome is a Pandora biome
    // This should be expanded to include all Pandora biomes
    Biome biome = level.getBiome(pos).value();
    return biome.getRegistryName() != null
        && biome.getRegistryName().getNamespace().equals(CHEX.MOD_ID)
        && biome.getRegistryName().getPath().contains("pandora");
  }

  private static void checkLevitationUpdrafts(Player player, Level level, int suitTier) {
    // Only apply if player is below a certain Y level (in the "valleys" of Pandora)
    if (player.getY() > 80 || suitTier >= getLevitationImmuneTier() || !isLevitationEnabled()) {
      return;
    }

    // Check if player is in a levitation zone (e.g., near floating islands)
    AABB checkArea = player.getBoundingBox().inflate(5.0);
    if (level
        .getBlockStates(checkArea)
        .anyMatch(
            state ->
                state.getBlock().getRegistryName() != null
                    && state.getBlock().getRegistryName().getPath().contains("levitation"))) {

      // Apply levitation effect with reduced strength for higher tier suits
      float strength = getLevitationStrength() * (1.0f - (suitTier * 0.2f));
      player.addEffect(
          new MobEffectInstance(
              MobEffects.LEVITATION,
              getLevitationDuration(),
              (int) (strength * 5) // Convert to amplifier
              ));

      // Add particle effects and play sound
      if (level instanceof ServerLevel serverLevel) {
        spawnParticles(serverLevel, player.position().add(0, 0.5, 0), ParticleTypes.CLOUD, 10, 0.5);

        // Play levitation field sound
        serverLevel.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            CHEXSoundEvents.HAZARD_LEVITATION_FIELD.get(),
            SoundSource.AMBIENT,
            0.7f,
            0.8f + RANDOM.nextFloat() * 0.4f);
      }
    }
  }

  private static void checkHeatAura(Player player, Level level, int suitTier) {
    // Only apply in specific biomes (e.g., volcanic areas)
    if (!isInHotBiome(level, player.blockPosition())
        || suitTier >= getHeatImmuneTier()
        || !isHeatAuraEnabled()) {
      return;
    }

    // Only damage at intervals
    if (level.getGameTime() % getHeatDamageInterval() != 0) {
      return;
    }

    // Apply damage with resistance based on suit tier
    float damage = getHeatDamageAmount() * (1.0f - (suitTier * 0.15f));
    if (damage > 0) {
      player.hurt(level.damageSources().hotFloor(), damage);

      // Visual feedback
      if (level instanceof ServerLevel serverLevel) {
        spawnParticles(serverLevel, player.position().add(0, 1.0, 0), ParticleTypes.FLAME, 5, 0.2);
        spawnParticles(serverLevel, player.position().add(0, 1.0, 0), ParticleTypes.LAVA, 3, 0.1);

        // Play heat haze sound
        serverLevel.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            CHEXSoundEvents.HAZARD_HEAT_HAZE.get(),
            SoundSource.AMBIENT,
            0.7f,
            0.9f + RANDOM.nextFloat() * 0.2f);
      }
    }
  }

  private static boolean isInHotBiome(Level level, BlockPos pos) {
    // Check if the current biome is a hot biome in Pandora
    Biome biome = level.getBiome(pos).value();
    return biome.getRegistryName() != null && biome.getRegistryName().getPath().contains("volcanic")
        || biome.getRegistryName().getPath().contains("magma");
  }

  private static void checkSporeBlindness(Player player, Level level, int suitTier) {
    // Only apply in spore-heavy biomes
    if (!isInSporeBiome(level, player.blockPosition())
        || suitTier >= getBlindnessImmuneTier()
        || !isSporeBlindnessEnabled()) {
      return;
    }

    // Check if player is in a spore cloud
    if (isInSporeCloud(level, player.blockPosition())) {
      // Apply blindness effect with duration based on suit tier
      int duration = getBlindnessDuration() / (suitTier + 1);
      player.addEffect(
          new MobEffectInstance(
              MobEffects.BLINDNESS,
              duration,
              0, // Amplifier 0 for regular effect
              false, // Not ambient
              true // Show particles
              ));

      // Visual and audio feedback
      if (level instanceof ServerLevel serverLevel) {
        spawnParticles(
            serverLevel,
            player.position().add(0, 1.5, 0),
            ParticleTypes.SPORE_BLOSSOM_AIR,
            15,
            0.3);

        // Play spore cloud sound
        serverLevel.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            CHEXSoundEvents.HAZARD_SPORE_CLOUD.get(),
            SoundSource.AMBIENT,
            0.8f,
            0.7f + RANDOM.nextFloat() * 0.6f);
      }
    }
  }

  private static boolean isInSporeBiome(Level level, BlockPos pos) {
    // Check if the current biome is a spore-heavy biome in Pandora
    Biome biome = level.getBiome(pos).value();
    return biome.getRegistryName() != null
        && (biome.getRegistryName().getPath().contains("fungal")
            || biome.getRegistryName().getPath().contains("spore"));
  }

  private static boolean isInSporeCloud(Level level, BlockPos pos) {
    // Check if there are spore particles in the air around the player
    // This is a simplified check - in a real implementation, you'd want to check for actual spore
    // blocks or effects
    return level.canSeeSky(pos)
        && level.isRaining()
        && level.getBiome(pos).value().getPrecipitation() == Biome.Precipitation.RAIN;
  }

  private static void spawnParticles(
      ServerLevel level,
      net.minecraft.world.phys.Vec3 pos,
      net.minecraft.core.particles.ParticleOptions type,
      int count,
      double speed) {
    for (int i = 0; i < count; i++) {
      double x = pos.x + (RANDOM.nextDouble() - 0.5) * 2.0;
      double y = pos.y + (RANDOM.nextDouble() - 0.5) * 2.0;
      double z = pos.z + (RANDOM.nextDouble() - 0.5) * 2.0;

      level.sendParticles(
          type, x, y, z, 1, // Count
          0.0, 0.0, 0.0, // Delta movement
          speed);
    }
  }

  // Ambient sound system
  @SubscribeEvent
  public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
    LivingEntity entity = event.getEntity();
    if (!(entity instanceof Player) || entity.level().isClientSide) {
      return;
    }

    Level level = entity.level();
    BlockPos pos = entity.blockPosition();

    // Only play ambient sounds in Pandora if enabled
    if (!isInPandoraBiome(level, pos) || !isAmbientAudioEnabled()) {
      return;
    }

    // Play ambient sounds at random intervals
    int interval = getAmbientSoundInterval();
    if (level.getGameTime() % interval == 0 && RANDOM.nextFloat() < 0.3f) {
      SoundEvent sound = getRandomAmbientSound(level, pos);
      if (sound != null) {
        level.playSound(
            null,
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            sound,
            SoundSource.AMBIENT,
            getAmbientSoundVolume(),
            0.8f + RANDOM.nextFloat() * 0.4f);
      }
    }
  }

  private static SoundEvent getRandomAmbientSound(Level level, BlockPos pos) {
    // Return different ambient sounds based on biome and conditions
    if (isInSporeBiome(level, pos)) {
      // In spore biomes, play bioluminescent forest sounds
      return CHEXSoundEvents.AMBIENT_PANDORA_FOREST.get();
    } else if (isInHotBiome(level, pos)) {
      // In hot biomes, play cave/volcanic sounds
      return CHEXSoundEvents.AMBIENT_PANDORA_CAVES.get();
    } else if (level.getRandom().nextFloat() < 0.3f) {
      // Occasionally play wind sounds
      return CHEXSoundEvents.AMBIENT_PANDORA_WIND.get();
    } else {
      // Default to biolume sounds
      return CHEXSoundEvents.AMBIENT_PANDORA_BIOLUME.get();
    }
  }
}
