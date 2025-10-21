package com.netroaki.chex.world.ringworld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public class RingworldBiomeProvider extends BiomeSource {
  public static final Codec<RingworldBiomeProvider> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.LONG.fieldOf("seed").stable().forGetter(provider -> provider.seed),
                      Codec.list(ResourceKey.codec(Registries.BIOME))
                          .fieldOf("biomes")
                          .forGetter(provider -> provider.biomes))
                  .apply(instance, RingworldBiomeProvider::new));

  private static final int BIOME_SIZE = 4;
  private static final int BIOME_DEPTH = 1;

  private final long seed;
  private final List<ResourceKey<Biome>> biomes;
  private final ImprovedNoise temperatureNoise;
  private final ImprovedNoise humidityNoise;
  private final ImprovedNoise altitudeNoise;
  private final ImprovedNoise weirdnessNoise;
  private final Climate.Sampler sampler;

  public RingworldBiomeProvider(long seed, List<ResourceKey<Biome>> biomes) {
    super(
        biomes.stream()
            .map(key -> () -> (Biome) null) // Placeholder, will be filled by registry
            .map(Holder::direct)
            .toList());

    this.seed = seed;
    this.biomes = biomes;

    WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(seed));
    this.temperatureNoise = ImprovedNoise.create(random, -3, 1.0, 1.0);
    this.humidityNoise = ImprovedNoise.create(random, -3, 1.0, 1.0);
    this.altitudeNoise = ImprovedNoise.create(random, 0, 1.0, 1.0);
    this.weirdnessNoise = ImprovedNoise.create(random, 0, 1.0, 1.0);

    this.sampler =
        new Climate.Sampler(
            new DensityFunction() {
              @Override
              public double compute(Function<DensityFunction, Double> function) {
                return 0;
              }

              @Override
              public void fillArray(double[] ds, ContextProvider contextProvider) {
                for (int i = 0; i < ds.length; ++i) {
                  ds[i] = 0.0;
                }
              }

              @Override
              public DensityFunction mapAll(Visitor visitor) {
                return this;
              }

              @Override
              public double minValue() {
                return 0.0;
              }

              @Override
              public double maxValue() {
                return 0.0;
              }

              @Override
              public Codec<? extends DensityFunction> codec() {
                return null;
              }
            },
            new DensityFunction() {
              @Override
              public double compute(Function<DensityFunction, Double> function) {
                return 0;
              }

              @Override
              public void fillArray(double[] ds, ContextProvider contextProvider) {
                for (int i = 0; i < ds.length; ++i) {
                  ds[i] = 0.0;
                }
              }

              @Override
              public DensityFunction mapAll(Visitor visitor) {
                return this;
              }

              @Override
              public double minValue() {
                return 0.0;
              }

              @Override
              public double maxValue() {
                return 0.0;
              }

              @Override
              public Codec<? extends DensityFunction> codec() {
                return null;
              }
            },
            new DensityFunction() {
              @Override
              public double compute(Function<DensityFunction, Double> function) {
                return 0;
              }

              @Override
              public void fillArray(double[] ds, ContextProvider contextProvider) {
                for (int i = 0; i < ds.length; ++i) {
                  ds[i] = 0.0;
                }
              }

              @Override
              public DensityFunction mapAll(Visitor visitor) {
                return this;
              }

              @Override
              public double minValue() {
                return 0.0;
              }

              @Override
              public double maxValue() {
                return 0.0;
              }

              @Override
              public Codec<? extends DensityFunction> codec() {
                return null;
              }
            },
            new DensityFunction() {
              @Override
              public double compute(Function<DensityFunction, Double> function) {
                return 0;
              }

              @Override
              public void fillArray(double[] ds, ContextProvider contextProvider) {
                for (int i = 0; i < ds.length; ++i) {
                  ds[i] = 0.0;
                }
              }

              @Override
              public DensityFunction mapAll(Visitor visitor) {
                return this;
              }

              @Override
              public double minValue() {
                return 0.0;
              }

              @Override
              public double maxValue() {
                return 0.0;
              }

              @Override
              public Codec<? extends DensityFunction> codec() {
                return null;
              }
            },
            new DensityFunction() {
              @Override
              public double compute(Function<DensityFunction, Double> function) {
                return 0;
              }

              @Override
              public void fillArray(double[] ds, ContextProvider contextProvider) {
                for (int i = 0; i < ds.length; ++i) {
                  ds[i] = 0.0;
                }
              }

              @Override
              public DensityFunction mapAll(Visitor visitor) {
                return this;
              }

              @Override
              public double minValue() {
                return 0.0;
              }

              @Override
              public double maxValue() {
                return 0.0;
              }

              @Override
              public Codec<? extends DensityFunction> codec() {
                return null;
              }
            },
            new DensityFunction() {
              @Override
              public double compute(Function<DensityFunction, Double> function) {
                return 0;
              }

              @Override
              public void fillArray(double[] ds, ContextProvider contextProvider) {
                for (int i = 0; i < ds.length; ++i) {
                  ds[i] = 0.0;
                }
              }

              @Override
              public DensityFunction mapAll(Visitor visitor) {
                return this;
              }

              @Override
              public double minValue() {
                return 0.0;
              }

              @Override
              public double maxValue() {
                return 0.0;
              }

              @Override
              public Codec<? extends DensityFunction> codec() {
                return null;
              }
            });
  }

  @Override
  protected Codec<? extends BiomeSource> codec() {
    return CODEC;
  }

  @Override
  public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler sampler) {
    // Get the biome based on the position
    int i = QuartPos.toBlock(x);
    int j = QuartPos.toBlock(y);
    int k = QuartPos.toBlock(z);

    // Calculate distance from the center of the ring
    double dist = Math.sqrt(i * i + k * k);

    // Normalize distance to 0-1 range (0 = center, 1 = edge)
    double normDist = dist / RingworldGravity.RING_RADIUS;

    // Edge biome (last 10% of the ring)
    if (normDist > 0.9) {
      return getBiomeHolder(RingworldBiomes.RING_EDGE);
    }

    // Structural biome (first 5% of the ring)
    if (normDist < 0.05) {
      return getBiomeHolder(RingworldBiomes.RING_STRUCTURAL);
    }

    // Use noise to determine biome type
    double temp = temperatureNoise.noise(i * 0.01, k * 0.01, false) * 0.5 + 0.5;
    double humidity = humidityNoise.noise(i * 0.01, k * 0.01, true) * 0.5 + 0.5;

    // River biomes (based on humidity and distance from center)
    if (humidity > 0.7 && normDist > 0.2 && normDist < 0.8) {
      return getBiomeHolder(RingworldBiomes.RING_RIVER);
    }

    // Mountain biomes (based on noise and distance from center)
    if (altitudeNoise.noise(i * 0.05, k * 0.05, false) > 0.6 && normDist > 0.3 && normDist < 0.7) {
      return getBiomeHolder(RingworldBiomes.RING_MOUNTAINS);
    }

    // Forest biomes (based on temperature and humidity)
    if (temp > 0.5 && humidity > 0.5) {
      return getBiomeHolder(RingworldBiomes.RING_FOREST);
    }

    // Default to plains biome
    return getBiomeHolder(RingworldBiomes.RING_PLAINS);
  }

  private Holder<Biome> getBiomeHolder(ResourceKey<Biome> key) {
    return this.biomes.stream()
        .filter(key::equals)
        .findFirst()
        .flatMap(k -> this.possibleBiomes().stream().filter(h -> h.is(k)).findFirst())
        .orElse(this.possibleBiomes().get(0)); // Fallback to first biome
  }

  @Override
  protected Stream<Holder<Biome>> collectPossibleBiomes() {
    return this.possibleBiomes().stream();
  }

  @Override
  public void addDebugInfo(List<String> list, BlockPos pos, Sampler sampler) {
    int i = QuartPos.fromBlock(pos.getX());
    int j = QuartPos.fromBlock(pos.getY());
    int k = QuartPos.fromBlock(pos.getZ());
    list.add("Ringworld Biome Builder");
    list.add(
        "Biome: "
            + this.getNoiseBiome(i, j, k, sampler)
                .unwrapKey()
                .map(key -> key.location().toString())
                .orElse("[unregistered]"));
  }

  @Override
  public Climate.Sampler climateSampler() {
    return this.sampler;
  }

  @Override
  public void applyCarvers(
      WorldGenLevel p_256133_,
      long p_255955_,
      RandomSource p_256596_,
      BiomeManager p_256470_,
      StructureManager p_256181_,
      ChunkAccess p_256530_,
      GenerationStep.Carving p_256004_) {
    // No-op for now
  }

  @Override
  public void buildSurface(
      WorldGenRegion p_256592_,
      StructureManager p_256411_,
      RandomState p_256197_,
      ChunkAccess p_256018_) {
    // No-op for now
  }

  @Override
  public void spawnOriginalMobs(WorldGenLevel p_256230_) {
    // No-op for now
  }

  @Override
  public int getGenDepth() {
    return 256;
  }

  @Override
  public int getSeaLevel() {
    return 63;
  }

  @Override
  public int getMinY() {
    return 0;
  }

  @Override
  public int getBaseHeight(
      int p_223045_,
      int p_223046_,
      Heightmap.Types p_223047_,
      LevelHeightAccessor p_223048_,
      RandomState p_223049_) {
    return 0;
  }

  @Override
  public NoiseColumn getBaseColumn(
      int p_223040_, int p_223041_, LevelHeightAccessor p_223042_, RandomState p_223043_) {
    return new NoiseColumn(0, new BlockState[0]);
  }

  @Override
  public void applyBiomeDecoration(
      WorldGenLevel p_256596_, ChunkAccess p_255682_, StructureManager p_255920_) {
    // No-op for now
  }

  public static class RingworldBiomeProviderType
      implements BiomeSourceType<RingworldBiomeProvider> {
    @Override
    public Codec<RingworldBiomeProvider> codec() {
      return RingworldBiomeProvider.CODEC;
    }
  }
}
