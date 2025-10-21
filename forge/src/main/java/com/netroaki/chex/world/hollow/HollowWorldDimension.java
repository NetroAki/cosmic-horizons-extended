package com.netroaki.chex.world.hollow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.lighting.LightLayer;
import net.minecraftforge.common.util.ITeleporter;

public class HollowWorldDimension {
  public static final ResourceKey<Level> HOLLOW_WORLD =
      ResourceKey.create(
          Registries.DIMENSION, new ResourceLocation("cosmic_horizons_extended", "hollow_world"));

  public static final ResourceKey<LevelStem> HOLLOW_WORLD_STEM =
      ResourceKey.create(
          Registries.LEVEL_STEM, new ResourceLocation("cosmic_horizons_extended", "hollow_world"));

  public static final ResourceKey<DimensionType> HOLLOW_WORLD_TYPE =
      ResourceKey.create(
          Registries.DIMENSION_TYPE,
          new ResourceLocation("cosmic_horizons_extended", "hollow_world"));

  public static void register() {
    // Registration handled by Forge registry
  }

  public static class HollowWorldTeleporter implements ITeleporter {
    private final ServerLevel level;

    public HollowWorldTeleporter(ServerLevel level) {
      this.level = level;
    }

    @Override
    public Entity placeEntity(
        Entity entity,
        ServerLevel currentWorld,
        ServerLevel destWorld,
        float yaw,
        Function<Boolean, Entity> repositionEntity) {
      Entity newEntity = repositionEntity.apply(false);

      // Find a suitable spawn position
      BlockPos spawnPos = findSpawnPosition(destWorld);

      // Set position and rotation
      newEntity.moveTo(
          spawnPos.getX() + 0.5,
          spawnPos.getY() + 1.0,
          spawnPos.getZ() + 0.5,
          yaw,
          newEntity.getXRot());

      // Apply initial effects if needed
      if (newEntity instanceof net.minecraft.world.entity.player.Player) {
        // Add any initial effects for players entering the Hollow World
      }

      return newEntity;
    }

    private BlockPos findSpawnPosition(ServerLevel level) {
      // Start at world spawn
      BlockPos spawnPos = level.getSharedSpawnPos();

      // Find a suitable surface position
      return new BlockPos(
          spawnPos.getX(),
          level.getHeight(Heightmap.Types.WORLD_SURFACE, spawnPos.getX(), spawnPos.getZ()) + 1,
          spawnPos.getZ());
    }
  }

  public static class HollowWorldChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<HollowWorldChunkGenerator> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        BiomeSource.CODEC
                            .fieldOf("biome_source")
                            .forGetter(generator -> generator.biomeSource),
                        Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
                        NoiseGeneratorSettings.CODEC
                            .fieldOf("settings")
                            .forGetter(generator -> generator.settings))
                    .apply(instance, instance.stable(HollowWorldChunkGenerator::new)));

    public HollowWorldChunkGenerator(
        BiomeSource biomeSource, long seed, Holder<NoiseGeneratorSettings> settings) {
      super(biomeSource, seed, settings);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
      return CODEC;
    }
  }

  public static class HollowWorldBiomeSource extends BiomeSource {
    public static final Codec<HollowWorldBiomeSource> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed))
                    .apply(instance, instance.stable(HollowWorldBiomeSource::new)));

    private final long seed;

    public HollowWorldBiomeSource(long seed) {
      super(
          Stream.of(
                  HollowWorldBiomes.BIOLUMINESCENT_CAVERNS,
                  HollowWorldBiomes.VOID_CHASMS,
                  HollowWorldBiomes.CRYSTAL_GROVES,
                  HollowWorldBiomes.STALACTITE_FOREST,
                  HollowWorldBiomes.SUBTERRANEAN_RIVERS)
              .map(key -> key.registry().getHolderOrThrow(key))
              .toList());

      this.seed = seed;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
      return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
      // Delegate to our biome provider
      return HollowWorldBiomeProvider.INSTANCE.getNoiseBiome(x, y, z, sampler);
    }
  }

  // Helper method to get the light level at a specific position, considering the inner sun
  public static int getLightLevel(Level level, BlockPos pos) {
    if (level.dimension() != HOLLOW_WORLD) {
      return level.getBrightness(LightLayer.SKY, pos);
    }

    // Get base light level from the inner sun
    float sunIntensity = InnerSunManager.getSunIntensity(level, pos);
    int sunLight = (int) (15 * sunIntensity);

    // Get block light (torches, etc.)
    int blockLight = level.getBrightness(LightLayer.BLOCK, pos);

    // Return the higher of the two light levels
    return Math.max(sunLight, blockLight);
  }

  // Helper method to get the sky color for the Hollow World
  public static int getSkyColor(Level level, BlockPos pos) {
    if (level.dimension() != HOLLOW_WORLD) {
      return 0; // Default sky color
    }

    // Calculate distance from the center (0,0,0)
    double distance =
        Math.sqrt(pos.getX() * pos.getX() + pos.getY() * pos.getY() + pos.getZ() * pos.getZ());
    double normalizedDistance = Mth.clamp(distance / 1000.0, 0.0, 1.0);

    // Calculate sun intensity
    float sunIntensity = InnerSunManager.getSunIntensity(level, pos);

    // Base color (dark blue/purple for the cavern)
    int r = 10;
    int g = 5;
    int b = 20;

    // Add sun color (orange/yellow) based on intensity
    r += (int) (40 * sunIntensity * (1.0 - normalizedDistance));
    g += (int) (30 * sunIntensity * (1.0 - normalizedDistance));

    return (r << 16) | (g << 8) | b;
  }

  // Helper method to get the fog color for the Hollow World
  public static int getFogColor(Level level, BlockPos pos) {
    if (level.dimension() != HOLLOW_WORLD) {
      return 0; // Default fog color
    }

    // Similar to sky color but slightly darker
    int skyColor = getSkyColor(level, pos);
    int r = (skyColor >> 16) & 0xFF;
    int g = (skyColor >> 8) & 0xFF;
    int b = skyColor & 0xFF;

    // Darken the color for fog
    r = (int) (r * 0.8);
    g = (int) (g * 0.8);
    b = (int) (b * 0.9);

    return (r << 16) | (g << 8) | b;
  }
}
