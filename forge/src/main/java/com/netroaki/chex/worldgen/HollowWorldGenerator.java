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

public class HollowWorldGenerator extends CHEXChunkGenerator {

  public static final Codec<HollowWorldGenerator> CODEC =
      RecordCodecBuilder.create(
          (instance) ->
              instance
                  .group(
                      BiomeSource.CODEC
                          .fieldOf("biome_source")
                          .forGetter(ChunkGenerator::getBiomeSource))
                  .apply(instance, HollowWorldGenerator::new));

  private static final int WORLD_RADIUS = 800; // Radius of the hollow world
  private static final int SHELL_THICKNESS = 50; // Thickness of the world shell
  private static final int INNER_SURFACE_HEIGHT = 100; // Height of the inner surface
  private static final int OUTER_SURFACE_HEIGHT = 200; // Height of the outer surface

  public HollowWorldGenerator(BiomeSource biomeSource) {
    super(biomeSource);
  }

  @Override
  protected Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  @Override
  protected ChunkAccess generateCustomChunk(ChunkAccess chunk, RandomState randomState) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    // Generate the hollow world structure
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        int worldX = chunk.getPos().getMinBlockX() + x;
        int worldZ = chunk.getPos().getMinBlockZ() + z;

        // Calculate distance from center
        double distance = Math.sqrt(worldX * worldX + worldZ * worldZ);

        // Check if we're within the world
        if (distance <= WORLD_RADIUS) {
          // Generate inner surface (bottom of the hollow world)
          int innerY = INNER_SURFACE_HEIGHT;

          // Set inner surface blocks
          pos.set(worldX, innerY, worldZ);
          chunk.setBlockState(pos, Blocks.GRASS_BLOCK.defaultBlockState(), false);

          // Set some dirt below
          for (int y = innerY - 1; y >= innerY - 3; y--) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, Blocks.DIRT.defaultBlockState(), false);
          }

          // Set stone foundation
          for (int y = innerY - 4; y >= innerY - SHELL_THICKNESS; y--) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, Blocks.STONE.defaultBlockState(), false);
          }

          // Generate outer surface (top of the hollow world)
          int outerY = OUTER_SURFACE_HEIGHT;

          // Set outer surface blocks
          pos.set(worldX, outerY, worldZ);
          chunk.setBlockState(pos, Blocks.GRASS_BLOCK.defaultBlockState(), false);

          // Set some dirt above
          for (int y = outerY + 1; y <= outerY + 3; y++) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, Blocks.DIRT.defaultBlockState(), false);
          }

          // Set stone foundation
          for (int y = outerY + 4; y <= outerY + SHELL_THICKNESS; y++) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, Blocks.STONE.defaultBlockState(), false);
          }

          // Add some variation
          if ((worldX + worldZ) % 25 == 0) {
            pos.set(worldX, innerY + 1, worldZ);
            chunk.setBlockState(pos, Blocks.GRASS.defaultBlockState(), false);
          }
        } else {
          // Outside the world - void
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

    if (distance <= WORLD_RADIUS) {
      return OUTER_SURFACE_HEIGHT; // Return the outer surface height
    }

    return 0; // Void
  }
}
