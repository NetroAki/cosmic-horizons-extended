package com.netroaki.chex.world.structures;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.world.structures.processors.OceanSovereignSpawnerProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureProcessors {
  public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS =
      DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, CosmicHorizonsExpanded.MOD_ID);

  public static final RegistryObject<StructureProcessorType<OceanSovereignSpawnerProcessor>>
      OCEAN_SOVEREIGN_SPAWNER_PROCESSOR =
          STRUCTURE_PROCESSORS.register(
              "ocean_sovereign_spawner_processor",
              () -> () -> OceanSovereignSpawnerProcessor.CODEC);

  public static void register(IEventBus eventBus) {
    STRUCTURE_PROCESSORS.register(eventBus);
  }
}
