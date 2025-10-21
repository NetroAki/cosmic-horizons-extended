package com.netroaki.chex.world.hazards;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.api.suit.ISuitTier;
import com.netroaki.chex.api.suit.SuitTier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID)
public class AlphaCentauriHazards {

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Player player = event.player;
    if (!(player.level() instanceof ServerLevel level)) return;
    if (!level.dimension().location().getPath().equals("alpha_centauri_a")) return;

    // Check if player is in a protected structure or has proper protection
    if (isProtected(player)) return;

    // Get hazard level based on biome and position
    float hazardLevel = getHazardLevel(player);

    // Apply effects based on hazard level
    if (hazardLevel > 0) {
      applyHazardEffects(player, hazardLevel);
    }
  }

  private static boolean isProtected(Player player) {
    // Check if player has proper suit tier (IV or higher)
    ISuitTier suitTier = SuitTier.getSuitTier(player);
    if (suitTier.getTier() >= 4) {
      return true;
    }

    // TODO: Add check for protected structures (shelters, etc.)

    return false;
  }

  private static float getHazardLevel(Player player) {
    // Base hazard level (0.0 - 1.0)
    float hazardLevel = 0.5f;

    // Increase hazard during solar flares
    if (SolarFlareManager.isFlareActive(player.level())) {
      hazardLevel = 1.0f;
    }

    // Adjust based on Y level (higher = more exposure)
    double y = player.getY();
    if (y > 100) {
      hazardLevel = Math.min(1.0f, hazardLevel + (float) (y - 100) * 0.01f);
    }

    // Adjust based on time of day (simplified for star surface)
    long time = player.level().getDayTime() % 24000;
    if (time > 12000 && time < 18000) {
      // "Daytime" - increased hazard
      hazardLevel = Math.min(1.0f, hazardLevel + 0.3f);
    }

    return hazardLevel;
  }

  private static void applyHazardEffects(Player player, float hazardLevel) {
    // Apply radiation effect with strength based on hazard level
    int duration = 200; // 10 seconds
    int amplifier = (int) (hazardLevel * 2); // 0-2 for normal, 3-4 for flares

    player.addEffect(
        new MobEffectInstance(
            ModEffects.RADIATION_EFFECT.get(), duration, amplifier, false, true, true));

    // Visual and audio feedback
    if (player.level().random.nextFloat() < hazardLevel * 0.1) {
      player
          .level()
          .playSound(
              null,
              player.getX(),
              player.getY(),
              player.getZ(),
              SoundEvents.FIRE_EXTINGUISH,
              SoundSource.AMBIENT,
              0.5f,
              0.5f + player.level().random.nextFloat() * 0.5f);
    }
  }

  @SubscribeEvent
  public static void onEntitySpawn(LivingEvent.LivingTickEvent event) {
    LivingEntity entity = event.getEntity();
    if (!(entity.level() instanceof ServerLevel level)) return;
    if (!level.dimension().location().getPath().equals("alpha_centauri_a")) return;

    // Prevent certain mobs from spawning in hazardous areas
    if (entity.tickCount == 0 && !isHazardResistant(entity)) {
      double y = entity.getY();
      if (y > 100 || SolarFlareManager.isFlareActive(level)) {
        entity.remove(Entity.RemovalReason.DISCARDED);
      }
    }
  }

  private static boolean isHazardResistant(LivingEntity entity) {
    // TODO: Add check for mobs that can survive in the star environment
    return false;
  }
}
