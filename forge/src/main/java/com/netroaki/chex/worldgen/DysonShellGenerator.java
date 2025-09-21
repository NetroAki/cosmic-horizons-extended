package com.netroaki.chex.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

public class DysonShellGenerator extends CHEXChunkGenerator {

  public static final Codec<DysonShellGenerator> CODEC =
      RecordCodecBuilder.create(
          (instance) ->
              instance
                  .group(
                      BiomeSource.CODEC
                          .fieldOf("biome_source")
                          .forGetter(ChunkGenerator::getBiomeSource))
                  .apply(instance, DysonShellGenerator::new));

  private static final int SHELL_RADIUS = 500; // Radius of the Dyson shell
  private static final int SHELL_THICKNESS = 20; // Thickness of the shell
  private static final int SURFACE_HEIGHT = 100; // Height of the inner surface

  public DysonShellGenerator(BiomeSource biomeSource) {
    super(biomeSource);
  }

  @Override
  protected Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  @Override
  protected ChunkAccess generateCustomChunk(ChunkAccess chunk, RandomState randomState) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    // Generate the Dyson shell structure
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        int worldX = chunk.getPos().getMinBlockX() + x;
        int worldZ = chunk.getPos().getMinBlockZ() + z;

        // Calculate distance from center
        double distance = Math.sqrt(worldX * worldX + worldZ * worldZ);

        // Check if we're within the shell
        if (distance <= SHELL_RADIUS) {
          // Generate shell surface
          int surfaceY = SURFACE_HEIGHT;

          // Set surface blocks
          pos.set(worldX, surfaceY, worldZ);
          chunk.setBlockState(pos, Blocks.GRASS_BLOCK.defaultBlockState(), false);

          // Set some dirt below
          for (int y = surfaceY - 1; y >= surfaceY - 3; y--) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, Blocks.DIRT.defaultBlockState(), false);
          }

          // Set stone foundation
          for (int y = surfaceY - 4; y >= surfaceY - SHELL_THICKNESS; y--) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, Blocks.STONE.defaultBlockState(), false);
          }

          // Add some solar panel structures
          if ((worldX + worldZ) % 15 == 0) {
            pos.set(worldX, surfaceY + 1, worldZ);
            chunk.setBlockState(pos, Blocks.IRON_BLOCK.defaultBlockState(), false);
          }
        } else {
          // Outside the shell - void
          for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);
          }
        }
      }
    }

    return chunk;
  }

  @Override
  protected int getCustomHeight(int x, int z, Heightmap.Types heightmapType) {
    double distance = Math.sqrt(x * x + z * z);

    if (distance <= SHELL_RADIUS) {
      return SURFACE_HEIGHT;
    }

    return 0; // Void
  }
}
