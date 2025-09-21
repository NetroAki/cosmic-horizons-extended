package com.netroaki.chex.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

public abstract class CHEXChunkGenerator extends ChunkGenerator {

  public static final Codec<CHEXChunkGenerator> CODEC =
      RecordCodecBuilder.create(
          (instance) ->
              instance
                  .group(
                      BiomeSource.CODEC
                          .fieldOf("biome_source")
                          .forGetter(ChunkGenerator::getBiomeSource))
                  .apply(instance, CHEXChunkGenerator::create));

  protected CHEXChunkGenerator(BiomeSource biomeSource) {
    super(biomeSource);
  }

  protected static CHEXChunkGenerator create(BiomeSource biomeSource) {
    throw new UnsupportedOperationException(
        "CHEXChunkGenerator base factory is unused; no default generator.");
  }

  @Override
  public int getMinY() {
    return -64;
  }

  @Override
  public int getGenDepth() {
    return 384;
  }

  @Override
  public int getSeaLevel() {
    return 63;
  }

  @Override
  public void spawnOriginalMobs(WorldGenRegion region) {
    // Custom generators handle their own mob spawning
  }

  @Override
  protected Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  @Override
  public void applyCarvers(
      WorldGenRegion region,
      long seed,
      RandomState randomState,
      BiomeManager biomeManager,
      StructureManager structureManager,
      ChunkAccess chunk,
      GenerationStep.Carving step) {
    // Custom generators handle their own carving
  }

  @Override
  public void buildSurface(
      WorldGenRegion region,
      StructureManager structureManager,
      RandomState randomState,
      ChunkAccess chunk) {
    // Custom generators handle their own surface generation
  }

  @Override
  public void applyBiomeDecoration(
      WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
    // Custom generators handle their own decoration
  }

  @Override
  public CompletableFuture<ChunkAccess> fillFromNoise(
      Executor executor,
      Blender blender,
      RandomState randomState,
      StructureManager structureManager,
      ChunkAccess chunk) {
    ChunkAccess result = generateCustomChunk(chunk, randomState);
    return CompletableFuture.completedFuture(result);
  }

  @Override
  public int getBaseHeight(
      int x,
      int z,
      Heightmap.Types heightmapType,
      LevelHeightAccessor level,
      RandomState randomState) {
    return getCustomHeight(x, z, heightmapType);
  }

  @Override
  public NoiseColumn getBaseColumn(
      int x, int z, LevelHeightAccessor level, RandomState randomState) {
    return new NoiseColumn(level.getMinBuildHeight(), new BlockState[0]);
  }

  @Override
  public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
    info.add("CHEX Custom Generator");
  }

  // Abstract methods to be implemented by subclasses
  protected abstract ChunkAccess generateCustomChunk(ChunkAccess chunk, RandomState randomState);

  protected abstract int getCustomHeight(int x, int z, Heightmap.Types heightmapType);
}
