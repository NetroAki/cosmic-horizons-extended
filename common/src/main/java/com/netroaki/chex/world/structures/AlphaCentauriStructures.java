package com.netroaki.chex.world.structures;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class AlphaCentauriStructures {
  // Structure Keys
  public static final ResourceKey<Structure> SOLAR_COLLECTOR = createKey("solar_collector");
  public static final ResourceKey<Structure> FLUX_PYLON = createKey("flux_pylon");
  public static final ResourceKey<Structure> CORONAL_LOOP = createKey("coronal_loop");

  // Structure Set Keys
  public static final ResourceKey<StructureSet> SOLAR_COLLECTOR_SET =
      createSetKey("solar_collector");
  public static final ResourceKey<StructureSet> FLUX_PYLON_SET = createSetKey("flux_pylon");
  public static final ResourceKey<StructureSet> CORONAL_LOOP_SET = createSetKey("coronal_loop");

  private static ResourceKey<Structure> createKey(String name) {
    return ResourceKey.create(
        Registries.STRUCTURE, new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, name));
  }

  private static ResourceKey<StructureSet> createSetKey(String name) {
    return ResourceKey.create(
        Registries.STRUCTURE_SET, new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, name));
  }

  public static void bootstrap(BootstapContext<Structure> context) {
    // Register structures here
  }

  public static void bootstrapSets(BootstapContext<StructureSet> context) {
    // Register structure sets here
  }

  private static StructurePlacement createPlacement(
      int spacing, int separation, RandomSpreadType spreadType) {
    return new RandomSpreadStructurePlacement(
        spacing, // spacing
        separation, // separation
        spreadType, // spread type
        12345 // salt
        );
  }
}
