package com.netroaki.chex.world.library.dimension;

import static com.netroaki.chex.CHEX.MOD_ID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalLong;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

/** Handles the registration and configuration of the Infinite Library dimension. */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MOD_ID)
public class LibraryDimension {
  // Dimension type for the Infinite Library
  public static final ResourceKey<DimensionType> LIBRARY_DIM_TYPE =
      ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(MOD_ID, "library"));

  // Dimension ID for the Infinite Library
  public static final ResourceKey<Level> LIBRARY_DIMENSION =
      ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MOD_ID, "library"));

  // Dimension type registration
  public static final RegistryObject<DimensionType> LIBRARY_DIM_TYPE_REGISTRY =
      CHEX.DIMENSION_TYPES.register(
          "library",
          () ->
              new DimensionType(
                  OptionalLong.empty(), // fixed time
                  true, // has skylight
                  false, // has ceiling
                  false, // ultra warm
                  true, // natural
                  1.0, // coordinate scale
                  true, // bed works
                  false, // respawn anchor works
                  0, // min y
                  256, // height
                  256, // logical height
                  BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                  BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effects location
                  0.1f, // ambient light
                  new DimensionType.MonsterSettings(
                      false, // piglin safe
                      true, // has raiders
                      false, // bed works
                      false // respawn anchor works
                      )));

  @SubscribeEvent
  public static void onCommonSetup(FMLCommonSetupEvent event) {
    // Register the dimension type
    event.enqueueWork(
        () -> {
          // The dimension type is automatically registered by the deferred register
          // We can add additional setup here if needed
        });
  }

  /** Creates a dimension settings object for the Infinite Library */
  public static NoiseGeneratorSettings createLibraryNoiseSettings() {
    return new NoiseGeneratorSettings(
        // Noise settings
        NoiseGeneratorSettings.overworld(false, false).noiseSettings(),
        // Default block
        Blocks.STONE.defaultBlockState(),
        // Default fluid
        Blocks.WATER.defaultBlockState(),
        // Noise router (simplified for library)
        NoiseRouterData.overworld(
            null, // Noise settings
            false, // Use legacy random source
            false // Use large biomes
            ),
        // Surface rules (simplified for library)
        SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(
                    Biomes.PLAINS), // Temporary, will be replaced with library biome
                SurfaceRules.state(Blocks.OAK_PLANKS.defaultBlockState()))),
        // Sea level
        0,
        // Disable mob spawning
        false,
        // Aquifers enabled
        false,
        // Noise caves enabled
        false,
        // Ore veins enabled
        false,
        // Use legacy random source
        false,
        // Surface rule type (overworld by default)
        Optional.empty());
  }

  /** Creates a dimension stem for the Infinite Library */
  public static LevelStem createDimensionStem(RegistryAccess registryAccess) {
    // Get the dimension type
    Registry<DimensionType> dimensionTypes =
        registryAccess.registryOrThrow(Registries.DIMENSION_TYPE);
    DimensionType libraryType = dimensionTypes.getOrThrow(LIBRARY_DIM_TYPE);

    // Create a simple biome source with a single biome (will be replaced later)
    Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);
    Holder<Biome> libraryBiome = biomeRegistry.getOrCreateHolderOrThrow(Biomes.PLAINS); // Temporary

    // Create the chunk generator
    NoiseBasedChunkGenerator chunkGenerator =
        new NoiseBasedChunkGenerator(
            new FixedBiomeSource(libraryBiome),
            registryAccess.registryOrThrow(Registries.STRUCTURE_SET),
            registryAccess.registryOrThrow(Registries.NOISE),
            LibraryDimension.createLibraryNoiseSettings());

    // Create and return the dimension stem
    return new LevelStem(Holder.direct(libraryType), chunkGenerator);
  }

  /** A simple biome source that always returns a single biome */
  private static class FixedBiomeSource extends BiomeSource {
    private static final Codec<FixedBiomeSource> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(Biome.CODEC.fieldOf("biome").forGetter(source -> source.biome))
                    .apply(instance, FixedBiomeSource::new));

    private final Holder<Biome> biome;

    public FixedBiomeSource(Holder<Biome> biome) {
      super(List.of(biome));
      this.biome = biome;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
      return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
      return biome;
    }
  }
}
