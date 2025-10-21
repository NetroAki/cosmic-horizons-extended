package com.netroaki.chex.world.stormworld;

import com.netroaki.chex.api.suit.ISuitTier;
import com.netroaki.chex.api.suit.SuitTier;
import com.netroaki.chex.config.StormworldMechanicsConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StormworldMechanics {

  private static final ResourceLocation BIOME_UPPER =
      new ResourceLocation("cosmic_horizons_extended", "storm_upper_atmosphere");
  private static final ResourceLocation BIOME_BANDS =
      new ResourceLocation("cosmic_horizons_extended", "storm_storm_bands");
  private static final ResourceLocation BIOME_FIELDS =
      new ResourceLocation("cosmic_horizons_extended", "storm_lightning_fields");
  private static final ResourceLocation BIOME_EYE =
      new ResourceLocation("cosmic_horizons_extended", "storm_eye");
  private static final ResourceLocation BIOME_DEPTHS =
      new ResourceLocation("cosmic_horizons_extended", "storm_metallic_hydrogen_depths");

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    Player player = event.player;
    Level level = player.level();
    if (level.isClientSide) return;

    // Resolve biome id
    ResourceLocation biomeId =
        level.getBiome(player.blockPosition()).unwrapKey().map(k -> k.location()).orElse(null);
    if (biomeId == null) return;

    // Suit tier
    ISuitTier suitTier = SuitTier.getSuitTier(player);
    int tier = suitTier != null ? suitTier.getTier() : 0;

    // Gravity mechanics
    if (biomeId.equals(BIOME_BANDS)) {
      // Simulate high gravity by applying a brief movement slowdown effect
      double mult = StormworldMechanicsConfig.GRAVITY_HIGH_MULT.get();
      int amplifier = mult < 0.9 ? 0 : 0; // conservative amp
      player.addEffect(
          new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, amplifier, false, false, true));
    } else if (biomeId.equals(BIOME_EYE) || biomeId.equals(BIOME_UPPER)) {
      // Low gravity feel by granting Slow Falling if enabled
      if (StormworldMechanicsConfig.GRAVITY_ENABLE_SLOW_FALLING.get()
          && player.fallDistance > 1.0f) {
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 0, false, false, true));
      }
    }

    // Lightning mechanics in Lightning Fields
    if (biomeId.equals(BIOME_FIELDS) && StormworldMechanicsConfig.LIGHTNING_ENABLED.get()) {
      int interval = Math.max(5, StormworldMechanicsConfig.LIGHTNING_CHECK_INTERVAL_TICKS.get());
      if (level.getGameTime() % interval == 0) {
        double chance = StormworldMechanicsConfig.LIGHTNING_STRIKE_CHANCE.get();
        if (level.random.nextDouble() < chance) {
          // Strike near player; mitigate if suit tier high enough
          if (level instanceof ServerLevel serverLevel) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
            if (bolt != null) {
              bolt.moveTo(
                  player.getX() + (serverLevel.random.nextDouble() - 0.5) * 6.0,
                  player.getY(),
                  player.getZ() + (serverLevel.random.nextDouble() - 0.5) * 6.0);
              serverLevel.addFreshEntity(bolt);
            }
          }
          int requiredTier = StormworldMechanicsConfig.ELECTRIC_REQUIRED_TIER.get();
          if (tier < requiredTier) {
            int dmg = Math.max(0, StormworldMechanicsConfig.ELECTRIC_DAMAGE_BASE.get());
            player.hurt(player.damageSources().lightningBolt(), dmg);
          }
        }
      }
    }

    // Hunger acceleration in bands and fields (wind, exertion)
    if ((biomeId.equals(BIOME_BANDS) || biomeId.equals(BIOME_FIELDS))
        && StormworldMechanicsConfig.HUNGER_ACCEL_ENABLED.get()) {
      int interval = Math.max(5, StormworldMechanicsConfig.HUNGER_CHECK_INTERVAL_TICKS.get());
      if (level.getGameTime() % interval == 0) {
        float exhaust = StormworldMechanicsConfig.HUNGER_ACCEL_RATE.get().floatValue();
        player.causeFoodExhaustion(exhaust);
      }
    }

    // Pressure in depths
    if (biomeId.equals(BIOME_DEPTHS) && StormworldMechanicsConfig.PRESSURE_ENABLED.get()) {
      int requiredTier = StormworldMechanicsConfig.PRESSURE_REQUIRED_TIER.get();
      if (tier < requiredTier) {
        float dmg = StormworldMechanicsConfig.PRESSURE_DAMAGE_PER_TICK.get().floatValue();
        if (dmg > 0) {
          player.hurt(player.damageSources().cramming(), dmg);
        }
        // add slowness to emulate crushing pressure
        player.addEffect(
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1, false, false, true));
      }
    }
  }
}
