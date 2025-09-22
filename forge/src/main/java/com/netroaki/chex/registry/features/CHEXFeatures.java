package com.netroaki.chex.registry.features;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.features.CloudstoneIslandFeature;
import com.netroaki.chex.worldgen.features.FungalTowerFeature;
import com.netroaki.chex.worldgen.features.MagmaSpireFeature;
import com.netroaki.chex.worldgen.features.PandoraKelpFeature;
import com.netroaki.chex.worldgen.features.SkybarkTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEXFeatures {

  public static final DeferredRegister<Feature<?>> FEATURES =
      DeferredRegister.create(Registries.FEATURE, CHEX.MOD_ID);

  // Pandora Flora Features
  public static final RegistryObject<FungalTowerFeature> FUNGAL_TOWER = 
      FEATURES.register("fungal_tower", 
          () -> new FungalTowerFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<SkybarkTreeFeature> SKYBARK_TREE =
      FEATURES.register("skybark_tree",
          () -> new SkybarkTreeFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<PandoraKelpFeature> PANDORA_KELP =
      FEATURES.register("pandora_kelp",
          () -> new PandoraKelpFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<MagmaSpireFeature> MAGMA_SPIRE =
      FEATURES.register("magma_spire",
          () -> new MagmaSpireFeature(NoneFeatureConfiguration.CODEC));
          
  public static final RegistryObject<CloudstoneIslandFeature> CLOUDSTONE_ISLAND =
      FEATURES.register("cloudstone_island",
          () -> new CloudstoneIslandFeature(NoneFeatureConfiguration.CODEC));

  // GTCEu integration is now handled directly through world generation events
  // No custom features needed for GTCEu ore generation
}
