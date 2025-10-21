package com.netroaki.chex.world.eden.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class CelestialGazeboStructure extends Structure {
  public static final Codec<CelestialGazeboStructure> CODEC =
      RecordCodecBuilder.<CelestialGazeboStructure>mapCodec(
              builder ->
                  builder
                      .group(
                          settingsCodec(builder),
                          HeightProvider.CODEC
                              .fieldOf("start_height")
                              .forGetter(structure -> structure.startHeight),
                          Codec.intRange(0, 100)
                              .fieldOf("spawn_chance")
                              .forGetter(structure -> structure.spawnChance))
                      .apply(builder, CelestialGazeboStructure::new))
          .codec();

  private final HeightProvider startHeight;
  private final int spawnChance;

  public CelestialGazeboStructure(
      Structure.StructureSettings config, HeightProvider startHeight, int spawnChance) {
    super(config);
    this.startHeight = startHeight;
    this.spawnChance = spawnChance;
  }

  @Override
  public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    // Skip if the seed-based random check fails
    if (context.random().nextInt(100) >= spawnChance) {
      return Optional.empty();
    }

    // Get the height at the chunk center
    int startY =
        this.startHeight.sample(
            context.random(),
            new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));

    // Convert chunk coordinates to actual world coordinates
    ChunkPos chunkPos = context.chunkPos();
    int x = chunkPos.getMiddleBlockX();
    int z = chunkPos.getMiddleBlockZ();

    // Find the top Y level of the terrain at this X/Z position
    int surfaceY =
        context
            .chunkGenerator()
            .getFirstOccupiedHeight(
                x,
                z,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState());

    // Use the higher of the two Y values to ensure the structure generates above ground
    int y = Math.max(startY, surfaceY);
    BlockPos blockPos = new BlockPos(x, y, z);

    return Optional.of(
        new Structure.GenerationStub(
            blockPos,
            structurePiecesBuilder -> generatePieces(structurePiecesBuilder, context, blockPos)));
  }

  private static void generatePieces(
      StructurePiecesBuilder piecesBuilder, Structure.GenerationContext context, BlockPos pos) {
    Rotation rotation = Rotation.getRandom(context.random());
    CelestialGazeboPieces.addPieces(
        context.structureTemplateManager(), pos, rotation, piecesBuilder, context.random());
  }

  @Override
  public StructureType<?> type() {
    return EdenStructures.CELESTIAL_GAZEBO.get();
  }
}
