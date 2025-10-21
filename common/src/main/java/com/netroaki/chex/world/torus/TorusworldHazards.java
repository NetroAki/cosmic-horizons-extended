package com.netroaki.chex.world.torus;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
public class TorusworldHazards {

  private static final int RADIATION_INTERVAL_TICKS = 120; // every 6s
  private static final float RADIATION_DAMAGE = 1.0f;

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    if (!(event.player instanceof ServerPlayer player)) return;
    ServerLevel level = (ServerLevel) player.level();

    Biome biome = level.getBiome(player.blockPosition()).value();
    String key =
        level
            .registryAccess()
            .registryOrThrow(net.minecraft.core.registries.Registries.BIOME)
            .getKey(biome)
            .toString();

    // Gravity shifts by rim
    if (key.endsWith("torus_inner_rim_forest")) {
      // Slight low-gravity feeling: slow falling when airborne
      if (!player.onGround()) {
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, true, false, false));
      }
    } else if (key.endsWith("torus_outer_rim_desert")) {
      // Heavier gravity feel: slowness
      player.addEffect(
          new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, true, false, false));
    }

    // Structural spine: occasional debris hit (small damage) and mining fatigue cue
    if (key.endsWith("torus_structural_spine")) {
      if (level.getGameTime() % 100 == 0) {
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, true, false, false));
        // 20% chance of debris chip
        if (level.random.nextFloat() < 0.2f) {
          DamageSources sources = level.damageSources();
          player.hurt(sources.fall(), 1.0f);
        }
      }
    }

    // Radiant fields: periodic radiation pulses
    if (key.endsWith("torus_radiant_fields")) {
      if (level.getGameTime() % RADIATION_INTERVAL_TICKS == 0) {
        DamageSources sources = level.damageSources();
        player.hurt(sources.magic(), RADIATION_DAMAGE);
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, true, false, false));
      }
    }

    // Null-G hubs: light levitation to simulate zero-g control
    if (key.endsWith("torus_null_g_hubs")) {
      player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0, true, false, false));
    }
  }
}
