package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.biome.KeplerBiomeFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;

public class ModConfiguredFeatures {
    
    public static void register() {
        // Configured features are registered during data generation
    }
    
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        KeplerBiomeFeatures.bootstrap(context);
    }
    
    public static void bootstrapPlacements(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        KeplerBiomeFeatures.bootstrapPlacements(context);
    }
}
