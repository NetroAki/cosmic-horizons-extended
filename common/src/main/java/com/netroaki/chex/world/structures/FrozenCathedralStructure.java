package com.netroaki.chex.world.structures;

import com.mojang.serialization.Codec;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.entity.ModEntities;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

public class FrozenCathedralStructure extends StructureFeature<NoneFeatureConfiguration> {
  private static final ResourceLocation STRUCTURE =
      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "frozen_cathedral");

  public FrozenCathedralStructure(Codec<NoneFeatureConfiguration> codec) {
    super(codec, FrozenCathedralStructure::createPiecesGenerator);
  }

  @Override
  public GenerationStep.Decoration step() {
    return GenerationStep.Decoration.SURFACE_STRUCTURES;
  }

  private static boolean isFeatureChunk(
      PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
    // Only place in Crystalis dimension
    if (!context.validBiome()) {
      return false;
    }

    // Check if the chunk is in a valid position
    int chunkX = context.chunkPos().x;
    int chunkZ = context.chunkPos().z;

    // Only generate in certain chunks to prevent too many structures
    int spacing = 32; // chunks between structures
    int chunkXMod = Math.floorMod(chunkX, spacing);
    int chunkZMod = Math.floorMod(chunkZ, spacing);

    return chunkXMod == 0 && chunkZMod == 0;
  }

  private static Optional<PieceGenerator<NoneFeatureConfiguration>> createPiecesGenerator(
      PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
    if (!isFeatureChunk(context)) {
      return Optional.empty();
    }

    return PieceGenerator.simplePiecesBuilder(
        (pieceBuilder, context1) -> {
          // Get the structure's position
          BlockPos blockpos =
              new BlockPos(
                  context1.chunkPos().getMinBlockX(),
                  64, // Base Y level, will be adjusted to surface
                  context1.chunkPos().getMinBlockZ());

          // Adjust Y to be on the surface
          blockpos =
              new BlockPos(
                  blockpos.getX(),
                  context1
                      .chunkGenerator()
                      .getFirstFreeHeight(
                          blockpos.getX(),
                          blockpos.getZ(),
                          Heightmap.Types.WORLD_SURFACE_WG,
                          context1.heightAccessor(),
                          context1.randomState()),
                  blockpos.getZ());

          // Add the main structure piece
          pieceBuilder.addPiece(
              new FrozenCathedralPiece(
                  context1.structureTemplateManager(),
                  STRUCTURE,
                  blockpos,
                  Rotation.getRandom(context1.random())));
        },
        context);
  }

  public static void spawnCryoMonarch(ServerLevel level, BlockPos pos) {
    // Spawn the Cryo Monarch at the center of the structure
    ModEntities.CRYO_MONARCH
        .get()
        .spawn(
            level,
            pos.above(2), // Spawn 2 blocks above the structure's base
            MobSpawnType.STRUCTURE);
  }
}
