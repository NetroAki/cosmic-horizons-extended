package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.carver.ArrakisCanyonCarver;
import com.netroaki.chex.worldgen.features.DuneFeature;
import com.netroaki.chex.worldgen.features.MesaFeature;
import com.netroaki.chex.worldgen.features.RockFormationFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

public class ArrakisFeatures {
    // Standard features
    public static final ResourceKey<ConfiguredFeature<?, ?>> DUNES = registerKey("dunes");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROCK_FORMATIONS = registerKey("rock_formations");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MESAS = registerKey("mesas");
    
    // Carvers
    public static final ResourceKey<ConfiguredWorldCarver<?>> CANYON = registerCarverKey("canyon");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Register features
        register(context, DUNES, new DuneFeature(NoneFeatureConfiguration.CODEC), NoneFeatureConfiguration.INSTANCE);
        register(context, ROCK_FORMATIONS, new RockFormationFeature(NoneFeatureConfiguration.CODEC), NoneFeatureConfiguration.INSTANCE);
        register(context, MESAS, new MesaFeature(NoneFeatureConfiguration.CODEC), NoneFeatureConfiguration.INSTANCE);
    }
    
    public static void bootstrapCarvers(BootstapContext<ConfiguredWorldCarver<?>> context) {
        context.register(CANYON, new ArrakisCanyonCarver(CaveCarverConfiguration.CODEC)
            .configured(new CaveCarverConfiguration(
                0.02F, // Probability
                UniformHeight.of(VerticalAnchor.absolute(50), VerticalAnchor.absolute(120)),
                ConstantFloat.of(0.5F), // Y scale
                VerticalAnchor.aboveBottom(10),
                false,  // Aquifers enabled
                BiomeTags.IS_DESERT,
                UniformFloat.of(0.1F, 0.9F),
                UniformFloat.of(0.1F, 0.9F),
                UniformFloat.of(0.7F, 1.2F)
            )));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CHEX.MOD_ID, name));
    }
    
    public static ResourceKey<ConfiguredWorldCarver<?>> registerCarverKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, new ResourceLocation(CHEX.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
