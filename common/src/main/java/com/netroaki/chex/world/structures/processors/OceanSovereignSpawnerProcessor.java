package com.netroaki.chex.world.structures.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class OceanSovereignSpawnerProcessor extends StructureProcessor {
  public static final OceanSovereignSpawnerProcessor INSTANCE =
      new OceanSovereignSpawnerProcessor();
  public static final Codec<OceanSovereignSpawnerProcessor> CODEC = Codec.unit(INSTANCE);

  private OceanSovereignSpawnerProcessor() {}

  @Override
  protected StructureProcessorType<?> getType() {
    return ModStructureProcessors.OCEAN_SOVEREIGN_SPAWNER_PROCESSOR.get();
  }

  @Nullable
  @Override
  public StructureTemplate.StructureEntityInfo processEntity(
      LevelReader level,
      BlockPos seedPos,
      BlockPos structurePos,
      StructureTemplate.StructureEntityInfo rawEntityInfo,
      StructureTemplate.StructureEntityInfo entityInfo,
      StructurePlaceSettings placementSettings,
      @Nullable StructureTemplate template) {
    if (level instanceof ServerLevel serverLevel) {
      if ("ocean_sovereign_spawner".equals(entityInfo.nbt.getString("id"))) {
        // Spawn the Ocean Sovereign
        EntityType.by(entityInfo.nbt)
            .ifPresent(
                entityType -> {
                  BlockPos spawnPos = entityInfo.blockPos.above();

                  // Create a new entity with the correct position and rotation
                  var entity =
                      entityType.create(
                          serverLevel,
                          entityInfo.nbt,
                          null,
                          null,
                          spawnPos,
                          MobSpawnType.STRUCTURE,
                          false,
                          false);
                  if (entity != null) {
                    // Set the entity's rotation to match the structure
                    entity.setYRot(entityInfo.blockPos.getY() * 16);

                    // Add the entity to the world
                    serverLevel.addFreshEntityWithPassengers(entity);
                  }
                });

        // Return null to prevent the original entity from being placed
        return null;
      }
    }

    return entityInfo;
  }

  @Override
  protected StructureProcessorType<?> getTypeInstance() {
    return getType();
  }
}
