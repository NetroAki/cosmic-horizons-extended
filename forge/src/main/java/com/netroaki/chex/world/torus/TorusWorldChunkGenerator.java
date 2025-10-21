package com.netroaki.chex.world.torus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkPos;
import net.minecraft.world.level.levelgen.*;

/**
 * Minimal torus-world chunk generator scaffold. Produces flat bands with inner/outer rim hints via
 * height offsets for now.
 */
public class TorusWorldChunkGenerator extends ChunkGenerator {
  public static final Codec<TorusWorldChunkGenerator> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      BiomeSource.CODEC
                          .fieldOf("biome_source")
                          .forGetter(TorusWorldChunkGenerator::getBiomeSource),
                      Codec.INT.fieldOf("major_radius").orElse(2048).forGetter(g -> g.majorRadius),
                      Codec.INT.fieldOf("minor_radius").orElse(256).forGetter(g -> g.minorRadius))
                  .apply(instance, TorusWorldChunkGenerator::new));

  private final int majorRadius;
  private final int minorRadius;

  public TorusWorldChunkGenerator(BiomeSource biomeSource, int majorRadius, int minorRadius) {
    super(biomeSource);
    this.majorRadius = Math.max(256, majorRadius);
    this.minorRadius = Math.max(32, minorRadius);
  }

  @Override
  protected Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  @Override
  public void buildSurface(WorldGenRegion region, RandomState random, ChunkAccess chunk) {
    // Simple height hinting: inner vs outer rim by Z, bands along X
    ChunkPos pos = chunk.getPos();
    int baseY = 64;
    for (int dx = 0; dx < 16; dx++) {
      for (int dz = 0; dz < 16; dz++) {
        int wx = pos.getMinBlockX() + dx;
        int wz = pos.getMinBlockZ() + dz;
        int band = Math.floorMod(wx / 128, 6); // 5 biomes + spacer
        int rimBias = (wz % (minorRadius * 2) < minorRadius) ? 2 : 0; // crude inner rim lift
        int h = baseY + rimBias + (band == 2 ? 1 : 0);
        chunk.setHeight(Heightmap.Types.WORLD_SURFACE_WG, dx, dz, h);
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
    // no carvers in scaffold
  }

  @Override
  public void spawnOriginalMobs(WorldGenRegion region) {
    // none for scaffold
  }
}
