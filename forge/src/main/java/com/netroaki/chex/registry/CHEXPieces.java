package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEXPieces {
  public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, CHEX.MOD_ID);

  // Library pieces disabled pending implementation
  public static final RegistryObject<StructurePieceType> LIBRARY_ROOM = null;

  private static RegistryObject<StructurePieceType> registerPiece(
      String name, StructurePieceType type) {
    return null;
  }

  public static void register() {
    // This method is called to register the pieces
  }
}
