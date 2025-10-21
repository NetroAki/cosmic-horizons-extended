package com.netroaki.chex.world.structures;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class StructureUtils {
  public static StructureTemplatePool createTemplatePool(
      String name, List<ResourceLocation> templates) {
    StructureTemplatePool.Projection projection = Projection.RIGID;

    // Create a list of pool elements
    List<StructurePoolElement> elements =
        templates.stream()
            .map(
                template ->
                    StructurePoolElement.single(
                        template.toString(), Holder.direct(createEmptyProcessorList())))
            .toList();

    return new StructureTemplatePool(
        new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, name),
        new ResourceLocation("empty"),
        elements,
        projection);
  }

  private static StructureProcessorList createEmptyProcessorList() {
    return new StructureProcessorList(List.of());
  }

  public static ResourceLocation structure(String name) {
    return new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "structures/" + name + ".nbt");
  }
}
