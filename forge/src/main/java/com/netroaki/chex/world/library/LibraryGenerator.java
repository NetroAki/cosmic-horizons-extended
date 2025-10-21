package com.netroaki.chex.world.library;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class LibraryGenerator extends Structure {
  public static final Codec<LibraryGenerator> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(Structure.settingsCodec(instance))
                  .apply(instance, LibraryGenerator::new));

  public LibraryGenerator(StructureSettings settings) {
    super(settings);
  }

  @Override
  public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    if (!isFeatureChunk(context)) {
      return Optional.empty();
    }

    return onTopOfChunkCenter(
        context,
        Heightmap.Types.WORLD_SURFACE_WG,
        (builder) -> {
          generatePieces(builder, context);
        });
  }

  private static boolean isFeatureChunk(GenerationContext context) {
    ChunkPos chunkPos = context.chunkPos();
    return chunkPos.x % 5 == 0 && chunkPos.z % 5 == 0; // Generate every 5 chunks
  }

  private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
    ChunkPos chunkPos = context.chunkPos();
    WorldGenLevel level = context.level();
    RandomSource random = context.random();

    // Generate a central library structure
    BlockPos centerPos =
        new BlockPos(
            chunkPos.getMinBlockX() + 8,
            level.getMinBuildHeight() + 10,
            chunkPos.getMinBlockZ() + 8);

    // Add main library room
    builder.addPiece(
        new LibraryRoomPiece(
            context.structureTemplateManager(),
            new BlockPos(centerPos.getX(), centerPos.getY(), centerPos.getZ()),
            random));

    // Add connecting hallways and rooms
    if (random.nextFloat() < 0.7f) {
      addConnectingRoom(builder, context, centerPos, random);
    }
  }

  private static void addConnectingRoom(
      StructurePiecesBuilder builder,
      GenerationContext context,
      BlockPos centerPos,
      RandomSource random) {
    // Implementation for adding connecting rooms
  }

  @Override
  public StructureType<?> type() {
    return CHEXStructures.LIBRARY_GENERATOR.get();
  }

  public static class LibraryRoomPiece extends StructurePiece {
    private final ResourceLocation templateLocation;
    private final BlockPos pos;
    private final Rotation rotation;

    public LibraryRoomPiece(
        StructureTemplateManager templateManager, BlockPos pos, RandomSource random) {
      super(CHEXPieces.LIBRARY_ROOM.get(), 0, createBoundingBox(pos, templateManager));
      this.templateLocation =
          new ResourceLocation("cosmic_horizons_extended:library/rooms/library_room");
      this.pos = pos;
      this.rotation = Rotation.getRandom(random);
    }

    public LibraryRoomPiece(StructurePieceSerializationContext context, CompoundTag tag) {
      super(CHEXPieces.LIBRARY_ROOM.get(), tag);
      this.templateLocation = new ResourceLocation(tag.getString("Template"));
      this.pos = NbtUtils.readBlockPos(tag.getCompound("Pos"));
      this.rotation = Rotation.valueOf(tag.getString("Rot"));
    }

    private static BoundingBox createBoundingBox(
        BlockPos pos, StructureTemplateManager templateManager) {
      // Return a bounding box that fits the structure
      return new BoundingBox(
          pos.getX() - 16,
          pos.getY() - 1,
          pos.getZ() - 16,
          pos.getX() + 16,
          pos.getY() + 10,
          pos.getZ() + 16);
    }

    @Override
    protected void addAdditionalSaveData(
        StructurePieceSerializationContext context, CompoundTag tag) {
      tag.putString("Template", this.templateLocation.toString());
      tag.put("Pos", NbtUtils.writeBlockPos(this.pos));
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
      StructureTemplate template = structureManager.getOrCreate(this.templateLocation);

      if (template == null) {
        CHEX.LOGGER.error("Library room template not found: {}", this.templateLocation);
        return;
      }

      StructurePlaceSettings settings =
          new StructurePlaceSettings()
              .setRotation(this.rotation)
              .setMirror(Mirror.NONE)
              .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);

      template.placeInWorld(level, this.pos, this.pos, settings, random, 2);
    }
  }
}
