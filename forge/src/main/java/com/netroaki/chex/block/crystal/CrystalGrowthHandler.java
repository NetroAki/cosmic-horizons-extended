package com.netroaki.chex.block.crystal;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class CrystalGrowthHandler {
  private static final int MAX_GROWTH_STAGE = 3; // Small -> Medium -> Large -> Cluster
  private static final int GROWTH_CHANCE = 25; // 1 in X chance per random tick

  // Directions for checking adjacent blocks
  private static final Direction[] GROWTH_DIRECTIONS = {
    Direction.UP, Direction.DOWN, Direction.NORTH,
    Direction.SOUTH, Direction.EAST, Direction.WEST
  };

  // Valid blocks that can support crystal growth
  private static final Set<Block> VALID_GROWTH_BLOCKS =
      Set.of(Blocks.STONE, Blocks.DEEPSLATE, Blocks.BASALT, Blocks.CALCITE);

  public static void onRandomTick(
      BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (!canGrow(level, pos, state)) {
      return;
    }

    // Check growth chance
    if (random.nextInt(GROWTH_CHANCE) != 0) {
      return;
    }

    // Get current growth stage
    int currentStage = state.getValue(CrystalBlock.VARIANT).ordinal();

    if (currentStage < MAX_GROWTH_STAGE) {
      // Grow to next stage
      level.setBlockAndUpdate(
          pos,
          state.setValue(
              CrystalBlock.VARIANT, CrystalBlock.CrystalVariant.values()[currentStage + 1]));
    } else {
      // Try to spread to adjacent blocks
      trySpread(level, pos, random);
    }
  }

  private static boolean canGrow(Level level, BlockPos pos, BlockState state) {
    // Check if block has a valid supporting block
    Direction facing = state.getValue(BlockStateProperties.FACING);
    BlockPos supportPos = pos.relative(facing.getOpposite());
    BlockState supportState = level.getBlockState(supportPos);

    return supportState.isFaceSturdy(level, supportPos, facing) && hasSpaceToGrow(level, pos);
  }

  private static boolean hasSpaceToGrow(Level level, BlockPos pos) {
    // Check if there's enough space around for the crystal to grow
    int airBlocks = 0;
    for (Direction dir : GROWTH_DIRECTIONS) {
      if (level.isEmptyBlock(pos.relative(dir))) {
        airBlocks++;
        if (airBlocks >= 2) { // Need at least 2 air blocks to grow
          return true;
        }
      }
    }
    return false;
  }

  private static void trySpread(ServerLevel level, BlockPos pos, RandomSource random) {
    // Try to find a valid adjacent position to spread to
    Set<BlockPos> checkedPositions = new HashSet<>();

    for (int i = 0; i < 6; i++) {
      BlockPos checkPos =
          pos.offset(
              random.nextInt(3) - 1, // -1 to 1
              random.nextInt(3) - 1,
              random.nextInt(3) - 1);

      if (checkedPositions.contains(checkPos) || checkPos.equals(pos)) {
        continue;
      }
      checkedPositions.add(checkPos);

      // Check if position is valid for crystal growth
      if (isValidGrowthPosition(level, checkPos)) {
        // Place a new small crystal
        // TODO: Fix when CHEXBlocks is properly implemented
        // BlockState newCrystal =
        // CHEXBlocks.CRYSTAL_BLOCK
        // .get()
        // .defaultBlockState()
        // .setValue(CrystalBlock.VARIANT, CrystalBlock.CrystalVariant.SMALL)
        // .setValue(BlockStateProperties.FACING, getValidGrowthDirection(level,
        // checkPos));

        // level.setBlockAndUpdate(checkPos, newCrystal);
        break;
      }
    }
  }

  private static boolean isValidGrowthPosition(Level level, BlockPos pos) {
    // Check if block is air or replaceable
    if (!level.isEmptyBlock(pos) && !level.getFluidState(pos).is(Fluids.WATER)) {
      return false;
    }

    // Check if there's a valid block to grow on adjacent
    for (Direction dir : GROWTH_DIRECTIONS) {
      BlockPos supportPos = pos.relative(dir);
      BlockState supportState = level.getBlockState(supportPos);

      if (VALID_GROWTH_BLOCKS.contains(supportState.getBlock())
          && supportState.isFaceSturdy(level, supportPos, dir.getOpposite())) {
        // Check if there's enough space
        int airCount = 0;
        for (Direction checkDir : GROWTH_DIRECTIONS) {
          if (level.isEmptyBlock(pos.relative(checkDir))) {
            airCount++;
            if (airCount >= 2) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  private static Direction getValidGrowthDirection(Level level, BlockPos pos) {
    // Find a valid direction to place the crystal
    for (Direction dir : GROWTH_DIRECTIONS) {
      BlockPos supportPos = pos.relative(dir);
      BlockState supportState = level.getBlockState(supportPos);

      if (VALID_GROWTH_BLOCKS.contains(supportState.getBlock())
          && supportState.isFaceSturdy(level, supportPos, dir.getOpposite())) {
        return dir.getOpposite();
      }
    }

    // Default to up if no valid direction found (shouldn't happen if position is
    // valid)
    return Direction.UP;
  }
}
