package com.netroaki.chex.world.neutronforge;

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
public class NeutronForgeHazards {

  private static final int MAGNETIC_TICK = 100; // 5s cadence
  private static final int PLASMA_TICK = 140; // 7s cadence
  private static final int GRAVITY_TICK = 160; // 8s cadence

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
    if (!key.startsWith("cosmic_horizons_extended:neutron_")) return;

    long t = level.getGameTime();

    // Suit hazard config for Neutron Forge planet
    String planetId = "cosmic_horizons_extended:neutron_forge";
    SuitHazardsConfigCore.Config cfg = SuitHazardsRuntime.get();
    int suitTier =
        player
            .getCapability(PlayerTierCapability.INSTANCE)
            .map(PlayerTierCapability::getSuitTier)
            .orElse(1);
    double mitTherm = SuitHazardsRuntime.mitigationForTier(suitTier, m -> m.thermal);
    double mitRad = SuitHazardsRuntime.mitigationForTier(suitTier, m -> m.radiation);
    double mitPress = SuitHazardsRuntime.mitigationForTier(suitTier, m -> m.pressure);
    double baseTherm = 0.6, baseRad = 0.6, basePress = 0.6; // defaults
    if (cfg != null && cfg.rules != null) {
      for (SuitHazardsConfigCore.HazardRule r : cfg.rules) {
        if ("planet".equals(r.scope) && planetId.equals(r.idOrTag)) {
          baseTherm = r.thermal;
          baseRad = r.radiation;
          basePress = r.pressure;
          break;
        }
      }
    }

    // Magnetic storms (disrupt movement/tools slightly)
    if (t % MAGNETIC_TICK == 0
        && (key.endsWith("magnetar_belts") || key.endsWith("forge_platforms"))) {
      double eff = basePress * (1.0 - mitPress);
      if (eff >= 0.15) {
        player.addEffect(
            new MobEffectInstance(
                MobEffects.WEAKNESS, (int) Math.max(20, 60 * eff), 0, true, false, false));
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.ANVIL_HIT,
            player.getSoundSource(),
            0.6f,
            0.7f);
        level.sendParticles(
            ParticleTypes.ELECTRIC_SPARK,
            player.getX(),
            player.getY() + 0.8,
            player.getZ(),
            10,
            0.6,
            0.4,
            0.6,
            0.02);
      }
    }

    // Plasma bursts (burn cue)
    if (t % PLASMA_TICK == 0
        && (key.endsWith("accretion_rim") || key.endsWith("forge_platforms"))) {
      double eff = baseTherm * (1.0 - mitTherm);
      if (eff >= 0.15) {
        player.addEffect(
            new MobEffectInstance(
                MobEffects.FIRE_RESISTANCE, (int) Math.max(20, 40 * eff), 0, true, false, false));
        player.hurt(level.damageSources().hotFloor(), (float) Math.max(0.5f, eff));
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.FIRECHARGE_USE,
            player.getSoundSource(),
            0.6f,
            1.1f);
        level.sendParticles(
            ParticleTypes.FLAME,
            player.getX(),
            player.getY() + 0.2,
            player.getZ(),
            14,
            0.6,
            0.2,
            0.6,
            0.02);
      }
    }

    // Gravity wells (pull down, slow)
    if (t % GRAVITY_TICK == 0 && key.endsWith("gravity_wells")) {
      double eff = basePress * (1.0 - mitPress);
      if (eff >= 0.15) {
        player.addEffect(
            new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN, (int) Math.max(20, 80 * eff), 0, true, false, false));
        player.push(0.0, -0.25 - 0.2 * eff, 0.0);
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.WARDEN_HEARTBEAT,
            player.getSoundSource(),
            0.5f,
            0.6f);
        level.sendParticles(
            ParticleTypes.ASH,
            player.getX(),
            player.getY() + 0.4,
            player.getZ(),
            12,
            0.6,
            0.3,
            0.6,
            0.02);
      }
    }
  }
}
