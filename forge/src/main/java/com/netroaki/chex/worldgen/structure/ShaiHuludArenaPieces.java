package com.netroaki.chex.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.ArrakisBlocks;
import com.netroaki.chex.registry.ArrakisEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Random;

public class ShaiHuludArenaPieces {
    private static final int ARENA_RADIUS = 24;
    private static final int ARENA_HEIGHT = 5;
    
    public static void addPieces(StructureTemplateManager structureManager, BlockPos centerPos, StructurePiecesBuilder builder, Random random) {
        int x = centerPos.getX();
        int z = centerPos.getZ();
        int y = centerPos.getY();
        
        // Create the main arena piece
        builder.addPiece(new ArenaPiece(
            x - ARENA_RADIUS,
            y - ARENA_HEIGHT / 2,
            z - ARENA_RADIUS,
            ARENA_RADIUS * 2,
            ARENA_HEIGHT,
            ARENA_RADIUS * 2
        ));
    }
    
    public static class ArenaPiece extends StructurePiece {
        private final int width;
        private final int height;
        private final int depth;
        
        public ArenaPiece(int x, int y, int z, int width, int height, int depth) {
            super(ArrakisStructurePieces.ARENA_PIECE.get(), 0, 
                new BoundingBox(
                    x, y, z, 
                    x + width, y + height, z + depth
                )
            );
            this.width = width;
            this.height = height;
            this.depth = depth;
        }
        
        public ArenaPiece(StructurePieceSerializationContext context, CompoundTag tag) {
            super(ArrakisStructurePieces.ARENA_PIECE.get(), tag);
            this.width = tag.getInt("Width");
            this.height = tag.getInt("Height");
            this.depth = tag.getInt("Depth");
        }
        
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            tag.putInt("Width", this.width);
            tag.putInt("Height", this.height);
            tag.putInt("Depth", this.depth);
        }
        
        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, 
                              Random random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            // Create the arena floor
            BlockState sand = Blocks.SANDSTONE.defaultBlockState();
            BlockState chiseledSand = Blocks.CHISELED_SANDSTONE.defaultBlockState();
            BlockState smoothSand = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
            
            // Fill the arena with sand
            for (int x = 0; x < this.width; x++) {
                for (int z = 0; z < this.depth; z++) {
                    // Skip if outside the circular arena
                    double distFromCenter = Math.sqrt(
                        Math.pow(x - this.width/2.0, 2) + 
                        Math.pow(z - this.depth/2.0, 2)
                    );
                    
                    if (distFromCenter > ARENA_RADIUS) {
                        continue;
                    }
                    
                    // Create a slightly uneven floor
                    for (int y = 0; y < this.height; y++) {
                        BlockState blockToPlace = sand;
                        
                        // Add some variation
                        if (y == this.height - 1) {
                            if (random.nextFloat() < 0.1) {
                                blockToPlace = chiseledSand;
                            } else if (random.nextFloat() < 0.2) {
                                blockToPlace = smoothSand;
                            }
                        }
                        
                        this.placeBlock(level, blockToPlace, x, y, z, box);
                    }
                }
            }
            
            // Spawn the boss in the center
            if (this.boundingBox.getCenter().equals(new BlockPos(
                this.boundingBox.minX() + this.width/2,
                this.boundingBox.minY() + 1,
                this.boundingBox.minZ() + this.depth/2
            ).getCenter())) {
                BlockPos spawnPos = new BlockPos(
                    this.boundingBox.minX() + this.width/2,
                    this.boundingBox.minY() + 1,
                    this.boundingBox.minZ() + this.depth/2
                );
                
                ArrakisEntities.SHAI_HULUD.get().spawn(
                    level, 
                    spawnPos, 
                    MobSpawnType.STRUCTURE
                );
            }
        }
        
        private void placeBlock(LevelAccessor level, BlockState state, int x, int y, int z, BoundingBox box) {
            BlockPos pos = new BlockPos(
                this.boundingBox.minX() + x,
                this.boundingBox.minY() + y,
                this.boundingBox.minZ() + z
            );
            
            if (box.isInside(pos)) {
                level.setBlock(pos, state, 2);
            }
        }
        
        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, 
                              Random random, BoundingBox box, ChunkPos chunkPos) {
            // This method is deprecated, using the one with BlockPos instead
            this.postProcess(level, structureManager, generator, random, box, chunkPos, null);
        }
    }
}
