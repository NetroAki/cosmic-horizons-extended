package com.netroaki.chex.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record FloatingIslandConfig(
    IntProvider minHeight,
    IntProvider maxHeight,
    FloatProvider sizeMultiplier,
    float islandDensity,
    boolean generateChains)
    implements FeatureConfiguration {
  public static final Codec<FloatingIslandConfig> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      IntProvider.CODEC
                          .fieldOf("min_height")
                          .forGetter(FloatingIslandConfig::minHeight),
                      IntProvider.CODEC
                          .fieldOf("max_height")
                          .forGetter(FloatingIslandConfig::maxHeight),
                      FloatProvider.CODEC
                          .fieldOf("size_multiplier")
                          .forGetter(FloatingIslandConfig::sizeMultiplier),
                      Codec.FLOAT
                          .fieldOf("island_density")
                          .forGetter(FloatingIslandConfig::islandDensity),
                      Codec.BOOL
                          .fieldOf("generate_chains")
                          .orElse(true)
                          .forGetter(FloatingIslandConfig::generateChains))
                  .apply(instance, FloatingIslandConfig::new));

  public static final class Builder {
    private IntProvider minHeight;
    private IntProvider maxHeight;
    private FloatProvider sizeMultiplier;
    private float islandDensity = 0.1f;
    private boolean generateChains = true;

    public Builder minHeight(IntProvider minHeight) {
      this.minHeight = minHeight;
      return this;
    }

    public Builder maxHeight(IntProvider maxHeight) {
      this.maxHeight = maxHeight;
      return this;
    }

    public Builder sizeMultiplier(FloatProvider sizeMultiplier) {
      this.sizeMultiplier = sizeMultiplier;
      return this;
    }

    public Builder islandDensity(float islandDensity) {
      this.islandDensity = islandDensity;
      return this;
    }

    public Builder generateChains(boolean generateChains) {
      this.generateChains = generateChains;
      return this;
    }

    public FloatingIslandConfig build() {
      return new FloatingIslandConfig(
          minHeight, maxHeight, sizeMultiplier, islandDensity, generateChains);
    }
  }
}
