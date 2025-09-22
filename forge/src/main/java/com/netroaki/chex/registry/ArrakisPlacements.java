package com.netroaki.chex.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ArrakisPlacements {
    // Placement keys
    public static final ResourceKey<PlacedFeature> PLACED_DUNES = registerKey("dunes_placement");
    public static final ResourceKey<PlacedFeature> PLACED_ROCK_FORMATIONS = registerKey("rock_formations_placement");
    public static final ResourceKey<PlacedFeature> PLACED_MESAS = registerKey("mesas_placement");
    
    // Biome modifier keys
    public static final ResourceKey<BiomeModifier> ADD_DUNES = registerBiomeModifierKey("add_dunes");
    public static final ResourceKey<BiomeModifier> ADD_ROCK_FORMATIONS = registerBiomeModifierKey("add_rock_formations");
    public static final ResourceKey<BiomeModifier> ADD_MESAS = registerBiomeModifierKey("add_mesas");
    public static final ResourceKey<BiomeModifier> ADD_CANYON_CARVER = registerBiomeModifierKey("add_canyon_carver");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Configure dune placement
        register(context, PLACED_DUNES, 
            configuredFeatures.getOrThrow(ArrakisFeatures.DUNES),
            RarityFilter.onAverageOnceEvery(10), // About 1 in 10 chunks
            InSquarePlacement.spread(),
            BiomeFilter.biome(),
            HeightRangePlacement.uniform(
                VerticalAnchor.absolute(60),
                VerticalAnchor.absolute(100)
            )
        );
        
        // Configure rock formation placement
        register(context, PLACED_ROCK_FORMATIONS,
            configuredFeatures.getOrThrow(ArrakisFeatures.ROCK_FORMATIONS),
            RarityFilter.onAverageOnceEvery(15), // About 1 in 15 chunks
            InSquarePlacement.spread(),
            BiomeFilter.biome(),
            HeightRangePlacement.uniform(
                VerticalAnchor.absolute(60),
                VerticalAnchor.absolute(120)
            )
        );
        
        // Configure mesa placement (rarer and more spread out)
        register(context, PLACED_MESAS,
            configuredFeatures.getOrThrow(ArrakisFeatures.MESAS),
            RarityFilter.onAverageOnceEvery(30), // Rare - about 1 in 30 chunks
            InSquarePlacement.spread(),
            BiomeFilter.biome(),
            HeightRangePlacement.uniform(
                VerticalAnchor.absolute(60),
                VerticalAnchor.absolute(150)
            )
        );
    }
    
    public static void bootstrapBiomeModifiers(BootstapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);
        
        // Add dunes to desert biomes
        context.register(ADD_DUNES, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_DESERT),
            Holder.direct(placedFeatures.getOrThrow(PLACED_DUNES)),
            GenerationStep.Decoration.LOCAL_MODIFICATIONS
        ));
        
        // Add rock formations to desert biomes
        context.register(ADD_ROCK_FORMATIONS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_DESERT),
            Holder.direct(placedFeatures.getOrThrow(PLACED_ROCK_FORMATIONS)),
            GenerationStep.Decoration.LOCAL_MODIFICATIONS
        ));
        
        // Add mesas to desert biomes (rarer)
        context.register(ADD_MESAS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_DESERT),
            Holder.direct(placedFeatures.getOrThrow(PLACED_MESAS)),
            GenerationStep.Decoration.RAW_GENERATION
        ));
        
        // Add canyon carver to desert biomes
        context.register(ADD_CANYON_CARVER, new ForgeBiomeModifiers.AddCarversBiomeModifier(
            context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_DESERT),
            Holder.direct(carvers.getOrThrow(ArrakisFeatures.CANYON))
        ));
    }
    
    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CHEX.MOD_ID, name));
    }
    
    private static ResourceKey<BiomeModifier> registerBiomeModifierKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CHEX.MOD_ID, name));
    }
    
    @SafeVarargs
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
