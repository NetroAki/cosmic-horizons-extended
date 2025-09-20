package com.netroaki.chex.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import net.minecraft.core.BlockPos;
// import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
// import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.util.RandomSource;
// import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Simple ring strip generator:
 * - Inside |z| <= BAND_HALF_WIDTH: delegate to vanilla overworld noise.
 * - At |z| == BAND_HALF_WIDTH: build black wall columns up to WALL_HEIGHT.
 * - Outside |z| > BAND_HALF_WIDTH: generate void (air) with bedrock floor at
 * y=-64.
 */
public class StripRingGenerator extends ChunkGenerator {

    public static final Codec<StripRingGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ChunkGenerator.CODEC.fieldOf("inner").forGetter(g -> g.inner))
            .apply(instance, StripRingGenerator::new));

    private static final int BAND_HALF_WIDTH = 128; // 8 chunks
    private static final int WALL_HEIGHT = 160;

    private final ChunkGenerator inner;

    public StripRingGenerator(ChunkGenerator inner) {
        super(inner.getBiomeSource());
        this.inner = inner;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState,
            StructureManager structureManager, ChunkAccess chunk) {
        // Always generate vanilla terrain first
        return inner.fillFromNoise(executor, blender, randomState, structureManager, chunk)
                .thenApply((c) -> {
                    // Apply terrain lift to reduce ocean generation
                    applyTerrainLift(c, c.getMinBuildHeight(), c.getMaxBuildHeight());
                    // Then cull outside-band columns to void and build walls at the edge
                    cullOutsideBandColumns(c, c.getMinBuildHeight(), c.getMaxBuildHeight());
                    enforceInsideBandBasement(c, c.getMinBuildHeight(), c.getMaxBuildHeight());
                    buildWallsIfEdge(c, c.getMinBuildHeight(), c.getMaxBuildHeight());
                    return c;
                });
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState,
            ChunkAccess chunk) {
        // Always delegate surface; rely on culling for outside-band void
        inner.buildSurface(region, structureManager, randomState, chunk);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        // Always delegate decoration; rely on culling for outside-band void
        inner.applyBiomeDecoration(level, chunk, structureManager);

        // Add alien flora to the ringworld
        addAlienFlora(level, chunk);
    }

    private void addAlienFlora(WorldGenLevel level, ChunkAccess chunk) {
        int minZ = chunk.getPos().getMinBlockZ();
        int maxZ = minZ + 15;

        // Only add flora inside the band
        if (Math.max(Math.abs(minZ), Math.abs(maxZ)) > BAND_HALF_WIDTH) {
            return;
        }

        RandomSource random = RandomSource.create(level.getSeed() + chunk.getPos().toLong());

        // Generate alien flora patches
        for (int i = 0; i < 20; i++) {
            int x = chunk.getPos().getMinBlockX() + random.nextInt(16);
            int z = chunk.getPos().getMinBlockZ() + random.nextInt(16);

            // Find surface
            BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, new BlockPos(x, 0, z));

            if (canPlaceFlora(level, surfacePos)) {
                if (random.nextFloat() < 0.3f) {
                    // Place alien grass
                    if (level.getBlockState(surfacePos).isAir() &&
                            level.getBlockState(surfacePos.below()).is(Blocks.GRASS_BLOCK)) {
                        level.setBlock(surfacePos, CHEXBlocks.AURELIAN_GRASS.get().defaultBlockState(), 2);
                    }
                } else if (random.nextFloat() < 0.1f) {
                    // Place small alien tree
                    placeAlienTree(level, surfacePos, random);
                }
            }
        }
    }

    private boolean canPlaceFlora(WorldGenLevel level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.DIRT) || below.is(Blocks.STONE);
    }

    private void placeAlienTree(WorldGenLevel level, BlockPos pos, RandomSource random) {
        int height = 3 + random.nextInt(3); // 3-5 blocks tall

        // Place trunk
        for (int i = 0; i < height; i++) {
            BlockPos trunkPos = pos.above(i);
            if (level.getBlockState(trunkPos).isAir()) {
                level.setBlock(trunkPos, CHEXBlocks.AURELIAN_LOG.get().defaultBlockState(), 2);
            }
        }

        // Place leaves
        BlockPos topPos = pos.above(height - 1);
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= 2; y++) {
                    BlockPos leafPos = topPos.offset(x, y, z);
                    if (level.getBlockState(leafPos).isAir() &&
                            (x * x + z * z + y * y) <= 4) { // Spherical-ish shape
                        level.setBlock(leafPos, CHEXBlocks.AURELIAN_LEAVES.get().defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager,
            StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
        // Always delegate carvers; rely on culling for outside-band void
        inner.applyCarvers(region, seed, randomState, biomeManager, structureManager, chunk, step);
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmapType, LevelHeightAccessor level,
            RandomState randomState) {
        if (Math.abs(z) <= BAND_HALF_WIDTH) {
            return inner.getBaseHeight(x, z, heightmapType, level, randomState);
        }
        return level.getMinBuildHeight();
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        if (Math.abs(z) <= BAND_HALF_WIDTH) {
            return inner.getBaseColumn(x, z, level, randomState);
        }
        return new NoiseColumn(level.getMinBuildHeight(), new BlockState[0]);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        inner.spawnOriginalMobs(region);
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
        info.add("StripRingGenerator (band " + BAND_HALF_WIDTH + ")");
    }

    private void clearToVoid(ChunkAccess chunk, int minY, int maxY) {
        final BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
        // bedrock floor at minY
        for (int lx = 0; lx < 16; lx++) {
            int worldX = chunk.getPos().getMinBlockX() + lx;
            for (int lz = 0; lz < 16; lz++) {
                int worldZ = chunk.getPos().getMinBlockZ() + lz;
                bp.set(worldX, minY, worldZ);
                chunk.setBlockState(bp, Blocks.BEDROCK.defaultBlockState(), false);
                for (int y = minY + 1; y < maxY; y++) {
                    bp.set(worldX, y, worldZ);
                    chunk.setBlockState(bp, Blocks.AIR.defaultBlockState(), false);
                }
            }
        }
    }

    private void cullOutsideBandColumns(ChunkAccess chunk, int minY, int maxY) {
        final BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
        for (int lx = 0; lx < 16; lx++) {
            int worldX = chunk.getPos().getMinBlockX() + lx;
            for (int lz = 0; lz < 16; lz++) {
                int worldZ = chunk.getPos().getMinBlockZ() + lz;
                if (Math.abs(worldZ) > BAND_HALF_WIDTH) {
                    // Replace entire column with air for true void outside the ring
                    for (int y = minY; y < maxY; y++) {
                        bp.set(worldX, y, worldZ);
                        chunk.setBlockState(bp, Blocks.AIR.defaultBlockState(), false);
                    }
                }
            }
        }
    }

    // Removed synthetic surface fallback to preserve natural vanilla terrain
    // heights

    private void applyTerrainLift(ChunkAccess chunk, int minY, int maxY) {
        // Lift terrain by 8 blocks to reduce ocean generation
        int liftAmount = 8;
        boolean foundTerrain = false;
        int terrainCount = 0;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Find the highest non-air block in this column
                int highestBlock = minY;
                for (int y = maxY - 1; y >= minY; y--) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!chunk.getBlockState(pos).isAir()) {
                        highestBlock = y;
                        break;
                    }
                }

                // If we found terrain, lift it up
                if (highestBlock > minY) {
                    foundTerrain = true;
                    terrainCount++;

                    // Move blocks up by liftAmount
                    for (int y = highestBlock; y >= minY; y--) {
                        BlockPos sourcePos = new BlockPos(x, y, z);
                        BlockPos targetPos = new BlockPos(x, y + liftAmount, z);

                        if (targetPos.getY() < maxY) {
                            BlockState blockState = chunk.getBlockState(sourcePos);
                            if (!blockState.isAir()) {
                                chunk.setBlockState(targetPos, blockState, false);
                                chunk.setBlockState(sourcePos, Blocks.AIR.defaultBlockState(), false);
                            }
                        }
                    }
                }
            }
        }

        // Debug logging
        if (foundTerrain) {
            System.out.println("StripRingGenerator: Applied terrain lift to " + terrainCount + " columns in chunk "
                    + chunk.getPos());
        } else {
            System.out.println("StripRingGenerator: No terrain found to lift in chunk " + chunk.getPos());
        }
    }

    private void enforceInsideBandBasement(ChunkAccess chunk, int minY, int maxY) {
        final BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
        int minZ = chunk.getPos().getMinBlockZ();
        int maxZ = minZ + 15;
        if (Math.max(Math.abs(minZ), Math.abs(maxZ)) > BAND_HALF_WIDTH)
            return;
        for (int lx = 0; lx < 16; lx++) {
            int worldX = chunk.getPos().getMinBlockX() + lx;
            for (int lz = 0; lz < 16; lz++) {
                int worldZ = chunk.getPos().getMinBlockZ() + lz;
                if (Math.abs(worldZ) > BAND_HALF_WIDTH)
                    continue;
                // Ensure bedrock at absolute floor
                bp.set(worldX, minY, worldZ);
                chunk.setBlockState(bp, Blocks.BEDROCK.defaultBlockState(), false);
                // Clamp any lava in the deep layer to stone
                int clampTop = Math.min(minY + 10, maxY - 1);
                for (int y = minY + 1; y <= clampTop; y++) {
                    bp.set(worldX, y, worldZ);
                    var st = chunk.getBlockState(bp);
                    if (!st.getFluidState().isEmpty() && st.getFluidState().is(FluidTags.LAVA)) {
                        chunk.setBlockState(bp, Blocks.STONE.defaultBlockState(), false);
                    }
                }
            }
        }
    }

    private void buildWallsIfEdge(ChunkAccess chunk, int minY, int maxY) {
        int minZ = chunk.getPos().getMinBlockZ();
        int maxZ = minZ + 15;
        if (!(Math.min(Math.abs(minZ), Math.abs(maxZ)) <= BAND_HALF_WIDTH
                && Math.max(Math.abs(minZ), Math.abs(maxZ)) >= BAND_HALF_WIDTH)) {
            return;
        }
        final BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
        int wallTop = Math.min(WALL_HEIGHT, maxY - 1);
        BlockState wall = Blocks.BLACK_CONCRETE.defaultBlockState();
        for (int lx = 0; lx < 16; lx++) {
            int worldX = chunk.getPos().getMinBlockX() + lx;
            for (int lz = 0; lz < 16; lz++) {
                int worldZ = chunk.getPos().getMinBlockZ() + lz;
                if (Math.abs(worldZ) == BAND_HALF_WIDTH) {
                    for (int y = minY; y <= wallTop; y++) {
                        bp.set(worldX, y, worldZ);
                        chunk.setBlockState(bp, wall, false);
                    }
                }
            }
        }
    }

    @Override
    public int getMinY() {
        return -64;
    }

    @Override
    public int getGenDepth() {
        return 384;
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }
}
