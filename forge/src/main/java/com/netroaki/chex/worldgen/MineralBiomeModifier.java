package com.netroaki.chex.worldgen;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;

/**
 * Biome modifier that injects configured mineral placed features at runtime based on the loaded
 * mineral configuration.
 */
public final class MineralBiomeModifier implements BiomeModifier {

  public static final Codec<MineralBiomeModifier> CODEC = Codec.unit(MineralBiomeModifier::new);

  @Override
  public void modify(
      Holder<Biome> biome,
      Phase phase,
      net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder builder) {
    if (phase != Phase.ADD || !MineralGenerationRegistry.isInitialized()) {
      return;
    }

    ResourceLocation biomeId = biome.unwrapKey().map(ResourceKey::location).orElse(null);
    if (biomeId == null) {
      return;
    }

    List<ResourceLocation> tagIds = biome.tags().map(TagKey::location).collect(Collectors.toList());
    List<Holder<PlacedFeature>> features =
        MineralGenerationRegistry.gatherFeatures(biomeId, tagIds);
    if (features.isEmpty()) {
      return;
    }

    features.forEach(
        feature ->
            builder
                .getGeneration()
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature));
  }

  @Override
  public Codec<? extends BiomeModifier> codec() {
    return CODEC;
  }
}
