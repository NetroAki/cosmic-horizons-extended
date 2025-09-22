package com.netroaki.chex.worldgen.placement;

import com.netroaki.chex.worldgen.features.ArrakisOreFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static com.netroaki.chex.CHEX.MOD_ID;

public class ArrakisOrePlacements {
    // Placed feature keys
    public static final ResourceKey<PlacedFeature> ORE_SPICE = createKey("ore_spice");
    public static final ResourceKey<PlacedFeature> ORE_CRYSTALLINE_SALT = createKey("ore_crystalline_salt");
    public static final ResourceKey<PlacedFeature> SPICE_GEODE = createKey("spice_geode");
    
    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MOD_ID, name));
    }
    
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        // Spice Ore Placement
        register(context, ORE_SPICE,
            context.lookup(Registries.CONFIGURED_FEATURE)
                .getOrThrow(ArrakisOreFeatures.ORE_SPICE),
            commonOrePlacement(7, // Veins per chunk
                HeightRangePlacement.uniform(
                    VerticalAnchor.absolute(0),
                    VerticalAnchor.absolute(64)
                )
            )
        );
        
        // Crystalline Salt Ore Placement
        register(context, ORE_CRYSTALLINE_SALT,
            context.lookup(Registries.CONFIGURED_FEATURE)
                .getOrThrow(ArrakisOreFeatures.ORE_CRYSTALLINE_SALT),
            commonOrePlacement(5, // Veins per chunk
                HeightRangePlacement.uniform(
                    VerticalAnchor.absolute(0),
                    VerticalAnchor.absolute(32)
                )
            )
        );
        
        // Spice Geode Placement
        register(context, SPICE_GEODE,
            context.lookup(Registries.CONFIGURED_FEATURE)
                .getOrThrow(ArrakisOreFeatures.SPICE_GEODE),
            List.of(
                RarityFilter.onAverageOnceEvery(20), // 1 in 20 chunks
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(
                    VerticalAnchor.aboveBottom(20),
                    VerticalAnchor.belowTop(40)
                ),
                BiomeFilter.biome()
            )
        );
    }
    
    private static List<PlacementModifier> commonOrePlacement(int countPerChunk, PlacementModifier height) {
        return orePlacement(CountPlacement.of(countPerChunk), height);
    }
    
    private static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier height) {
        return List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }
    
    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> config, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, modifiers));
    }
}
