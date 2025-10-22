package com.netroaki.chex.terrablender;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.biomes.CHEXBiomes;

public final class CHEXTerraBlender {
  private CHEXTerraBlender() {}

  public static void register() {
    CHEX.LOGGER.info("Registering CHEX TerraBlender regions...");
    CHEXBiomes.registerRegions();
  }
}
