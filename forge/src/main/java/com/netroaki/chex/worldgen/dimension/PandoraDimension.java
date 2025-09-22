package com.netroaki.chex.worldgen.dimension;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.OptionalLong;
import java.util.function.Function;

public class PandoraDimension {
    public static final ResourceKey<Level> PANDORA = ResourceKey.create(Registries.DIMENSION, 
        new ResourceLocation("chex", "pandora"));
    
    public static final ResourceKey<NoiseGeneratorSettings> PANDORA_NOISE_SETTINGS = 
        ResourceKey.create(Registries.NOISE_SETTINGS, new ResourceLocation("chex", "pandora"));
    
    public static final ResourceKey<DimensionType> PANDORA_DIMENSION_TYPE = 
        ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation("chex", "pandora"));

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParams = context.lookup(Registries.NOISE);
        
        context.register(PANDORA_NOISE_SETTINGS, createNoiseGeneratorSettings(
            densityFunctions, 
            noiseParams,
            Blocks.STONE.defaultBlockState(),
            Blocks.WATER.defaultBlockState(),
            -64,  // minY
            320,  // height
            1,    // noiseSizeHorizontal
            2,    // noiseSizeVertical
            false // largeBiomes
        ));
    }
    
    public static NoiseGeneratorSettings createNoiseGeneratorSettings(
        HolderGetter<DensityFunction> densityFunctions,
        HolderGetter<NormalNoise.NoiseParameters> noiseParams,
        BlockState defaultBlock,
        BlockState defaultFluid,
        int minY,
        int height,
        int noiseSizeHorizontal,
        int noiseSizeVertical,
        boolean largeBiomes
    ) {
        return new NoiseGeneratorSettings(
            new NoiseSettings(
                minY,
                height,
                noiseSizeHorizontal,
                noiseSizeVertical
            ),
            defaultBlock,
            defaultFluid,
            NoiseRouterData.overworld(densityFunctions, noiseParams, false, false),
            SurfaceRules.overworld(),
            List.of(),
            -10,  // seaLevel
            false, // disableMobGeneration
            true,  // aquifersEnabled
            true,  // noiseCavesEnabled
            false, // oreVeinsEnabled
            true,  // useLegacyRandomSource
            false, // useFixedTime
            false  // hasCeiling
        );
    }
    
    public static void register() {
        // Register dimension type is handled by JSON
        // This method is a placeholder for any runtime registration needed
    }
    
    // Custom sky properties for Pandora's twilight gradient
    public static class PandoraSkyProperties extends DimensionType {
        public PandoraSkyProperties(Properties properties) {
            super(properties);
        }
        
        @Override
        public float[] getSunriseColor(float timeOfDay, float partialTicks) {
            // Create a purple/blue twilight gradient
            float f = Mth.cos(timeOfDay * ((float)Math.PI * 2F)) - 0.0F;
            if (f >= -0.4F && f <= 0.4F) {
                float f1 = (f - -0.0F) / 0.4F * 0.5F + 0.5F;
                float f2 = 1.0F - (1.0F - Mth.sin(f1 * (float)Math.PI)) * 0.99F;
                f2 = f2 * f2;
                return new float[]{0.6F + f1 * 0.1F, 0.3F, 0.6F + f1 * 0.4F, f2};
            }
            return null;
        }
    }
}
