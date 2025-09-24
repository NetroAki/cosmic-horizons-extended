package com.netroaki.chex.registry.features;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.features.CloudstoneIslandFeature;
import com.netroaki.chex.worldgen.features.MagmaSpireFeature;
import com.netroaki.chex.worldgen.features.PandoraFungalTowerFeature;
import com.netroaki.chex.worldgen.features.PandoraKelpForestFeature;
import com.netroaki.chex.worldgen.features.SkybarkTreeFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEXFeatures {

  public static final DeferredRegister<Feature<?>> FEATURES =
      DeferredRegister.create(Registries.FEATURE, CHEX.MOD_ID);

  // Pandora Features
  public static final RegistryObject<Feature<NoneFeatureConfiguration>> PANDORA_FUNGAL_TOWER =
      FEATURES.register("pandora_fungal_tower", 
          () -> new PandoraFungalTowerFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<Feature<NoneFeatureConfiguration>> SKYBARK_TREE =
      FEATURES.register("skybark_tree", 
          () -> new SkybarkTreeFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<Feature<NoneFeatureConfiguration>> PANDORA_KELP_FOREST =
      FEATURES.register("pandora_kelp_forest", 
          () -> new PandoraKelpForestFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<Feature<NoneFeatureConfiguration>> MAGMA_SPIRE =
      FEATURES.register("magma_spire", 
          () -> new MagmaSpireFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<Feature<NoneFeatureConfiguration>> CLOUDSTONE_ISLAND =
      FEATURES.register("cloudstone_island", 
          () -> new CloudstoneIslandFeature(NoneFeatureConfiguration.CODEC));

  // GTCEu integration is now handled directly through world generation events
  // No custom features needed for GTCEu ore generation
}
