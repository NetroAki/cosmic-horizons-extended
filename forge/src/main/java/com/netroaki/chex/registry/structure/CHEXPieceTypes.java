package com.netroaki.chex.registry.structure;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Structure-piece registration is disabled while the arena structure is stubbed. This class remains
 * so existing startup wiring stays intact without introducing new compile errors.
 */
public final class CHEXPieceTypes {
  private CHEXPieceTypes() {}

  public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, CHEX.MOD_ID);

  public static void register(IEventBus eventBus) {
    STRUCTURE_PIECES.register(eventBus);
  }
}
