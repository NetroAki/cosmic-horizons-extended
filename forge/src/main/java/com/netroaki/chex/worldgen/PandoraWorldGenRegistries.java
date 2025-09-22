package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class PandoraWorldGenRegistries {
    private PandoraWorldGenRegistries() {}

    public static void bootstrap(BootstapContext<Biome> biomeContext) {
        PandoraWorldGen.bootstrap(biomeContext);
    }

    public static void bootstrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> featureContext) {
        PandoraFeatures.bootstrapConfiguredFeatures(featureContext);
    }

    public static void bootstrapPlacedFeatures(BootstapContext<PlacedFeature> placedFeatureContext) {
        PandoraFeatures.bootstrapPlacedFeatures(placedFeatureContext);
    }

    public static ResourceKey<Biome> createBiomeKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(CHEX.MOD_ID, name));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createConfiguredFeatureKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CHEX.MOD_ID, name));
    }

    public static ResourceKey<PlacedFeature> createPlacedFeatureKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CHEX.MOD_ID, name));
    }
}
