package com.netroaki.chex.world.library.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.phys.Vec3;

/**
 * Generates a non-Euclidean space for the Infinite Library dimension. This creates a mind-bending
 * environment where space warps and folds in on itself.
 */
public class NonEuclideanSpaceGenerator extends ChunkGenerator {
  public static final Codec<NonEuclideanSpaceGenerator> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      BiomeSource.CODEC
                          .fieldOf("biome_source")
                          .forGetter(generator -> generator.biomeSource))
                  .apply(instance, instance.stable(NonEuclideanSpaceGenerator::new)));

  private static final BlockState AIR = Blocks.AIR.defaultBlockState();
  private static final BlockState BOOKSHELF = Blocks.BOOKSHELF.defaultBlockState();
  private static final BlockState OAK_PLANKS = Blocks.OAK_PLANKS.defaultBlockState();
  private static final BlockState GLASS_PANE = Blocks.GLASS_PANE.defaultBlockState();

  private final PerlinSimplexNoise warpNoise;
  private final PerlinSimplexNoise detailNoise;

  public NonEuclideanSpaceGenerator(BiomeSource biomeSource) {
    super(biomeSource);

    // Initialize noise generators for warping space
    Random random = new Random(0); // Fixed seed for consistency
    this.warpNoise = new PerlinSimplexNoise(random, List.of(0, 1, 2, 3));
    this.detailNoise = new PerlinSimplexNoise(random, List.of(0, 1, 2, 3));
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
    // No carvers in the library
  }

  @Override
  public void buildSurface(
      WorldGenRegion region,
      StructureManager structureManager,
      RandomState randomState,
      ChunkAccess chunk) {
    // No surface generation needed
  }

  @Override
  public int getBaseHeight(
      int x, int z, Heightmap.Types heightmap, LevelHeightAccessor level, RandomState randomState) {
    return 64; // Default height
  }

  @Override
  public void applyBiomeDecoration(
      WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
    // No biome decoration in the library
  }

  @Override
  public CompletableFuture<ChunkAccess> fillFromNoise(
      Executor executor,
      Blender blender,
      RandomState randomState,
      StructureManager structureManager,
      ChunkAccess chunk) {
    return CompletableFuture.supplyAsync(
        () -> {
          ChunkPos chunkPos = chunk.getPos();
          int chunkX = chunkPos.x;
          int chunkZ = chunkPos.z;

          // Generate the chunk with non-Euclidean warping
          for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
              // Get the world coordinates
              int worldX = (chunkX << 4) + x;
              int worldZ = (chunkZ << 4) + z;

              // Apply non-Euclidean transformation
              Vec3 warpedPos = warpSpace(worldX, 64, worldZ);

              // Generate library structure based on warped coordinates
              generateLibraryStructure(chunk, warpedPos.x, warpedPos.y, warpedPos.z);
            }
          }

          return chunk;
        },
        executor);
  }

  /** Warps 3D space to create non-Euclidean effects */
  private Vec3 warpSpace(double x, double y, double z) {
    // Scale factors for the warping effect
    double scale = 0.1;
    double intensity = 10.0;

    // Sample noise at different frequencies for organic warping
    double warpX = warpNoise.getValue(x * scale, y * scale, z * scale) * intensity;
    double warpY = warpNoise.getValue(y * scale, z * scale, x * scale) * intensity;
    double warpZ = warpNoise.getValue(z * scale, x * scale, y * scale) * intensity;

    // Apply Möbius strip-like twist
    double radius = Math.sqrt(x * x + z * z);
    double angle = Math.atan2(z, x);
    double twist = Math.sin(radius * 0.1) * Math.PI;

    // Apply the twist to the coordinates
    double twistedX = x * Math.cos(twist) - z * Math.sin(twist);
    double twistedZ = x * Math.sin(twist) + z * Math.cos(twist);

    // Combine warp and twist
    return new Vec3(twistedX + warpX, y + warpY, twistedZ + warpZ);
  }

  /** Generates library structures at the given warped coordinates */
  private void generateLibraryStructure(ChunkAccess chunk, double x, double y, double z) {
    // Convert to chunk-relative coordinates
    int blockX = (int) Math.floor(x) & 15;
    int blockZ = (int) Math.floor(z) & 15;
    int blockY = (int) Math.floor(y);

    // Generate floor
    if (blockY >= 0 && blockY < 256) {
      chunk.setBlockState(new BlockPos(blockX, blockY, blockZ), OAK_PLANKS, false);

      // Add bookshelves with some probability based on noise
      double shelfNoise = detailNoise.getValue(x * 0.1, y * 0.1, z * 0.1);
      if (shelfNoise > 0.7) {
        chunk.setBlockState(new BlockPos(blockX, blockY + 1, blockZ), BOOKSHELF, false);

        // Add glass panes on top of some bookshelves
        if (shelfNoise > 0.9) {
          chunk.setBlockState(new BlockPos(blockX, blockY + 2, blockZ), GLASS_PANE, false);
        }
      }
    }
  }

  @Override
  public void spawnOriginalMobs(WorldGenRegion region) {
    // No mob spawning in the library
  }

  @Override
  public int getMinY() {
    return 0;
  }

  @Override
  public int getGenDepth() {
    return 256;
  }

  @Override
  public int getSeaLevel() {
    return 0; // No water in the library
  }

  @Override
  public int getSpawnHeight(LevelHeightAccessor level) {
    return 64; // Default spawn height
  }

  @Override
  public void createReferences(
      WorldGenLevel level, StructureManager structureManager, ChunkAccess chunk) {
    // No structure references in the library
  }

  @Override
  public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor level) {
    return getBaseHeight(x, z, heightmap, level, null);
  }

  @Override
  public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level) {
    // Return a simple column of air with a floor at y=0
    BlockState[] states = new BlockState[level.getHeight()];
    states[0] = OAK_PLANKS;
    for (int y = 1; y < states.length; y++) {
      states[y] = AIR;
    }
    return new NoiseColumn(level.getMinBuildHeight(), states);
  }

  @Override
  public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos pos) {
    // Add debug information about the non-Euclidean space
    Vec3 warped = warpSpace(pos.getX(), pos.getY(), pos.getZ());
    list.add(String.format("Warped: %.2f, %.2f, %.2f", warped.x, warped.y, warped.z));
  }
}
