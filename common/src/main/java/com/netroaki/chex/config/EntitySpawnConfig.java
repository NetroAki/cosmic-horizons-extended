package com.netroaki.chex.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class EntitySpawnConfig {
  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

  // Spawn configuration for Plasma Wraith
  public static final ForgeConfigSpec.IntValue PLASMA_WRAITH_WEIGHT =
      BUILDER
          .comment("Spawn weight for Plasma Wraith")
          .defineInRange("plasmaWraithWeight", 30, 1, 100);
  public static final ForgeConfigSpec.IntValue PLASMA_WRAITH_MIN_GROUP =
      BUILDER
          .comment("Minimum group size for Plasma Wraith")
          .defineInRange("plasmaWraithMinGroup", 1, 1, 10);
  public static final ForgeConfigSpec.IntValue PLASMA_WRAITH_MAX_GROUP =
      BUILDER
          .comment("Maximum group size for Plasma Wraith")
          .defineInRange("plasmaWraithMaxGroup", 2, 1, 10);

  // Spawn configuration for Flare Sprite
  public static final ForgeConfigSpec.IntValue FLARE_SPRITE_WEIGHT =
      BUILDER
          .comment("Spawn weight for Flare Sprite")
          .defineInRange("flareSpriteWeight", 40, 1, 100);
  public static final ForgeConfigSpec.IntValue FLARE_SPRITE_MIN_GROUP =
      BUILDER
          .comment("Minimum group size for Flare Sprite")
          .defineInRange("flareSpriteMinGroup", 1, 1, 10);
  public static final ForgeConfigSpec.IntValue FLARE_SPRITE_MAX_GROUP =
      BUILDER
          .comment("Maximum group size for Flare Sprite")
          .defineInRange("flareSpriteMaxGroup", 3, 1, 10);

  // Spawn configuration for Solar Engineer Drone
  public static final ForgeConfigSpec.IntValue DRONE_WEIGHT =
      BUILDER
          .comment("Spawn weight for Solar Engineer Drone")
          .defineInRange("droneWeight", 15, 1, 100);
  public static final ForgeConfigSpec.IntValue DRONE_MIN_GROUP =
      BUILDER
          .comment("Minimum group size for Solar Engineer Drone")
          .defineInRange("droneMinGroup", 1, 1, 10);
  public static final ForgeConfigSpec.IntValue DRONE_MAX_GROUP =
      BUILDER
          .comment("Maximum group size for Solar Engineer Drone")
          .defineInRange("droneMaxGroup", 2, 1, 10);

  public static final ForgeConfigSpec SPEC = BUILDER.build();

  // Biome spawns list
  private static final List<MobSpawnSettings.SpawnerData> SPAWNS = new ArrayList<>();

  public static void onLoad(ModConfigEvent.Loading event) {
    // Rebuild spawns when config is loaded
    SPAWNS.clear();

    // TODO: Add spawns based on config when ModEntities is implemented
    // SPAWNS.add(
    // new MobSpawnSettings.SpawnerData(
    // com.netroaki.chex.entity.ModEntities.PLASMA_WRAITH.get(),
    // PLASMA_WRAITH_WEIGHT.get(),
    // PLASMA_WRAITH_MIN_GROUP.get(),
    // PLASMA_WRAITH_MAX_GROUP.get()));

    // SPAWNS.add(
    // new MobSpawnSettings.SpawnerData(
    // com.netroaki.chex.entity.ModEntities.FLARE_SPRITE.get(),
    // FLARE_SPRITE_WEIGHT.get(),
    // FLARE_SPRITE_MIN_GROUP.get(),
    // FLARE_SPRITE_MAX_GROUP.get()));

    // SPAWNS.add(
    // new MobSpawnSettings.SpawnerData(
    // com.netroaki.chex.entity.ModEntities.SOLAR_ENGINEER_DRONE.get(),
    // DRONE_WEIGHT.get(),
    // DRONE_MIN_GROUP.get(),
    // DRONE_MAX_GROUP.get()));
  }

  // TODO: Fix when StructureSpawnListGatherEvent is available
  // public static void onStructureSpawnListGather(StructureSpawnListGatherEvent
  // event) {
  // // Add structure-specific spawns if needed
  // if (event.getStructure().equals(BuiltinStructures.WOODLAND_MANSION)) {
  // // Example: Add custom spawns for specific structures
  // }
  // }

  /**
   * Get all configured spawns for a specific biome
   *
   * @param biomeName The name of the biome to get spawns for
   * @return List of spawner data for the biome
   */
  private static List<MobSpawnSettings.SpawnerData> getSpawnsForBiome(ResourceLocation biomeName) {
    // Filter spawns based on biome if needed
    return SPAWNS;
  }

  // TODO: Register entity attributes when ModEntities is implemented
  // @SubscribeEvent
  // public static void onEntityAttributeCreation(EntityAttributeCreationEvent
  // event) {
  // // Register entity attributes
  // event.put(
  // com.netroaki.chex.entity.ModEntities.PLASMA_WRAITH.get(),
  // com.netroaki.chex.entity.alpha_centauri.PlasmaWraithEntity.createAttributes().build());

  // event.put(
  // com.netroaki.chex.entity.ModEntities.FLARE_SPRITE.get(),
  // com.netroaki.chex.entity.alpha_centauri.FlareSpriteEntity.createAttributes().build());

  // event.put(
  // com.netroaki.chex.entity.ModEntities.SOLAR_ENGINEER_DRONE.get(),
  // com.netroaki.chex.entity.alpha_centauri.SolarEngineerDrone.createAttributes().build());
  // }
}
