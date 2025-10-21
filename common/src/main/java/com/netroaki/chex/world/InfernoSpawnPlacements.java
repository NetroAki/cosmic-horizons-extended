package com.netroaki.chex.world;

import com.netroaki.chex.entity.ModEntities;
import com.netroaki.chex.entity.inferno.AshCrawlerEntity;
import com.netroaki.chex.entity.inferno.FireWraithEntity;
import com.netroaki.chex.entity.inferno.MagmaHopperEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class InfernoSpawnPlacements {

  @SubscribeEvent
  public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
    // Ash Crawler - spawns on solid ground in the open
    event.register(
        ModEntities.ASH_CRAWLER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        AshCrawlerEntity::checkMonsterSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    // Fire Wraith - spawns in air with space around it
    event.register(
        ModEntities.FIRE_WRAITH.get(),
        SpawnPlacements.Type.IN_AIR,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        FireWraithEntity::checkMonsterSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    // Magma Hopper - spawns on solid ground with space to jump
    event.register(
        ModEntities.MAGMA_HOPPER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        MagmaHopperEntity::checkMonsterSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.ASH_CRAWLER.get(), AshCrawlerEntity.createAttributes().build());
    event.put(ModEntities.FIRE_WRAITH.get(), FireWraithEntity.createAttributes().build());
    event.put(ModEntities.MAGMA_HOPPER.get(), MagmaHopperEntity.createAttributes().build());
  }
}
