package com.netroaki.chex.world.feature;

import com.mojang.serialization.Codec;
import com.netroaki.chex.registry.CHEXBlocks;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;

/**
 * Generates the Spore Tyrant boss arena with unique environmental features. The arena consists of a
 * large circular platform with spore-emitting pillars and hazardous spore clouds.
 */
public class SporeTyrantArenaFeature extends Feature<NoneFeatureConfiguration> {
  // Arena dimensions
  private static final int ARENA_RADIUS = 32;
  private static final int ARENA_HEIGHT = 15;
  private static final int PLATFORM_RADIUS = 24;
  private static final int PILLAR_COUNT = 6;
  private static final int PILLAR_RADIUS = 3;
  private static final int PILLAR_HEIGHT = 12;

  // Block states
  private static final BlockState FLOOR_BLOCK = CHEXBlocks.INFESTED_SOIL.get().defaultBlockState();
  private static final BlockState PILLAR_BLOCK =
      CHEXBlocks.INFESTED_STONE.get().defaultBlockState();
  private static final BlockState SPORE_EMITTER =
      CHEXBlocks.SPORE_EMITTER.get().defaultBlockState();
  private static final BlockState SPORE_CLOUD = CHEXBlocks.SPORE_CLOUD.get().defaultBlockState();

  public SporeTyrantArenaFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos origin = context.origin();
    RandomSource random = context.random();

    // Find a suitable surface position
    origin = findSurface(level, origin);
    if (origin == null) {
      return false;
    }

    // Track all positions we modify
    Set<BlockPos> modifiedPositions = new HashSet<>();

