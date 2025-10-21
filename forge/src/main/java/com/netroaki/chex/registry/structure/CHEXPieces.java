package com.netroaki.chex.registry.structure;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.ringworld.structure.SpaceportStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEXPieces {
  public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, CHEX.MOD_ID);

  public static final RegistryObject<StructurePieceType> SPACEPORT_PIECE =
      registerPiece("spaceport_piece", SpaceportStructure.SpaceportPiece::new);

  private static RegistryObject<StructurePieceType> registerPiece(
      String name, StructurePieceType type) {
    return STRUCTURE_PIECES.register(name, () -> type);
  }

  private static RegistryObject<StructurePieceType> registerPiece(
      String name, StructurePieceType.StructureTemplateType type) {
    return STRUCTURE_PIECES.register(name, () -> type);
  }

  public static void register(IEventBus eventBus) {
    STRUCTURE_PIECES.register(eventBus);
  }
}
