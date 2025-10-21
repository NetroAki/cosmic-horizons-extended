package com.netroaki.chex.event;

// import com.netroaki.chex.api.suit.ISuitTier; // TODO: Implement suit API
// import com.netroaki.chex.api.suit.SuitTier; // TODO: Implement suit API
import com.netroaki.chex.config.CrystalisHazardsConfig;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FrostbiteHandler {
  // TODO: Fix when BiomeTags.create is available
  // private static final TagKey<Biome> COLD_BIOMES =
  // BiomeTags.create("forge:is_cold");
  private static final TagKey<Biome> COLD_BIOMES = null; // Placeholder
  private static final int FROSTBITE_DURATION = 300; // 15 seconds base duration

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Player player = event.player;
    Level level = player.level();
    if (level.isClientSide) return;

    // Check if config is loaded before accessing it
    try {
      if (!CrystalisHazardsConfig.FROSTBITE_ENABLED.get()) return;
    } catch (IllegalStateException e) {
      // Config not loaded yet, skip this tick
      return;
    }

    // Only check periodically for performance
    int interval;
    try {
      interval = CrystalisHazardsConfig.FROSTBITE_INTERVAL_TICKS.get();
    } catch (IllegalStateException e) {
      // Config not loaded yet, use default interval
      interval = 20;
    }
    if (interval <= 0) interval = 20;
    if (level.getGameTime() % interval != 0) return;

    // Check if player is in a cold biome
    if (COLD_BIOMES != null && isInColdBiome(player)) {
      // Check if player is exposed to the elements (not in water, not wearing full
      // armor, etc.)
      if (isExposedToElements(player)) {
        // TODO: Suit tier mitigation when suit API is implemented
        // ISuitTier suitTier = SuitTier.getSuitTier(player);
        // int tier = suitTier != null ? suitTier.getTier() : 0;
        // if (tier >= 3) {
        // // Proper suit mitigates ambient frostbite entirely on Crystalis-tier worlds
        // return;
        // }
        // Apply vanilla freezing effects instead of custom frostbite
        int amplifier = 0; // Base level

        // Increase effect in blizzard conditions
        if (level.isRaining() && level.canSeeSky(player.blockPosition())) {
          amplifier = 1; // More severe in blizzards
        }

        // If player is wet, increase effect
        if (player.isInWaterRainOrBubble()) {
          amplifier++;
        }

        // Cap amplifier
        amplifier = Math.min(amplifier, 2);

        // Apply vanilla movement slowdown (freezing effect)
        MobEffectInstance currentSlowdown = player.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (currentSlowdown == null || currentSlowdown.getAmplifier() < amplifier) {
          player.addEffect(
              new MobEffectInstance(
                  MobEffects.MOVEMENT_SLOWDOWN,
                  FROSTBITE_DURATION,
                  amplifier,
                  false, // Don't show particles (we'll handle them separately)
                  true, // Show icon
                  true // Show particles
                  ));
        }

        // Apply mining fatigue at higher levels (simulating frozen hands)
        if (amplifier >= 1) {
          MobEffectInstance currentMiningFatigue = player.getEffect(MobEffects.DIG_SLOWDOWN);
          if (currentMiningFatigue == null || currentMiningFatigue.getAmplifier() < amplifier) {
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.DIG_SLOWDOWN,
                    FROSTBITE_DURATION,
                    amplifier - 1, // One level lower than movement slowdown
                    false,
                    true,
                    true));
          }
        }

        // Apply freeze damage periodically (simulating frostbite damage)
        if (player.tickCount % 100 == 0) { // Every 5 seconds
          player.hurt(player.damageSources().freeze(), 1.0f + (amplifier * 0.5f));
        }
      }
    } else {
      // Remove freezing effects when leaving cold biomes
      if (player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
        // Only remove if it's a freezing-related slowdown (not from other sources)
        MobEffectInstance slowdown = player.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (slowdown != null && slowdown.getDuration() > 200) { // Only remove long-duration effects
          player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        }
      }
      if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
        // Only remove if it's a freezing-related mining fatigue (not from other sources)
        MobEffectInstance miningFatigue = player.getEffect(MobEffects.DIG_SLOWDOWN);
        if (miningFatigue != null
            && miningFatigue.getDuration() > 200) { // Only remove long-duration effects
          player.removeEffect(MobEffects.DIG_SLOWDOWN);
        }
      }
    }
  }

  private static boolean isInColdBiome(LivingEntity entity) {
    // First check the tag
    if (entity.level().getBiome(entity.blockPosition()).is(COLD_BIOMES)) {
      return true;
    }

    // Then check specific biome conditions
    // TODO: Add more biome checks as needed
    return false;
  }

  private static boolean isExposedToElements(Player player) {
    // Check if player is in water (more severe)
    if (player.isInWater()) {
      return true;
    }

    // Check if player is in rain/snow
    if (player.isInWaterRainOrBubble()) {
      // Check if player has a helmet (basic protection)
      if (player.getInventory().getArmor(3).isEmpty()) {
        return true;
      }
    }

    // Check if player is wearing full armor (basic protection)
    if (player.getInventory().armor.stream().allMatch(stack -> !stack.isEmpty())) {
      return false;
    }

    // Exposed if not in a solid block and not in water
    return !player.isInWater() && !player.isInLava() && !player.isInWall();
  }
}
