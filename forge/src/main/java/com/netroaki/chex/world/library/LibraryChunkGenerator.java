package com.netroaki.chex.world.library;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.CHEXStructures;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.jetbrains.annotations.NotNull;

public class LibraryChunkGenerator extends ChunkGenerator {
  public static final Codec<LibraryChunkGenerator> CODEC =
      RecordCodecBuilder.create(
          instance ->
              commonCodec(instance)
                  .and(
                      instance.group(
                          BiomeSource.CODEC
                              .fieldOf("biome_source")
                              .forGetter(generator -> generator.biomeSource),
                          NoiseGeneratorSettings.CODEC
                              .fieldOf("settings")
                              .forGetter(generator -> generator.settings)))
                  .apply(instance, instance.stable(LibraryChunkGenerator::new)));

  private static final BlockState AIR = Blocks.AIR.defaultBlockState();
  private static final BlockState BEDROCK = Blocks.BEDROCK.defaultBlockState();
  private static final BlockState BOOKSHELF = Blocks.BOOKSHELF.defaultBlockState();
  private static final BlockState OAK_PLANKS = Blocks.OAK_PLANKS.defaultBlockState();
  private static final BlockState OAK_STAIRS = Blocks.OAK_STAIRS.defaultBlockState();

  private final Holder<NoiseGeneratorSettings> settings;
  private final PerlinSimplexNoise roomNoise;
  private final PerlinSimplexNoise detailNoise;

