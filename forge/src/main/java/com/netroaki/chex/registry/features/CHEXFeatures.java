package com.netroaki.chex.registry.features;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;

public class CHEXFeatures {

  public static final DeferredRegister<Feature<?>> FEATURES =
      DeferredRegister.create(Registries.FEATURE, CHEX.MOD_ID);

  // GTCEu integration is now handled directly through world generation events
  // No custom features needed for GTCEu ore generation
}