    try {
      // Create the main platform
      createPlatform(level, origin, modifiedPositions);

      // Create the outer ring with pillars
      createPillars(level, origin, random, modifiedPositions);

      // Add spore hazards and environmental details
      addHazards(level, origin, random, modifiedPositions);

      return true;
    } catch (Exception e) {
      // Clean up if something goes wrong
      for (BlockPos pos : modifiedPositions) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
      }
      throw new RuntimeException("Failed to generate Spore Tyrant arena", e);
    }
  }

  private BlockPos findSurface(WorldGenLevel level, BlockPos pos) {
    // Find a suitable surface position
    int surfaceY = level.getHeight() - 1;
    while (surfaceY > level.getMinBuildHeight() + 10) {
      BlockPos checkPos = new BlockPos(pos.getX(), surfaceY, pos.getZ());
      if (!level.isEmptyBlock(checkPos)) {
        return checkPos.above(5); // Start a bit above the surface
      }
      surfaceY--;
    }
    return null;
  }

  private void createPlatform(
      WorldGenLevel level, BlockPos center, Set<BlockPos> modifiedPositions) {
    // Create the main circular platform
    for (int x = -ARENA_RADIUS; x <= ARENA_RADIUS; x++) {
      for (int z = -ARENA_RADIUS; z <= ARENA_RADIUS; z++) {
        double distance = Math.sqrt(x * x + z * z);
        if (distance <= PLATFORM_RADIUS) {
          // Main platform
          BlockPos pos = center.offset(x, 0, z);
          setBlock(level, pos, FLOOR_BLOCK, modifiedPositions);

          // Add some variation to the floor
          if (level.getRandom().nextFloat() < 0.1f) {
            setBlock(level, pos.below(), PILLAR_BLOCK, modifiedPositions);
          }
        } else if (distance <= ARENA_RADIUS) {
          // Outer ring with gaps
          if (level.getRandom().nextFloat() < 0.7f) {
            BlockPos pos = center.offset(x, 0, z);
            setBlock(level, pos, FLOOR_BLOCK, modifiedPositions);

            // Add supports below
            for (int y = -1; y > -ARENA_HEIGHT; y--) {
              BlockPos supportPos = pos.offset(0, y, 0);
              if (!level.isEmptyBlock(supportPos)) {
                break;
              }
              setBlock(level, supportPos, PILLAR_BLOCK, modifiedPositions);
            }
          }
        }
      }
    }
  }

  private void createPillars(
      WorldGenLevel level, BlockPos center, RandomSource random, Set<BlockPos> modifiedPositions) {
    // Create pillars around the arena
    for (int i = 0; i < PILLAR_COUNT; i++) {
      double angle = (2 * Math.PI * i) / PILLAR_COUNT;
      int x = (int) (Math.cos(angle) * (PLATFORM_RADIUS + 8));
      int z = (int) (Math.sin(angle) * (PLATFORM_RADIUS + 8));

      // Create a pillar
      for (int y = 0; y < PILLAR_HEIGHT; y++) {
        // Add some variation to pillar shapes
        if (y % 3 == 0) {
          // Add some horizontal extensions
          for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (random.nextFloat() < 0.3f) {
              BlockPos extension = center.offset(x, y, z).relative(direction);
              setBlock(level, extension, PILLAR_BLOCK, modifiedPositions);
            }
          }
        }

        // Main pillar blocks
        BlockPos pillarPos = center.offset(x, y, z);
        setBlock(level, pillarPos, PILLAR_BLOCK, modifiedPositions);

        // Add spore emitters to some pillars
        if (y == PILLAR_HEIGHT - 1 && random.nextFloat() < 0.7f) {
          setBlock(level, pillarPos.above(), SPORE_EMITTER, modifiedPositions);
        }
      }
    }
  }

  private void addHazards(
      WorldGenLevel level, BlockPos center, RandomSource random, Set<BlockPos> modifiedPositions) {
    // Add spore clouds in some areas
    for (int i = 0; i < 15; i++) {
      double angle = random.nextDouble() * 2 * Math.PI;
      double distance = random.nextDouble() * (PLATFORM_RADIUS - 5);
      int x = (int) (Math.cos(angle) * distance);
      int z = (int) (Math.sin(angle) * distance);

      // Create a small cloud of spore blocks
      for (int dx = -1; dx <= 1; dx++) {
        for (int dy = 0; dy <= 2; dy++) {
          for (int dz = -1; dz <= 1; dz++) {
            if (random.nextFloat() < 0.7f) {
              BlockPos cloudPos = center.offset(x + dx, dy, z + dz);
              if (level.isEmptyBlock(cloudPos)) {
                setBlock(level, cloudPos, SPORE_CLOUD, modifiedPositions);
              }
            }
          }
        }
      }
    }

    // Add some decorative spore clusters
    for (int i = 0; i < 20; i++) {
      double angle = random.nextDouble() * 2 * Math.PI;
      double distance = random.nextDouble() * PLATFORM_RADIUS;
      int x = (int) (Math.cos(angle) * distance);
      int z = (int) (Math.sin(angle) * distance);

      BlockPos clusterPos = center.offset(x, 0, z);
      createSporeCluster(level, clusterPos, random, modifiedPositions);
    }
  }

  private void createSporeCluster(
      WorldGenLevel level, BlockPos pos, RandomSource random, Set<BlockPos> modifiedPositions) {
    // Find the top of the platform
    while (level.isEmptyBlock(pos) && pos.getY() > level.getMinBuildHeight()) {
      pos = pos.below();
    }
    pos = pos.above();

    // Create a small cluster of spore blocks
    int clusterSize = 2 + random.nextInt(3);
    for (int i = 0; i < clusterSize; i++) {
      BlockPos clusterPos =
          pos.offset(random.nextInt(3) - 1, random.nextInt(2), random.nextInt(3) - 1);

      if (level.isEmptyBlock(clusterPos)) {
        setBlock(level, clusterPos, SPORE_CLOUD, modifiedPositions);
      }
    }
  }

  private void setBlock(
      WorldGenLevel level, BlockPos pos, BlockState state, Set<BlockPos> modifiedPositions) {
    if (level.isInWorldBounds(pos)) {
      level.setBlock(pos, state, 3);
      modifiedPositions.add(pos.immutable());
    }
  }

  /** Checks if a position is within the arena bounds. */
  public static boolean isInArena(BlockPos center, BlockPos pos) {
    int dx = pos.getX() - center.getX();
    int dz = pos.getZ() - center.getZ();
    double distanceSq = dx * dx + dz * dz;
    return distanceSq <= ARENA_RADIUS * ARENA_RADIUS;
  }

  /** Gets the bounding box of the arena. */
  public static AABB getArenaBounds(BlockPos center) {
    return new AABB(
        center.offset(-ARENA_RADIUS, -ARENA_HEIGHT, -ARENA_RADIUS),
        center.offset(ARENA_RADIUS, ARENA_HEIGHT, ARENA_RADIUS));
  }
}
