package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RockFormationFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    private static final BlockState SMOOTH_SANDSTONE = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
    private static final BlockState CHISELED_SANDSTONE = Blocks.CHISELED_SANDSTONE.defaultBlockState();
    
    public RockFormationFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Get the surface position
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
        BlockPos surfacePos = new BlockPos(origin.getX(), surfaceY, origin.getZ());
        
        // Skip if not in a desert biome
        if (!level.getBiome(surfacePos).is(net.minecraft.tags.BiomeTags.IS_DESERT)) {
            return false;
        }
        
        // Rock formation parameters
        int radius = 2 + random.nextInt(4);  // 2-5 blocks radius
        int height = 3 + random.nextInt(5);  // 3-7 blocks high
        int type = random.nextInt(3);        // Different rock formation types
        
        // Generate different types of rock formations
        switch (type) {
            case 0 -> generatePillar(level, surfacePos, radius, height, random);
            case 1 -> generateArch(level, surfacePos, radius, height, random);
            case 2 -> generateBoulder(level, surfacePos, radius, random);
        }
        
        return true;
    }
    
    private void generatePillar(WorldGenLevel level, BlockPos pos, int radius, int height, RandomSource random) {
        // Create a simple pillar
        for (int y = 0; y < height; y++) {
            int currentRadius = (int)(radius * (1.0 - (y / (float)height * 0.5)));
            
            for (int x = -currentRadius; x <= currentRadius; x++) {
                for (int z = -currentRadius; z <= currentRadius; z++) {
                    if (x*x + z*z <= currentRadius*currentRadius) {
                        BlockPos blockPos = pos.offset(x, y, z);
                        BlockState blockState = getRockVariant(level, blockPos, random);
                        
                        if (level.getBlockState(blockPos).isAir() || 
                            level.getBlockState(blockPos).getMaterial().isReplaceable()) {
                            level.setBlock(blockPos, blockState, 2);
                        }
                    }
                }
            }
        }
    }
    
    private void generateArch(WorldGenLevel level, BlockPos pos, int radius, int height, RandomSource random) {
        // Create an arch shape
        int archHeight = height - 1;
        int archWidth = radius * 2 + 1;
        
        // Base
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x*x + z*z <= radius*radius) {
                    placeRockBlock(level, pos.offset(x, 0, z), random);
                }
            }
        }
        
        // Pillars
        for (int y = 1; y <= archHeight; y++) {
            int pillarRadius = Math.max(1, radius / 2);
            
            // Left pillar
            for (int x = -radius; x <= -radius + 1; x++) {
                for (int z = -pillarRadius; z <= pillarRadius; z++) {
                    placeRockBlock(level, pos.offset(x, y, z), random);
                }
            }
            
            // Right pillar
            for (int x = radius - 1; x <= radius; x++) {
                for (int z = -pillarRadius; z <= pillarRadius; z++) {
                    placeRockBlock(level, pos.offset(x, y, z), random);
                }
            }
            
            // Top arch
            if (y >= archHeight - 1) {
                for (int x = -radius + 2; x <= radius - 2; x++) {
                    placeRockBlock(level, pos.offset(x, y, 0), random);
                }
            }
        }
    }
    
    private void generateBoulder(WorldGenLevel level, BlockPos pos, int radius, RandomSource random) {
        // Create a boulder with some irregularity
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x*x + y*y*1.5 + z*z);
                    double noise = random.nextDouble() * 0.3;
                    
                    if (distance <= radius + noise) {
                        BlockPos blockPos = pos.offset(x, y, z);
                        
                        // Only place if below is solid or at the bottom
                        if (y == -radius || !level.getBlockState(blockPos.below()).isAir()) {
                            placeRockBlock(level, blockPos, random);
                        }
                    }
                }
            }
        }
    }
    
    private void placeRockBlock(WorldGenLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockState(pos).isAir() || 
            level.getBlockState(pos).getMaterial().isReplaceable()) {
            level.setBlock(pos, getRockVariant(level, pos, random), 2);
        }
    }
    
    private BlockState getRockVariant(WorldGenLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.1) {
            return CHISELED_SANDSTONE;
        } else if (random.nextFloat() < 0.3) {
            return SMOOTH_SANDSTONE;
        }
        return SANDSTONE;
    }
}
