package com.netroaki.chex.registry.biome;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.hollow.HollowWorldBiomes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HollowWorldBiomeRegistry {
  public static final DeferredRegister<Biome> BIOMES =
      DeferredRegister.create(ForgeRegistries.BIOMES, CHEX.MOD_ID);

  // Register all Hollow World biomes
  public static final RegistryObject<Biome> BIOLUMINESCENT_CAVERNS =
      registerBiome("hollow_world_biome_bioluminescent_caverns");
  public static final RegistryObject<Biome> VOID_CHASMS =
      registerBiome("hollow_world_biome_void_chasms");
  public static final RegistryObject<Biome> CRYSTAL_GROVES =
      registerBiome("hollow_world_biome_crystal_groves");
  public static final RegistryObject<Biome> STALACTITE_FOREST =
      registerBiome("hollow_world_biome_stalactite_forest");
  public static final RegistryObject<Biome> SUBTERRANEAN_RIVERS =
      registerBiome("hollow_world_biome_subterranean_rivers");

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
    context.register(
        HollowWorldBiomes.BIOLUMINESCENT_CAVERNS, HollowWorldBiomes.bioluminescentCaverns(context));
    context.register(HollowWorldBiomes.VOID_CHASMS, HollowWorldBiomes.voidChasms(context));
    context.register(HollowWorldBiomes.CRYSTAL_GROVES, HollowWorldBiomes.crystalGroves(context));
    context.register(
        HollowWorldBiomes.STALACTITE_FOREST, HollowWorldBiomes.stalactiteForest(context));
    context.register(
        HollowWorldBiomes.SUBTERRANEAN_RIVERS, HollowWorldBiomes.subterraneanRivers(context));
  }

  public static void register(IEventBus eventBus) {
    BIOMES.register(eventBus);
  }
}
