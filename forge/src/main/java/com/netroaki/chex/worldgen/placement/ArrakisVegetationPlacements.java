package com.netroaki.chex.worldgen.placement;

import com.netroaki.chex.worldgen.features.ArrakisVegetationFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ArrakisVegetationPlacements {
    public static final ResourceKey<PlacedFeature> PATCH_SPICE_CACTUS = createKey("patch_spice_cactus");
    public static final ResourceKey<PlacedFeature> PATCH_DESERT_SHRUB = createKey("patch_desert_shrub");
    public static final ResourceKey<PlacedFeature> PATCH_SPICE_BLOOM = createKey("patch_spice_bloom");
    
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Spice Cactus Placement
        register(context, PATCH_SPICE_CACTUS, 
            configuredFeatures.getOrThrow(ArrakisVegetationFeatures.PATCH_SPICE_CACTUS),
            RarityFilter.onAverageOnceEvery(3),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()
        );
        
        // Desert Shrub Placement
        register(context, PATCH_DESERT_SHRUB,
            configuredFeatures.getOrThrow(ArrakisVegetationFeatures.PATCH_DESERT_SHRUB),
            RarityFilter.onAverageOnceEvery(2),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()
        );
        
        // Spice Bloom Placement
        register(context, PATCH_SPICE_BLOOM,
            configuredFeatures.getOrThrow(ArrakisVegetationFeatures.PATCH_SPICE_BLOOM),
            RarityFilter.onAverageOnceEvery(5),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(
                VerticalAnchor.absolute(60),
                VerticalAnchor.absolute(100)
            ),
            BiomeFilter.biome()
        );
    }
    
    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("chex", name));
    }
    
    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                               Holder<ConfiguredFeature<?, ?>> configuration,
                               List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
    
    @SafeVarargs
    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                               Holder<ConfiguredFeature<?, ?>> configuration,
                               PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}
