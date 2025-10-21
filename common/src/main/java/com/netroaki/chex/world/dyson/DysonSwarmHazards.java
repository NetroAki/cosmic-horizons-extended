package com.netroaki.chex.world.dyson;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.config.SuitHazardsConfigCore;
import com.netroaki.chex.config.SuitHazardsRuntime;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = CosmicHorizonsExpanded.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DysonSwarmHazards {

  private static final int RADIATION_TICK = 120; // 6s
  private static final int DEBRIS_TICK = 160; // 8s
  private static final int VACUUM_TICK = 200; // 10s
  private static final int ZERO_G_ASSIST_TICK = 100; // 5s

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    if (!(event.player instanceof ServerPlayer player)) return;
    if (!(player.level() instanceof ServerLevel level)) return;

    // Only act in Dyson Swarm biomes
    Biome biome = level.getBiome(player.blockPosition()).value();
    String key =
        level
            .registryAccess()
            .registryOrThrow(net.minecraft.core.registries.Registries.BIOME)
            .getKey(biome)
            .toString();
    if (!key.startsWith("cosmic_horizons_extended:dyson_")) return;

    // Resolve planet id for rules
    String planetId = "cosmic_horizons_extended:shattered_dyson_swarm";
    // Load suit hazard config and mitigation
    SuitHazardsConfigCore.Config cfg = SuitHazardsRuntime.get();
    int suitTier =
        player
            .getCapability(PlayerTierCapability.INSTANCE)
            .map(PlayerTierCapability::getSuitTier)
            .orElse(1);
    double mitVac = SuitHazardsRuntime.mitigationForTier(suitTier, m -> m.vacuum);
    double mitRad = SuitHazardsRuntime.mitigationForTier(suitTier, m -> m.radiation);
    double baseVac = 0.5, baseRad = 0.5; // defaults if no rule
    if (cfg != null && cfg.rules != null) {
      for (SuitHazardsConfigCore.HazardRule r : cfg.rules) {
        if ("planet".equals(r.scope) && planetId.equals(r.idOrTag)) {
          baseVac = r.vacuum;
          baseRad = r.radiation;
          break;
        }
      }
    }

    long t = level.getGameTime();

    // Timed radiation bursts
    if (t % RADIATION_TICK == 0) {
      double eff = baseRad * (1.0 - mitRad);
      if (eff < 0.15) {
        // sufficiently mitigated; skip burst
      } else {
        // Apply brief weakness or wither-like danger via poison + particles as a stand-in
        int dur = (int) (60 * eff);
        player.addEffect(
            new MobEffectInstance(MobEffects.POISON, Math.max(20, dur), 0, true, false, false));
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.BEACON_POWER_SELECT,
            player.getSoundSource(),
            0.7f,
            0.7f);
        if (eff >= 0.8) {
          // high-intensity alarm cue
          level.playSound(
              null,
              player.blockPosition(),
              net.minecraft.sounds.SoundEvents.NOTE_BLOCK_BELL.value(),
              player.getSoundSource(),
              0.8f,
              1.8f);
        }
        level.sendParticles(
            ParticleTypes.GLOW,
            player.getX(),
            player.getY() + 0.8,
            player.getZ(),
            12,
            0.6,
            0.4,
            0.6,
            0.02);
      }
    }

    // Debris collisions (minor knock and damage cue)
    if (t % DEBRIS_TICK == 0) {
      player.hurt(level.damageSources().flyingBlock(), 1.0f);
      // Small random push
      double dx = (level.random.nextDouble() - 0.5) * 0.8;
      double dz = (level.random.nextDouble() - 0.5) * 0.8;
      player.push(dx, 0.15, dz);
      level.playSound(
          null,
          player.blockPosition(),
          SoundEvents.ANVIL_LAND,
          player.getSoundSource(),
          0.4f,
          1.6f);
      level.sendParticles(
          ParticleTypes.CRIT,
          player.getX(),
          player.getY() + 0.5,
          player.getZ(),
          8,
          0.5,
          0.3,
          0.5,
          0.01);
    }

    // Vacuum exposure check (simple stand-in unless future suit system exists)
    if (t % VACUUM_TICK == 0) {
      double eff = baseVac * (1.0 - mitVac);
      if (!player.isCreative() && !player.isSpectator()) {
        if (eff >= 0.15) {
          // brief suffocation-like effect scaled by eff
          int dur = (int) (80 * eff);
          player.addEffect(
              new MobEffectInstance(MobEffects.HUNGER, Math.max(20, dur), 0, true, false, false));
          player.addEffect(
              new MobEffectInstance(
                  MobEffects.MOVEMENT_SLOWDOWN, Math.max(20, dur), 0, true, false, false));
          level.playSound(
              null,
              player.blockPosition(),
              SoundEvents.CANDLE_EXTINGUISH,
              player.getSoundSource(),
              0.6f,
              0.9f);
          level.sendParticles(
              ParticleTypes.CLOUD,
              player.getX(),
              player.getY() + 0.6,
              player.getZ(),
              10,
              0.5,
              0.3,
              0.5,
              0.02);
        }
      }
    }

    // Zero-G navigation assist (periodic slow falling to ease movement)
    if (t % ZERO_G_ASSIST_TICK == 0) {
      player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0, true, false, false));
    }

    // Occasional ambient hum
    if (t % 1200 == 0) {
      level.playSound(
          null,
          player.blockPosition(),
          net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE.value(),
          player.getSoundSource(),
          0.4f,
          0.6f);
    }
  }
}
