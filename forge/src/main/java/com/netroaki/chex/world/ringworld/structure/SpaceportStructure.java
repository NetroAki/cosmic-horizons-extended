package com.netroaki.chex.world.ringworld.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.structure.CHEXPieces;
import com.netroaki.chex.registry.structure.CHEXStructures;
import com.netroaki.chex.world.ringworld.RingworldSpaceportManager;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.phys.Vec3;

public class SpaceportStructure extends Structure {
  public static final Codec<SpaceportStructure> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance.group(settingsCodec(instance)).apply(instance, SpaceportStructure::new));

  public SpaceportStructure(StructureSettings settings) {
    super(settings);
  }

  @Override
  public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    return onTopOfChunkCenter(
        context,
        Heightmap.Types.WORLD_SURFACE_WG,
        (builder) -> {
          generatePieces(builder, context);
        });
  }

  private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
    ChunkPos chunkPos = context.chunkPos();
    WorldGenLevel level = context.level();
    int x = chunkPos.getMinBlockX() + 8;
    int z = chunkPos.getMinBlockZ() + 8;

    // Find a suitable Y position for the spaceport
    int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);
    BlockPos centerPos = new BlockPos(x, y, z);

    // Add the spaceport piece
    builder.addPiece(new SpaceportPiece(centerPos));
  }

  @Override
  public StructureType<?> type() {
    return CHEXStructures.SPACEPORT_STRUCTURE.get();
  }

  public static class SpaceportPiece extends StructurePiece {
    public static final Codec<SpaceportPiece> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(BlockPos.CODEC.fieldOf("pos").forGetter(piece -> piece.position))
                    .apply(instance, SpaceportPiece::new));

    private final BlockPos position;

    public SpaceportPiece(BlockPos position) {
      super(
          CHEXPieces.SPACEPORT_PIECE.get(),
          0,
          new BoundingBox(position.offset(-16, -1, -16), position.offset(16, 4, 16)));
      this.position = position;
    }

    @Override
    public void postProcess(
        WorldGenLevel level,
        StructureManager structureManager,
        ChunkGenerator generator,
        RandomState random,
        BoundingBox box,
        ChunkPos chunkPos,
        BlockPos pos) {
      // Create a simple platform for now
      BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

      // Create a circular platform
      int radius = 16;
      for (int x = -radius; x <= radius; x++) {
        for (int z = -radius; z <= radius; z++) {
          if (x * x + z * z <= radius * radius) {
            // Platform base
            mutablePos.set(position).move(x, -1, z);
            level.setBlock(mutablePos, Blocks.STONE_BRICKS.defaultBlockState(), 2);

            // Platform surface
            mutablePos.move(0, 1, 0);
            level.setBlock(mutablePos, Blocks.POLISHED_ANDESITE.defaultBlockState(), 2);

            // Add some decorative elements
            if (Math.abs(x) == radius - 1 || Math.abs(z) == radius - 1) {
              // Add walls around the edge
              for (int h = 0; h < 3; h++) {
                mutablePos.move(0, 1, 0);
                level.setBlock(mutablePos, Blocks.STONE_BRICK_WALL.defaultBlockState(), 2);
              }

              // Add some decorative blocks
              if ((x + z) % 4 == 0) {
                mutablePos.move(0, 1, 0);
                level.setBlock(mutablePos, Blocks.SEA_LANTERN.defaultBlockState(), 2);
              }
            }
          }
        }
      }

      // Register this spaceport with the manager
      if (level instanceof ServerLevel serverLevel) {
        RingworldSpaceportManager.registerSpaceport(
            position,
            Level.OVERWORLD, // Default to overworld for now
            new Vec3(position.getX() + 0.5, position.getY() + 2, position.getZ() + 0.5),
            0.0f,
            0.0f);
      }
    }

    @Override
    protected void addAdditionalSaveData(
        StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putInt("X", position.getX());
      tag.putInt("Y", position.getY());
      tag.putInt("Z", position.getZ());
    }

    @Override
    public StructurePieceType getType() {
      return CHEXPieces.SPACEPORT_PIECE.get();
    }
  }
}
