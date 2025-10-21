package com.netroaki.chex.world.hollow;

import static com.netroaki.chex.CHEX.MOD_ID;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.*;

public class HollowWorldBiomes {
  // Biome keys
  public static final ResourceKey<Biome> BIOLUMINESCENT_CAVERNS =
      register("bioluminescent_caverns");
  public static final ResourceKey<Biome> VOID_CHASMS = register("void_chasms");
  public static final ResourceKey<Biome> CRYSTAL_GROVES = register("crystal_groves");
  public static final ResourceKey<Biome> STALACTITE_FOREST = register("stalactite_forest");
  public static final ResourceKey<Biome> SUBTERRANEAN_RIVERS = register("subterranean_rivers");

  // Helper method to register biome keys
  private static ResourceKey<Biome> register(String name) {
    return ResourceKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, name));
  }

  // Biome generation settings
  public static void bootstrap(BootstapContext<Biome> context) {
    context.register(BIOLUMINESCENT_CAVERNS, bioluminescentCaverns(context));
    context.register(VOID_CHASMS, voidChasms(context));
    context.register(CRYSTAL_GROVES, crystalGroves(context));
    context.register(STALACTITE_FOREST, stalactiteForest(context));
    context.register(SUBTERRANEAN_RIVERS, subterraneanRivers(context));
  }

  // Biome configurations
  private static Biome bioluminescentCaverns(BootstapContext<Biome> context) {
    MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
    BiomeGenerationSettings.Builder biomeBuilder =
        new BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE),
            context.lookup(Registries.CONFIGURED_CARVER));

    // Add biome features here

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .downfall(0.5f)
        .temperature(0.7f)
        .generationSettings(biomeBuilder.build())
        .mobSpawnSettings(spawnBuilder.build())
        .specialEffects(
            (new BiomeSpecialEffects.Builder())
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(10518688)
                .skyColor(0)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES))
                .build())
        .build();
  }

  private static Biome voidChasms(BootstapContext<Biome> context) {
    MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
    BiomeGenerationSettings.Builder biomeBuilder =
        new BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE),
            context.lookup(Registries.CONFIGURED_CARVER));

    // Add biome features here

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .downfall(0.1f)
        .temperature(0.2f)
        .generationSettings(biomeBuilder.build())
        .mobSpawnSettings(spawnBuilder.build())
        .specialEffects(
            (new BiomeSpecialEffects.Builder())
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(0x0F0F1A)
                .skyColor(0)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS))
                .build())
        .build();
  }

  private static Biome crystalGroves(BootstapContext<Biome> context) {
    MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
    BiomeGenerationSettings.Builder biomeBuilder =
        new BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE),
            context.lookup(Registries.CONFIGURED_CARVER));

    // Add biome features here

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .downfall(0.3f)
        .temperature(0.5f)
        .generationSettings(biomeBuilder.build())
        .mobSpawnSettings(spawnBuilder.build())
        .specialEffects(
            (new BiomeSpecialEffects.Builder())
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(0xE6E6FA)
                .skyColor(0)
                .ambientParticle(
                    new AmbientParticleSettings(
                        new AmbientParticleSettings.ParticleSettings(
                            net.minecraft.core.particles.ParticleTypes.END_ROD, 0.001F),
                        0.01F))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES))
                .build())
        .build();
  }

  private static Biome stalactiteForest(BootstapContext<Biome> context) {
    MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
    BiomeGenerationSettings.Builder biomeBuilder =
        new BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE),
            context.lookup(Registries.CONFIGURED_CARVER));

    // Add biome features here

    return new Biome.BiomeBuilder()
        .hasPrecipitation(true)
        .downfall(0.8f)
        .temperature(0.4f)
        .generationSettings(biomeBuilder.build())
        .mobSpawnSettings(spawnBuilder.build())
        .specialEffects(
            (new BiomeSpecialEffects.Builder())
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(0x8B8B83)
                .skyColor(0)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES))
                .build())
        .build();
  }

  private static Biome subterraneanRivers(BootstapContext<Biome> context) {
    MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
    BiomeGenerationSettings.Builder biomeBuilder =
        new BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE),
            context.lookup(Registries.CONFIGURED_CARVER));

    // Add biome features here

    return new Biome.BiomeBuilder()
        .hasPrecipitation(true)
        .downfall(1.0f)
        .temperature(0.6f)
        .generationSettings(biomeBuilder.build())
        .mobSpawnSettings(spawnBuilder.build())
        .specialEffects(
            (new BiomeSpecialEffects.Builder())
                .waterColor(0x1E90FF)
                .waterFogColor(0x1E90FF)
                .fogColor(0x1E90FF)
                .skyColor(0)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES))
                .build())
        .build();
  }
}
