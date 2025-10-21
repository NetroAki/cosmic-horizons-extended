package com.netroaki.chex.registry.biome;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.ringworld.RingworldBiomes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RingworldBiomeRegistry {
  public static final DeferredRegister<Biome> BIOMES =
      DeferredRegister.create(ForgeRegistries.BIOMES, CHEX.MOD_ID);

  // Register all Ringworld biomes
  public static final RegistryObject<Biome> RING_PLAINS = registerBiome("ring_plains");
  public static final RegistryObject<Biome> RING_FOREST = registerBiome("ring_forest");
  public static final RegistryObject<Biome> RING_MOUNTAINS = registerBiome("ring_mountains");
  public static final RegistryObject<Biome> RING_RIVER = registerBiome("ring_river");
  public static final RegistryObject<Biome> RING_EDGE = registerBiome("ring_edge");
  public static final RegistryObject<Biome> RING_STRUCTURAL = registerBiome("ring_structural");

  private static RegistryObject<Biome> registerBiome(String name) {
    return BIOMES.register(
        name,
        () -> {
          // This will be filled by the bootstrap context
          return null;
        });
  }

  public static void bootstrap(BootstapContext<Biome> context) {
    // Register all biomes with the bootstrap context
    context.register(RingworldBiomes.RING_PLAINS, RingworldBiomes.createRingPlains());
    context.register(RingworldBiomes.RING_FOREST, RingworldBiomes.createRingForest());
    context.register(RingworldBiomes.RING_MOUNTAINS, RingworldBiomes.createRingMountains());
    context.register(RingworldBiomes.RING_RIVER, RingworldBiomes.createRingRiver());
    context.register(RingworldBiomes.RING_EDGE, RingworldBiomes.createRingEdge());
    context.register(RingworldBiomes.RING_STRUCTURAL, RingworldBiomes.createRingStructural());
  }

  public static void register(IEventBus eventBus) {
    BIOMES.register(eventBus);
  }
}
