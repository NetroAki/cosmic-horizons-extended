package com.netroaki.chex.world.ringworld;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RingworldGravity {
  // Radius of the ringworld in blocks
  public static final double RING_RADIUS = 10000.0;
  // Standard gravity constant (m/s^2, but scaled for Minecraft's tick rate)
  private static final double GRAVITY_STRENGTH = 0.08;
  // Maximum distance from the ring's surface where gravity is active
  private static final double GRAVITY_RANGE = 32.0;
  // Threshold for gravity transition smoothing
  private static final double GRAVITY_TRANSITION_START = 8.0;

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.START) return;

    Player player = event.player;
    Level level = player.level();

    if (!level.dimension().equals(RingworldDimension.RINGWORLD_LEVEL)) {
      return;
    }

    // Only apply gravity on server side
    if (level.isClientSide) return;

    // Calculate distance from the ring's center in the XZ plane
    double dx = player.getX();
    double dz = player.getZ();
    double horizontalDistSq = dx * dx + dz * dz;
    double horizontalDist = Math.sqrt(horizontalDistSq);

    // Calculate distance from the ring's surface
    double surfaceDist = Math.abs(horizontalDist - RING_RADIUS);

    if (surfaceDist > GRAVITY_RANGE) {
      // Too far from the ring, no gravity
      return;
    }

    // Calculate gravity direction (towards the ring's surface)
    double gravityDirection = (horizontalDist > RING_RADIUS) ? -1.0 : 1.0;

    // Calculate gravity strength with smooth transition near the surface
    double strength = GRAVITY_STRENGTH;
    if (surfaceDist < GRAVITY_TRANSITION_START) {
      // Smooth transition near the surface
      double t = surfaceDist / GRAVITY_TRANSITION_START;
      strength *= t * t * (3 - 2 * t); // Smoothstep interpolation
    }

    // Calculate gravity vector components
    double gx = (dx / horizontalDist) * gravityDirection * strength;
    double gz = (dz / horizontalDist) * gravityDirection * strength;

    // Apply gravity to the player's motion
    if (!player.isNoGravity()) {
      player.setDeltaMovement(
          player.getDeltaMovement().x() + gx,
          player.getDeltaMovement().y(),
          player.getDeltaMovement().z() + gz);
      player.hurtMarked = true; // Mark the player for velocity update
    }

    // Adjust player rotation to match gravity
    if (player instanceof LivingEntity) {
      float targetYRot = (float) Math.toDegrees(Math.atan2(-dz, -dx)) - 90.0f;
      float deltaYRot = ((targetYRot - player.getYRot()) % 360.0f + 540.0f) % 360.0f - 180.0f;

      if (Math.abs(deltaYRot) > 1.0f) {
        player.setYRot(player.getYRot() + deltaYRot * 0.1f);
      }
    }
  }

  @SubscribeEvent
  public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
    if (!event.getLevel().isClientSide()
        && event.getLevel().dimension().equals(RingworldDimension.RINGWORLD_LEVEL)
        && event.getEntity() instanceof LivingEntity) {

      // Initialize entity position to be on the ring's surface if it's a new entity
      if (event.getEntity().tickCount == 0) {
        double angle = event.getLevel().random.nextDouble() * Math.PI * 2;
        double x = Math.cos(angle) * RING_RADIUS;
        double z = Math.sin(angle) * RING_RADIUS;

        // Find the surface at this position
        BlockPos surfacePos =
            event
                .getLevel()
                .getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    new BlockPos((int) x, 0, (int) z));

        event
            .getEntity()
            .moveTo(
                x + 0.5,
                surfacePos.getY() + 1.0,
                z + 0.5,
                (float) (angle * 180.0 / Math.PI) - 90.0f,
                0.0f);
      }
    }
  }

  /** Teleports an entity to the ringworld with proper gravity alignment */
  public static void teleportToRingworld(Entity entity, ServerLevel destination) {
    if (!destination.dimension().equals(RingworldDimension.RINGWORLD_LEVEL)) {
      return;
    }

    // Calculate a position on the ring's surface
    double angle = destination.random.nextDouble() * Math.PI * 2;
    double x = Math.cos(angle) * RING_RADIUS;
    double z = Math.sin(angle) * RING_RADIUS;

    // Find the surface at this position
    BlockPos surfacePos =
        destination.getHeightmapPos(
            net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            new BlockPos((int) x, 0, (int) z));

    // Calculate the facing angle (pointing towards the ring's center)
    float yRot = (float) (angle * 180.0 / Math.PI) - 90.0f;

    // Teleport the entity
    if (entity instanceof Player) {
      entity.teleportTo(destination, x + 0.5, surfacePos.getY() + 1.0, z + 0.5, yRot, 0.0f);
    } else {
      entity.moveTo(x + 0.5, surfacePos.getY() + 1.0, z + 0.5, yRot, 0.0f);
    }
  }

  /** Gets the gravity vector at a specific position in the ringworld */
  public static Vec3 getGravityAt(double x, double y, double z) {
    double dx = x;
    double dz = z;
    double horizontalDistSq = dx * dx + dz * dz;
    double horizontalDist = Math.sqrt(horizontalDistSq);

    // Calculate distance from the ring's surface
    double surfaceDist = Math.abs(horizontalDist - RING_RADIUS);

    if (surfaceDist > GRAVITY_RANGE) {
      return Vec3.ZERO;
    }

    // Calculate gravity direction (towards the ring's surface)
    double gravityDirection = (horizontalDist > RING_RADIUS) ? -1.0 : 1.0;

    // Calculate gravity strength with smooth transition near the surface
    double strength = GRAVITY_STRENGTH;
    if (surfaceDist < GRAVITY_TRANSITION_START) {
      // Smoothstep interpolation
      double t = surfaceDist / GRAVITY_TRANSITION_START;
      strength *= t * t * (3 - 2 * t);
    }

    // Calculate gravity vector components
    double gx = (dx / horizontalDist) * gravityDirection * strength;
    double gz = (dz / horizontalDist) * gravityDirection * strength;

    return new Vec3(gx, 0, gz);
  }
}
