package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CloudstoneIslandFeature extends Feature<NoneFeatureConfiguration> {
    
    private static final BlockState CLOUDSTONE = Blocks.QUARTZ_BLOCK.defaultBlockState();
    private static final BlockState CLOUDSTONE_DECORATION = Blocks.QUARTZ_PILLAR.defaultBlockState();
    
    public CloudstoneIslandFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Check if there's enough space
        if (origin.getY() < level.getMinBuildHeight() + 4) {
            return false;
        }
        
        // Generate the main island
        int radius = 3 + random.nextInt(3); // Radius between 3-5 blocks
        
        // Generate the bottom layer (flat disk)
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x*x + z*z <= radius * radius) {
                    placeCloudstone(level, origin.offset(x, 0, z), random, 0);
                }
            }
        }
        
        // Generate the top layers (smaller radius)
        for (int y = 1; y <= 1 + random.nextInt(2); y++) {
            int layerRadius = radius - y;
            for (int x = -layerRadius; x <= layerRadius; x++) {
                for (int z = -layerRadius; z <= layerRadius; z++) {
                    if (x*x + z*z <= layerRadius * layerRadius) {
                        placeCloudstone(level, origin.offset(x, y, z), random, y);
                    }
                }
            }
        }
        
        // Add some decorative pillars
        for (int i = 0; i < 1 + random.nextInt(3); i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            int x = (int) (Math.cos(angle) * (radius - 1));
            int z = (int) (Math.sin(angle) * (radius - 1));
            
            int pillarHeight = 1 + random.nextInt(3);
            for (int y = 1; y <= pillarHeight; y++) {
                BlockPos pos = origin.offset(x, y, z);
                if (level.isEmptyBlock(pos)) {
                    level.setBlock(pos, CLOUDSTONE_DECORATION, 2);
                }
            }
        }
        
        return true;
    }
    
    private void placeCloudstone(WorldGenLevel level, BlockPos pos, RandomSource random, int depth) {
        if (level.isEmptyBlock(pos) || level.getBlockState(pos).is(Blocks.WATER)) {
            BlockState state = depth == 0 && random.nextFloat() < 0.1f ? 
                CLOUDSTONE_DECORATION : CLOUDSTONE;
            level.setBlock(pos, state, 2);
        }
    }
}
