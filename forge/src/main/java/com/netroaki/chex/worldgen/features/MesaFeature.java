package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import com.netroaki.chex.worldgen.noise.ArrakisNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MesaFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    private static final BlockState CUT_SANDSTONE = Blocks.CUT_SANDSTONE.defaultBlockState();
    private static final BlockState SMOOTH_SANDSTONE = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
    private static final BlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.defaultBlockState();
    private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
    
    private ArrakisNoise noise;
    private long lastSeed = -1;
    
    public MesaFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        // Initialize noise
        if (noise == null || lastSeed != level.getSeed()) {
            this.noise = new ArrakisNoise(RandomSource.create(level.getSeed()), 3);
            this.lastSeed = level.getSeed();
        }
        
        // Check if we're in a desert biome
        if (!level.getBiome(origin).is(net.minecraft.tags.BiomeTags.IS_DESERT)) {
            return false;
        }
        
        // Mesa parameters
        int radius = 10 + random.nextInt(15); // 10-24 blocks radius
        int height = 20 + random.nextInt(30); // 20-49 blocks high
        double steepness = 2.0D + random.nextDouble() * 3.0D; // 2.0-5.0
        
        // Generate layers
        List<Layer> layers = new ArrayList<>();
        int currentHeight = 0;
        
        // Base layer (wider)
        layers.add(new Layer(SANDSTONE, (int)(radius * 1.2), 3 + random.nextInt(3)));
        currentHeight += layers.get(0).thickness;
        
        // Middle layers
        int layerCount = 3 + random.nextInt(4);
        for (int i = 0; i < layerCount; i++) {
            BlockState block;
            switch (random.nextInt(4)) {
                case 0 -> block = CUT_SANDSTONE;
                case 1 -> block = SMOOTH_SANDSTONE;
                case 2 -> block = RED_SANDSTONE;
                default -> block = TERRACOTTA;
            }
            
            int layerThickness = 1 + random.nextInt(3);
            int layerRadius = radius - (i * 2);
            
            if (layerRadius <= 2) break;
            
            layers.add(new Layer(block, layerRadius, layerThickness));
            currentHeight += layerThickness;
            
            // Cap height
            if (currentHeight >= height) {
                height = currentHeight;
                break;
            }
        }
        
        // Cap the mesa
        int topLayer = Math.max(1, radius / 3);
        layers.add(new Layer(SANDSTONE, topLayer, 1));
        
        // Generate the mesa
        int centerX = origin.getX();
        int centerZ = origin.getZ();
        int baseY = origin.getY();
        
        // Find the surface
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, centerX, centerZ);
        if (baseY < surfaceY - 10) {
            baseY = surfaceY;
        }
        
        // Generate the mesa layers
        int currentY = baseY;
        for (Layer layer : layers) {
            generateLayer(level, centerX, currentY, centerZ, layer.radius, layer.thickness, steepness, layer.block, random);
            currentY += layer.thickness;
        }
        
        // Add some erosion
        addErosion(level, centerX, baseY, centerZ, radius, height, steepness, random);
        
        return true;
    }
    
    private void generateLayer(WorldGenLevel level, int centerX, int baseY, int centerZ, 
                             int radius, int thickness, double steepness, 
                             BlockState block, RandomSource random) {
        
        int radiusSq = radius * radius;
        
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distSq = x * x + z * z;
                if (distSq > radiusSq) continue;
                
                // Calculate height at this position
                double dist = Math.sqrt(distSq);
                double heightFactor = Math.pow(1.0 - (dist / radius), steepness);
                int layerHeight = (int)(thickness * heightFactor);
                
                // Place blocks
                for (int y = 0; y < layerHeight; y++) {
                    BlockPos pos = new BlockPos(centerX + x, baseY + y, centerZ + z);
                    
                    // Only replace air or replaceable blocks
                    BlockState current = level.getBlockState(pos);
                    if (current.isAir() || current.getMaterial().isReplaceable()) {
                        level.setBlock(pos, block, 2);
                    }
                }
                
                // Add some variation to the top layer
                if (layerHeight > 0 && random.nextFloat() < 0.1f) {
                    BlockPos topPos = new BlockPos(centerX + x, baseY + layerHeight, centerZ + z);
                    if (level.getBlockState(topPos).isAir()) {
                        if (random.nextFloat() < 0.3f) {
                            level.setBlock(topPos, Blocks.DEAD_BUSH.defaultBlockState(), 2);
                        } else if (random.nextFloat() < 0.1f) {
                            level.setBlock(topPos, Blocks.CACTUS.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
    }
    
    private void addErosion(WorldGenLevel level, int centerX, int baseY, int centerZ, 
                           int radius, int height, double steepness, RandomSource random) {
        
        int erosionCount = 5 + random.nextInt(10);
        
        for (int i = 0; i < erosionCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double dist = random.nextDouble() * radius * 0.8;
            
            int x = centerX + (int)(Math.cos(angle) * dist);
            int z = centerZ + (int)(Math.sin(angle) * dist);
            
            // Make sure we're within bounds
            if (x < centerX - radius || x > centerX + radius || 
                z < centerZ - radius || z > centerZ + radius) {
                continue;
            }
            
            // Calculate height at this position
            double distFromCenter = Math.sqrt((x - centerX) * (x - centerX) + (z - centerZ) * (z - centerZ));
            double heightFactor = Math.pow(1.0 - (distFromCenter / radius), steepness);
            int y = baseY + (int)(height * heightFactor);
            
            // Carve out a hole
            int holeRadius = 1 + random.nextInt(3);
            int holeDepth = 2 + random.nextInt(3);
            
            for (int dx = -holeRadius; dx <= holeRadius; dx++) {
                for (int dz = -holeRadius; dz <= holeRadius; dz++) {
                    if (dx * dx + dz * dz > holeRadius * holeRadius) continue;
                    
                    for (int dy = 0; dy < holeDepth; dy++) {
                        BlockPos pos = new BlockPos(x + dx, y - dy, z + dz);
                        if (pos.getY() < baseY) break;
                        
                        BlockState state = level.getBlockState(pos);
                        if (state.is(Blocks.SANDSTONE) || state.is(Blocks.RED_SANDSTONE) || 
                            state.is(Blocks.CUT_SANDSTONE) || state.is(Blocks.SMOOTH_SANDSTONE)) {
                            
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                            
                            // Add some sand at the bottom
                            if (dy == holeDepth - 1 && random.nextFloat() < 0.7f) {
                                level.setBlock(pos.below(), Blocks.SAND.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static class Layer {
        public final BlockState block;
        public final int radius;
        public final int thickness;
        
        public Layer(BlockState block, int radius, int thickness) {
            this.block = block;
            this.radius = radius;
            this.thickness = thickness;
        }
    }
}
