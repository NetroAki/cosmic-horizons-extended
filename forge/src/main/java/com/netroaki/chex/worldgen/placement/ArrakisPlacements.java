package com.netroaki.chex.worldgen.placement;

import com.netroaki.chex.worldgen.features.ArrakisFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ArrakisPlacements {
    // Placed Features
    public static final ResourceKey<PlacedFeature> ORE_SPICE = createKey("ore_spice");
    public static final ResourceKey<PlacedFeature> ORE_CRYSTALLINE_SALT = createKey("ore_crystalline_salt");
    public static final ResourceKey<PlacedFeature> SPICE_GEODE = createKey("spice_geode");
    public static final ResourceKey<PlacedFeature> SANDSTONE_PILLAR = createKey("sandstone_pillar");
    public static final ResourceKey<PlacedFeature> DUNE_RIBBLE = createKey("dune_ribble");
    
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Spice Ore Placement
        register(context, ORE_SPICE, configuredFeatures.getOrThrow(ArrakisFeatures.ORE_SPICE),
            CountPlacement.of(20),
            InSquarePlacement.spread(),
            BiomeFilter.biome(),
            HeightRangePlacement.uniform(
                HeightRangePlacement.bottomOffset(0),
                HeightRangePlacement.absolute(64)
            )
        );
        
        // Crystalline Salt Placement
        register(context, ORE_CRYSTALLINE_SALT, configuredFeatures.getOrThrow(ArrakisFeatures.ORE_CRYSTALLINE_SALT),
            CountPlacement.of(10),
            InSquarePlacement.spread(),
            BiomeFilter.biome(),
            HeightRangePlacement.uniform(
                HeightRangePlacement.bottomOffset(0),
                HeightRangePlacement.absolute(32)
            )
        );
        
        // Spice Geode Placement
        register(context, SPICE_GEODE, configuredFeatures.getOrThrow(ArrakisFeatures.SPICE_GEODE),
            RarityFilter.onAverageOnceEvery(30),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(
                HeightRangePlacement.aboveBottom(20),
                HeightRangePlacement.absolute(50)
            ),
            BiomeFilter.biome()
        );
        
        // Sandstone Pillar Placement
        register(context, SANDSTONE_PILLAR, configuredFeatures.getOrThrow(ArrakisFeatures.SANDSTONE_PILLAR),
            RarityFilter.onAverageOnceEvery(15),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            BiomeFilter.biome()
        );
        
        // Dune Ribble Placement
        register(context, DUNE_RIBBLE, configuredFeatures.getOrThrow(ArrakisFeatures.DUNE_RIBBLE),
            RarityFilter.onAverageOnceEvery(5),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
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
