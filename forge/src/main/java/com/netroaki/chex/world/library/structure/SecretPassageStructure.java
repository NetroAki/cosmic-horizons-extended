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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class SecretPassageStructure extends Structure {
  public static final Codec<SecretPassageStructure> CODEC =
      RecordCodecBuilder.create(
          builder ->
              builder
                  .group(
                      settingsCodec(builder),
                      Codec.INT.fieldOf("min_length").forGetter(structure -> structure.minLength),
                      Codec.INT.fieldOf("max_length").forGetter(structure -> structure.maxLength))
                  .apply(builder, SecretPassageStructure::new));

  private final int minLength;
  private final int maxLength;

  // Types of secret passages
  private enum PassageType {
    BOOKSHELF_DOOR,
    HIDDEN_STAIRCASE,
    WALL_LEVER,
    FLOOR_TRAPDOOR,
    CEILING_CHANDELIER
  }

  public SecretPassageStructure(StructureSettings settings, int minLength, int maxLength) {
    super(settings);
    this.minLength = minLength;
    this.maxLength = maxLength;
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

    // Find a suitable position for the secret passage
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

    // Select a random passage type
    PassageType passageType = PassageType.values()[random.nextInt(PassageType.values().length)];
    int length = minLength + random.nextInt(maxLength - minLength + 1);
    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);

    // Add the secret passage piece
    builder.addPiece(
        new SecretPassagePiece(
            new BlockPos(x, y + 1, z), length, passageType, direction, random.nextLong()));
  }

  @Override
  public StructureType<?> type() {
    return CHEXStructures.SECRET_PASSAGE_STRUCTURE.get();
  }

  public static class SecretPassagePiece extends StructurePiece {
    private final int length;
    private final PassageType passageType;
    private final Direction direction;
    private final Random random;

    public SecretPassagePiece(
        BlockPos pos, int length, PassageType passageType, Direction direction, long seed) {
      super(
          CHEXStructures.SECRET_PASSAGE_PIECE.get(),
          0,
          calculateBoundingBox(pos, length, direction));
      this.length = length;
      this.passageType = passageType;
      this.direction = direction;
      this.random = new Random(seed);
    }

    public SecretPassagePiece(StructurePieceSerializationContext context, CompoundTag tag) {
      super(CHEXStructures.SECRET_PASSAGE_PIECE.get(), tag);
      this.length = tag.getInt("Length");
      this.passageType = PassageType.values()[tag.getInt("PassageType")];
      this.direction = Direction.from2DDataValue(tag.getInt("Direction"));
      this.random = new Random(tag.getLong("Seed"));
    }

    private static BoundingBox calculateBoundingBox(BlockPos pos, int length, Direction direction) {
      return switch (direction) {
        case NORTH -> new BoundingBox(
            pos.getX() - 1,
            pos.getY() - 2,
            pos.getZ() - length,
            pos.getX() + 1,
            pos.getY() + 2,
            pos.getZ());
        case SOUTH -> new BoundingBox(
            pos.getX() - 1,
            pos.getY() - 2,
            pos.getZ(),
            pos.getX() + 1,
            pos.getY() + 2,
            pos.getZ() + length);
        case WEST -> new BoundingBox(
            pos.getX() - length,
            pos.getY() - 2,
            pos.getZ() - 1,
            pos.getX(),
            pos.getY() + 2,
            pos.getZ() + 1);
        case EAST -> new BoundingBox(
            pos.getX(),
            pos.getY() - 2,
            pos.getZ() - 1,
            pos.getX() + length,
            pos.getY() + 2,
            pos.getZ() + 1);
        default -> new BoundingBox(pos, pos);
      };
    }

    @Override
    protected void addAdditionalSaveData(
        StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putInt("Length", length);
      tag.putInt("PassageType", passageType.ordinal());
      tag.putInt("Direction", direction.get2DDataValue());
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

      BlockState planks = Blocks.OAK_PLANKS.defaultBlockState();
      BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
      BlockState air = Blocks.AIR.defaultBlockState();
      BlockState stoneBricks = Blocks.STONE_BRICKS.defaultBlockState();
      BlockState mossyBricks = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
      BlockState crackedBricks = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
      BlockState chiseledBricks = Blocks.CHISELED_STONE_BRICKS.defaultBlockState();
      BlockState lever = Blocks.LEVER.defaultBlockState();
      BlockState torch = Blocks.WALL_TORCH.defaultBlockState();
      BlockState ladder = Blocks.LADDER.defaultBlockState();
      BlockState trapdoor = Blocks.OAK_TRAPDOOR.defaultBlockState();
      BlockState chain = Blocks.CHAIN.defaultBlockState();
      BlockState lantern = Blocks.LANTERN.defaultBlockState();

      // Generate the appropriate passage based on type
      switch (passageType) {
        case BOOKSHELF_DOOR -> generateBookshelfDoor(level, box, planks, bookshelf, air);
        case HIDDEN_STAIRCASE -> generateHiddenStaircase(level, box, planks, air, ladder);
        case WALL_LEVER -> generateWallLever(
            level, box, stoneBricks, mossyBricks, crackedBricks, lever, torch);
        case FLOOR_TRAPDOOR -> generateFloorTrapdoor(level, box, planks, air, trapdoor);
        case CEILING_CHANDELIER -> generateCeilingChandelier(
            level, box, planks, air, chain, lantern);
      }
    }

    private void generateBookshelfDoor(
        WorldGenLevel level,
        BoundingBox box,
        BlockState planks,
        BlockState bookshelf,
        BlockState air) {
      // Create a hidden door in a bookshelf wall
      BlockPos startPos = new BlockPos(box.minX(), box.minY() + 1, box.minZ());

      // Create a 3x3 bookshelf wall with a door in the middle
      for (int y = 0; y < 3; y++) {
        for (int x = 0; x < 3; x++) {
          BlockPos pos = startPos.offset(x, y, 0);

          // Leave the middle block empty for the door
          if (x == 1 && y == 1) {
            level.setBlock(pos, air, 2);
            continue;
          }

          // Place a bookshelf with a button on the side
          level.setBlock(pos, bookshelf, 2);

          // Add a button on the side of the bookshelf that opens the door
          if ((x == 0 || x == 2) && y == 1) {
            Direction buttonDir = (x == 0) ? Direction.WEST : Direction.EAST;
            BlockPos buttonPos = pos.relative(buttonDir);
            if (level.getBlockState(buttonPos).isAir()) {
              level.setBlock(
                  buttonPos,
                  Blocks.OAK_BUTTON
                      .defaultBlockState()
                      .setValue(BlockStateProperties.HORIZONTAL_FACING, buttonDir),
                  2);
            }
          }
        }
      }

      // Create a hidden passage behind the bookshelf
      BlockPos passageStart = startPos.offset(1, 1, 0);
      for (int i = 1; i <= length; i++) {
        BlockPos floorPos = passageStart.offset(0, -1, direction.getStepZ() * i);
        BlockPos ceilingPos = passageStart.offset(0, 2, direction.getStepZ() * i);
        BlockPos wall1Pos = passageStart.offset(-1, 0, direction.getStepZ() * i);
        BlockPos wall2Pos = passageStart.offset(1, 0, direction.getStepZ() * i);

        // Floor and ceiling
        level.setBlock(floorPos, planks, 2);
        level.setBlock(ceilingPos, planks, 2);

        // Walls
        level.setBlock(wall1Pos, bookshelf, 2);
        level.setBlock(wall2Pos, bookshelf, 2);

        // Add some torches for lighting
        if (i % 3 == 0) {
          BlockPos torchPos = wall1Pos.relative(direction.getOpposite());
          if (level.getBlockState(torchPos).isAir()) {
            level.setBlock(
                torchPos, torch.setValue(WallTorchBlock.FACING, direction.getOpposite()), 2);
          }
        }
      }

      // Add a secret room at the end of the passage
      if (length > 5) {
        BlockPos roomCenter = passageStart.offset(0, 0, direction.getStepZ() * (length - 2));
        createSecretRoom(level, roomCenter, planks, bookshelf);
      }
    }

    private void generateHiddenStaircase(
        WorldGenLevel level,
        BoundingBox box,
        BlockState planks,
        BlockState air,
        BlockState ladder) {
      // Create a hidden staircase behind a wall
      BlockPos startPos = new BlockPos(box.minX(), box.minY(), box.minZ());

      // Create a 3x3 wall with a hidden entrance
      for (int y = 0; y < 4; y++) {
        for (int x = 0; x < 3; x++) {
          BlockPos pos = startPos.offset(x, y, 0);

          // Leave a gap at the bottom for the entrance
          if (y == 0 && x == 1) {
            level.setBlock(pos, air, 2);
            continue;
          }

          level.setBlock(pos, planks, 2);
        }
      }

      // Create a descending staircase
      for (int i = 1; i <= length; i++) {
        int y = box.minY() - i;

        // Stair steps
        for (int x = 0; x < 3; x++) {
          BlockPos stepPos = startPos.offset(x, y - box.minY(), i);
          level.setBlock(stepPos, planks, 2);
        }

        // Ladder on one side
        BlockPos ladderPos = startPos.offset(2, y - box.minY() + 1, i);
        level.setBlock(ladderPos, ladder.setValue(LadderBlock.FACING, Direction.WEST), 2);

        // Torch for lighting
        if (i % 3 == 0) {
          BlockPos torchPos = startPos.offset(0, y - box.minY() + 1, i);
          if (level.getBlockState(torchPos).isAir()) {
            level.setBlock(torchPos, torch.setValue(WallTorchBlock.FACING, Direction.SOUTH), 2);
          }
        }
      }

      // Add a hidden room at the bottom
      if (length > 5) {
        BlockPos roomCenter = startPos.offset(1, box.minY() - length, length / 2);
        createSecretRoom(level, roomCenter, planks, planks);
      }
    }

    private void generateWallLever(
        WorldGenLevel level,
        BoundingBox box,
        BlockState stoneBricks,
        BlockState mossyBricks,
        BlockState crackedBricks,
        BlockState lever,
        BlockState torch) {
      // Create a hidden door activated by a lever
      BlockPos wallPos = new BlockPos(box.minX(), box.minY() + 1, box.minZ());

      // Create a stone brick wall with a hidden door
      for (int y = 0; y < 3; y++) {
        for (int x = 0; x < 5; x++) {
          BlockPos pos = wallPos.offset(x, y, 0);

          // Randomly mix stone brick types for a natural look
          BlockState brickType =
              switch (random.nextInt(10)) {
                case 0 -> mossyBricks;
                case 1 -> crackedBricks;
                case 2 -> Blocks.CHISELED_STONE_BRICKS.defaultBlockState();
                default -> stoneBricks;
              };

          // Leave a gap for the door
          if (x > 0 && x < 4 && y < 2) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            continue;
          }

          level.setBlock(pos, brickType, 2);
        }
      }

      // Add a lever on the side
      BlockPos leverPos = wallPos.offset(4, 1, 0);
      level.setBlock(leverPos, lever.setValue(LeverBlock.FACING, Direction.NORTH), 2);

      // Add a hidden passage behind the wall
      BlockPos passageStart = wallPos.offset(2, 1, 0);
      for (int i = 1; i <= length; i++) {
        BlockPos floorPos = passageStart.offset(0, -1, i);
        BlockPos ceilingPos = passageStart.offset(0, 2, i);
        BlockPos wall1Pos = passageStart.offset(-1, 0, i);
        BlockPos wall2Pos = passageStart.offset(1, 0, i);

        // Floor and ceiling
        level.setBlock(floorPos, stoneBricks, 2);
        level.setBlock(ceilingPos, stoneBricks, 2);

        // Walls with occasional torches
        level.setBlock(wall1Pos, stoneBricks, 2);
        level.setBlock(wall2Pos, stoneBricks, 2);

        // Add some torches for lighting
        if (i % 3 == 0) {
          BlockPos torchPos = wall1Pos.relative(Direction.SOUTH);
          if (level.getBlockState(torchPos).isAir()) {
            level.setBlock(torchPos, torch.setValue(WallTorchBlock.FACING, Direction.SOUTH), 2);
          }
        }
      }

      // Add a secret room at the end of the passage
      if (length > 5) {
        BlockPos roomCenter = passageStart.offset(0, 0, length / 2);
        createSecretRoom(level, roomCenter, stoneBricks, stoneBricks);
      }
    }

    private void generateFloorTrapdoor(
        WorldGenLevel level,
        BoundingBox box,
        BlockState planks,
        BlockState air,
        BlockState trapdoor) {
      // Create a hidden trapdoor in the floor
      BlockPos trapdoorPos = new BlockPos(box.minX() + 1, box.minY(), box.minZ() + 1);

      // Create a wooden floor
      for (int x = 0; x < 3; x++) {
        for (int z = 0; z < 3; z++) {
          BlockPos pos = trapdoorPos.offset(x, 0, z);
          level.setBlock(pos, planks, 2);
        }
      }

      // Add the trapdoor
      level.setBlock(
          trapdoorPos,
          trapdoor
              .setValue(TrapDoorBlock.OPEN, false)
              .setValue(TrapDoorBlock.HALF, DoubleBlockHalf.BOTTOM),
          2);

      // Add a ladder below the trapdoor
      for (int y = 1; y <= length; y++) {
        BlockPos ladderPos = trapdoorPos.below(y);
        level.setBlock(ladderPos, ladder.setValue(LadderBlock.FACING, Direction.NORTH), 2);

        // Add torches for lighting
        if (y % 3 == 0) {
          BlockPos torchPos = ladderPos.relative(Direction.WEST);
          if (level.getBlockState(torchPos).isAir()) {
            level.setBlock(torchPos, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 2);
          }
        }
      }

      // Add a hidden room at the bottom
      if (length > 5) {
        BlockPos roomCenter = trapdoorPos.below(length + 2);
        createSecretRoom(level, roomCenter, planks, planks);
      }
    }

    private void generateCeilingChandelier(
        WorldGenLevel level,
        BoundingBox box,
        BlockState planks,
        BlockState air,
        BlockState chain,
        BlockState lantern) {
      // Create a chandelier that opens a hidden passage when activated
      BlockPos chandelierPos = new BlockPos(box.minX() + 1, box.maxY() - 1, box.minZ() + 1);

      // Create a ceiling with a chandelier
      for (int x = 0; x < 3; x++) {
        for (int z = 0; z < 3; z++) {
          BlockPos pos = chandelierPos.offset(x, 0, z);
          level.setBlock(pos, planks, 2);
        }
      }

      // Add chains and lantern
      BlockPos chainPos = chandelierPos.offset(1, 0, 1);
      level.setBlock(chainPos.below(1), chain.setValue(ChainBlock.AXIS, Direction.Axis.Y), 2);
      level.setBlock(chainPos.below(2), lantern, 2);

      // Create a hidden passage that opens when the lantern is activated
      BlockPos passageStart = chandelierPos.below(3);
      for (int y = 0; y < length; y++) {
        BlockPos floorPos = passageStart.below(y * 2);
        BlockPos ceilingPos = passageStart.below(y * 2 + 1);

        // Create a vertical shaft
        level.setBlock(floorPos, air, 2);
        level.setBlock(ceilingPos, air, 2);

        // Add ladders on one side
        BlockPos ladderPos = floorPos.relative(Direction.NORTH);
        level.setBlock(ladderPos, ladder.setValue(LadderBlock.FACING, Direction.SOUTH), 2);

        // Add some decorative chains
        if (y % 2 == 0) {
          BlockPos chainSidePos = floorPos.relative(Direction.SOUTH);
          level.setBlock(chainSidePos, chain.setValue(ChainBlock.AXIS, Direction.Axis.Y), 2);
        }
      }

      // Add a hidden room at the bottom
      if (length > 3) {
        BlockPos roomCenter = passageStart.below(length * 2);
        createSecretRoom(level, roomCenter, planks, planks);
      }
    }

    private void createSecretRoom(
        WorldGenLevel level, BlockPos center, BlockState floorBlock, BlockState wallBlock) {
      int roomSize = 5 + random.nextInt(4); // 5-8 blocks in size
      int halfSize = roomSize / 2;

      // Create the room
      for (int x = -halfSize; x <= halfSize; x++) {
        for (int z = -halfSize; z <= halfSize; z++) {
          for (int y = -2; y <= 2; y++) {
            BlockPos pos = center.offset(x, y, z);

            // Skip if outside the room
            if (x == -halfSize
                || x == halfSize
                || z == -halfSize
                || z == halfSize
                || y == -2
                || y == 2) {

              // Create walls, floor, and ceiling
              if (y == -2) {
                // Floor
                level.setBlock(pos, floorBlock, 2);
              } else if (y == 2) {
                // Ceiling
                level.setBlock(pos, floorBlock, 2);
              } else {
                // Walls
                level.setBlock(pos, wallBlock, 2);
              }
            } else {
              // Air inside the room
              level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
          }
        }
      }

      // Add some decorative elements
      addRoomDecorations(level, center, floorBlock);
    }

    private void addRoomDecorations(WorldGenLevel level, BlockPos center, BlockState floorBlock) {
      // Add a chest with loot
      BlockPos chestPos = center.above();
      if (level.getBlockState(chestPos).isAir()) {
        level.setBlock(
            chestPos,
            Blocks.CHEST
                .defaultBlockState()
                .setValue(ChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)),
            2);

        // Add loot to the chest (handled by loot tables)
      }

      // Add some decorative elements based on a random theme
      switch (random.nextInt(4)) {
        case 0 -> {
          // Study nook
          BlockPos tablePos = center.offset(2, -1, 0);
          level.setBlock(tablePos, Blocks.CRAFTING_TABLE.defaultBlockState(), 2);

          BlockPos chairPos = tablePos.relative(Direction.NORTH);
          level.setBlock(
              chairPos,
              Blocks.OAK_STAIRS
                  .defaultBlockState()
                  .setValue(StairBlock.FACING, Direction.SOUTH)
                  .setValue(StairBlock.HALF, Half.BOTTOM),
              2);

          // Add a bookshelf
          BlockPos shelfPos = center.offset(-2, -1, 0);
          level.setBlock(shelfPos, Blocks.BOOKSHELF.defaultBlockState(), 2);
        }
        case 1 -> {
          // Treasure display
          BlockPos pedestalPos = center.above(-1);
          level.setBlock(pedestalPos, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 2);
          level.setBlock(pedestalPos.above(), Blocks.ENCHANTED_BOOK.defaultBlockState(), 2);

          // Add some gold blocks around
          for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos goldPos = pedestalPos.relative(dir);
            if (level.getBlockState(goldPos).isAir()) {
              level.setBlock(goldPos, Blocks.GOLD_BLOCK.defaultBlockState(), 2);
            }
          }
        }
        case 2 -> {
          // Alchemy station
          BlockPos brewingPos = center.above(-1);
          level.setBlock(brewingPos, Blocks.BREWING_STAND.defaultBlockState(), 2);

          // Add some potions
          for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos potionPos = brewingPos.relative(dir);
            if (level.getBlockState(potionPos).isAir()) {
              level.setBlock(potionPos, Blocks.GLASS_PANE.defaultBlockState(), 2);
            }
          }
        }
        case 3 -> {
          // Armor stand with gear
          BlockPos standPos = center.above(-1);
          level.setBlock(standPos, Blocks.ARMOR_STAND.defaultBlockState(), 2);

          // Add some weapon racks
          for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (random.nextBoolean()) {
              BlockPos weaponPos = standPos.relative(dir, 2);
              if (level.getBlockState(weaponPos).isAir()) {
                level.setBlock(
                    weaponPos,
                    Blocks.ITEM_FRAME
                        .defaultBlockState()
                        .setValue(ItemFrameBlock.FACING, dir.getOpposite()),
                    2);
              }
            }
          }
        }
      }

      // Add some lighting
      for (int i = 0; i < 4; i++) {
        Direction dir = Direction.from2DDataValue(i);
        BlockPos torchPos = center.relative(dir, 3).above();
        if (level.getBlockState(torchPos).isAir()) {
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
}
