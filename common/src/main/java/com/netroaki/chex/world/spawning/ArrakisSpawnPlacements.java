package com.netroaki.chex.world.spawning;

import com.netroaki.chex.init.EntityInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "cosmic_horizons_extended", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArrakisSpawnPlacements {

  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onBiomeLoading(BiomeLoadingEvent event) {
    // Only process Arrakis biomes
    if (!isArrakisBiome(event)) {
      return;
    }

    // Add spawns for each entity type
    addSpawnEntries(event, EntityInit.SANDWORM_JUVENILE.get(), 8, 1, 2, MobCategory.MONSTER);
    addSpawnEntries(event, EntityInit.STORM_HAWK.get(), 6, 1, 3, MobCategory.CREATURE);
    addSpawnEntries(event, EntityInit.SPICE_GATHERER.get(), 4, 1, 1, MobCategory.CREATURE);
  }

  private static boolean isArrakisBiome(BiomeLoadingEvent event) {
    // Check if the biome is from Arrakis
    return event.getName() != null
        && event.getName().getNamespace().equals("cosmic_horizons_extended")
        && event.getName().getPath().contains("arrakis");
  }

  private static <T extends net.minecraft.world.entity.Mob> void addSpawnEntries(
      BiomeLoadingEvent event,
      EntityType<T> entityType,
      int weight,
      int minCount,
      int maxCount,
      MobCategory category) {

    event
        .getSpawns()
        .addSpawn(
            category, new MobSpawnSettings.SpawnerData(entityType, weight, minCount, maxCount));
  }

  public static void registerSpawnPlacements() {
    // Sandworm Juvenile - spawns on solid ground below sea level
    SpawnPlacements.register(
        EntityInit.SANDWORM_JUVENILE.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        Monster::checkMonsterSpawnRules);

    // Storm Hawk - spawns in air
    SpawnPlacements.register(
        EntityInit.STORM_HAWK.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        Monster::checkMonsterSpawnRules);

    // Spice Gatherer - spawns on solid ground
    SpawnPlacements.register(
        EntityInit.SPICE_GATHERER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        Animal::checkAnimalSpawnRules);

    // Sand Emperor - special spawn placement (handled by SandEmperorSpawnPlacement)
    SpawnPlacements.register(
        EntityInit.SAND_EMPEROR.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        (entityType, level, spawnType, pos, random) -> false // Prevent natural spawning
        );
  }
}
