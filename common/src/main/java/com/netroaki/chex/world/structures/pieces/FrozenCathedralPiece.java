package com.netroaki.chex.world.structures.pieces;

import com.netroaki.chex.world.structures.FrozenCathedralStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class FrozenCathedralPiece extends TemplateStructurePiece {
  private final ResourceLocation structure;
  private final Rotation rotation;

  public FrozenCathedralPiece(
      StructureManager templateManager,
      ResourceLocation structure,
      BlockPos pos,
      Rotation rotation) {
    super(
        ModStructurePieceTypes.FROZEN_CATHEDRAL_PIECE.get(),
        0,
        templateManager,
        structure,
        structure.toString(),
        makeSettings(rotation),
        pos);
    this.structure = structure;
    this.rotation = rotation;
  }

  public FrozenCathedralPiece(StructurePieceSerializationContext context, CompoundTag tag) {
    super(
        ModStructurePieceTypes.FROZEN_CATHEDRAL_PIECE.get(),
        tag,
        context.structureTemplateManager(),
        (resourceLocation) -> {
          return makeSettings(Rotation.valueOf(tag.getString("Rot")));
        });
    this.structure = new ResourceLocation(tag.getString("Template"));
    this.rotation = Rotation.valueOf(tag.getString("Rot"));
  }

  private static StructurePlaceSettings makeSettings(Rotation rotation) {
    return new StructurePlaceSettings()
        .setRotation(rotation)
        .setMirror(Mirror.NONE)
        .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
  }

  @Override
  protected void addAdditionalSaveData(
      StructurePieceSerializationContext context, CompoundTag tag) {
    super.addAdditionalSaveData(context, tag);
    tag.putString("Template", this.structure.toString());
    tag.putString("Rot", this.rotation.name());
  }

  @Override
  protected void handleDataMarker(
      String function,
      BlockPos pos,
      ServerLevelAccessor level,
      net.minecraft.util.RandomSource random,
      BoundingBox box) {
    if ("cryo_monarch_spawn".equals(function)) {
      // Spawn the Cryo Monarch when the structure generates
      if (level instanceof ServerLevel serverLevel) {
        FrozenCathedralStructure.spawnCryoMonarch(serverLevel, pos);
      }
    }
  }
}
