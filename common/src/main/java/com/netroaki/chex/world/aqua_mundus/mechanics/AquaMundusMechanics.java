package com.netroaki.chex.world.aqua_mundus.mechanics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Handles all Aqua Mundus specific mechanics including pressure, oxygen, and thermal systems. */
@Mod.EventBusSubscriber
public class AquaMundusMechanics {

  // Player oxygen level tracking
  private static final Map<UUID, Integer> playerOxygenLevels = new HashMap<>();

  // Get configuration values
  private static double getBasePressureDamage() {
    return AquaMundusConfig.BASE_PRESSURE_DAMAGE.get();
  }

  private static double getPressureIncreasePerMeter() {
    return AquaMundusConfig.PRESSURE_INCREASE_PER_METER.get();
  }

  private static double getMaxPressureDamage() {
    return AquaMundusConfig.MAX_PRESSURE_DAMAGE.get();
  }

  private static int getOxygenDepletionInterval() {
    return AquaMundusConfig.OXYGEN_DEPLETION_INTERVAL.get();
  }

  private static int getOxygenRestoreRate() {
    return AquaMundusConfig.OXYGEN_RESTORE_RATE.get();
  }

  private static int getMaxOxygenLevel() {
    return AquaMundusConfig.MAX_OXYGEN_LEVEL.get();
  }

  private static double getColdBiomeThreshold() {
    return AquaMundusConfig.COLD_BIOME_THRESHOLD.get();
  }

  private static double getHotBiomeThreshold() {
    return AquaMundusConfig.HOT_BIOME_THRESHOLD.get();
  }

  /** Handles player tick events to manage oxygen and pressure systems. */
  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Player player = event.player;
    Level level = player.level();

    // Only process in Aqua Mundus dimension
    if (!isInAquaMundus(level)) return;

    // Initialize oxygen level if not present
    playerOxygenLevels.putIfAbsent(player.getUUID(), getMaxOxygenLevel());
    int oxygenLevel = playerOxygenLevels.get(player.getUUID());

    // Handle oxygen consumption/restoration
    if (isUnderwater(player)) {
      // Consume oxygen when underwater
      if (level.getGameTime() % getOxygenDepletionInterval() == 0) {
        oxygenLevel--;
        if (oxygenLevel <= 0) {
          // Apply drowning damage
          player.hurt(level.damageSources().drown(), 2.0F);
        }
      }
    } else if (player.isInWaterRainOrBubble()) {
      // Restore oxygen when at surface
      oxygenLevel = Math.min(oxygenLevel + getOxygenRestoreRate(), getMaxOxygenLevel());
    }

    // Update oxygen level
    playerOxygenLevels.put(player.getUUID(), oxygenLevel);

    // Handle pressure effects
    if (isUnderwater(player)) {
      applyPressureEffects(player);
    }

