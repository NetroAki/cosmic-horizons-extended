package com.netroaki.chex.world.ringworld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

/**
 * Minimal ringworld chunk generator scaffold. Produces flat strips with a simple zoning function
 * based on longitudinal position (X) and a wrap period. This is a placeholder to wire
 * data/registration and iterate later.
 */
public class RingworldChunkGenerator extends ChunkGenerator {
  public static final Codec<RingworldChunkGenerator> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      BiomeSource.CODEC
                          .fieldOf("biome_source")
                          .forGetter(RingworldChunkGenerator::getBiomeSource),
                      Codec.INT.fieldOf("strip_period").orElse(1024).forGetter(g -> g.stripPeriod))
                  .apply(instance, RingworldChunkGenerator::new));

  private final int stripPeriod;

  public RingworldChunkGenerator(BiomeSource biomeSource, int stripPeriod) {
    super(biomeSource);
    this.stripPeriod = Math.max(64, stripPeriod);
  }

  @Override
  protected Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  @Override
  public void buildSurface(WorldGenRegion region, RandomState random, ChunkAccess chunk) {
    // Placeholder: generate simple flat strips at y=64 with band metadata via heightmap
    ChunkPos pos = chunk.getPos();
    int baseY = 64;
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        int wx = pos.getMinBlockX() + x;
        int wz = pos.getMinBlockZ() + z;
        int band = Math.floorMod(wx / (stripPeriod / 8), 8);
        int h = baseY + (band % 2 == 0 ? 0 : 2);
        chunk.setHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z, h);
      }
    }
  }

  @Override
  public void applyCarvers(
      WorldGenRegion region,
      long seed,
      RandomState random,
      BiomeManager biomeManager,
      ChunkAccess chunk,
      GenerationStep.Carving carving) {
    // no-op for placeholder
  }

  @Override
  public void spawnOriginalMobs(WorldGenRegion region) {
    // no-op in scaffold
  }
}
