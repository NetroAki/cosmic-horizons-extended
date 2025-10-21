package com.netroaki.chex.world.library.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.CHEXStructures;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class LibraryRoomStructure extends Structure {
  public static final Codec<LibraryRoomStructure> CODEC =
      RecordCodecBuilder.create(
          builder ->
              builder
                  .group(
                      settingsCodec(builder),
                      Codec.INT
                          .fieldOf("min_room_size")
                          .forGetter(structure -> structure.minRoomSize),
                      Codec.INT
                          .fieldOf("max_room_size")
                          .forGetter(structure -> structure.maxRoomSize))
                  .apply(builder, LibraryRoomStructure::new));

  private final int minRoomSize;
  private final int maxRoomSize;

  // Room themes
  private enum RoomTheme {
    STUDY,
    ARCHIVE,
    OBSERVATORY,
    ALCHEMY,
    TREASURE
  }

  public LibraryRoomStructure(StructureSettings settings, int minRoomSize, int maxRoomSize) {
    super(settings);
    this.minRoomSize = minRoomSize;
    this.maxRoomSize = maxRoomSize;
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

    // Find a suitable position for the room
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

    // Generate room with random size and theme
    int roomSize = minRoomSize + random.nextInt(maxRoomSize - minRoomSize + 1);
    RoomTheme theme = RoomTheme.values()[random.nextInt(RoomTheme.values().length)];

    // Add the room structure piece
    builder.addPiece(
        new LibraryRoomPiece(new BlockPos(x, y + 1, z), roomSize, theme, random.nextLong()));
  }

  @Override
  public StructureType<?> type() {
    return CHEXStructures.LIBRARY_ROOM_STRUCTURE.get();
  }

  public static class LibraryRoomPiece extends StructurePiece {
    private final int roomSize;
    private final RoomTheme theme;
    private final Random random;

    public LibraryRoomPiece(BlockPos pos, int roomSize, RoomTheme theme, long seed) {
      super(CHEXStructures.LIBRARY_ROOM_PIECE.get(), 0, calculateBoundingBox(pos, roomSize));
      this.roomSize = roomSize;
      this.theme = theme;
      this.random = new Random(seed);
    }

    public LibraryRoomPiece(StructurePieceSerializationContext context, CompoundTag tag) {
      super(CHEXStructures.LIBRARY_ROOM_PIECE.get(), tag);
      this.roomSize = tag.getInt("RoomSize");
      this.theme = RoomTheme.values()[tag.getInt("Theme")];
      this.random = new Random(tag.getLong("Seed"));
    }

    private static BoundingBox calculateBoundingBox(BlockPos pos, int roomSize) {
      int radius = roomSize / 2;
      return new BoundingBox(
          pos.getX() - radius,
          pos.getY(),
          pos.getZ() - radius,
          pos.getX() + radius + 1,
          pos.getY() + 6, // Rooms are 6 blocks tall
          pos.getZ() + radius + 1);
    }

    @Override
    protected void addAdditionalSaveData(
        StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putInt("RoomSize", roomSize);
      tag.putInt("Theme", theme.ordinal());
      tag.putLong("Seed", random.nextLong());
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

      // Common blocks
      BlockState planks = getPlanks(theme);
      BlockState log = getLog(theme);
      BlockState slab = getSlab(theme);
      BlockState stairs = getStairs(theme);
      BlockState carpet = getCarpet(theme);
      BlockState lantern = Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true);
      BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
      BlockState glass = Blocks.GLASS_PANE.defaultBlockState();

      BlockState topSlab = slab.setValue(SlabBlock.TYPE, SlabType.TOP);
      BlockState bottomSlab = slab.setValue(SlabBlock.TYPE, SlabType.BOTTOM);

      int centerX = box.minX() + (box.getXSpan() / 2);
      int centerZ = box.minZ() + (box.getZSpan() / 2);
      int radius = roomSize / 2;

      // Floor and ceiling
      for (int x = box.minX(); x <= box.maxX(); x++) {
        for (int z = box.minZ(); z <= box.maxZ(); z++) {
          // Floor
          if (level.getBlockState(new BlockPos(x, box.minY() - 1, z)).isSolid()) {
            // Main floor
            level.setBlock(new BlockPos(x, box.minY(), z), planks, 2);

            // Carpet in the center
            int dx = x - centerX;
            int dz = z - centerZ;
            if (dx * dx + dz * dz <= (radius - 1) * (radius - 1)) {
              level.setBlock(new BlockPos(x, box.minY() + 1, z), carpet, 2);
            }
          }

          // Ceiling
          level.setBlock(new BlockPos(x, box.maxY(), z), planks, 2);
        }
      }

      // Walls and decorations
      for (int y = box.minY(); y <= box.maxY(); y++) {
        for (int x = box.minX(); x <= box.maxX(); x++) {
          for (int z = box.minZ(); z <= box.maxZ(); z++) {
            boolean isWall =
                x == box.minX() || x == box.maxX() || z == box.minZ() || z == box.maxZ();
            boolean isCorner =
                (x == box.minX() || x == box.maxX()) && (z == box.minZ() || z == box.maxZ());

            if (isWall) {
              // Logs in corners
              if (isCorner) {
                level.setBlock(new BlockPos(x, y, z), log, 2);
              }
              // Bookshelves on walls
              else if (y > box.minY() && y < box.maxY() && random.nextFloat() < 0.8f) {
                level.setBlock(new BlockPos(x, y, z), bookshelf, 2);
              }
              // Windows in the upper part of walls
              else if (y == box.minY() + 4 && random.nextFloat() < 0.3f) {
                level.setBlock(new BlockPos(x, y, z), glass, 2);
              }
            }
          }
        }
      }

      // Add theme-specific decorations
      switch (theme) {
        case STUDY -> decorateStudy(level, box, planks, stairs, lantern, bookshelf);
        case ARCHIVE -> decorateArchive(level, box, planks, bookshelf, lantern);
        case OBSERVATORY -> decorateObservatory(level, box, glass, lantern);
        case ALCHEMY -> decorateAlchemy(level, box, planks, lantern);
        case TREASURE -> decorateTreasure(level, box, planks, lantern);
      }

      // Add lighting
      if (random.nextFloat() < 0.2f) {
        BlockPos chandelierPos = new BlockPos(centerX, box.maxY() - 1, centerZ);
        if (level.getBlockState(chandelierPos.below()).isAir()) {
          level.setBlock(chandelierPos, lantern, 2);
        }
      }

      // Add entrance/exit
      for (Direction dir : Direction.Plane.HORIZONTAL) {
        if (random.nextFloat() < 0.3f) {
          BlockPos doorPos =
              new BlockPos(
                  centerX + dir.getStepX() * (radius - 1),
                  box.minY() + 1,
                  centerZ + dir.getStepZ() * (radius - 1));

          if (level.getBlockState(doorPos).isAir()) {
            level.setBlock(
                doorPos,
                Blocks.OAK_DOOR
                    .defaultBlockState()
                    .setValue(DoorBlock.FACING, dir.getOpposite())
                    .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER),
                2);
            level.setBlock(
                doorPos.above(),
                Blocks.OAK_DOOR
                    .defaultBlockState()
                    .setValue(DoorBlock.FACING, dir.getOpposite())
                    .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER),
                2);
          }
        }
      }
    }

    private void decorateStudy(
        WorldGenLevel level,
        BoundingBox box,
        BlockState planks,
        BlockState stairs,
        BlockState lantern,
        BlockState bookshelf) {
      int centerX = box.minX() + (box.getXSpan() / 2);
      int centerZ = box.minZ() + (box.getZSpan() / 2);

      // Add a large table in the center
      int tableRadius = roomSize / 4;
      for (int x = -tableRadius; x <= tableRadius; x++) {
        for (int z = -tableRadius; z <= tableRadius; z++) {
          if (x * x + z * z <= tableRadius * tableRadius) {
            BlockPos pos = new BlockPos(centerX + x, box.minY() + 1, centerZ + z);
            if (level.getBlockState(pos.below()).isSolid()) {
              level.setBlock(pos, planks, 2);

              // Add some books on the table
              if (random.nextFloat() < 0.3f) {
                level.setBlock(pos.above(), bookshelf, 2);
              }
            }
          }
        }
      }

      // Add chairs around the table
      for (Direction dir : Direction.Plane.HORIZONTAL) {
        BlockPos chairPos =
            new BlockPos(
                centerX + dir.getStepX() * (tableRadius + 1),
                box.minY() + 1,
                centerZ + dir.getStepZ() * (tableRadius + 1));

        if (level.getBlockState(chairPos.below()).isSolid()) {
          level.setBlock(
              chairPos,
              stairs
                  .setValue(StairBlock.FACING, dir.getOpposite())
                  .setValue(StairBlock.HALF, Half.BOTTOM),
              2);
        }
      }

      // Add some bookshelves along the walls
      for (Direction dir : Direction.Plane.HORIZONTAL) {
        for (int i = -2; i <= 2; i++) {
          if (i == 0) continue;

          BlockPos shelfPos =
              new BlockPos(
                  centerX + dir.getStepX() * (roomSize / 2 - 1) + dir.getClockWise().getStepX() * i,
                  box.minY() + 1,
                  centerZ
                      + dir.getStepZ() * (roomSize / 2 - 1)
                      + dir.getClockWise().getStepZ() * i);

          if (level.getBlockState(shelfPos.below()).isSolid()) {
            level.setBlock(shelfPos, bookshelf, 2);
            if (random.nextFloat() < 0.7f) {
              level.setBlock(shelfPos.above(), bookshelf, 2);
            }
          }
        }
      }
    }

    private void decorateArchive(
        WorldGenLevel level,
        BoundingBox box,
        BlockState planks,
        BlockState bookshelf,
        BlockState lantern) {
      // Fill the room with bookshelves in a grid pattern
      int spacing = 2;
      for (int x = box.minX() + 1; x < box.maxX(); x += spacing) {
        for (int z = box.minZ() + 1; z < box.maxZ(); z += spacing) {
          if (level.getBlockState(new BlockPos(x, box.minY() - 1, z)).isSolid()) {
            // Create tall bookshelf stacks
            int height = 1 + random.nextInt(3);
            for (int y = 0; y < height; y++) {
              level.setBlock(new BlockPos(x, box.minY() + y, z), bookshelf, 2);
            }

            // Add lanterns on top of some bookshelves
            if (random.nextFloat() < 0.2f && height > 1) {
              BlockPos lightPos = new BlockPos(x, box.minY() + height, z);
              if (level.getBlockState(lightPos).isAir()) {
                level.setBlock(lightPos, lantern, 2);
              }
            }
          }
        }
      }

      // Add ladders for accessing higher shelves
      for (Direction dir : Direction.Plane.HORIZONTAL) {
        if (random.nextFloat() < 0.5f) {
          BlockPos ladderPos =
              new BlockPos(
                  box.minX() + 1 + random.nextInt(box.getXSpan() - 2),
                  box.minY(),
                  box.minZ() + 1 + random.nextInt(box.getZSpan() - 2));

          if (level.getBlockState(ladderPos.below()).isSolid()) {
            int ladderHeight = 2 + random.nextInt(3);
            for (int y = 0; y < ladderHeight; y++) {
              BlockPos pos = ladderPos.above(y);
              if (pos.getY() < box.maxY() && level.getBlockState(pos).isAir()) {
                level.setBlock(
                    pos,
                    Blocks.LADDER
                        .defaultBlockState()
                        .setValue(
                            LadderBlock.FACING,
                            Direction.Plane.HORIZONTAL.getRandomDirection(random)),
                    2);
              }
            }
          }
        }
      }
    }

    private void decorateObservatory(
        WorldGenLevel level, BoundingBox box, BlockState glass, BlockState lantern) {
      int centerX = box.minX() + (box.getXSpan() / 2);
      int centerZ = box.minZ() + (box.getZSpan() / 2);

      // Create a glass dome ceiling
      int radius = roomSize / 2;
      for (int x = box.minX(); x <= box.maxX(); x++) {
        for (int z = box.minZ(); z <= box.maxZ(); z++) {
          int dx = x - centerX;
          int dz = z - centerZ;
          double distance = Math.sqrt(dx * dx + dz * dz);

          if (distance <= radius + 0.5) {
            // Dome shape
            int height = (int) (Math.sqrt(radius * radius - distance * distance) * 0.7);

            for (int y = 0; y < height; y++) {
              BlockPos pos = new BlockPos(x, box.maxY() - y, z);
              if (level.getBlockState(pos).getMaterial() != Material.AIR) {
                level.setBlock(pos, glass, 2);
              }
            }
          }
        }
      }

      // Add a telescope or observation platform
      BlockPos platformPos = new BlockPos(centerX, box.minY() + 1, centerZ);
      if (level.getBlockState(platformPos.below()).isSolid()) {
        // Create a raised platform
        for (int x = -1; x <= 1; x++) {
          for (int z = -1; z <= 1; z++) {
            level.setBlock(
                platformPos.offset(x, 0, z), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 2);
          }
        }

        // Add a telescope (using a tripod and end rod)
        level.setBlock(platformPos.above(1), Blocks.IRON_BARS.defaultBlockState(), 2);
        level.setBlock(platformPos.above(2), Blocks.IRON_BARS.defaultBlockState(), 2);
        level.setBlock(
            platformPos.above(3),
            Blocks.END_ROD.defaultBlockState().setValue(EndRodBlock.FACING, Direction.UP),
            2);

        // Add some books and papers around
        for (int i = 0; i < 4; i++) {
          Direction dir = Direction.from2DDataValue(i);
          BlockPos paperPos = platformPos.relative(dir, 2);
          if (level.getBlockState(paperPos.below()).isSolid()) {
            level.setBlock(
                paperPos,
                Blocks.LECTERN.defaultBlockState().setValue(LecternBlock.FACING, dir.getOpposite()),
                2);

            // Add a book on the lectern
            // Note: This would need to be handled with a block entity in a real implementation
          }
        }
      }
    }

    private void decorateAlchemy(
        WorldGenLevel level, BoundingBox box, BlockState planks, BlockState lantern) {
      int centerX = box.minX() + (box.getXSpan() / 2);
      int centerZ = box.minZ() + (box.getZSpan() / 2);

      // Add brewing stands and potion shelves
      for (int i = 0; i < 2 + random.nextInt(3); i++) {
        int x = box.minX() + 2 + random.nextInt(box.getXSpan() - 4);
        int z = box.minZ() + 2 + random.nextInt(box.getZSpan() - 4);
        BlockPos standPos = new BlockPos(x, box.minY() + 1, z);

        if (level.getBlockState(standPos.below()).isSolid()) {
          // Place a brewing stand
          level.setBlock(standPos, Blocks.BREWING_STAND.defaultBlockState(), 2);

          // Add potion shelves above
          if (random.nextFloat() < 0.7f) {
            for (int y = 1; y <= 1 + random.nextInt(2); y++) {
              BlockPos shelfPos = standPos.above(y);
              if (level.getBlockState(shelfPos).isAir()) {
                level.setBlock(shelfPos, Blocks.BOOKSHELF.defaultBlockState(), 2);

                // Add some potions on the shelf
                if (random.nextFloat() < 0.3f) {
                  level.setBlock(shelfPos.above(), Blocks.GLASS_PANE.defaultBlockState(), 2);
                }
              }
            }
          }
        }
      }

      // Add a cauldron in the center
      BlockPos cauldronPos = new BlockPos(centerX, box.minY() + 1, centerZ);
      if (level.getBlockState(cauldronPos.below()).isSolid()) {
        level.setBlock(cauldronPos, Blocks.CAULDRON.defaultBlockState(), 2);

        // Add some ingredients on the floor around the cauldron
        for (Direction dir : Direction.Plane.HORIZONTAL) {
          BlockPos ingredientPos = cauldronPos.relative(dir);
          if (level.getBlockState(ingredientPos.below()).isSolid()) {
            level.setBlock(
                ingredientPos,
                random.nextBoolean()
                    ? Blocks.REDSTONE_WIRE.defaultBlockState()
                    : Blocks.GLOWSTONE.defaultBlockState(),
                2);
          }
        }
      }

      // Add some bookshelves with potion recipes
      for (Direction dir : Direction.Plane.HORIZONTAL) {
        BlockPos shelfPos =
            new BlockPos(
                centerX + dir.getStepX() * (roomSize / 2 - 2),
                box.minY() + 1,
                centerZ + dir.getStepZ() * (roomSize / 2 - 2));

        if (level.getBlockState(shelfPos.below()).isSolid()) {
          level.setBlock(shelfPos, Blocks.BOOKSHELF.defaultBlockState(), 2);
          level.setBlock(shelfPos.above(), Blocks.ENCHANTING_TABLE.defaultBlockState(), 2);
        }
      }
    }

    private void decorateTreasure(
        WorldGenLevel level, BoundingBox box, BlockState planks, BlockState lantern) {
      int centerX = box.minX() + (box.getXSpan() / 2);
      int centerZ = box.minZ() + (box.getZSpan() / 2);

      // Create a treasure pedestal in the center
      BlockPos pedestalPos = new BlockPos(centerX, box.minY() + 1, centerZ);
      if (level.getBlockState(pedestalPos.below()).isSolid()) {
        // Create a multi-level pedestal
        level.setBlock(pedestalPos, Blocks.POLISHED_BLACKSTONE.defaultBlockState(), 2);
        level.setBlock(
            pedestalPos.above(1), Blocks.POLISHED_BLACKSTONE_SLAB.defaultBlockState(), 2);
        level.setBlock(pedestalPos.above(2), Blocks.GOLD_BLOCK.defaultBlockState(), 2);

        // Add a special item on top (e.g., enchanted book, rare item)
        level.setBlock(pedestalPos.above(3), Blocks.ENCHANTED_BOOK.defaultBlockState(), 2);

        // Add some decorative chains from the ceiling
        for (Direction dir : Direction.Plane.HORIZONTAL) {
          BlockPos chainPos =
              new BlockPos(
                  centerX + dir.getStepX() * 2, box.maxY() - 1, centerZ + dir.getStepZ() * 2);

          for (int y = 0; y < 2; y++) {
            BlockPos pos = chainPos.below(y);
            if (level.getBlockState(pos).isAir()) {
              level.setBlock(
                  pos,
                  Blocks.CHAIN.defaultBlockState().setValue(ChainBlock.AXIS, Direction.Axis.Y),
                  2);
            }
          }
        }
      }

      // Add some treasure chests around the room
      for (Direction dir : Direction.Plane.HORIZONTAL) {
        BlockPos chestPos =
            new BlockPos(
                centerX + dir.getStepX() * (roomSize / 3),
                box.minY() + 1,
                centerZ + dir.getStepZ() * (roomSize / 3));

        if (level.getBlockState(chestPos.below()).isSolid()) {
          level.setBlock(
              chestPos,
              Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, dir.getOpposite()),
              2);

          // Add some gold blocks and other valuables around the chest
          for (Direction side : Direction.Plane.HORIZONTAL) {
            if (side != dir && side != dir.getOpposite()) {
              BlockPos goldPos = chestPos.relative(side);
              if (level.getBlockState(goldPos.below()).isSolid()) {
                level.setBlock(goldPos, Blocks.GOLD_BLOCK.defaultBlockState(), 2);
              }
            }
          }
        }
      }

      // Add some decorative elements to the walls
      for (Direction dir : Direction.Plane.HORIZONTAL) {
        BlockPos wallPos =
            new BlockPos(
                centerX + dir.getStepX() * (roomSize / 2 - 1),
                box.minY() + 2,
                centerZ + dir.getStepZ() * (roomSize / 2 - 1));

        // Add item frames with rare items
        if (level.getBlockState(wallPos.below()).isSolid()) {
          level.setBlock(
              wallPos,
              Blocks.ITEM_FRAME
                  .defaultBlockState()
                  .setValue(ItemFrameBlock.FACING, dir.getOpposite()),
              2);

          // Add a torch below the item frame
          BlockPos torchPos = wallPos.below();
          if (level.getBlockState(torchPos.below()).isSolid()) {
            level.setBlock(
                torchPos,
                Blocks.WALL_TORCH
                    .defaultBlockState()
                    .setValue(WallTorchBlock.FACING, dir.getOpposite()),
                2);
          }
        }
      }
    }

    private BlockState getPlanks(RoomTheme theme) {
      return switch (theme) {
        case STUDY -> Blocks.OAK_PLANKS.defaultBlockState();
        case ARCHIVE -> Blocks.SPRUCE_PLANKS.defaultBlockState();
        case OBSERVATORY -> Blocks.DARK_OAK_PLANKS.defaultBlockState();
        case ALCHEMY -> Blocks.WARPED_PLANKS.defaultBlockState();
        case TREASURE -> Blocks.CRIMSON_PLANKS.defaultBlockState();
      };
    }

    private BlockState getLog(RoomTheme theme) {
      return switch (theme) {
        case STUDY -> Blocks.OAK_LOG.defaultBlockState();
        case ARCHIVE -> Blocks.SPRUCE_LOG.defaultBlockState();
        case OBSERVATORY -> Blocks.DARK_OAK_LOG.defaultBlockState();
        case ALCHEMY -> Blocks.WARPED_STEM.defaultBlockState();
        case TREASURE -> Blocks.CRIMSON_STEM.defaultBlockState();
      };
    }

    private BlockState getSlab(RoomTheme theme) {
      return switch (theme) {
        case STUDY -> Blocks.OAK_SLAB.defaultBlockState();
        case ARCHIVE -> Blocks.SPRUCE_SLAB.defaultBlockState();
        case OBSERVATORY -> Blocks.DARK_OAK_SLAB.defaultBlockState();
        case ALCHEMY -> Blocks.WARPED_SLAB.defaultBlockState();
        case TREASURE -> Blocks.CRIMSON_SLAB.defaultBlockState();
      };
    }

    private BlockState getStairs(RoomTheme theme) {
      return switch (theme) {
        case STUDY -> Blocks.OAK_STAIRS.defaultBlockState();
        case ARCHIVE -> Blocks.SPRUCE_STAIRS.defaultBlockState();
        case OBSERVATORY -> Blocks.DARK_OAK_STAIRS.defaultBlockState();
        case ALCHEMY -> Blocks.WARPED_STAIRS.defaultBlockState();
        case TREASURE -> Blocks.CRIMSON_STAIRS.defaultBlockState();
      };
    }

    private BlockState getCarpet(RoomTheme theme) {
      return switch (theme) {
        case STUDY -> Blocks.RED_CARPET.defaultBlockState();
        case ARCHIVE -> Blocks.BLUE_CARPET.defaultBlockState();
        case OBSERVATORY -> Blocks.PURPLE_CARPET.defaultBlockState();
        case ALCHEMY -> Blocks.LIME_CARPET.defaultBlockState();
        case TREASURE -> Blocks.YELLOW_CARPET.defaultBlockState();
      };
    }
  }
}
