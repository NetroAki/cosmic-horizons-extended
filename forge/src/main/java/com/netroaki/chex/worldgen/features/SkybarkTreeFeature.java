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

public class SkybarkTreeFeature extends Feature<NoneFeatureConfiguration> {
    
    private static final BlockState TRUNK = Blocks.OAK_LOG.defaultBlockState();
    private static final BlockState LEAVES = Blocks.OAK_LEAVES.defaultBlockState();
    
    public SkybarkTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Check if the tree can be placed here
        if (!isValidGround(level, origin.below())) {
            return false;
        }
        
        // Generate the trunk
        int height = 6 + random.nextInt(3); // Height between 6-8 blocks
        for (int y = 0; y < height; y++) {
            BlockPos pos = origin.above(y);
            if (canPlaceAt(level, pos)) {
                level.setBlock(pos, TRUNK, 2);
            } else {
                return false; // Abort if we can't place the trunk
            }
        }
        
        // Generate the canopy
        int canopyRadius = 2 + random.nextInt(2); // Radius between 2-3 blocks
        for (int x = -canopyRadius; x <= canopyRadius; x++) {
            for (int z = -canopyRadius; z <= canopyRadius; z++) {
                for (int y = 0; y < 3; y++) {
                    BlockPos pos = origin.offset(x, height - 2 + y, z);
                    if (level.isEmptyBlock(pos) && pos.distSqr(origin) <= (canopyRadius * canopyRadius)) {
                        level.setBlock(pos, LEAVES, 2);
                    }
                }
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
