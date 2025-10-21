package com.netroaki.chex.world.hollowworld;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = CosmicHorizonsExpanded.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HollowWorldHazards {

  private static final int DARKNESS_TICK = 80; // 4s cadence
  private static final int RADIATION_TICK = 140; // 7s cadence
  private static final int VOID_PULSE_TICK = 100; // 5s cadence

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

    long t = level.getGameTime();

    // Darkness ambience: brief Darkness or Night Vision interplay
    if (key.endsWith("hollow_bioluminescent_caverns")
        || key.endsWith("hollow_crystal_groves")
        || key.endsWith("hollow_stalactite_forest")) {
      if (t % DARKNESS_TICK == 0) {
        // Flicker of bioluminescence: give short Night Vision and glow particles
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 60, 0, true, false, false));
        level.sendParticles(
            ParticleTypes.GLOW,
            player.getX(),
            player.getY() + 1.0,
            player.getZ(),
            6,
            0.6,
            0.4,
            0.6,
            0.01);
      }
    }

    // Void chasms: periodic void pulse causing wither-like danger and gravity tug
    if (key.endsWith("hollow_void_chasms")) {
      if (t % VOID_PULSE_TICK == 0) {
        DamageSources sources = level.damageSources();
        player.hurt(sources.magic(), 1.0f);
        // slight tug downward to suggest the abyss
        player.push(0.0, -0.3, 0.0);
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.WARDEN_HEARTBEAT,
            player.getSoundSource(),
            0.6f,
            0.6f);
        level.sendParticles(
            ParticleTypes.ASH,
            player.getX(),
            player.getY() + 0.5,
            player.getZ(),
            10,
            0.6,
            0.3,
            0.6,
            0.02);
      }
    }

    // Subterranean rivers: mild radiation-like nausea/fatigue pulses
    if (key.endsWith("hollow_subterranean_rivers")) {
      if (t % RADIATION_TICK == 0) {
        player.addEffect(
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, true, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 40, 0, true, false, false));
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
            player.getSoundSource(),
            0.6f,
            0.8f);
        level.sendParticles(
            ParticleTypes.BUBBLE,
            player.getX(),
            player.getY() + 0.2,
            player.getZ(),
            8,
            0.4,
            0.2,
            0.4,
            0.01);
      }
    }
  }
}
