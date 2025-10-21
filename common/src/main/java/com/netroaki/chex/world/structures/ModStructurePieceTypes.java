package com.netroaki.chex.world.structures;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.world.structures.pieces.FrozenCathedralPiece;
import com.netroaki.chex.world.structures.pieces.OceanSovereignArenaPiece;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructurePieceTypes {
  public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, CosmicHorizonsExpanded.MOD_ID);

  public static final RegistryObject<StructurePieceType> OCEAN_SOVEREIGN_ARENA_PIECE =
      registerPiece(
          "ocean_sovereign_arena_piece",
          () -> (StructurePieceType.ContextlessType) OceanSovereignArenaPiece::new);

  public static final RegistryObject<StructurePieceType> FROZEN_CATHEDRAL_PIECE =
      registerPiece(
          "frozen_cathedral_piece",
          () -> (StructurePieceType.ContextlessType) FrozenCathedralPiece::new);

  private static RegistryObject<StructurePieceType> registerPiece(
      String name, Supplier<StructurePieceType> type) {
    return STRUCTURE_PIECE_TYPES.register(name, type);
  }

  public static void register(IEventBus eventBus) {
    STRUCTURE_PIECE_TYPES.register(eventBus);
  }
}
