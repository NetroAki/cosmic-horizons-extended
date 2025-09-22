package com.netroaki.chex.worldgen.features;

import com.netroaki.chex.registry.ArrakisBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static com.netroaki.chex.CHEX.MOD_ID;

public class ArrakisOreFeatures {
    // Rule tests for ore generation
    public static final RuleTest ARRAKIS_STONE = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
    public static final RuleTest SANDSTONE = new BlockMatchTest(Blocks.SANDSTONE);
    
    // Configured feature keys
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SPICE = createKey("ore_spice");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CRYSTALLINE_SALT = createKey("ore_crystalline_salt");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPICE_GEODE = createKey("spice_geode");
    
    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MOD_ID, name));
    }
    
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Spice Ore
        register(context, ORE_SPICE, Feature.ORE, new OreConfiguration(
            SANDSTONE,
            ArrakisBlocks.SPICE_ORE.get().defaultBlockState(),
            9  // Vein size
        ));
        
        // Crystalline Salt Ore
        register(context, ORE_CRYSTALLINE_SALT, Feature.ORE, new OreConfiguration(
            ARRAKIS_STONE,
            ArrakisBlocks.CRYSTALLINE_SALT_ORE.get().defaultBlockState(),
            7  // Vein size
        ));
        
        // Spice Geode
        register(context, SPICE_GEODE, Feature.GEODE, new GeodeConfiguration(
            // Geode layer settings
            new GeodeBlockSettings(
                BlockStateProvider.simple(Blocks.AIR),
                BlockStateProvider.simple(ArrakisBlocks.SPICE_BLOCK.get()),
                BlockStateProvider.simple(ArrakisBlocks.SPICE_CRYSTAL_BLOCK.get()),
                BlockStateProvider.simple(Blocks.CALCITE),
                BlockStateProvider.simple(ArrakisBlocks.SPICE_CRYSTAL_CLUSTER.get()),
                List.of(
                    ArrakisBlocks.SMALL_SPICE_CRYSTAL_BUD.get().defaultBlockState(),
                    ArrakisBlocks.MEDIUM_SPICE_CRYSTAL_BUD.get().defaultBlockState(),
                    ArrakisBlocks.LARGE_SPICE_CRYSTAL_BUD.get().defaultBlockState()
                ),
                BlockTags.FEATURES_CANNOT_REPLACE,
                BlockTags.GEODE_INVALID_BLOCKS
            ),
            // Geode layer settings
            new GeodeLayerSettings(1.7, 2.2, 3.2, 4.2),
            // Geode crack settings
            new GeodeCrackSettings(0.95, 2.0, 2),
            // Other settings
            0.35,  // Outer wall distance
            0.083, // Distribution points
            true,  // Use potential
            UniformInt.of(4, 6),  // Point offset
            UniformInt.of(2, 4),  // Min gen offset
            -16,    // Bottom offset
            16,     // Top offset
            0.05,   // Noise multiplier
            1.0,    // Use alternate noise
            false   // Use second layer noise
        ));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
        BootstapContext<ConfiguredFeature<?, ?>> context,
        ResourceKey<ConfiguredFeature<?, ?>> key,
        F feature,
        FC config
    ) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
