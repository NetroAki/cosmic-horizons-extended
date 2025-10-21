package com.netroaki.chex.world.eden;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class EdenBiomeRegistry {
  public static final ResourceKey<Biome> EDEN_GARDEN = registerKey("eden_garden");

  private static ResourceKey<Biome> registerKey(String name) {
    return ResourceKey.create(Registries.BIOME, new ResourceLocation(CHEX.MOD_ID, name));
  }

  public static void bootstrap(BootstapContext<Biome> context) {
    context.register(EDEN_GARDEN, EdenGardenBiome.create(context));
  }

  public static void registerBiomes() {
    CHEX.LOGGER.info("Registering Eden's Garden biomes for " + CHEX.MOD_ID);
  }
}
