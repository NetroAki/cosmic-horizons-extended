package com.netroaki.chex.world.library.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.CHEXStructures;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class LibraryReadingNookStructure extends Structure {
  public static final Codec<LibraryReadingNookStructure> CODEC =
      RecordCodecBuilder.create(
          builder ->
              builder
                  .group(
                      settingsCodec(builder),
                      Codec.INT.fieldOf("min_size").forGetter(structure -> structure.minSize),
                      Codec.INT.fieldOf("max_size").forGetter(structure -> structure.maxSize))
                  .apply(builder, LibraryReadingNookStructure::new));

  private final int minSize;
  private final int maxSize;

  public LibraryReadingNookStructure(StructureSettings settings, int minSize, int maxSize) {
    super(settings);
    this.minSize = minSize;
    this.maxSize = maxSize;
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
    RandomSource random = context.random();
    int x = chunkPos.getMinBlockX() + random.nextInt(16);
    int z = chunkPos.getMinBlockZ() + random.nextInt(16);
    int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);

    // Only generate on solid ground
    if (y < level.getMinBuildHeight() + 5) {
      return;
    }

    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);

    // Get the surface block
    BlockState surfaceBlock = level.getBlockState(pos);
    if (!surfaceBlock.isSolid()) {
      return;
    }

    // Generate reading nook structure
    int size = minSize + random.nextInt(maxSize - minSize + 1);
    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);

    // Add the structure piece
    builder.addPiece(
        new ReadingNookPiece(new BlockPos(x, y + 1, z), size, direction, random.nextLong()));
  }

  @Override
  public StructureType<?> type() {
    return CHEXStructures.READING_NOOK_STRUCTURE.get();
  }

  public static class ReadingNookPiece extends StructurePiece {
    private final int size;
    private final Direction direction;

    public ReadingNookPiece(BlockPos pos, int size, Direction direction, long seed) {
      super(CHEXStructures.READING_NOOK_PIECE.get(), 0, calculateBoundingBox(pos, size, direction));
      this.size = size;
      this.direction = direction;
    }

    public ReadingNookPiece(StructurePieceSerializationContext context, CompoundTag tag) {
      super(CHEXStructures.READING_NOOK_PIECE.get(), tag);
      this.size = tag.getInt("Size");
      this.direction = Direction.from2DDataValue(tag.getInt("Direction"));
    }

    private static BoundingBox calculateBoundingBox(BlockPos pos, int size, Direction direction) {
      int radius = size / 2;
      return new BoundingBox(
          pos.getX() - radius,
          pos.getY(),
          pos.getZ() - radius,
          pos.getX() + radius + 1,
          pos.getY() + 3,
          pos.getZ() + radius + 1);
    }

    @Override
    protected void addAdditionalSaveData(
        StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putInt("Size", size);
      tag.putInt("Direction", direction.get2DDataValue());
    }

    @Override
    public void postProcess(
        WorldGenLevel level,
        StructureManager structureManager,
        ChunkGenerator generator,
        RandomSource random,
        BoundingBox box,
        ChunkPos chunkPos,
        BlockPos pos) {

      BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
      BlockState slab = Blocks.OAK_SLAB.defaultBlockState();
      BlockState stair = Blocks.OAK_STAIRS.defaultBlockState();
      BlockState carpet = Blocks.RED_CARPET.defaultBlockState();
      BlockState lantern = Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true);
      BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
      BlockState lectern =
          Blocks.LECTERN.defaultBlockState().setValue(LecternBlock.FACING, direction);

      BlockState topSlab = slab.setValue(SlabBlock.TYPE, SlabType.TOP);
      BlockState bottomSlab = slab.setValue(SlabBlock.TYPE, SlabType.BOTTOM);

      // Create a circular or square reading nook
      int centerX = box.minX() + (box.getXSpan() / 2);
      int centerZ = box.minZ() + (box.getZSpan() / 2);

      // Floor
      for (int x = box.minX(); x <= box.maxX(); x++) {
        for (int z = box.minZ(); z <= box.maxZ(); z++) {
          BlockPos floorPos = new BlockPos(x, box.minY(), z);
          if (level.getBlockState(floorPos.below()).isSolid()) {
            level.setBlock(floorPos, planks, 2);

            // Add carpet in the center
            int dx = x - centerX;
            int dz = z - centerZ;
            if (dx * dx + dz * dz <= (size / 2) * (size / 2)) {
              level.setBlock(floorPos.above(), carpet, 2);
            }
          }
        }
      }

      // Walls and furniture
      for (int y = box.minY() + 1; y <= box.maxY(); y++) {
        // Bookshelves around the edges
        for (int x = box.minX(); x <= box.maxX(); x++) {
          for (int z = box.minZ(); z <= box.maxZ(); z++) {
            boolean isEdge =
                x == box.minX() || x == box.maxX() || z == box.minZ() || z == box.maxZ();
            if (isEdge && level.getBlockState(new BlockPos(x, y - 1, z)).isSolid()) {
              level.setBlock(new BlockPos(x, y, z), bookshelf, 2);
            }
          }
        }

        // Add some decorative elements
        if (y == box.minY() + 1) {
          // Add a lectern facing the center
          BlockPos lecternPos = new BlockPos(centerX, y, centerZ).relative(direction, size / 2 - 1);
          if (level.getBlockState(lecternPos.below()).isSolid()) {
            level.setBlock(lecternPos, lectern, 2);
          }

          // Add some chairs
          for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (dir != direction && dir != direction.getOpposite()) {
              BlockPos chairPos =
                  new BlockPos(centerX, y, centerZ)
                      .relative(direction, size / 4)
                      .relative(dir, size / 4);

              if (level.getBlockState(chairPos.below()).isSolid()) {
                level.setBlock(
                    chairPos,
                    Blocks.OAK_STAIRS
                        .defaultBlockState()
                        .setValue(StairBlock.FACING, dir.getOpposite())
                        .setValue(StairBlock.HALF, Half.BOTTOM),
                    2);
              }
            }
          }
        }
      }

      // Add a lantern above the center
      BlockPos lanternPos = new BlockPos(centerX, box.maxY() + 1, centerZ);
      if (level.getBlockState(lanternPos.below()).isAir()) {
        level.setBlock(lanternPos, lantern, 2);
      }

      // Add some decorative elements
      if (random.nextFloat() < 0.3f) {
        // Add a table with books
        BlockPos tablePos =
            new BlockPos(centerX, box.minY() + 1, centerZ)
                .relative(direction.getOpposite(), size / 4);

        if (level.getBlockState(tablePos.below()).isSolid()) {
          level.setBlock(
              tablePos,
              Blocks.BARREL
                  .defaultBlockState()
                  .setValue(BarrelBlock.FACING, direction.getOpposite()),
              2);

          // Add some books on top
          if (level.getBlockState(tablePos.above()).isAir()) {
            level.setBlock(tablePos.above(), Blocks.BOOKSHELF.defaultBlockState(), 2);
          }
        }
      }
    }
  }
}