  public LibraryChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
    this(biomeSource, settings, new RandomSource.XoroshiroRandomSource(0));
  }

  public LibraryChunkGenerator(
      BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, long seed) {
    super(
        new StructureSet.ListBuilder().add(CHEXStructures.LIBRARY_ROOMS).build(),
        Optional.empty(),
        biomeSource,
        biomeSource,
        seed);
    this.settings = settings;
    this.roomNoise = new PerlinSimplexNoise(new LegacyRandomSource(seed), List.of(0));
    this.detailNoise = new PerlinSimplexNoise(new LegacyRandomSource(seed + 1), List.of(0));
  }

  @Override
  protected @NotNull Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  @Override
  public void applyCarvers(
      WorldGenRegion level,
      long seed,
      RandomState randomState,
      BiomeManager biomeManager,
      StructureManager structureManager,
      ChunkAccess chunk,
      GenerationStep.@NotNull Carving step) {
    // No carvers in the library
  }

  @Override
  public void buildSurface(
      WorldGenRegion level,
      StructureManager structureManager,
      RandomState randomState,
      ChunkAccess chunk) {
    // Custom surface building for the library
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    ChunkPos chunkPos = chunk.getPos();

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        int worldX = chunkPos.getMinBlockX() + x;
        int worldZ = chunkPos.getMinBlockZ() + z;

        // Generate library floor and ceiling
        for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
          pos.set(worldX, y, worldZ);

          // Base floor and ceiling
          if (y == 0) {
            chunk.setBlockState(pos, BEDROCK, false);
          } else if (y == 1) {
            chunk.setBlockState(pos, OAK_PLANKS, false);
          } else if (y == 5) {
            chunk.setBlockState(pos, OAK_PLANKS, false);
          } else if (y == 6) {
            chunk.setBlockState(pos, BEDROCK, false);
          } else if (y > 1 && y < 5) {
            // Walls and bookshelves
            if (x == 0 || x == 15 || z == 0 || z == 15) {
              chunk.setBlockState(pos, BOOKSHELF, false);
            } else if ((x % 4 == 0 || z % 4 == 0) && y == 2) {
              chunk.setBlockState(pos, OAK_STAIRS, false);
            }
          }
        }
      }
    }
  }

  @Override
  public void spawnOriginalMobs(WorldGenRegion level) {
    // No passive mobs in the library
  }

  @Override
  public int getGenDepth() {
    return 256;
  }

  @Override
  public CompletableFuture<ChunkAccess> fillFromNoise(
      Executor executor,
      Blender blender,
      RandomState randomState,
      StructureManager structureManager,
      ChunkAccess chunk) {
    // Fill with air by default, buildSurface will handle the rest
    return CompletableFuture.completedFuture(chunk);
  }

  @Override
  public int getSeaLevel() {
    return 0; // No water in the library
  }

  @Override
  public int getMinY() {
    return 0;
  }

  @Override
  public int getBaseHeight(
      int x, int z, Heightmap.Types heightmap, LevelHeightAccessor level, RandomState randomState) {
    return 0;
  }

  @Override
  public NoiseColumn getBaseColumn(
      int x, int z, LevelHeightAccessor level, RandomState randomState) {
    return new NoiseColumn(0, new BlockState[0]);
  }

  @Override
  public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos pos) {
    // Add debug info if needed
  }

  @Override
  public void applyBiomeDecoration(
      WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
    // Handle structure generation
    super.applyBiomeDecoration(level, chunk, structureManager);

    // Add custom decoration logic here
    if (chunk instanceof LevelChunk levelChunk) {
      generateRoom(level, levelChunk);
    }
  }

  private void generateRoom(WorldGenLevel level, LevelChunk chunk) {
    ChunkPos chunkPos = chunk.getPos();
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    // Check if we should generate a special room
    double roomValue = roomNoise.getValue(chunkPos.x * 0.1, chunkPos.z * 0.1, false);

    if (roomValue > 0.7) {
      // Generate a reading nook
      generateReadingNook(level, chunkPos);
    } else if (roomValue < -0.7) {
      // Generate a study area
      generateStudyArea(level, chunkPos);
    }

    // Add some random bookshelves
    if (level.getRandom().nextFloat() < 0.3) {
      int x = chunkPos.getMinBlockX() + 4 + level.getRandom().nextInt(8);
      int z = chunkPos.getMinBlockZ() + 4 + level.getRandom().nextInt(8);

      if (level.getBlockState(pos.set(x, 2, z)).isAir()) {
        level.setBlock(pos, BOOKSHELF, 2);
      }
    }
  }

  private void generateReadingNook(WorldGenLevel level, ChunkPos chunkPos) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    int centerX = chunkPos.getMinBlockX() + 8;
    int centerZ = chunkPos.getMinBlockZ() + 8;

    // Create a circular reading nook
    for (int x = -3; x <= 3; x++) {
      for (int z = -3; z <= 3; z++) {
        if (x * x + z * z <= 9) { // Circle equation
          // Floor
          level.setBlock(
              pos.set(centerX + x, 1, centerZ + z), Blocks.OAK_PLANKS.defaultBlockState(), 2);

          // Chairs and tables
          if ((x == -2 || x == 2) && z == 0) {
            level.setBlock(
                pos.set(centerX + x, 2, centerZ + z), Blocks.OAK_STAIRS.defaultBlockState(), 2);
          } else if (x == 0 && (z == -2 || z == 2)) {
            level.setBlock(
                pos.set(centerX + x, 2, centerZ + z),
                Blocks.OAK_PRESSURE_PLATE.defaultBlockState(),
                2);
          }
        }
      }
    }
  }

  private void generateStudyArea(WorldGenLevel level, ChunkPos chunkPos) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    int centerX = chunkPos.getMinBlockX() + 8;
    int centerZ = chunkPos.getMinBlockZ() + 8;

    // Create a study area with a table and chairs
    for (int x = -2; x <= 2; x++) {
      for (int z = -4; z <= 4; z++) {
        // Table
        if (Math.abs(x) <= 1 && Math.abs(z) <= 3) {
          level.setBlock(
              pos.set(centerX + x, 1, centerZ + z), Blocks.OAK_PLANKS.defaultBlockState(), 2);

          if (x == 0 && z == 0) {
            // Add a book on the table
            level.setBlock(
                pos.set(centerX + x, 2, centerZ + z), Blocks.BOOKSHELF.defaultBlockState(), 2);
          }
        }

        // Chairs
        if ((x == -2 || x == 2) && (z == -2 || z == 0 || z == 2)) {
          level.setBlock(
              pos.set(centerX + x, 1, centerZ + z), Blocks.OAK_STAIRS.defaultBlockState(), 2);
        }
      }
    }
  }

  @Override
  public void createStructures(
      RegistryAccess registryAccess,
      ChunkGeneratorStructureState state,
      StructureManager structureManager,
      ChunkAccess chunk,
      StructureManager structureFeatureManager,
      BiomeManager biomeManager) {
    // Handle structure generation
    if (chunk.getPos().x % 10 == 0 && chunk.getPos().z % 10 == 0) {
      // Generate a special room at grid points
      generateSpecialRoom(chunk);
    }
  }

  private void generateSpecialRoom(ChunkAccess chunk) {
    // Implementation for special rooms like treasure rooms or puzzle rooms
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    ChunkPos chunkPos = chunk.getPos();

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        // Add special room features here
      }
    }
  }
}
