package com.netroaki.chex.world.structures;

import com.mojang.serialization.Codec;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.entity.ModEntities;
import com.netroaki.chex.world.structures.processors.OceanSovereignSpawnerProcessor;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class OceanSovereignArenaStructure extends StructureFeature<NoneFeatureConfiguration> {
  private static final ResourceLocation ARENA_STRUCTURE =
      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "ocean_sovereign_arena");

  public OceanSovereignArenaStructure(Codec<NoneFeatureConfiguration> codec) {
    super(codec, OceanSovereignArenaStructure::createPiecesGenerator);
  }

  @Override
  public GenerationStep.Decoration step() {
    return GenerationStep.Decoration.SURFACE_STRUCTURES;
  }

  private static boolean isFeatureChunk(
      PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
    // Only place in deep ocean biomes
    if (!context.validBiomeOnTop(Heightmap.Types.OCEAN_FLOOR_WG)) {
      return false;
    }

    // Ensure we're in deep water
    int seaLevel = context.chunkGenerator().getSeaLevel();
    int oceanFloor =
        context
            .chunkGenerator()
            .getBaseHeight(
                context.chunkPos().getMinBlockX(),
                context.chunkPos().getMinBlockZ(),
                Heightmap.Types.OCEAN_FLOOR_WG,
                context.heightAccessor());

    // Make sure we have enough water above the ocean floor
    return oceanFloor < seaLevel - 15; // At least 15 blocks of water
  }

  private static Optional<PieceGenerator<NoneFeatureConfiguration>> createPiecesGenerator(
      PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
    if (!isFeatureChunk(context)) {
      return Optional.empty();
    }

    return Optional.of(
        (builder, context1) -> {
          BlockPos blockpos =
              new BlockPos(
                  context.chunkPos().getMinBlockX(),
                  context.chunkGenerator().getSeaLevel() - 20, // Place well below sea level
                  context.chunkPos().getMinBlockZ());

          // Add the main structure piece
          builder.addPiece(
              new OceanSovereignArenaPiece(
                  context.structureManager(),
                  ARENA_STRUCTURE,
                  blockpos,
                  Rotation.getRandom(context.random())));
        });
  }

  private static class OceanSovereignArenaPiece extends BaseStructurePiece {
    private final ResourceLocation structure;
    private final Rotation rotation;

    public OceanSovereignArenaPiece(
        StructureManager structureManager,
        ResourceLocation structure,
        BlockPos pos,
        Rotation rotation) {
      super(ModStructureTypes.OCEAN_SOVEREIGN_ARENA.get(), 0, structureManager, structure, pos);
      this.structure = structure;
      this.rotation = rotation;
    }

    @Override
    protected void handleDataMarker(
        String function,
        BlockPos pos,
        LevelAccessor level,
        Random random,
        StructurePlaceSettings placementIn) {
      // Handle any special markers in the structure
      if ("ocean_sovereign_spawn".equals(function)) {
        // The boss will be spawned by the structure processor
      }
    }

    @Override
    protected StructurePlaceSettings getPlacement(StructureManager structureManager, BlockPos pos) {
      return new StructurePlaceSettings()
          .setRotation(this.rotation)
          .setMirror(mirror)
          .setRotationPivot(new BlockPos(16, 0, 16))
          .addProcessor(StructureUtils.IGNORE_AIR_AND_STRUCTURE_BLOCKS)
          .addProcessor(OceanSovereignSpawnerProcessor.INSTANCE);
    }

    @Override
    protected void handleDataMarker(
        String function,
        BlockPos pos,
        LevelAccessor level,
        Random random,
        StructurePlaceSettings placementIn) {
      if (level instanceof ServerLevel serverLevel) {
        if ("ocean_sovereign_spawn".equals(function)) {
          // The boss will be spawned by the structure processor
          // This marker is just a fallback in case the processor doesn't work
          ModEntities.OCEAN_SOVEREIGN
              .get()
              .spawn(
                  serverLevel,
                  (CompoundTag) null,
                  null,
                  null,
                  pos,
                  MobSpawnType.STRUCTURE,
                  false,
                  false);
        }
      }
    }
  }
}
