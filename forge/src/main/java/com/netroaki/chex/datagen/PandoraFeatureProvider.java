package com.netroaki.chex.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.features.PandoraFeatures;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class PandoraFeatureProvider {

  public static void generate(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    // Generate biome modifiers for Pandora features
    generateBiomeModifiers(output, lookupProvider, existingFileHelper);
  }

  private static void generateBiomeModifiers(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    // Create a resource location for our biome modifier
    ResourceLocation pandoraBiomeModifiers = new ResourceLocation(CHEX.MOD_ID, "pandora_biome_modifiers");
    
    // Create a builder for our biome modifiers
    JsonCodecProvider<BiomeModifier> provider = JsonCodecProvider.forDatapackRegistry(
        output,
        new ResourceLocation(CHEX.MOD_ID, "biome_modifier"),
        ForgeRegistries.KNOWN_BIOME_MODIFIER_SERIALIZERS.get().getKey(ForgeBiomeModifiers.AddFeaturesBiomeModifier.TYPE),
        Map.of(
            // Add fungal towers to Pandora biomes
            pandoraBiomeModifiers,
            new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                lookupProvider.get().lookupOrThrow(Registries.BIOME)
                    .filter(biome -> biome.is(BiomeTags.IS_OVERWORLD)),
                lookupProvider.get().lookupOrThrow(Registries.PLACED_FEATURE)
                    .getOrThrow(PandoraFeatures.PANDORA_FUNGI),
                GenerationStep.Decoration.VEGETAL_DECORATION
            )
        ),
        CHEX.MOD_ID,
        existingFileHelper
    );
    
    // Generate the JSON file
    provider.run(output);
  }

  // Helper method to create a simple loot table provider
  public static LootTableProvider createLootTableProvider(PackOutput output) {
    return new LootTableProvider(
        output,
        Set.of(),
        Set.of(
            new LootTableProvider.SubProviderEntry(
                PandoraLootTableProvider::new, LootContextParamSets.BLOCK)),
        CompletableFuture.completedFuture(null));
  }

  // Simple loot table provider for Pandora blocks
  private static class PandoraLootTableProvider extends net.minecraft.data.loot.BlockLootSubProvider {
    protected PandoraLootTableProvider() {
      super(Set.of(), FeatureFlags.REGISTRY);
    }

    @Override
    protected void generate() {
      // Add loot tables for Pandora blocks here
      // Example:
      // dropSelf(CHEXBlocks.PANDORITE_BLOCK.get());
    }
  }
}
