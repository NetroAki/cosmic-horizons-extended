package com.netroaki.chex.world.library.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.CHEXStructures;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class LibraryBookshelfStructure extends Structure {
  public static final Codec<LibraryBookshelfStructure> CODEC =
      RecordCodecBuilder.create(
          builder ->
              builder
                  .group(
                      settingsCodec(builder),
                      Codec.INT.fieldOf("min_height").forGetter(structure -> structure.minHeight),
                      Codec.INT.fieldOf("max_height").forGetter(structure -> structure.maxHeight))
                  .apply(builder, LibraryBookshelfStructure::new));

  private final int minHeight;
  private final int maxHeight;

  public LibraryBookshelfStructure(StructureSettings settings, int minHeight, int maxHeight) {
    super(settings);
    this.minHeight = minHeight;
    this.maxHeight = maxHeight;
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

    // Generate bookshelf structure
    int height = 3 + random.nextInt(3); // 3-5 blocks tall
    int width = 1 + random.nextInt(3); // 1-3 blocks wide
    int depth = 1 + random.nextInt(3); // 1-3 blocks deep

    // Random rotation (0, 90, 180, 270 degrees)
    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);

    // Add the structure piece
    builder.addPiece(
        new BookshelfPiece(
            new BlockPos(x, y + 1, z), height, width, depth, direction, random.nextLong()));
  }

  @Override
  public StructureType<?> type() {
    return CHEXStructures.BOOKSHELF_STRUCTURE.get();
  }

  public static class BookshelfPiece extends StructurePiece {
    private final int height;
    private final int width;
    private final int depth;
    private final Direction direction;

    public BookshelfPiece(
        BlockPos pos, int height, int width, int depth, Direction direction, long seed) {
      super(
          CHEXStructures.BOOKSHELF_PIECE.get(),
          0,
          calculateBoundingBox(pos, width, height, depth, direction));
      this.height = height;
      this.width = width;
      this.depth = depth;
      this.direction = direction;
    }

    public BookshelfPiece(StructurePieceSerializationContext context, CompoundTag tag) {
      super(CHEXStructures.BOOKSHELF_PIECE.get(), tag);
      this.height = tag.getInt("Height");
      this.width = tag.getInt("Width");
      this.depth = tag.getInt("Depth");
      this.direction = Direction.from2DDataValue(tag.getInt("Direction"));
    }

    private static BoundingBox calculateBoundingBox(
        BlockPos pos, int width, int height, int depth, Direction direction) {
      int x0 = pos.getX();
      int y = pos.getY();
      int z0 = pos.getZ();

      int x1 = x0;
      int z1 = z0;

      switch (direction) {
        case NORTH -> x1 += width - 1;
        case SOUTH -> x1 += width - 1;
        case EAST -> z1 += width - 1;
        case WEST -> z1 += width - 1;
        default -> {}
      }

      return new BoundingBox(
          Math.min(x0, x1),
          y,
          Math.min(z0, z1),
          Math.max(x0, x1) + 1,
          y + height,
          Math.max(z0, z1) + 1);
    }

    @Override
    protected void addAdditionalSaveData(
        StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putInt("Height", height);
      tag.putInt("Width", width);
      tag.putInt("Depth", depth);
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

      BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
      BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
      BlockState slab = Blocks.OAK_SLAB.defaultBlockState();
      BlockState stair =
          Blocks.OAK_STAIRS
              .defaultBlockState()
              .setValue(StairBlock.FACING, direction.getOpposite());

      BlockState topSlab = slab.setValue(SlabBlock.TYPE, SlabType.TOP);

      // Generate the bookshelf structure
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          for (int z = 0; z < depth; z++) {
            BlockPos blockPos =
                new BlockPos(
                    box.minX() + x + (direction.getAxis() == Direction.Axis.X ? 0 : 1),
                    box.minY() + y,
                    box.minZ() + z + (direction.getAxis() == Direction.Axis.Z ? 0 : 1));

            // Only place blocks within the current chunk
            if (level.getChunk(blockPos).getPos().equals(chunkPos)) {
              if (y == 0) {
                // Bottom layer - planks
                level.setBlock(blockPos, planks, 2);
              } else if (y == height - 1) {
                // Top layer - slab
                level.setBlock(blockPos, topSlab, 2);
              } else {
                // Middle layers - bookshelves
                level.setBlock(blockPos, bookshelf, 2);
              }
            }
          }
        }
      }

      // Add some decorative elements
      if (random.nextFloat() < 0.3f) {
        // Add a lantern on top
        BlockPos lanternPos =
            new BlockPos(box.minX() + width / 2, box.maxY(), box.minZ() + depth / 2);

        if (level.getBlockState(lanternPos).isAir()
            && level.getBlockState(lanternPos.below()).is(Blocks.OAK_SLAB)) {
          level.setBlock(
              lanternPos,
              Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true),
              2);
        }
      }

      // Add some books on top
      if (random.nextFloat() < 0.5f) {
        BlockPos bookPos =
            new BlockPos(
                box.minX() + random.nextInt(width), box.maxY(), box.minZ() + random.nextInt(depth));

        if (level.getBlockState(bookPos).isAir()
            && level.getBlockState(bookPos.below()).is(Blocks.OAK_SLAB)) {
          level.setBlock(bookPos, Blocks.BOOKSHELF.defaultBlockState(), 2);
        }
      }
    }
  }
}
