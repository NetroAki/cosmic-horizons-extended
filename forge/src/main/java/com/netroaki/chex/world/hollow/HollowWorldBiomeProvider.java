package com.netroaki.chex.world.hollow;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public class HollowWorldBiomeProvider extends BiomeSource {
  public static final Codec<HollowWorldBiomeProvider> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(Codec.LONG.fieldOf("seed").stable().forGetter(provider -> provider.seed))
                  .apply(instance, instance.stable(HollowWorldBiomeProvider::new)));

  private static final float BIOME_SCALE = 0.1f;
  private static final float BIOME_DEPTH_WEIGHT = 0.2f;
  private static final float RIVER_THRESHOLD = 0.1f;
  private static final float VOID_CHASM_THRESHOLD = 0.7f;
  private static final float CRYSTAL_GROVE_THRESHOLD = 0.6f;
  private static final float STALACTITE_FOREST_THRESHOLD = 0.5f;

  private final long seed;
  private final ImprovedNoise temperatureNoise;
  private final ImprovedNoise humidityNoise;
  private final ImprovedNoise altitudeNoise;
  private final Climate.Sampler sampler;

  public HollowWorldBiomeProvider(long seed) {
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
    this.temperatureNoise = new ImprovedNoise(new LegacyRandomSource(seed));
    this.humidityNoise = new ImprovedNoise(new LegacyRandomSource(seed + 1));
    this.altitudeNoise = new ImprovedNoise(new LegacyRandomSource(seed + 2));

    this.sampler =
        new Climate.Sampler(
            (x, y, z) -> 0.0, // Temperature (not used for biome selection)
            (x, y, z) -> 0.0, // Humidity (not used for biome selection)
            (x, y, z) -> 0.0, // Continentalness (not used for biome selection)
            (x, y, z) -> 0.0, // Erosion (not used for biome selection)
            (x, y, z) -> 0.0, // Depth (not used for biome selection)
            (x, y, z) -> 0.0, // Weirdness (not used for biome selection)
            this.seed);
  }

  @Override
  protected Codec<? extends BiomeSource> codec() {
    return CODEC;
  }

  @Override
  public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
    // Get noise values for this position
    double temperature = getTemperature(x, y, z);
    double humidity = getHumidity(x, y, z);
    double altitude = getAltitude(x, y, z);

    // River biomes have priority
    if (isRiver(temperature, humidity, altitude)) {
      return this.biomes.get(4); // SUBTERRANEAN_RIVERS
    }

    // Check for void chasms (low altitude areas)
    if (altitude < -VOID_CHASM_THRESHOLD) {
      return this.biomes.get(1); // VOID_CHASMS
    }

    // Check for crystal groves (high humidity and medium altitude)
    if (humidity > CRYSTAL_GROVE_THRESHOLD && altitude > 0.2) {
      return this.biomes.get(2); // CRYSTAL_GROVES
    }

    // Check for stalactite forests (high temperature and high altitude)
    if (temperature > STALACTITE_FOREST_THRESHOLD && altitude > 0.3) {
      return this.biomes.get(3); // STALACTITE_FOREST
    }

    // Default to bioluminescent caverns
    return this.biomes.get(0); // BIOLUMINESCENT_CAVERNS
  }

  private boolean isRiver(double temperature, double humidity, double altitude) {
    // Rivers should be in valleys (lower altitude) and follow humidity patterns
    return Math.abs(humidity - 0.5) < RIVER_THRESHOLD
        && altitude < 0.2
        && Math.abs(temperature - 0.5) < 0.3;
  }

  private double getTemperature(int x, int y, int z) {
    return (temperatureNoise.noise(x * BIOME_SCALE, y * BIOME_SCALE, z * BIOME_SCALE) + 1.0) / 2.0;
  }

  private double getHumidity(int x, int y, int z) {
    return (humidityNoise.noise(x * BIOME_SCALE, 0, z * BIOME_SCALE) + 1.0) / 2.0;
  }

  private double getAltitude(int x, int y, int z) {
    // Use 3D noise for altitude to create more interesting cave systems
    double baseNoise =
        (altitudeNoise.noise(x * BIOME_SCALE, y * BIOME_DEPTH_WEIGHT, z * BIOME_SCALE) + 1.0) / 2.0;

    // Add some vertical variation
    double verticalBias = 1.0 - Math.abs((y / 64.0) - 1.0); // Center bias at y=64
    return baseNoise * verticalBias;
  }

  @Override
  public void addDebugInfo(List<String> list, BlockPos pos, Climate.Sampler sampler) {
    int i = QuartPos.fromBlock(pos.getX());
    int j = QuartPos.fromBlock(pos.getY());
    int k = QuartPos.fromBlock(pos.getZ());

    list.add("Hollow World Biome Builder");
    list.add("Temperature: " + getTemperature(i, j, k));
    list.add("Humidity: " + getHumidity(i, j, k));
    list.add("Altitude: " + getAltitude(i, j, k));
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
}
