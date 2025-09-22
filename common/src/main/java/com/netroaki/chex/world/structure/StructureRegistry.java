package com.netroaki.chex.world.structure;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureRegistry {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
        DeferredRegister.create(Registries.STRUCTURE_TYPE, CHEX.MOD_ID);
    
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
        DeferredRegister.create(Registries.STRUCTURE_PIECE, CHEX.MOD_ID);

    // Structure Types
    public static final RegistryObject<StructureType<ColossusArenaStructure>> COLOSSUS_ARENA =
        STRUCTURE_TYPES.register("colossus_arena", 
            () -> () -> ColossusArenaStructure.CODEC);

    // Structure Piece Types
    public static final RegistryObject<StructurePieceType> COLOSSUS_ARENA_PIECE =
        STRUCTURE_PIECES.register("colossus_arena_piece",
            () -> ColossusArenaPiece::new);
    
    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPES.register(eventBus);
        STRUCTURE_PIECES.register(eventBus);
    }
}
