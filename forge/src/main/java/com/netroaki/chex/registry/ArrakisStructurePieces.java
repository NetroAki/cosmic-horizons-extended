package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.structure.ShairHuludArenaPieces;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ArrakisStructurePieces {
    public static StructurePieceType ARENA_PIECE;
    
    public static void register() {
        ARENA_PIECE = register("arena_piece", 
            ShairHuludArenaPieces.ArenaPiece::new);
    }
    
    private static StructurePieceType register(String name, StructurePieceType type) {
        return Registry.register(
            BuiltInRegistries.STRUCTURE_PIECE,
            new ResourceLocation(CHEX.MOD_ID, name),
            type
        );
    }
}
