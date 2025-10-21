package com.netroaki.chex.world.ringworld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RingworldSpaceportManager {
  private static final Map<UUID, SpaceportData> activeTeleports = new HashMap<>();
  private static final int SPACEPORT_RADIUS = 32;

  public static class SpaceportData {
    public final BlockPos position;
    public final ResourceKey<Level> targetDimension;
    public final Vec3 targetPos;
    public final float targetYaw;
    public final float targetPitch;

    public SpaceportData(
        BlockPos position,
        ResourceKey<Level> targetDimension,
        Vec3 targetPos,
        float targetYaw,
        float targetPitch) {
      this.position = position;
      this.targetDimension = targetDimension;
      this.targetPos = targetPos;
      this.targetYaw = targetYaw;
      this.targetPitch = targetPitch;
    }
  }

  @SubscribeEvent
  public static void onEntityTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Player player = event.player;
    if (!(player.level() instanceof ServerLevel level)) return;

    // Check if player is near a spaceport
    if (level.dimension().equals(RingworldDimension.RINGWORLD_LEVEL)) {
      checkSpaceportProximity(level, player);
    }
  }

  @SubscribeEvent
  public static void onDimensionTravel(EntityTravelToDimensionEvent event) {
    // Handle dimension travel for spaceport teleportation
    if (activeTeleports.containsKey(event.getEntity().getUUID())) {
      SpaceportData data = activeTeleports.get(event.getEntity().getUUID());
      if (event.getDimension().equals(data.targetDimension)) {
        // Cancel the normal teleport and handle it ourselves
        event.setCanceled(true);

        Entity entity = event.getEntity();
        entity.teleportTo(
            ((ServerLevel) entity.level()).getServer().getLevel(data.targetDimension),
            data.targetPos.x,
            data.targetPos.y,
            data.targetPos.z,
            data.targetYaw,
            data.targetPitch);

        // Remove from active teleports
        activeTeleports.remove(entity.getUUID());
      }
    }
  }

  private static void checkSpaceportProximity(ServerLevel level, Player player) {
    BlockPos playerPos = player.blockPosition();

    // Check if player is at a spaceport location
    if (isSpaceportLocation(level, playerPos)) {
      // Start teleport sequence if not already in progress
      if (!activeTeleports.containsKey(player.getUUID())) {
        // Calculate target position (for now, just the overworld spawn)
        BlockPos targetPos = level.getServer().overworld().getSharedSpawnPos();
        Vec3 targetVec = new Vec3(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);

        // Add to active teleports
        activeTeleports.put(
            player.getUUID(),
            new SpaceportData(
                playerPos, Level.OVERWORLD, targetVec, player.getYRot(), player.getXRot()));

        // Start teleport cooldown
        player
            .getCooldowns()
            .addCooldown(
                Items.ENDER_PEARL, // Temporary, replace with custom item
                20 // 1 second cooldown
                );

        // Play effects
        level.playSound(
            null, playerPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);

        // Schedule the actual teleport
        level
            .getServer()
            .execute(
                () -> {
                  if (player.isAlive()) {
                    player.changeDimension(
                        level.getServer().getLevel(Level.OVERWORLD),
                        new RingworldDimension.RingworldTeleporter(level.getServer().overworld()));
                  }
                });
      }
    }
  }

  private static boolean isSpaceportLocation(Level level, BlockPos pos) {
    // Check if the position is a valid spaceport location
    // This is a placeholder - in a real implementation, you would check for specific blocks or
    // structures

    // For now, just check if the player is at the world spawn
    return pos.distSqr(level.getSharedSpawnPos()) < 16.0;
  }

  public static void registerSpaceport(
      BlockPos position,
      ResourceKey<Level> targetDimension,
      Vec3 targetPos,
      float targetYaw,
      float targetPitch) {
    // Register a new spaceport location
    // This would be called when a spaceport structure is generated or built
  }
}
