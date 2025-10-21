package com.netroaki.chex.registry;

import com.mojang.serialization.Codec;
import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Placeholder biome-provider registry. The custom providers will return once the dimension work
 * resumes; for now we simply keep the deferred register alive so existing wiring compiles.
 */
public final class CHEXBiomeProviders {
  private CHEXBiomeProviders() {}

  public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES =
      DeferredRegister.create(Registries.BIOME_SOURCE, CHEX.MOD_ID);

  public static final ResourceKey<Biome> PANDORA_BIOME_KEY =
      ResourceKey.create(Registries.BIOME, new ResourceLocation(CHEX.MOD_ID, "pandora"));

  public static void register(IEventBus eventBus) {
    BIOME_SOURCES.register(eventBus);
  }
}
