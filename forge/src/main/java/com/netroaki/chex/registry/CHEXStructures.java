package com.netroaki.chex.registry;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEXStructures {
  public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
      DeferredRegister.create(Registries.STRUCTURE_TYPE, CosmicHorizonsExpanded.MOD_ID);

  // Structures disabled pending implementation
  public static final RegistryObject<StructureType<?>> SPORE_TYRANT_ARENA = null;

  public static final RegistryObject<StructureType<?>> INFINITE_LIBRARY = null;

  public static void registerStructures() {}
}
