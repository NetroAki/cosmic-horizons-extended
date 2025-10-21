package com.netroaki.chex.world.aquamundus;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AquaMundusMechanics {
  private static final int OXYGEN_DEPLETION_RATE = 1; // Oxygen units per second
  private static final int OXYGEN_DAMAGE_INTERVAL = 20; // Ticks between damage when out of oxygen
  private static final float PRESSURE_DAMAGE = 2.0f; // Damage per second when pressure is too high
  private static final float THERMAL_DAMAGE = 1.0f; // Damage per second when temperature is extreme

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Player player = event.player;
    Level level = player.level();

    // Only apply mechanics in Aqua Mundus dimension
    if (!level.dimension().location().getPath().equals("aqua_mundus")) {
      return;
    }

    // Update oxygen system
    updateOxygen(player, level);

    // Update pressure system
    updatePressure(player, level);

    // Update thermal system
    updateThermal(player, level);
  }

  private static void updateOxygen(Player player, Level level) {
    // Check if player is in water or has water breathing
    if (player.isInWater()
        || player.hasEffect(net.minecraft.world.effect.MobEffects.WATER_BREATHING)) {
      // Replenish oxygen when in water or with water breathing
      player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + 1));
    } else {
      // Deplete oxygen when not in water
      player.setAirSupply(player.getAirSupply() - OXYGEN_DEPLETION_RATE);

      // Damage player if out of oxygen
      if (player.getAirSupply() <= 0) {
        if (player.tickCount % OXYGEN_DAMAGE_INTERVAL == 0) {
          player.hurt(level.damageSources().drown(), 1.0F);
        }
      }
    }
  }

  private static void updatePressure(Player player, Level level) {
    BlockPos playerPos = player.blockPosition();
    int depth = calculateWaterDepth(level, playerPos);

    // Damage increases with depth
    if (depth > 15 && player.tickCount % 20 == 0) {
      float damage = PRESSURE_DAMAGE * (depth / 15.0f);
      player.hurt(level.damageSources().dryOut(), damage);
    }
  }

  private static void updateThermal(Player player, Level level) {
    BlockPos playerPos = player.blockPosition();

    // Check for thermal vents nearby
    if (isNearThermalVent(level, playerPos)) {
      // Damage player if too close to thermal vent
      if (player.tickCount % 20 == 0) {
        player.hurt(level.damageSources().hotFloor(), THERMAL_DAMAGE);
      }
    }
  }

  private static int calculateWaterDepth(Level level, BlockPos pos) {
    int depth = 0;
    BlockPos.MutableBlockPos checkPos = pos.mutable();

    // Count water blocks above the player
    while (checkPos.getY() < level.getMaxBuildHeight()
        && level.getFluidState(checkPos).is(Fluids.WATER)) {
      depth++;
      checkPos.move(0, 1, 0);
    }

    return depth;
  }

  private static boolean isNearThermalVent(Level level, BlockPos pos) {
    // Check in a 5x5x5 area around the player for thermal vents
    int radius = 2;
    for (int x = -radius; x <= radius; x++) {
      for (int y = -radius; y <= radius; y++) {
        for (int z = -radius; z <= radius; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          if (level.getBlockState(checkPos).is(Blocks.MAGMA_BLOCK)) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
