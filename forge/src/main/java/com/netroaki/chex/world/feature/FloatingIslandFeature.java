package com.netroaki.chex.world.feature;

import com.mojang.serialization.Codec;
import com.netroaki.chex.registry.CHEXBlocks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FloatingIslandFeature extends Feature<NoneFeatureConfiguration> {
  private static final BlockState AIR = Blocks.AIR.defaultBlockState();
  private static final BlockState STONE = Blocks.STONE.defaultBlockState();
  private static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
  private static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
  private static final BlockState PANDORA_STONE =
      CHEXBlocks.PANDORA_STONE.get().defaultBlockState();

  public FloatingIslandFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos origin = context.origin();
    RandomSource random = context.random();

    // Ensure we're generating in the air
    if (!level.isEmptyBlock(origin)) {
      return false;
    }

    // Island dimensions
    int radius = 6 + random.nextInt(8);
    int height = 3 + random.nextInt(4);
    int bottomY = origin.getY() - height / 2;

    // Generate island shape
    List<BlockPos> islandBlocks = new ArrayList<>();

    // Generate the main island shape (ellipsoid)
    for (int x = -radius; x <= radius; x++) {
      for (int z = -radius; z <= radius; z++) {
        for (int y = -height; y <= height; y++) {
          double dx = (double) x / (radius * 0.8);
          double dy = (double) y / (height * 0.8);
          double dz = (double) z / (radius * 0.8);
          double distance = dx * dx + dy * dy + dz * dz;

          if (distance <= 1.0) {
            BlockPos pos = origin.offset(x, y, z);
            if (level.getBlockState(pos).isAir()) {
              islandBlocks.add(pos);
            }
          }
        }
      }
    }

    // Place the island blocks
    for (BlockPos pos : islandBlocks) {
      // Skip if position is outside world bounds
      if (!level.isInWorldBounds(pos)) continue;

      // Skip if position is already occupied
      if (!level.getBlockState(pos).isAir()) continue;

      // Determine block type based on position in island
      double centerDist =
          Math.sqrt(
                  Math.pow(pos.getX() - origin.getX(), 2) + Math.pow(pos.getZ() - origin.getZ(), 2))
              / radius;

      BlockState blockToPlace;

      if (pos.getY() >= origin.getY()) {
        // Top layer - grass or dirt
        blockToPlace = centerDist < 0.7 ? GRASS_BLOCK : DIRT;
      } else if (pos.getY() > origin.getY() - 2) {
        // Middle layers - dirt or stone
        blockToPlace = random.nextFloat() < 0.7 ? DIRT : PANDORA_STONE;
      } else {
        // Bottom layers - stone
        blockToPlace = PANDORA_STONE;
      }

      // Place the block
      level.setBlock(pos, blockToPlace, 3);

      // Add some surface features
      if (blockToPlace == GRASS_BLOCK && random.nextFloat() < 0.1) {
        // Add some grass or flowers on top
        BlockPos above = pos.above();
        if (level.getBlockState(above).isAir()) {
          if (random.nextFloat() < 0.7) {
            level.setBlock(above, Blocks.GRASS.defaultBlockState(), 3);
          } else {
            level.setBlock(above, Blocks.DANDELION.defaultBlockState(), 3);
          }
        }
      }
    }

    // Add some hanging vines
    addHangingVines(level, origin, radius, height, random);

    // Add some floating rocks below
    addFloatingRocks(level, origin, radius, height, random);

    return true;
  }

  private void addHangingVines(
      WorldGenLevel level, BlockPos origin, int radius, int height, RandomSource random) {
    int vineCount = 5 + random.nextInt(10);

    for (int i = 0; i < vineCount; i++) {
      // Random position around the edge of the island
      double angle = random.nextDouble() * Math.PI * 2;
      double distance = (0.7 + random.nextDouble() * 0.3) * radius;
      int x = (int) (origin.getX() + Math.cos(angle) * distance);
      int z = (int) (origin.getZ() + Math.sin(angle) * distance);

      // Find the bottom of the island at this x,z
      int y = origin.getY() - height / 2;
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);

      // Find the bottom-most solid block
      while (y > level.getMinBuildHeight() + 10 && !level.getBlockState(pos).isAir()) {
        y--;
        pos.setY(y);
      }

      // Add vines hanging down
      if (y > level.getMinBuildHeight() + 5) {
        int vineLength = 2 + random.nextInt(8);
        for (int vy = 0; vy < vineLength; vy++) {
          BlockPos vinePos = new BlockPos(x, y - vy, z);
          if (level.getBlockState(vinePos).isAir()) {
            level.setBlock(
                vinePos,
                Blocks.VINE
                    .defaultBlockState()
                    .setValue(net.minecraft.world.level.block.VineBlock.UP, false)
                    .setValue(net.minecraft.world.level.block.VineBlock.NORTH, false)
                    .setValue(net.minecraft.world.level.block.VineBlock.SOUTH, false)
                    .setValue(net.minecraft.world.level.block.VineBlock.EAST, false)
                    .setValue(net.minecraft.world.level.block.VineBlock.WEST, false),
                3);
          } else {
            break;
          }
        }
      }
    }
  }

  private void addFloatingRocks(
      WorldGenLevel level, BlockPos origin, int radius, int height, RandomSource random) {
    int rockCount = 3 + random.nextInt(5);

    for (int i = 0; i < rockCount; i++) {
      // Random position below the island
      double angle = random.nextDouble() * Math.PI * 2;
      double distance = (0.5 + random.nextDouble() * 0.5) * radius * 1.5;
      int x = (int) (origin.getX() + Math.cos(angle) * distance);
      int z = (int) (origin.getZ() + Math.sin(angle) * distance);
      int y = origin.getY() - height / 2 - 5 - random.nextInt(10);

      // Skip if position is too low
      if (y < level.getMinBuildHeight() + 5) continue;

      // Generate a small floating rock
      int rockRadius = 2 + random.nextInt(3);
      for (int rx = -rockRadius; rx <= rockRadius; rx++) {
        for (int rz = -rockRadius; rz <= rockRadius; rz++) {
          for (int ry = -1; ry <= 1; ry++) {
            double dist = Math.sqrt(rx * rx + rz * rz * 2 + ry * ry * 4) / rockRadius;
            if (dist <= 1.0) {
              BlockPos pos = new BlockPos(x + rx, y + ry, z + rz);
              if (level.getBlockState(pos).isAir()) {
                level.setBlock(pos, PANDORA_STONE, 3);
              }
            }
          }
        }
      }

      // Add a chain or vine connecting to the main island
      if (random.nextFloat() < 0.7) {
        int startY = origin.getY() - height / 2 - 1;
        int endY = y + 1;
        int chainX = x;
        int chainZ = z;

        for (int cy = startY; cy > endY; cy--) {
          BlockPos chainPos = new BlockPos(chainX, cy, chainZ);
          if (level.getBlockState(chainPos).isAir()) {
            level.setBlock(
                chainPos,
                Blocks.CHAIN
                    .defaultBlockState()
                    .setValue(net.minecraft.world.level.block.ChainBlock.AXIS, Direction.Axis.Y),
                3);
          }

          // Add some randomness to the chain
          if (random.nextFloat() < 0.3) {
            chainX += random.nextInt(3) - 1;
            chainZ += random.nextInt(3) - 1;
          }
        }
      }
    }
  }
}
