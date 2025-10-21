package com.netroaki.chex.config;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CHEXModConfig {
  public static void register() {
    // Register server config
    ModLoadingContext.get()
        .registerConfig(
            ModConfig.Type.SERVER,
            AquaMundusConfig.SERVER_SPEC,
            "cosmic_horizons_expanded-aqua_mundus.toml");
    ModLoadingContext.get()
        .registerConfig(
            ModConfig.Type.SERVER,
            CrystalisHazardsConfig.SERVER_SPEC,
            "cosmic_horizons_expanded-crystalis_hazards.toml");
    ModLoadingContext.get()
        .registerConfig(
            ModConfig.Type.SERVER,
            StormworldMechanicsConfig.SERVER_SPEC,
            "cosmic_horizons_expanded-stormworld_mechanics.toml");

    // Add config reload listener
    FMLJavaModLoadingContext.get().getModEventBus().addListener(CHEXModConfig::onConfigReload);
  }

  private static void onConfigReload(ModConfigEvent.Reloading event) {
    if (event.getConfig().getSpec() == AquaMundusConfig.SERVER_SPEC) {
      // Handle config reload if needed
    }
    if (event.getConfig().getSpec() == CrystalisHazardsConfig.SERVER_SPEC) {
      // Handle Crystalis hazards config reload if needed
    }
    if (event.getConfig().getSpec() == StormworldMechanicsConfig.SERVER_SPEC) {
      // Handle Stormworld mechanics config reload if needed
    }
  }
}
