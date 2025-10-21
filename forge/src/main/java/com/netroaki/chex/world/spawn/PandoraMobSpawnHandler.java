package com.netroaki.chex.world.spawn;

import com.netroaki.chex.config.PandoraMobSpawnsConfig;
import com.netroaki.chex.config.PandoraMobSpawnsConfig.SpawnConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles mob spawning in the Pandora dimension. Integrates with the configuration system to allow
 * customization of spawn rates and behaviors.
 */
@Mod.EventBusSubscriber
public class PandoraMobSpawnHandler {
  // Biome categories where mobs can spawn in Pandora
  private static final Set<Biome.BiomeCategory> PANDORA_BIOME_CATEGORIES =
      Set.of(
          Biome.BiomeCategory.FOREST,
          Biome.BiomeCategory.PLAINS,
          Biome.BiomeCategory.MOUNTAIN,
          Biome.BiomeCategory.JUNGLE,
          Biome.BiomeCategory.SWAMP);

  // Cache of spawn configurations
  private static final Map<EntityType<?>, SpawnConfig> SPAWN_CONFIGS = new HashMap<>();

  static {
    // Register spawn configurations for our mobs
    // These will be overridden by config values if present
    SPAWN_CONFIGS.put(
        EntityType.byString("chex:glowbeast").orElse(null),
        PandoraMobSpawnsConfig.getSpawnConfig(EntityType.byString("chex:glowbeast").orElse(null)));

    SPAWN_CONFIGS.put(
        EntityType.byString("chex:sporefly").orElse(null),
        PandoraMobSpawnsConfig.getSpawnConfig(EntityType.byString("chex:sporefly").orElse(null)));
  }

  @SubscribeEvent
  public static void onBiomeLoading(BiomeLoadingEvent event) {
    // Only process Pandora dimension biomes
    if (!event.getName().getNamespace().equals("chex") || !isPandoraBiome(event)) {
      return;
    }

    // Add spawns for each configured mob
    for (Map.Entry<EntityType<?>, SpawnConfig> entry : SPAWN_CONFIGS.entrySet()) {
      EntityType<?> entityType = entry.getKey();
      SpawnConfig config = entry.getValue();

      if (entityType == null || config == null) {
        continue;
      }

      // Check if this biome is a preferred type for this mob
      boolean isPreferredBiome = isPreferredBiomeType(event, config.biomeTypes());

      if (isPreferredBiome || shouldSpawnInNonPreferredBiome(event, config)) {
        addSpawnEntry(event, entityType, config, isPreferredBiome);
      }
    }
  }

  private static boolean isPandoraBiome(BiomeLoadingEvent event) {
    // Check if this is a Pandora biome by category
    return PANDORA_BIOME_CATEGORIES.contains(event.getCategory())
        || event.getName().getPath().contains("pandora")
        || event.getName().getPath().contains("pandoran");
  }

  private static boolean isPreferredBiomeType(BiomeLoadingEvent event, String[] preferredTypes) {
    if (preferredTypes == null || preferredTypes.length == 0) {
      return false;
    }

    ResourceKey<Biome> biomeKey = ResourceKey.create(Registries.BIOME, event.getName());

    for (String type : preferredTypes) {
      if (BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.getType(type))) {
        return true;
      }
    }

    return false;
  }

  private static boolean shouldSpawnInNonPreferredBiome(
      BiomeLoadingEvent event, SpawnConfig config) {
    // Random chance to spawn in non-preferred but valid biomes
    float spawnChance = event.getRandom().nextFloat();
    return spawnChance < (config.minSpawnChance() * 0.5f); // Reduced chance in non-preferred biomes
  }

  private static void addSpawnEntry(
      BiomeLoadingEvent event,
      EntityType<?> entityType,
      SpawnConfig config,
      boolean isPreferredBiome) {
    int weight = config.weight();
    int minCount = config.minCount();
    int maxCount = config.maxCount();

    if (!isPreferredBiome) {
      // Reduce spawn rates in non-preferred biomes
      weight = Math.max(1, weight / 2);
      minCount = Math.max(1, minCount - 1);
      maxCount = Math.max(1, maxCount - 1);
    }

    // Add the spawn entry
    event
        .getSpawns()
        .addSpawn(
            config.category(),
            new MobSpawnSettings.SpawnerData(entityType, weight, minCount, maxCount));
  }

  /**
   * Gets the spawn configuration for a mob type.
   *
   * @param entityType The entity type to get the config for
   * @return The spawn configuration, or null if not found
   */
  public static SpawnConfig getSpawnConfig(EntityType<?> entityType) {
    return SPAWN_CONFIGS.get(entityType);
  }

  /**
   * Updates the spawn configuration for a mob type at runtime.
   *
   * @param entityType The entity type to update
   * @param config The new spawn configuration
   */
  public static void updateSpawnConfig(EntityType<?> entityType, SpawnConfig config) {
    if (entityType != null && config != null) {
      SPAWN_CONFIGS.put(entityType, config);
    }
  }
}
