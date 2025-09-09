package com.netroaki.chex.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

public class HaloRingworldGenerator extends CHEXChunkGenerator {

    public static final Codec<HaloRingworldGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource))
            .apply(instance, HaloRingworldGenerator::new));

    private static final int BAND_HALF_WIDTH = 240; // between old (160) and 320
    private static final int SURFACE_Y = 80; // nominal surface altitude
    private static final int STRUCTURE_THICKNESS = 40; // structural deck thickness

    private static final int SEA_HALF_WIDTH = 4; // very thin or effectively removed
    private static final int SEA_LEVEL = 77; // unused when we don't fill with water
    private static final int WALL_THICKNESS = 1; // 1 block thick walls
    private static final int WALL_HEIGHT = 160; // height of atmosphere-retaining walls

    // Noise params for more natural terrain
    private static final double HILLS_FREQ = 1.0 / 96.0;
    private static final double HILLS_AMP = 28.0; // taller terrain
    private static final double DETAIL_FREQ = 1.0 / 28.0;
    private static final double DETAIL_AMP = 10.0;
    private static final double WARP_FREQ = 1.0 / 128.0;
    private static final double WARP_AMP = 12.0;
    private static final double CAVE_FREQ = 1.0 / 24.0;
    private static final double CAVE_THRESHOLD = 0.58;

    private static final double WRAP_LENGTH = 2000.0; // must match wrap hook for seamless seam
    private final SimplexNoise baseNoise;
    private final SimplexNoise detailNoise;
    private final SimplexNoise caveNoise;

    public HaloRingworldGenerator(BiomeSource biomeSource) {
        super(biomeSource);
        long seed = 12345L;
        this.baseNoise = new SimplexNoise(RandomSource.create(seed));
        this.detailNoise = new SimplexNoise(RandomSource.create(seed ^ 0x5DEECE66DL));
        this.caveNoise = new SimplexNoise(RandomSource.create(seed ^ 0x9E3779B97F4A7C15L));
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    protected ChunkAccess generateCustomChunk(ChunkAccess chunk, RandomState randomState) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunk.getPos().getMinBlockX() + x;
                int worldZ = chunk.getPos().getMinBlockZ() + z;

                if (Math.abs(worldZ) <= BAND_HALF_WIDTH) {
                    int beltDistance = Math.abs(worldZ);

                    double beltFactor = Math.min(1.0, Math.max(0.0,
                            (double) (beltDistance - SEA_HALF_WIDTH) / (double) (BAND_HALF_WIDTH - SEA_HALF_WIDTH)));
                    // Canonical X mapping to avoid stretch and tile every WRAP_LENGTH
                    int canonicalX = (int) (Math.floorMod(worldX + (int) (WRAP_LENGTH / 2.0), (int) WRAP_LENGTH)
                            - (WRAP_LENGTH / 2.0));

                    // domain warp using canonical space for more organic shapes
                    double wx = baseNoise.getValue(canonicalX * WARP_FREQ, 0.0, worldZ * WARP_FREQ) * WARP_AMP;
                    double wz = detailNoise.getValue(canonicalX * (WARP_FREQ * 0.9), 0.0, worldZ * (WARP_FREQ * 0.9))
                            * WARP_AMP * 0.8;

                    double nx = (canonicalX + wx) * HILLS_FREQ;
                    double nz = (worldZ + wz) * HILLS_FREQ;
                    double ndx = (canonicalX + wx * 0.5) * DETAIL_FREQ;
                    double ndz = (worldZ + wz * 0.5) * DETAIL_FREQ;

                    // add a bit of ridge via abs to break waves
                    double hills = Math.abs(baseNoise.getValue(nx, 0.0, nz)) * HILLS_AMP;
                    double detail = detailNoise.getValue(ndx, 0.0, ndz) * DETAIL_AMP;
                    int heightDelta = (int) Math.round((hills + detail) * beltFactor);
                    int surfaceY = SURFACE_Y + heightDelta;

                    boolean isSea = beltDistance <= SEA_HALF_WIDTH;
                    BlockState top;
                    BlockState sub;
                    BlockState base = Blocks.STONE.defaultBlockState();

                    if (isSea) {
                        // Thin seam: no sand/water fill; use grass/dirt like nearby land
                        top = Blocks.GRASS_BLOCK.defaultBlockState();
                        sub = Blocks.DIRT.defaultBlockState();
                    } else if (beltFactor > 0.85) {
                        top = Blocks.SNOW_BLOCK.defaultBlockState();
                        sub = Blocks.PACKED_ICE.defaultBlockState();
                    } else if (beltFactor > 0.55) {
                        top = Blocks.COARSE_DIRT.defaultBlockState();
                        sub = Blocks.DIRT.defaultBlockState();
                    } else if (beltFactor > 0.25) {
                        top = Blocks.GRASS_BLOCK.defaultBlockState();
                        sub = Blocks.DIRT.defaultBlockState();
                    } else {
                        top = Blocks.GRASS_BLOCK.defaultBlockState();
                        sub = Blocks.DIRT.defaultBlockState();
                    }

                    // Surface
                    pos.set(worldX, surfaceY, worldZ);
                    chunk.setBlockState(pos, top, false);

                    // Sub-surface
                    for (int y = surfaceY - 1; y >= surfaceY - 3; y--) {
                        pos.set(worldX, y, worldZ);
                        chunk.setBlockState(pos, sub, false);
                    }

                    // Bedrock layer similar to overworld (variable thickness 1-5)
                    long seed = ((long) worldX * 341873128712L) ^ ((long) worldZ * 132897987541L);
                    net.minecraft.util.RandomSource rs = net.minecraft.util.RandomSource.create(seed);
                    int bedrockThickness = 1 + rs.nextInt(5);

                    int minY = chunk.getMinBuildHeight();

                    // Stone body down to just above bedrock cap
                    for (int y = surfaceY - 4; y >= minY + bedrockThickness; y--) {
                        pos.set(worldX, y, worldZ);
                        // carve caves
                        double cv = caveNoise.getValue(worldX * CAVE_FREQ, y * (CAVE_FREQ * 0.8), worldZ * CAVE_FREQ);
                        if (cv > CAVE_THRESHOLD) {
                            chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);
                        } else {
                            chunk.setBlockState(pos, base, false);
                        }
                    }

                    // Place bedrock from bottom up to thickness
                    for (int i = 0; i < bedrockThickness; i++) {
                        int y = minY + i;
                        pos.set(worldX, y, worldZ);
                        chunk.setBlockState(pos, Blocks.BEDROCK.defaultBlockState(), false);
                    }

                    // no water fill to avoid visible strip

                    if (Math.abs(Math.abs(worldZ) - BAND_HALF_WIDTH) < WALL_THICKNESS) {
                        for (int y = surfaceY + 1; y <= WALL_HEIGHT; y++) {
                            pos.set(worldX, y, worldZ);
                            chunk.setBlockState(pos,
                                    com.netroaki.chex.registry.blocks.CHEXBlocks.AURELIA_WALL.get().defaultBlockState(),
                                    false);
                        }
                    }
                } else {
                    for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                        pos.set(worldX, y, worldZ);
                        chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);
                    }
                }
            }
        }

        return chunk;
    }

    @Override
    protected int getCustomHeight(int x, int z, Heightmap.Types heightmapType) {
        if (Math.abs(z) <= BAND_HALF_WIDTH) {
            int beltDistance = Math.abs(z);
            double beltFactor = Math.min(1.0, Math.max(0.0,
                    (double) (beltDistance - SEA_HALF_WIDTH) / (double) (BAND_HALF_WIDTH - SEA_HALF_WIDTH)));
            int canonicalX = (int) (Math.floorMod(x + (int) (WRAP_LENGTH / 2.0), (int) WRAP_LENGTH)
                    - (WRAP_LENGTH / 2.0));
            double wx = baseNoise.getValue(canonicalX * WARP_FREQ, 0.0, z * WARP_FREQ) * WARP_AMP;
            double wz = detailNoise.getValue(canonicalX * (WARP_FREQ * 0.9), 0.0, z * (WARP_FREQ * 0.9)) * WARP_AMP
                    * 0.8;
            double nx = (canonicalX + wx) * HILLS_FREQ;
            double nz = (z + wz) * HILLS_FREQ;
            double ndx = (canonicalX + wx * 0.5) * DETAIL_FREQ;
            double ndz = (z + wz * 0.5) * DETAIL_FREQ;
            double hills = Math.abs(baseNoise.getValue(nx, 0.0, nz)) * HILLS_AMP;
            double detail = detailNoise.getValue(ndx, 0.0, ndz) * DETAIL_AMP;
            int heightDelta = (int) Math.round((hills + detail) * beltFactor);
            return SURFACE_Y + heightDelta;
        }
        return 0;
    }
}
