package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PandoraKelpFeature extends Feature<NoneFeatureConfiguration> {
    
    private static final BlockState KELP = Blocks.KELP_PLANT.defaultBlockState();
    
    public PandoraKelpFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Find the water surface
        int x = origin.getX();
        int z = origin.getZ();
        int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        
        // Check if we're in water
        if (!level.getFluidState(pos).is(FluidTags.WATER)) {
            return false;
        }
        
        // Generate kelp patch
        int count = 10 + random.nextInt(10); // 10-20 kelp plants per patch
        int placed = 0;
        
        for(int i = 0; i < count; ++i) {
            int offsetX = random.nextInt(8) - random.nextInt(8);
            int offsetZ = random.nextInt(8) - random.nextInt(8);
            BlockPos kelpPos = new BlockPos(x + offsetX, y, z + offsetZ);
            
            // Find the top of the water column
            while(kelpPos.getY() > level.getMinBuildHeight() + 1 && 
                  level.getFluidState(kelpPos).is(FluidTags.WATER)) {
                kelpPos = kelpPos.below();
            }
            
            // Place kelp from the sea floor up
            if (placeKelp(level, kelpPos.above(), random)) {
                placed++;
            }
        }
        
        return placed > 0;
    }
    
    private boolean placeKelp(LevelAccessor level, BlockPos pos, RandomSource random) {
        int height = 1 + random.nextInt(10); // Height between 1-10 blocks
        
        for(int i = 0; i <= height; ++i) {
            if (level.getBlockState(pos).is(Blocks.WATER) && 
                level.getBlockState(pos.above()).is(Blocks.WATER) &&
                KELP.canSurvive(level, pos)) {
                
                if (i == height || !level.getFluidState(pos.above()).is(FluidTags.WATER)) {
                    level.setBlock(pos, Blocks.KELP.defaultBlockState(), 2);
                    return true;
                }
                
                level.setBlock(pos, KELP, 2);
                pos = pos.above();
            } else {
                return i > 0;
            }
        }
        
        return true;
    }
}
