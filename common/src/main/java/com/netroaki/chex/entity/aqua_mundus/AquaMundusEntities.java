package com.netroaki.chex.entity.aqua_mundus;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AquaMundusEntities {
  public static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CosmicHorizonsExpanded.MOD_ID);

  // Luminfish - Small, passive, bioluminescent fish
  public static final RegistryObject<EntityType<LuminfishEntity>> LUMINFISH =
      ENTITIES.register(
          "luminfish",
          () ->
              EntityType.Builder.of(LuminfishEntity::new, MobCategory.WATER_AMBIENT)
                  .sized(0.5F, 0.3F)
                  .clientTrackingRange(4)
                  .build(
                      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "luminfish").toString()));

  // Hydrothermal Drone - Neutral construct that harvests minerals
  public static final RegistryObject<EntityType<HydrothermalDroneEntity>> HYDROTHERMAL_DRONE =
      ENTITIES.register(
          "hydrothermal_drone",
          () ->
              EntityType.Builder.of(HydrothermalDroneEntity::new, MobCategory.WATER_CREATURE)
                  .sized(0.9F, 0.9F)
                  .clientTrackingRange(8)
                  .build(
                      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "hydrothermal_drone")
                          .toString()));

  // Tidal Jelly - Large, ethereal, bioluminescent jellyfish
  public static final RegistryObject<EntityType<TidalJellyEntity>> TIDAL_JELLY =
      ENTITIES.register(
          "tidal_jelly",
          () ->
              EntityType.Builder.of(TidalJellyEntity::new, MobCategory.WATER_CREATURE)
                  .sized(2.0F, 2.0F)
                  .clientTrackingRange(10)
                  .build(
                      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "tidal_jelly")
                          .toString()));

  // Abyssal Leviathan - Massive, aggressive predator
  public static final RegistryObject<EntityType<AbyssalLeviathanEntity>> ABYSSAL_LEVIATHAN =
      ENTITIES.register(
          "abyssal_leviathan",
          () ->
              EntityType.Builder.of(AbyssalLeviathanEntity::new, MobCategory.WATER_CREATURE)
                  .sized(4.0F, 2.0F)
                  .clientTrackingRange(10)
                  .build(
                      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "abyssal_leviathan")
                          .toString()));

  // Ocean Sovereign - Final boss of Aqua Mundus
  public static final RegistryObject<EntityType<OceanSovereignEntity>> OCEAN_SOVEREIGN =
      ENTITIES.register(
          "ocean_sovereign",
          () ->
              EntityType.Builder.of(OceanSovereignEntity::new, MobCategory.MONSTER)
                  .sized(3.0F, 3.0F)
                  .clientTrackingRange(12)
                  .fireImmune()
                  .build(
                      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "ocean_sovereign")
                          .toString()));

  @SubscribeEvent
  public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
    event.put(LUMINFISH.get(), LuminfishEntity.createAttributes().build());
    event.put(HYDROTHERMAL_DRONE.get(), HydrothermalDroneEntity.createAttributes().build());
    event.put(TIDAL_JELLY.get(), TidalJellyEntity.createAttributes().build());
    event.put(ABYSSAL_LEVIATHAN.get(), AbyssalLeviathanEntity.createAttributes().build());
    event.put(OCEAN_SOVEREIGN.get(), OceanSovereignEntity.createAttributes().build());
    event.put(TIDAL_JELLY.get(), TidalJellyEntity.createAttributes().build());
    event.put(ABYSSAL_LEVIATHAN.get(), AbyssalLeviathanEntity.createAttributes().build());
    event.put(OCEAN_SOVEREIGN.get(), OceanSovereignEntity.createAttributes().build());
  }

  @SubscribeEvent
  public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
    event.register(
        LUMINFISH.get(),
        SpawnPlacements.Type.IN_WATER,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LuminfishEntity::checkLuminfishSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    // Ocean Sovereign spawns in deep ocean biomes, only once per world
    event.register(
        OCEAN_SOVEREIGN.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        (entityType, world, spawnType, pos, random) -> {
          // Only spawn in deep ocean biomes
          if (!world.getBiome(pos).is(new ResourceLocation("minecraft:deep_ocean"))) {
            return false;
          }

          // Ensure there's enough space (5x5x5 area)
          for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
              BlockPos checkPos = pos.offset(x, 1, z);
              if (!world.getBlockState(checkPos).isAir()) {
                return false;
              }
            }
          }

          // Only spawn if no other Ocean Sovereign exists in a large radius
          return world
              .getEntitiesOfClass(OceanSovereignEntity.class, new AABB(pos).inflate(256), e -> true)
              .isEmpty();
        },
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        HYDROTHERMAL_DRONE.get(),
        SpawnPlacements.Type.IN_WATER,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        HydrothermalDroneEntity::checkDroneSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        TIDAL_JELLY.get(),
        SpawnPlacements.Type.IN_WATER,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        TidalJellyEntity::checkTidalJellySpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        ABYSSAL_LEVIATHAN.get(),
        SpawnPlacements.Type.IN_WATER,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        AbyssalLeviathanEntity::checkLeviathanSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        OCEAN_SOVEREIGN.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        OceanSovereignEntity::checkOceanSovereignSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);
  }
}
