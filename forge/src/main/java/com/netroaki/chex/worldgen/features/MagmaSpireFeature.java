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

public class MagmaSpireFeature extends Feature<NoneFeatureConfiguration> {
    
    private static final BlockState MAGMA = Blocks.MAGMA_BLOCK.defaultBlockState();
    private static final BlockState OBSIDIAN = Blocks.OBSIDIAN.defaultBlockState();
    private static final BlockState LAVA = Blocks.LAVA.defaultBlockState();
    
    public MagmaSpireFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Check if the feature can be placed here
        if (!isValidGround(level, origin.below())) {
            return false;
        }
        
        // Generate the main spire
        int height = 4 + random.nextInt(5); // Height between 4-8 blocks
        int width = 1 + random.nextInt(2);  // Width between 1-2 blocks
        
        // Generate the base (wider at the bottom)
        for (int y = 0; y < height; y++) {
            int radius = (int) (width * (1.0 - (double) y / height * 0.5));
            
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x*x + z*z <= radius * radius) {
                        BlockPos pos = origin.offset(x, y, z);
                        if (canPlaceAt(level, pos)) {
                            level.setBlock(pos, y == height - 1 ? LAVA : MAGMA, 2);
                        }
                    }
                }
            }
        }
        
        // Add some obsidian around the base for variety
        for (int i = 0; i < 3 + random.nextInt(5); i++) {
            int x = random.nextInt(5) - 2;
            int z = random.nextInt(5) - 2;
            BlockPos pos = origin.offset(x, -1, z);
            if (level.getBlockState(pos).isSolidRender(level, pos)) {
                level.setBlock(pos.above(), OBSIDIAN, 2);
            }
        }
        
        return true;
    }
    
    private boolean isValidGround(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).isSolidRender(level, pos);
    }
    
    private boolean canPlaceAt(WorldGenLevel level, BlockPos pos) {
        return level.isEmptyBlock(pos) || level.getBlockState(pos).is(Blocks.WATER);
    }
}
