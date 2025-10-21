package com.netroaki.chex.world.structure;

import com.mojang.serialization.Codec;
import com.netroaki.chex.registry.CHEXStructures;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class SporeTyrantArenaStructure extends Structure {
  public static final Codec<SporeTyrantArenaStructure> CODEC =
      simpleCodec(SporeTyrantArenaStructure::new);

  public SporeTyrantArenaStructure(StructureSettings settings) {
    super(settings);
  }

  @Override
  public StructureType<?> type() {
    return CHEXStructures.SPORE_TYRANT_ARENA.get();
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

  private static void generatePieces(
      StructurePiecesBuilder builder, Structure.GenerationContext context) {
    BlockPos centerPos =
        new BlockPos(context.chunkPos().getMinBlockX(), 64, context.chunkPos().getMinBlockZ());

    // Generate the main arena platform
    generateArenaPlatform(builder, centerPos, context.random());

    // Generate the central pillar
    generateCentralPillar(builder, centerPos, context.random());

    // Generate the outer ring of pillars
    generatePillarRing(builder, centerPos, 12, 6, context.random());
  }

  private static void generateArenaPlatform(
      StructurePiecesBuilder builder, BlockPos center, WorldgenRandom random) {
    int radius = 24;
    int centerX = center.getX();
    int centerZ = center.getZ();

    // Main platform (circular)
    for (int x = -radius; x <= radius; x++) {
      for (int z = -radius; z <= radius; z++) {
        double distance = Math.sqrt(x * x + z * z);
        if (distance <= radius) {
          // Main platform (stone bricks)
          BlockState block = Blocks.STONE_BRICKS.defaultBlockState();

          // Add some variation
          if (random.nextFloat() < 0.1) {
            if (random.nextBoolean()) {
              block = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
            } else {
              block = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            }
          }

          // Add some decorative elements
          if (distance > radius - 2 && random.nextFloat() < 0.3) {
            block = Blocks.STONE_BRICK_WALL.defaultBlockState();
          }

          builder.addBlock(new BlockPos(centerX + x, center.getY() - 1, centerZ + z), block);
        }
      }
    }
  }

  private static void generateCentralPillar(
      StructurePiecesBuilder builder, BlockPos center, WorldgenRandom random) {
    int height = 8 + random.nextInt(5);
    int radius = 3;

    for (int y = 0; y < height; y++) {
      for (int x = -radius; x <= radius; x++) {
        for (int z = -radius; z <= radius; z++) {
          if (x == -radius || x == radius || z == -radius || z == radius) {
            // Outer layer (chiseled stone bricks)
            builder.addBlock(
                center.offset(x, y, z), Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
          } else {
            // Inner core (mossy stone bricks)
            builder.addBlock(center.offset(x, y, z), Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
          }
        }
      }
    }

    // Add a spawner on top
    BlockPos spawnerPos = center.above(height);
    builder.addBlock(spawnerPos, Blocks.SPAWNER.defaultBlockState());
    // Note: The spawner will need to be configured with the Spore Tyrant entity in the world gen
  }

  private static void generatePillarRing(
      StructurePiecesBuilder builder,
      BlockPos center,
      int radius,
      int count,
      WorldgenRandom random) {
    double angleIncrement = 2 * Math.PI / count;

    for (int i = 0; i < count; i++) {
      double angle = i * angleIncrement;
      int x = (int) (center.getX() + radius * Math.cos(angle));
      int z = (int) (center.getZ() + radius * Math.sin(angle));

      int height = 4 + random.nextInt(3);

      for (int y = 0; y < height; y++) {
        BlockPos pos = new BlockPos(x, center.getY() + y, z);
        builder.addBlock(pos, Blocks.MOSSY_STONE_BRICKS.defaultBlockState());

        // Add some decorative blocks
        if (y == height - 1) {
          builder.addBlock(pos.above(), Blocks.STONE_BRICK_WALL.defaultBlockState());

          // Add some vines occasionally
          if (random.nextFloat() < 0.3) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
              if (random.nextBoolean()) {
                int vineLength = 1 + random.nextInt(3);
                for (int v = 1; v <= vineLength; v++) {
                  builder.addBlock(
                      pos.below(v).relative(dir),
                      Blocks.VINE
                          .defaultBlockState()
                          .setValue(
                              net.minecraft.world.level.block.VineBlock.getPropertyForFace(
                                  dir.getOpposite()),
                              true));
                }
              }
            }
          }
        }
      }
    }
  }
}