    // Handle thermal effects
    applyThermalEffects(player);
  }

  /** Handles player jump events to apply water resistance. */
  @SubscribeEvent
  public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
    if (!(event.getEntity() instanceof Player player)) return;

    if (isInAquaMundus(player.level()) && isUnderwater(player)) {
      // Reduce jump height in water
      player.setDeltaMovement(player.getDeltaMovement().multiply(0.5, 0.5, 0.5));
    }
  }

  /** Checks if the player is in the Aqua Mundus dimension. */
  private static boolean isInAquaMundus(Level level) {
    return level.dimension().location().getPath().equals("aqua_mundus");
  }

  /** Checks if the player is currently underwater. */
  private static boolean isUnderwater(LivingEntity entity) {
    return entity.isInWater() && entity.getEyeInFluidType().isAir();
  }

  /** Applies pressure-based effects to the player. */
  private static void applyPressureEffects(Player player) {
    // Calculate pressure based on depth
    BlockPos pos = player.blockPosition();
    int depth = getWaterDepth(player.level(), pos);
    double pressure = getBasePressureDamage() + (depth * getPressureIncreasePerMeter());

    // Cap the maximum pressure damage
    pressure = Math.min(pressure, getMaxPressureDamage());

    // Apply effects based on pressure
    if (pressure > getMaxPressureDamage() * 0.6) {
      // Apply mining fatigue at high pressure (above 60% of max pressure)
      int effectAmplifier =
          (int)
              ((pressure - (getMaxPressureDamage() * 0.6)) / (getMaxPressureDamage() * 0.4) * 2.0);
      effectAmplifier = Math.min(effectAmplifier, 4); // Cap at level 4

      player.addEffect(
          new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, effectAmplifier, false, false, true));

      // Apply slowness at very high pressure (above 80% of max pressure)
      if (pressure > getMaxPressureDamage() * 0.8) {
        player.addEffect(
            new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                100,
                (int)
                    ((pressure - (getMaxPressureDamage() * 0.8))
                        / (getMaxPressureDamage() * 0.2)
                        * 2.0),
                false,
                false,
                true));
      }
    }

    // Apply movement speed reduction (scales with pressure)
    float speedReduction = (float) ((pressure / getMaxPressureDamage()) * 0.3f);
    player.setSpeed(player.getSpeed() * (1.0f - speedReduction));

    // Apply damage if pressure is very high (above 90% of max pressure)
    if (pressure > getMaxPressureDamage() * 0.9 && player.tickCount % 20 == 0) {
      float damage =
          (float)
              ((pressure - (getMaxPressureDamage() * 0.9)) / (getMaxPressureDamage() * 0.1) * 2.0f);
      player.hurt(player.damageSources().drown(), damage);
    }
  }

  /** Applies thermal effects based on biome temperature. */
  private static void applyThermalEffects(Player player) {
    if (!(player.level() instanceof ServerLevel level)) return;

    Biome biome = level.getBiome(player.blockPosition()).value();
    float temperature = biome.getBaseTemperature();

    // Cold biome effects
    if (temperature < getColdBiomeThreshold()) {
      // Calculate cold intensity (0.0 at threshold, 1.0 at minimum temperature)
      float coldIntensity =
          (float) ((getColdBiomeThreshold() - temperature) / getColdBiomeThreshold());

      // Apply slowness based on cold intensity
      player.addEffect(
          new MobEffectInstance(
              MobEffects.MOVEMENT_SLOWDOWN,
              100,
              Math.min((int) (coldIntensity * 2), 2), // Up to level 2 slowness
              false,
              false,
              true));

      // Apply mining fatigue in very cold biomes
      if (coldIntensity > 0.5) {
        player.addEffect(
            new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN,
                100,
                Math.min((int) ((coldIntensity - 0.5) * 4), 3), // Up to level 3 mining fatigue
                false,
                false,
                true));
      }

      // Apply damage if extremely cold and not in water (hypothermia)
      if (coldIntensity > 0.8 && !player.isInWater() && player.tickCount % 80 == 0) {
        player.hurt(level.damageSources().freeze(), 1.0F);
      }
    }
    // Hot biome effects
    else if (temperature > getHotBiomeThreshold()) {
      // Calculate heat intensity (0.0 at threshold, 1.0 at maximum temperature)
      float heatIntensity =
          (float) ((temperature - getHotBiomeThreshold()) / (2.0 - getHotBiomeThreshold()));

      // Apply fire resistance to prevent burning in hot water
      player.addEffect(
          new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false, true));

      // Apply speed boost in hot water (thermals)
      if (player.isInWater()) {
        player.addEffect(
            new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                100,
                Math.min((int) (heatIntensity * 2), 1), // Up to level 1 speed
                false,
                false,
                true));
      }

      // Apply damage if extremely hot and not in water (heat stroke)
      if (heatIntensity > 0.8 && !player.isInWater() && player.tickCount % 60 == 0) {
        player.hurt(level.damageSources().onFire(), 1.0F);
      }
    }
  }

  /** Calculates the water depth at the given position. */
  private static int getWaterDepth(Level level, BlockPos pos) {
    int depth = 0;
    BlockPos.MutableBlockPos mutablePos = pos.mutable();

    // Count water blocks above
    while (mutablePos.getY() < level.getMaxBuildHeight()) {
      mutablePos.move(0, 1, 0);
      FluidState fluidState = level.getFluidState(mutablePos);
      if (fluidState.is(Fluids.WATER)) {
        depth++;
      } else {
        break;
      }
    }

    return depth;
  }

  /**
   * Gets the current oxygen level for a player (0-100%).
   *
   * @param player The player to check
   * @return Oxygen level as a percentage (0-100)
   */
  public static int getOxygenLevel(Player player) {
    int currentOxygen = playerOxygenLevels.getOrDefault(player.getUUID(), getMaxOxygenLevel());
    return (int) ((currentOxygen / (float) getMaxOxygenLevel()) * 100.0f);
  }
}
