package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FungalTowerFeature extends Feature<NoneFeatureConfiguration> {
    
    public FungalTowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Check if the feature can be placed here
        if (!level.getBlockState(origin.below()).isSolidRender(level, origin.below())) {
            return false;
        }
        
        // Generate the fungal tower
        int height = 5 + random.nextInt(4); // Height between 5-8 blocks
        BlockState stem = Blocks.MUSHROOM_STEM.defaultBlockState();
        BlockState cap = Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
        
        // Generate the stem
        for (int y = 0; y < height; y++) {
            BlockPos pos = origin.above(y);
            if (canPlaceAt(level, pos)) {
                level.setBlock(pos, stem, 2);
            } else {
                break;
            }
        }
        
        // Generate the cap
        int capRadius = 2 + random.nextInt(2); // Radius between 2-3 blocks
        for (int x = -capRadius; x <= capRadius; x++) {
            for (int z = -capRadius; z <= capRadius; z++) {
                if (x*x + z*z <= capRadius * capRadius) {
                    BlockPos pos = origin.offset(x, height, z);
                    if (canPlaceAt(level, pos)) {
                        level.setBlock(pos, cap, 2);
                    }
                }
            }
        }
        
        return true;
    }
    
    private boolean canPlaceAt(WorldGenLevel level, BlockPos pos) {
        return level.isEmptyBlock(pos) || level.getBlockState(pos).is(Blocks.WATER);
    }
}
