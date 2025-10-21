package com.netroaki.chex.world.structures;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class SolarCollectorStructure extends BaseStructure {
  public static final Codec<SolarCollectorStructure> CODEC =
      RecordCodecBuilder.<SolarCollectorStructure>mapCodec(
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
                      .apply(builder, SolarCollectorStructure::new))
          .codec();

  public SolarCollectorStructure(
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
    return ModStructureTypes.SOLAR_COLLECTOR.get();
  }

  public static SolarCollectorStructure createDefault() {
    return new SolarCollectorStructure(
        defaultSettings(),
        Holder.direct(createTemplatePool("solar_collector")),
        Optional.empty(),
        7, // Size of the structure (max distance from center)
        ConstantHeight.of(120), // Fixed height for floating structures
        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
        80 // Max distance from center
        );
  }
}
