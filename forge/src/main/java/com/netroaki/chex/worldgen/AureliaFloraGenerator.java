package com.netroaki.chex.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class AureliaFloraGenerator extends Feature<AureliaFloraGenerator.Config> {

  public static final Codec<AureliaFloraGenerator.Config> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.intRange(0, 100)
                          .fieldOf("tries")
                          .orElse(64)
                          .forGetter(config -> config.tries),
                      Codec.intRange(1, 16)
                          .fieldOf("xz_spread")
                          .orElse(6)
                          .forGetter(config -> config.xzSpread),
                      Codec.intRange(1, 8)
                          .fieldOf("y_spread")
                          .orElse(1)
                          .forGetter(config -> config.ySpread))
                  .apply(instance, Config::new));

  public AureliaFloraGenerator(Codec<Config> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<Config> context) {
    WorldGenLevel level = context.level();
    BlockPos origin = context.origin();
    RandomSource random = context.random();
    Config config = context.config();

    boolean placed = false;

    for (int i = 0; i < config.tries; i++) {
      BlockPos pos =
          origin.offset(
              random.nextInt(config.xzSpread * 2 + 1) - config.xzSpread,
              random.nextInt(config.ySpread * 2 + 1) - config.ySpread,
              random.nextInt(config.xzSpread * 2 + 1) - config.xzSpread);

      // Find the top surface
      BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);

      if (canPlaceFlora(level, surfacePos)) {
        // Place different types of alien flora
        if (random.nextFloat() < 0.3f) {
          // Place alien grass
          if (level.getBlockState(surfacePos).isAir()
              && level.getBlockState(surfacePos.below()).is(Blocks.GRASS_BLOCK)) {
            level.setBlock(surfacePos, CHEXBlocks.AURELIAN_GRASS.get().defaultBlockState(), 2);
            placed = true;
          }
        } else if (random.nextFloat() < 0.1f) {
          // Place small alien tree
          placeAlienTree(level, surfacePos, random);
          placed = true;
        }
      }
    }

    return placed;
  }

  private boolean canPlaceFlora(WorldGenLevel level, BlockPos pos) {
    BlockState below = level.getBlockState(pos.below());
    return below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.DIRT) || below.is(Blocks.STONE);
  }

  private void placeAlienTree(WorldGenLevel level, BlockPos pos, RandomSource random) {
    int height = 3 + random.nextInt(3); // 3-5 blocks tall

    // Place trunk
    for (int i = 0; i < height; i++) {
      BlockPos trunkPos = pos.above(i);
      if (level.getBlockState(trunkPos).isAir()) {
        level.setBlock(trunkPos, CHEXBlocks.AURELIAN_LOG.get().defaultBlockState(), 2);
      }
    }

    // Place leaves
    BlockPos topPos = pos.above(height - 1);
    for (int x = -2; x <= 2; x++) {
      for (int z = -2; z <= 2; z++) {
        for (int y = 0; y <= 2; y++) {
          BlockPos leafPos = topPos.offset(x, y, z);
          if (level.getBlockState(leafPos).isAir()
              && (x * x + z * z + y * y) <= 4) { // Spherical-ish shape
            level.setBlock(leafPos, CHEXBlocks.AURELIAN_LEAVES.get().defaultBlockState(), 2);
          }
        }
      }
    }
  }

  public static class Config implements FeatureConfiguration {
    public final int tries;
    public final int xzSpread;
    public final int ySpread;

    public Config(int tries, int xzSpread, int ySpread) {
      this.tries = tries;
      this.xzSpread = xzSpread;
      this.ySpread = ySpread;
    }
  }
}
