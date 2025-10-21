package com.netroaki.chex.world.eden.structure;

import com.mojang.serialization.Codec;
import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EdenStructures {
  public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
      DeferredRegister.create(Registries.STRUCTURE_TYPE, CHEX.MOD_ID);

  // Register structure pieces (separate register)
  public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, CHEX.MOD_ID);

  public static final RegistryObject<StructurePieceType> CELESTIAL_GAZEBO_PIECE =
      STRUCTURE_PIECE_TYPES.register(
          "celestial_gazebo_piece",
          () -> (StructurePieceType.ContextlessType) CelestialGazeboPieces.Piece::new);

  // Register structure types
  public static final RegistryObject<StructureType<CelestialGazeboStructure>> CELESTIAL_GAZEBO =
      STRUCTURE_TYPES.register(
          "celestial_gazebo", () -> explicitStructureTypeTyping(CelestialGazeboStructure.CODEC));

  // Helper method to handle type safety
  private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(
      Codec<T> structureCodec) {
    return () -> structureCodec;
  }

  public static void register(net.minecraftforge.eventbus.api.IEventBus modEventBus) {
    STRUCTURE_TYPES.register(modEventBus);
    STRUCTURE_PIECE_TYPES.register(modEventBus);
  }
}
