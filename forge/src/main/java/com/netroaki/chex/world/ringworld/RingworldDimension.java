package com.netroaki.chex.world.ringworld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalLong;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.registries.ForgeRegistries;

public class RingworldDimension {
  public static final ResourceKey<Level> RINGWORLD_LEVEL =
      ResourceKey.create(Registries.DIMENSION, new ResourceLocation("chex", "ringworld"));

  public static final ResourceKey<DimensionType> RINGWORLD_DIMENSION_TYPE =
      ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation("chex", "ringworld"));

  public static final ResourceKey<LevelStem> RINGWORLD_LEVEL_STEM =
      ResourceKey.create(Registries.LEVEL_STEM, new ResourceLocation("chex", "ringworld"));

  public static void register() {
    // Registration handled by DeferredRegister in CHEXDimensions
  }

  public static class RingworldTeleporter implements ITeleporter {
    private final ServerLevel level;

    public RingworldTeleporter(ServerLevel level) {
      this.level = level;
    }

    @Override
    public Entity placeEntity(
        Entity entity,
        ServerLevel currentWorld,
        ServerLevel destWorld,
        float yaw,
        BiFunction<Boolean, Entity, Entity> repositionEntity) {
      Entity newEntity = repositionEntity.apply(false, entity);
      if (newEntity == null) {
        return null;
      }

      if (destWorld.dimension() == RINGWORLD_LEVEL) {
        // Use the gravity system to find a valid spawn point on the ring
        RingworldGravity.teleportToRingworld(newEntity, destWorld);

        // Set fall distance to 0 to prevent fall damage
        newEntity.fallDistance = 0.0f;

        // Apply initial velocity to match gravity
        Vec3 gravity =
            RingworldGravity.getGravityAt(newEntity.getX(), newEntity.getY(), newEntity.getZ());
        newEntity.setDeltaMovement(gravity.scale(0.5));
      } else {
        // Return to overworld spawn or last position
        double y =
            currentWorld.getHeight(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (int) entity.getX(),
                (int) entity.getZ());
        newEntity.moveTo(entity.getX(), y + 1.0, entity.getZ(), yaw, entity.getXRot());
      }

      return newEntity;
    }
  }

  public static class RingworldDimensionType extends DimensionType {
    public static final Codec<RingworldDimensionType> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        Codec.LONG
                            .optionalFieldOf("fixed_time")
                            .forGetter(dim -> OptionalLong.empty()),
                        Codec.BOOLEAN.fieldOf("has_skylight").forGetter(DimensionType::hasSkyLight),
                        Codec.BOOLEAN.fieldOf("has_ceiling").forGetter(DimensionType::hasCeiling),
                        Codec.BOOLEAN.fieldOf("ultrawarm").forGetter(DimensionType::ultraWarm),
                        Codec.BOOLEAN.fieldOf("natural").forGetter(DimensionType::natural),
                        Codec.DOUBLE
                            .fieldOf("coordinate_scale")
                            .forGetter(DimensionType::coordinateScale),
                        Codec.BOOLEAN.fieldOf("piglin_safe").forGetter(DimensionType::piglinSafe),
                        Codec.BOOLEAN.fieldOf("bed_works").forGetter(DimensionType::bedWorks),
                        Codec.BOOLEAN
                            .fieldOf("respawn_anchor_works")
                            .forGetter(DimensionType::respawnAnchorWorks),
                        Codec.BOOLEAN.fieldOf("has_raids").forGetter(DimensionType::hasRaids),
                        Codec.INT.fieldOf("min_y").forGetter(DimensionType::minY),
                        Codec.INT.fieldOf("height").forGetter(DimensionType::height),
                        Codec.INT.fieldOf("logical_height").forGetter(DimensionType::logicalHeight),
                        ResourceLocation.CODEC
                            .fieldOf("infiniburn")
                            .forGetter(DimensionType::effectsLocation),
                        ResourceLocation.CODEC
                            .fieldOf("effects")
                            .orElse(new ResourceLocation("overworld"))
                            .forGetter(DimensionType::effectsLocation),
                        Codec.FLOAT.fieldOf("ambient_light").forGetter(DimensionType::ambientLight))
                    .apply(instance, RingworldDimensionType::new));

    public RingworldDimensionType(
        OptionalLong fixedTime,
        boolean hasSkyLight,
        boolean hasCeiling,
        boolean ultrawarm,
        boolean natural,
        double coordinateScale,
        boolean piglinSafe,
        boolean bedWorks,
        boolean respawnAnchorWorks,
        boolean hasRaids,
        int minY,
        int height,
        int logicalHeight,
        ResourceLocation infiniburn,
        ResourceLocation effects,
        float ambientLight) {
      super(
          fixedTime,
          hasSkyLight,
          hasCeiling,
          ultrawarm,
          natural,
          coordinateScale,
          piglinSafe,
          bedWorks,
          respawnAnchorWorks,
          hasRaids,
          minY,
          height,
          logicalHeight,
          infiniburn,
          effects,
          ambientLight,
          null);
    }

    @Override
    public double getCoordinateScale() {
      return 1.0; // Normal coordinate scale
    }

    @Override
    public boolean hasSkyLight() {
      return true; // Has a sky with stars
    }

    @Override
    public boolean hasCeiling() {
      return false; // No ceiling, open to space
    }

    @Override
    public boolean hasRaids() {
      return true; // Allow raids
    }

    @Override
    public int height() {
      return 512; // Taller than normal for the ring structure
    }

    @Override
    public int logicalHeight() {
      return 256; // Standard build height
    }

    @Override
    public int minY() {
      return 0; // Start at Y=0
    }
  }

  public static class RingworldChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<RingworldChunkGenerator> CODEC =
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
                    .apply(instance, instance.stable(RingworldChunkGenerator::new)));

    public RingworldChunkGenerator(
        BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
      super(biomeSource, settings);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
      return CODEC;
    }

    @Override
    public void applyBiomeDecoration(
        LevelAccessor level, ChunkAccess chunk, StructureManager structureManager) {
      // Custom decoration logic for the ringworld
      super.applyBiomeDecoration(level, chunk, structureManager);
    }

    @Override
    public int getBaseHeight(
        int x, int z, Heightmap.Types heightmapType, LevelHeightAccessor level, Random random) {
      // Base height for the ringworld surface
      return 64;
    }

    @Override
    public int getGenDepth() {
      return 256;
    }

    @Override
    public void buildSurface(
        WorldGenRegion region,
        StructureManager structureManager,
        Random random,
        ChunkAccess chunk) {
      // Custom surface generation for the ringworld
      super.buildSurface(region, structureManager, random, chunk);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
      // Custom mob spawning for the ringworld
      super.spawnOriginalMobs(region);
    }
  }

  public static class RingworldBiomeSource extends BiomeSource {
    public static final Codec<RingworldBiomeSource> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed))
                    .apply(instance, instance.stable(RingworldBiomeSource::new)));

    private final long seed;
    private final Holder<Biome>[] biomes;
    private final RingworldBiomeProvider biomeProvider;

    @SuppressWarnings("unchecked")
    public RingworldBiomeSource(long seed) {
      super(
          Stream.of(
                  RingworldBiomes.RING_PLAINS,
                  RingworldBiomes.RING_FOREST,
                  RingworldBiomes.RING_MOUNTAINS,
                  RingworldBiomes.RING_RIVER,
                  RingworldBiomes.RING_EDGE,
                  RingworldBiomes.RING_STRUCTURAL)
              .map(
                  key ->
                      ForgeRegistries.BIOMES
                          .getHolder(key)
                          .orElseThrow(
                              () ->
                                  new IllegalStateException(
                                      "Could not find biome: " + key.location())))
              .toList());

      this.seed = seed;
      this.biomes = this.possibleBiomes().toArray(Holder[]::new);
      this.biomeProvider =
          new RingworldBiomeProvider(
              seed,
              List.of(
                  RingworldBiomes.RING_PLAINS,
                  RingworldBiomes.RING_FOREST,
                  RingworldBiomes.RING_MOUNTAINS,
                  RingworldBiomes.RING_RIVER,
                  RingworldBiomes.RING_EDGE,
                  RingworldBiomes.RING_STRUCTURAL));
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
      return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
      return biomeProvider.getNoiseBiome(x, y, z, sampler);
    }

    @Override
    public void addDebugInfo(List<String> list, BlockPos pos, Climate.Sampler sampler) {
      biomeProvider.addDebugInfo(list, pos, sampler);
    }

    @Override
    public Climate.Sampler climateSampler() {
      return biomeProvider.climateSampler();
    }
  }
}
