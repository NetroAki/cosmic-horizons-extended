package com.netroaki.chex.world.structures.pieces;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class OceanSovereignArenaPiece extends TemplateStructurePiece {
  private static final ResourceLocation ARENA_STRUCTURE =
      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "ocean_sovereign_arena");

  private final Rotation rotation;

  public OceanSovereignArenaPiece(
      StructureManager structureManager,
      ResourceLocation structure,
      BlockPos pos,
      Rotation rotation) {
    super(
        ModStructurePieceTypes.OCEAN_SOVEREIGN_ARENA_PIECE.get(),
        0,
        structureManager,
        structure,
        structure.toString(),
        makeSettings(rotation),
        pos.offset(-8, -5, -8));
    this.rotation = rotation;
  }

  public OceanSovereignArenaPiece(StructureManager structureManager, CompoundTag tag) {
    super(
        ModStructurePieceTypes.OCEAN_SOVEREIGN_ARENA_PIECE.get(),
        tag,
        structureManager,
        (resourceLocation) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
    this.rotation = Rotation.valueOf(tag.getString("Rot"));
  }

  private static StructurePlaceSettings makeSettings(Rotation rotation) {
    return new StructurePlaceSettings()
        .setRotation(rotation)
        .setRotationPivot(new BlockPos(8, 0, 8));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putString("Rot", this.rotation.name());
  }

  @Override
  protected void handleDataMarker(
      String function, BlockPos pos, ServerLevelAccessor level, Random random, BoundingBox box) {
    // Handle any data markers in the structure
    if ("ocean_sovereign_spawn".equals(function)) {
      // The boss will be spawned by the structure processor
      // This is just a fallback in case the processor doesn't work
    }
  }
}
