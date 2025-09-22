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

public class DuneFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockState SAND = Blocks.SAND.defaultBlockState();
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    
    public DuneFeature(Codec<NoneFeatureConfiguration> codec) {
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
        
        // Dune parameters
        int width = 8 + random.nextInt(9);  // 8-16 blocks wide
        int length = 16 + random.nextInt(17); // 16-32 blocks long
        int height = 3 + random.nextInt(4);  // 3-6 blocks high
        
        // Dune direction (perpendicular to wind direction)
        boolean northSouth = random.nextBoolean();
        
        // Generate the dune
        for (int x = -width/2; x <= width/2; x++) {
            for (int z = -length/2; z <= length/2; z++) {
                // Calculate distance from center along the dune's length
                double dist = northSouth ? Math.abs(z) : Math.abs(x);
                double normDist = 2.0 * dist / (northSouth ? length : width);
                
                // Skip if outside the dune's width
                if (normDist > 1.0) continue;
                
                // Calculate height at this point (sine wave shape)
                double heightFactor = Math.cos(normDist * Math.PI / 2);
                int duneHeight = (int)(height * heightFactor * heightFactor);
                
                // Place blocks for the dune
                for (int y = 0; y <= duneHeight; y++) {
                    BlockPos pos = surfacePos.offset(
                        x, 
                        y - duneHeight/2, 
                        z
                    );
                    
                    // Use sandstone for the base layers, sand for the top
                    BlockState blockState = (y == duneHeight) ? SAND : SANDSTONE;
                    
                    // Place the block if it's air or replaceable
                    if (level.getBlockState(pos).isAir() || 
                        level.getBlockState(pos).getMaterial().isReplaceable()) {
                        level.setBlock(pos, blockState, 2);
                    }
                }
            }
        }
        
        return true;
    }
}
