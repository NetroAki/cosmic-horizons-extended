package com.netroaki.chex.worldgen.carver;

import com.mojang.serialization.Codec;
import com.netroaki.chex.worldgen.noise.ArrakisNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

import java.util.BitSet;
import java.util.function.Function;

public class ArrakisCanyonCarver extends WorldCarver<CaveCarverConfiguration> {
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    private static final BlockState SAND = Blocks.SAND.defaultBlockState();
    
    private ArrakisNoise noise;
    private long seed;
    
    public ArrakisCanyonCarver(Codec<CaveCarverConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean carve(CarvingContext context, CaveCarverConfiguration config, ChunkAccess chunk, 
                        Function<BlockPos, Biome> biomeAccessor, RandomSource random, 
                        Aquifer aquifer, ChunkPos chunkPos, CarvingMask carvingMask) {
        
        // Initialize noise with chunk-specific seed
        if (this.noise == null || this.seed != context.getSeed()) {
            this.seed = context.getSeed();
            this.noise = new ArrakisNoise(RandomSource.create(this.seed), 3);
        }
        
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        MutableBlockPos pos = new MutableBlockPos();
        
        // Only generate canyons in certain chunks to prevent overpopulation
        if (random.nextInt(4) != 0) {
            return false;
        }
        
        // Try to generate 1-3 canyons per chunk
        int canyons = 1 + random.nextInt(3);
        boolean generated = false;
        
        for (int i = 0; i < canyons; i++) {
            // Random position within chunk
            double x = (chunkX << 4) + random.nextInt(16);
            double z = (chunkZ << 4) + random.nextInt(16);
            
            // Only generate in desert biomes
            Biome biome = biomeAccessor.apply(new BlockPos((int)x, 64, (int)z));
            if (!biome.getBiomeCategory().equals(Biome.BiomeCategory.DESERT)) {
                continue;
            }
            
            // Canyon parameters
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double length = 50.0D + random.nextDouble() * 50.0D;
            double width = 3.0D + random.nextDouble() * 5.0D;
            double depth = 20.0D + random.nextDouble() * 15.0D;
            
            // Generate canyon
            if (generateCanyon(chunk, random.nextLong(), x, z, angle, length, width, depth, carvingMask)) {
                generated = true;
            }
        }
        
        return generated;
    }
    
    private boolean generateCanyon(ChunkAccess chunk, long seed, double startX, double startZ, 
                                 double angle, double length, double width, double depth, 
                                 CarvingMask carvingMask) {
        RandomSource random = RandomSource.create(seed);
        double x = startX;
        double z = startZ;
        
        // Carve the canyon along its length
        for (double l = 0.0D; l < length; l += 1.0D) {
            // Calculate position along curve
            double progress = l / length;
            double curve = Math.sin(progress * Math.PI) * 2.0D;
            
            // Add some random variation to the path
            angle += (random.nextDouble() - 0.5D) * 0.2D;
            
            // Calculate next position
            double dx = Math.sin(angle) * (1.0D + curve * 0.1D);
            double dz = Math.cos(angle) * (1.0D + curve * 0.1D);
            
            // Calculate current width and depth
            double currentWidth = width * (0.7D + 0.3D * Math.sin(progress * Math.PI));
            double currentDepth = depth * (0.5D + 0.5D * Math.sin(progress * Math.PI));
            
            // Carve this segment
            carveCanyonSegment(chunk, x, z, dx, dz, currentWidth, currentDepth, carvingMask);
            
            // Move to next position
            x += dx;
            z += dz;
        }
        
        return true;
    }
    
    private void carveCanyonSegment(ChunkAccess chunk, double x, double z, double dx, double dz, 
                                   double width, double depth, CarvingMask carvingMask) {
        int radius = (int)Math.ceil(width / 2.0D);
        int minX = (int)(x - radius) & 0xFFFFFFF0;
        int minZ = (int)(z - radius) & 0xFFFFFFF0;
        int maxX = (int)(x + radius + 16) & 0xFFFFFFF0;
        int maxZ = (int)(z + radius + 16) & 0xFFFFFFF0;
        
        MutableBlockPos pos = new MutableBlockPos();
        
        for (int cx = minX; cx <= maxX; cx += 16) {
            for (int cz = minZ; cz <= maxZ; cz += 16) {
                if (!chunk.getPos().equals(new ChunkPos(cx >> 4, cz >> 4))) {
                    continue;
                }
                
                for (int bx = 0; bx < 16; bx++) {
                    for (int bz = 0; bz < 16; bz++) {
                        int px = (cx & 0xFFFFFFF0) | bx;
                        int pz = (cz & 0xFFFFFFF0) | bz;
                        
                        // Calculate distance from canyon center
                        double dx2 = px - x;
                        double dz2 = pz - z;
                        double dist = Math.sqrt(dx2 * dx2 + dz2 * dz2);
                        
                        if (dist > width) {
                            continue;
                        }
                        
                        // Calculate depth at this point
                        double depthFactor = 1.0D - (dist / width);
                        int maxDepth = (int)(depth * depthFactor);
                        
                        // Find surface
                        int surfaceY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, px, pz);
                        
                        // Carve down from surface
                        for (int y = surfaceY; y > surfaceY - maxDepth && y > chunk.getMinBuildHeight(); y--) {
                            pos.set(px, y, pz);
                            
                            // Only carve solid blocks
                            BlockState state = chunk.getBlockState(pos);
                            if (state.isAir() || !state.getMaterial().isSolid()) {
                                continue;
                            }
                            
                            // Set to air or sandstone based on depth
                            if (y > surfaceY - 2) {
                                chunk.setBlockState(pos, SAND, false);
                            } else if (y > surfaceY - 4) {
                                chunk.setBlockState(pos, SANDSTONE, false);
                            } else {
                                chunk.setBlockState(pos, AIR, false);
                            }
                            
                            carvingMask.set(px & 15, y, pz & 15);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isStartChunk(CaveCarverConfiguration config, RandomSource random) {
        return random.nextFloat() <= config.probability;
    }
}
