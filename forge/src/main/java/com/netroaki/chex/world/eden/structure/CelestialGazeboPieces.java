package com.netroaki.chex.world.eden.structure;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class CelestialGazeboPieces {
  private static final ResourceLocation GAZEBO_MAIN =
      new ResourceLocation("cosmic_horizons_extended:edengarden/celestial_gazebo");

  public static class Piece extends StructurePiece {
    private final ResourceLocation template;
    private final Rotation rotation;

    public Piece(
        StructureTemplateManager templateManager,
        ResourceLocation template,
        BlockPos pos,
        Rotation rotation) {
      super(
          EdenStructures.CELESTIAL_GAZEBO_PIECE.get(),
          0,
          getBoundingBox(templateManager, template, pos, rotation));
      this.template = template;
      this.rotation = rotation;
    }

    public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
      super(EdenStructures.CELESTIAL_GAZEBO_PIECE.get(), tag);
      this.template = new ResourceLocation(tag.getString("Template"));
      this.rotation = Rotation.valueOf(tag.getString("Rot"));
    }

    @Override
    protected void addAdditionalSaveData(
        StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putString("Template", this.template.toString());
      tag.putString("Rot", this.rotation.name());
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
      StructureTemplateManager templatemanager =
          structureManager
              .registryAccess()
              .registryOrThrow(net.minecraft.core.registries.Registries.STRUCTURE_TEMPLATE_POOL);
      StructureTemplate template = templatemanager.getOrCreate(this.template);

      if (template == null) {
        return;
      }

      BlockPos centerPos = template.getSize().rotate(this.rotation);
      int x = (centerPos.getX() + 1) / 2;
      int z = (centerPos.getZ() + 1) / 2;
      int y =
          level.getHeight(
              Heightmap.Types.WORLD_SURFACE_WG,
              this.boundingBox.minX() + x,
              this.boundingBox.minZ() + z);

      StructurePlaceSettings settings =
          new StructurePlaceSettings()
              .setRotation(this.rotation)
              .setMirror(net.minecraft.world.level.block.Mirror.NONE)
              .addProcessor(
                  new StructureProcessor() {
                    @Override
                    protected StructureProcessorType<?> getType() {
                      return StructureProcessorType.RULE;
                    }

                    @Override
                    public List<StructureTemplate.StructureBlockInfo> processBlock(
                        LevelAccessor level,
                        BlockPos pos,
                        BlockPos pos2,
                        StructureTemplate.StructureBlockInfo blockInfo,
                        StructureTemplate.StructureBlockInfo relativeBlockInfo,
                        StructurePlaceSettings settings) {
                      // Process blocks if needed
                      return List.of(relativeBlockInfo);
                    }
                  });

      BlockPos center = new BlockPos(this.boundingBox.minX() + x, y, this.boundingBox.minZ() + z);
      template.placeInWorld(level, center, center, settings, random, 2);
    }

    private static BoundingBox getBoundingBox(
        StructureTemplateManager templateManager,
        ResourceLocation template,
        BlockPos pos,
        Rotation rotation) {
      StructureTemplate structuretemplate = templateManager.getOrCreate(template);
      if (structuretemplate == null) {
        return new BoundingBox(pos);
      } else {
        return structuretemplate.getBoundingBox(settings(rotation), pos);
      }
    }

    private static StructurePlaceSettings settings(Rotation rotation) {
      return new StructurePlaceSettings()
          .setRotation(rotation)
          .setMirror(net.minecraft.world.level.block.Mirror.NONE);
    }
  }

  public static void addPieces(
      StructureTemplateManager templateManager,
      BlockPos pos,
      Rotation rotation,
      StructurePiecesBuilder pieces,
      RandomSource random) {
    pieces.addPiece(new Piece(templateManager, GAZEBO_MAIN, pos, rotation));
  }
}
