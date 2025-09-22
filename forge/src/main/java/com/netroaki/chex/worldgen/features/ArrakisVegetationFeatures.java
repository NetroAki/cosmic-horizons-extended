package com.netroaki.chex.worldgen.features;

import com.netroaki.chex.registry.ArrakisBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ArrakisVegetationFeatures {
    // Configured Features
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SPICE_CACTUS = registerKey("patch_spice_cactus");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DESERT_SHRUB = registerKey("patch_desert_shrub");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SPICE_BLOOM = registerKey("patch_spice_bloom");
    
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Spice Cactus Patch
        register(context, PATCH_SPICE_CACTUS, Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(
                10, // Tries per chunk
                6,  // XZ spread
                2,  // Y spread
                PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ArrakisBlocks.SPICE_CACTUS.get())),
                    BlockPredicate.allOf(
                        BlockPredicate.matchesTag(BlockTags.SAND),
                        BlockPredicate.wouldSurvive(Blocks.CACTUS.defaultBlockState(), BlockPos.ZERO)
                    )
                )
            )
        );
        
        // Desert Shrub Patch
        register(context, PATCH_DESERT_SHRUB, Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(
                32, // Tries per chunk
                7,  // XZ spread
                3,  // Y spread
                PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ArrakisBlocks.DESERT_SHRUB.get())),
                    BlockPredicate.matchesTag(BlockTags.SAND)
                )
            )
        );
        
        // Spice Bloom Patch
        register(context, PATCH_SPICE_BLOOM, Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(
                8,  // Tries per chunk
                6,  // XZ spread
                2,  // Y spread
                PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ArrakisBlocks.SPICE_BLOOM.get())),
                    BlockPredicate.allOf(
                        BlockPredicate.matchesTag(BlockTags.SAND),
                        BlockPredicate.wouldSurvive(Blocks.POPPY.defaultBlockState(), BlockPos.ZERO)
                    )
                )
            )
        );
    }
    
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("chex", name));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
        BootstapContext<ConfiguredFeature<?, ?>> context,
        ResourceKey<ConfiguredFeature<?, ?>> key,
        F feature,
        FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
