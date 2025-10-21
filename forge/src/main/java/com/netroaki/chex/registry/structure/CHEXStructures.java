package com.netroaki.chex.registry.structure;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Structure registration is currently disabled while the world-generation refactor is underway. The
 * deferred register is kept so callers can continue to hook without compile failures.
 */
public final class CHEXStructures {
  private CHEXStructures() {}

  public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
      DeferredRegister.create(Registries.STRUCTURE_TYPE, CHEX.MOD_ID);

  public static void register(IEventBus eventBus) {
    STRUCTURE_TYPES.register(eventBus);
  }
}
