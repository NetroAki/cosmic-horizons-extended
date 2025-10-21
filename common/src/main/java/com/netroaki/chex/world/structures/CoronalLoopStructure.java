package com.netroaki.chex.world.structures;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class CoronalLoopStructure extends BaseStructure {
  public static final Codec<CoronalLoopStructure> CODEC =
      RecordCodecBuilder.<CoronalLoopStructure>mapCodec(
              builder ->
                  builder
                      .group(
                          settingsCodec(builder),
                          StructureTemplatePool.CODEC
                              .fieldOf("start_pool")
                              .forGetter(structure -> structure.startPool),
                          ResourceLocation.CODEC
                              .optionalFieldOf("start_jigsaw_name")
                              .forGetter(structure -> structure.startJigsawName),
                          Codec.intRange(0, 30)
                              .fieldOf("size")
                              .forGetter(structure -> structure.size),
                          HeightProvider.CODEC
                              .fieldOf("start_height")
                              .forGetter(structure -> structure.startHeight),
                          Heightmap.Types.CODEC
                              .optionalFieldOf("project_start_to_heightmap")
                              .forGetter(structure -> structure.projectStartToHeightmap),
                          Codec.intRange(1, 128)
                              .fieldOf("max_distance_from_center")
                              .forGetter(structure -> structure.maxDistanceFromCenter))
                      .apply(builder, CoronalLoopStructure::new))
          .codec();

  public CoronalLoopStructure(
      Structure.StructureSettings config,
      Holder<StructureTemplatePool> startPool,
      Optional<ResourceLocation> startJigsawName,
      int size,
      HeightProvider startHeight,
      Optional<Heightmap.Types> projectStartToHeightmap,
      int maxDistanceFromCenter) {
    super(
        config,
        startPool,
        startJigsawName,
        size,
        startHeight,
        projectStartToHeightmap,
        maxDistanceFromCenter);
  }

  @Override
  public StructureType<?> type() {
    return ModStructureTypes.CORONAL_LOOP.get();
  }

  public static CoronalLoopStructure createDefault() {
    return new CoronalLoopStructure(
        defaultSettings(),
        Holder.direct(createTemplatePool("coronal_loop")),
        Optional.empty(),
        10, // Size of the structure (max distance from center)
        ConstantHeight.of(150), // High up for coronal loops
        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
        100 // Max distance from center
        );
  }
}
