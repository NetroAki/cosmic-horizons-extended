package com.netroaki.chex.world.exotica;

import com.netroaki.chex.CosmicHorizonsExpanded;
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
public class ExoticaHazards {

  private static final int DISTORTION_INTERVAL = 140; // ~7s
  private static final int HAZE_INTERVAL = 40; // 2s

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    if (!(event.player instanceof ServerPlayer player)) return;
    if (!(player.level() instanceof ServerLevel level)) return;

    Biome biome = level.getBiome(player.blockPosition()).value();
    String key =
        level
            .registryAccess()
            .registryOrThrow(net.minecraft.core.registries.Registries.BIOME)
            .getKey(biome)
            .toString();

    long time = level.getGameTime();

    // Global chromatic haze visuals across Exotica biomes we created
    if (key.endsWith("exotica_chroma_steppes")
        || key.endsWith("exotica_resonant_dunes")
        || key.endsWith("exotica_quantum_glades")
        || key.endsWith("exotica_fractal_forest")
        || key.endsWith("exotica_prism_canyons")) {
      if (time % HAZE_INTERVAL == 0) {
        level.sendParticles(
            ParticleTypes.GLOW,
            player.getX(),
            player.getY() + 1.2,
            player.getZ(),
            6,
            0.6,
            0.4,
            0.6,
            0.01);
      }
    }

    // Quantum Glades: short-range random spatial distortion + nausea (refraction)
    if (key.endsWith("exotica_quantum_glades")) {
      if (time % DISTORTION_INTERVAL == 0) {
        double dx = (level.random.nextDouble() - 0.5) * 2.0; // -1..1
        double dz = (level.random.nextDouble() - 0.5) * 2.0;
        double nx = player.getX() + dx;
        double nz = player.getZ() + dz;
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.ENDERMAN_TELEPORT,
            player.getSoundSource(),
            1.0f,
            1.0f);
        player.teleportTo(nx, player.getY(), nz);
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, true, false, false));
      }
    }

    // Fractal Forest: fluctuating gravity cues (brief slow falling or levitation)
    if (key.endsWith("exotica_fractal_forest")) {
      if (time % 100 == 0) {
        if (level.random.nextBoolean()) {
          player.addEffect(
              new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, true, false, false));
        } else {
          player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0, true, false, false));
        }
      }
    }

    // Resonant Dunes: refraction debuff (blindness/weakness light), resonance hum
    if (key.endsWith("exotica_resonant_dunes")) {
      if (time % 120 == 0) {
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, false, false));
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.AMETHYST_BLOCK_RESONATE,
            player.getSoundSource(),
            0.6f,
            0.8f);
      }
    }

    // Prism Canyons: transient color flash via glowing, light sparkles
    if (key.endsWith("exotica_prism_canyons")) {
      if (time % 100 == 0) {
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, true, false, false));
        level.sendParticles(
            ParticleTypes.END_ROD,
            player.getX(),
            player.getY() + 1.0,
            player.getZ(),
            8,
            0.8,
            0.5,
            0.8,
            0.02);
      }
    }
  }
}
