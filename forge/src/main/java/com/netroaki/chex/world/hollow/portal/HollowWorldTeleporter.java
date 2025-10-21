package com.netroaki.chex.world.hollow.portal;

import com.netroaki.chex.world.hollow.HollowWorldDimension;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

public class HollowWorldTeleporter implements ITeleporter {
  protected final ServerLevel level;
  protected final BlockPos portalPos;

  public HollowWorldTeleporter(ServerLevel level) {
    this.level = level;
    this.portalPos = BlockPos.ZERO;
  }

  public HollowWorldTeleporter(ServerLevel level, BlockPos pos) {
    this.level = level;
    this.portalPos = pos;
  }

  @Override
  public Entity placeEntity(
      Entity entity,
      ServerLevel currentWorld,
      ServerLevel destWorld,
      float yaw,
      Function<Boolean, Entity> repositionEntity) {
    entity.fallDistance = 0;

    // Find or create a portal in the destination
    BlockPos destPos = findOrMakePortal(destWorld, entity);

    // Create a new entity in the destination
    Entity newEntity = entity.getType().create(destWorld);
    if (newEntity != null) {
      newEntity.restoreFrom(entity);
      newEntity.moveTo(
          destPos.getX() + 0.5, destPos.getY(), destPos.getZ() + 0.5, yaw, entity.getXRot());
      newEntity.setDeltaMovement(Vec3.ZERO);
      destWorld.addDuringTeleport(newEntity);

      // Play teleport sound
      destWorld.playSound(null, destPos, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 1.0F, 1.0F);

      // Add portal cooldown
      newEntity.setPortalCooldown();
    }

    return newEntity;
  }

  @Override
  public @Nullable PortalInfo getPortalInfo(
      Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
    BlockPos destPos = findOrMakePortal(destWorld, entity);
    return new PortalInfo(
        new Vec3(destPos.getX() + 0.5, destPos.getY(), destPos.getZ() + 0.5),
        entity.getDeltaMovement(),
        entity.getYRot(),
        entity.getXRot());
  }

  private BlockPos findOrMakePortal(ServerLevel destWorld, Entity entity) {
    // First, try to find an existing portal in the destination
    BlockPos.MutableBlockPos pos =
        new BlockPos.MutableBlockPos(
            Mth.floor(entity.getX()),
            Mth.clamp(
                entity.getY(), destWorld.getMinBuildHeight(), destWorld.getMaxBuildHeight() - 1),
            Mth.floor(entity.getZ()));

    // If we're going to the Hollow World, try to find a portal near the center
    if (destWorld.dimension() == HollowWorldDimension.HOLLOW_WORLD) {
      // Look for existing portal in a 128-block radius
      for (int x = -128; x <= 128; x += 16) {
        for (int z = -128; z <= 128; z += 16) {
          BlockPos checkPos = new BlockPos(x, 64, z);
          if (destWorld.getBlockState(checkPos).getBlock() instanceof HollowWorldPortalBlock) {
            return checkPos;
          }
        }
      }

      // No portal found, create one at the center
      return createPortal(destWorld, new BlockPos(0, 64, 0));
    } else {
      // In the Hollow World, return to the overworld at the same coordinates
      if (entity.level().dimension() == HollowWorldDimension.HOLLOW_WORLD) {
        return new BlockPos(entity.getX(), destWorld.getHeight(), entity.getZ());
      }

      // In the overworld, create a portal at the current position
      return createPortal(destWorld, pos);
    }
  }

  private BlockPos createPortal(ServerLevel level, BlockPos pos) {
    // Find a suitable Y position
    int y = findSuitableY(level, pos);

    // Create a simple 4x5 portal frame
    BlockPos framePos = pos.atY(y);

    // Create obsidian frame
    for (int x = -1; x <= 1; x++) {
      for (int z = -1; z <= 1; z++) {
        if (x == 0 && z == 0) continue; // Skip center
        BlockPos frameBlockPos = framePos.offset(x, 0, z);
        level.setBlock(frameBlockPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
      }
    }

    // Create portal blocks
    level.setBlock(
        framePos,
        HollowWorldPortalBlock.portal()
            .defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X),
        3);

    return framePos;
  }

  private int findSuitableY(Level level, BlockPos pos) {
    int y = pos.getY();

    // Try to find a suitable Y position with air blocks
    while (y > level.getMinBuildHeight() + 3) {
      BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
      if (level.isEmptyBlock(checkPos)
          && level.isEmptyBlock(checkPos.above())
          && level.getBlockState(checkPos.below()).isSolidRender(level, checkPos.below())) {
        return y;
      }
      y--;
    }

    // If no suitable position found, return original Y or world height / 2
    return Math.max(level.getMinBuildHeight() + 3, Math.min(y, level.getMaxBuildHeight() - 4));
  }
}
