package com.netroaki.chex.survival;

import com.netroaki.chex.CHEX;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class DesertSurvivalHandler {
  private static final Map<UUID, Integer> playerThirst = new HashMap<>();
  private static final Map<UUID, Integer> playerHeat = new HashMap<>();
  private static final int MAX_THIRST = 20;
  private static final int MAX_HEAT = 10;
  private static final int THIRST_DECREMENT = 20 * 30; // Every 30 seconds
  private static final int HEAT_INCREASE = 20 * 10; // Every 10 seconds in direct sun
  private static final int HEAT_DECREASE = 20 * 5; // Every 5 seconds in shade/water

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Player player = event.player;
    if (player.level().isClientSide) return;

    UUID playerId = player.getUUID();
    Level level = player.level();
    BlockPos pos = player.blockPosition();

    // Initialize player stats if not present
    playerThirst.putIfAbsent(playerId, MAX_THIRST);
    playerHeat.putIfAbsent(playerId, 0);

    // Get current values
    int thirst = playerThirst.get(playerId);
    int heat = playerHeat.get(playerId);

    // Handle thirst mechanics
    if (player.tickCount % THIRST_DECREMENT == 0) {
      if (thirst > 0) {
        playerThirst.put(playerId, thirst - 1);

        // Notify player when getting thirsty
        if (thirst <= 5 && player instanceof ServerPlayer serverPlayer) {
          serverPlayer.displayClientMessage(Component.literal("§6You are getting thirsty!"), true);
        }
      } else {
        // Apply damage when out of water
        player.hurt(level.damageSources().dryOut(), 1.0F);
        if (player.tickCount % 40 == 0) {
          player.displayClientMessage(Component.literal("§cYou are dehydrated!"), true);
        }
      }
    }

    // Handle heat mechanics
    boolean isInSun = isInDirectSunlight(level, pos);
    boolean isInWater = player.isInWater();
    boolean hasHelmet =
        !player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).isEmpty();

    if (player.tickCount % (isInSun ? HEAT_INCREASE : HEAT_DECREASE) == 0) {
      if (isInSun && !isInWater) {
        if (heat < MAX_HEAT) {
          playerHeat.put(playerId, heat + 1);

          // Notify player when getting hot
          if (heat >= MAX_HEAT / 2 && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.literal("§6You're overheating!"), true);
          }
        }
      } else if (heat > 0) {
        playerHeat.put(playerId, Math.max(0, heat - 1));
      }
    }

    // Apply heat effects
    if (heat >= MAX_HEAT) {
      player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false, true));
      player.addEffect(
          new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false, true));

      if (player.tickCount % 40 == 0) {
        player.hurt(level.damageSources().hotFloor(), 1.0F);
        player.displayClientMessage(
            Component.literal("§cYou're suffering from heat stroke!"), true);
      }
    }

    // Sync with client for HUD
    if (player.tickCount % 20 == 0 && player instanceof ServerPlayer serverPlayer) {
      syncSurvivalStats(serverPlayer);
    }
  }

  private static boolean isInDirectSunlight(Level level, BlockPos pos) {
    if (level.dimensionType().hasCeiling() || level.isRaining() || level.isThundering()) {
      return false;
    }

    // Check if there's a solid block above the player
    for (int y = pos.getY() + 1; y < level.getMaxBuildHeight(); y++) {
      BlockState state = level.getBlockState(new BlockPos(pos.getX(), y, pos.getZ()));
      if (state.isSolid()) {
        return false;
      }
    }

    return level.canSeeSky(pos.above());
  }

  @SubscribeEvent
  public static void onPlayerDrink(LivingEntityUseItemEvent.Finish event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }

    if (event.getItem().is(net.minecraft.world.item.Items.POTION)) {
      UUID playerId = player.getUUID();
      int currentThirst = playerThirst.getOrDefault(playerId, MAX_THIRST);
      playerThirst.put(playerId, Math.min(MAX_THIRST, currentThirst + 10));
    }
  }

  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    // Reset stats on respawn
    playerThirst.put(event.getEntity().getUUID(), MAX_THIRST);
    playerHeat.put(event.getEntity().getUUID(), 0);
  }

  public static void syncSurvivalStats(ServerPlayer player) {
    UUID playerId = player.getUUID();
    int thirst = playerThirst.getOrDefault(playerId, MAX_THIRST);
    int heat = playerHeat.getOrDefault(playerId, 0);

    // TODO: Implement packet to sync with client for HUD display
    // NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
    //     new SyncSurvivalStatsPacket(thirst, heat));
  }

  public static int getThirst(Player player) {
    return playerThirst.getOrDefault(player.getUUID(), MAX_THIRST);
  }

  public static int getHeat(Player player) {
    return playerHeat.getOrDefault(player.getUUID(), 0);
  }
}
