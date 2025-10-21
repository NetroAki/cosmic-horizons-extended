package com.netroaki.chex.entity.kepler;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeplerEntities {
  public static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CosmicHorizonsExpanded.MOD_ID);

  // River Grazer - Passive mob found near water sources
  public static final RegistryObject<EntityType<RiverGrazerEntity>> RIVER_GRAZER =
      register(
          "river_grazer",
          EntityType.Builder.of(RiverGrazerEntity::new, MobCategory.CREATURE)
              .sized(1.5f, 1.5f)
              .clientTrackingRange(10));

  // Meadow Flutterwing - Small flying mob in meadows
  public static final RegistryObject<EntityType<FlutterwingEntity>> MEADOW_FLUTTERWING =
      register(
          "meadow_flutterwing",
          EntityType.Builder.of(FlutterwingEntity::new, MobCategory.AMBIENT)
              .sized(0.5f, 0.5f)
              .clientTrackingRange(6));

  // Scrub Stalker - Hostile mob in rocky areas
  public static final RegistryObject<EntityType<ScrubStalkerEntity>> SCRUB_STALKER =
      register(
          "scrub_stalker",
          EntityType.Builder.of(ScrubStalkerEntity::new, MobCategory.MONSTER)
              .sized(0.9f, 2.0f)
              .clientTrackingRange(8));

  private static <T extends Animal> RegistryObject<EntityType<T>> register(
      String name, EntityType.Builder<T> builder) {
    return ENTITIES.register(name, () -> builder.build(CosmicHorizonsExpanded.MOD_ID + ":" + name));
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(RIVER_GRAZER.get(), RiverGrazerEntity.createAttributes().build());
    event.put(MEADOW_FLUTTERWING.get(), FlutterwingEntity.createAttributes().build());
    event.put(SCRUB_STALKER.get(), ScrubStalkerEntity.createAttributes().build());
  }

  @SubscribeEvent
  public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
    event.register(
        RIVER_GRAZER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        RiverGrazerEntity::checkSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        MEADOW_FLUTTERWING.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        FlutterwingEntity::checkSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        SCRUB_STALKER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        ScrubStalkerEntity::checkSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);
  }
}
