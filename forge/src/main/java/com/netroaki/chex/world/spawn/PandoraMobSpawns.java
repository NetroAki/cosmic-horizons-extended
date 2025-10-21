package com.netroaki.chex.world.spawn;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PandoraMobSpawns {
  // Biome categories where mobs can spawn
  private static final Set<Biome.BiomeCategory> PANDORA_BIOMES =
      Set.of(
          Biome.BiomeCategory.FOREST,
          Biome.BiomeCategory.PLAINS,
          Biome.BiomeCategory.MOUNTAIN,
          Biome.BiomeCategory.JUNGLE);

  // Mob spawn configurations
  private static final Map<EntityType<?>, MobSpawnConfig> MOB_SPAWN_CONFIGS = new HashMap<>();

  static {
    // Glowbeast spawn config
    // Glowbeast registration deferred until entity is enabled

    // Sporefly spawn config
    registerMobSpawn(
        com.netroaki.chex.registry.entities.CHEXEntities.SPOREFLIES.get(),
        new MobSpawnConfig(
            8, // weight
            2, // minCount
            6, // maxCount
            MobCategory.AMBIENT,
            Set.of("FOREST", "SWAMP"),
            0.4f,
            0.8f));
  }

  @SubscribeEvent
  public static void onBiomeLoading(BiomeLoadingEvent event) {
    // Only process our dimension's biomes
    if (event.getName() == null
        || !event.getName().getNamespace().equals(com.netroaki.chex.CHEX.MOD_ID)) {
      return;
    }

    // Check if this is a valid biome category for our mobs
    boolean isValidBiome = PANDORA_BIOMES.contains(event.getCategory());
    if (!isValidBiome) {
      return;
    }

    // Add spawns for each mob
    for (Map.Entry<EntityType<?>, MobSpawnConfig> entry : MOB_SPAWN_CONFIGS.entrySet()) {
      EntityType<?> entityType = entry.getKey();
      MobSpawnConfig config = entry.getValue();

      // Check if this biome matches any of the preferred types
      boolean matchesBiomeType =
          config.biomeTypes().stream()
              .anyMatch(
                  type ->
                      BiomeDictionary.hasType(
                          ResourceKey.create(Registries.BIOME, event.getName()),
                          BiomeDictionary.Type.getType(type)));

      if (matchesBiomeType) {
        // Higher spawn weight for preferred biomes
        event
            .getSpawns()
            .addSpawn(
                config.category(),
                new MobSpawnSettings.SpawnerData(
                    entityType, config.weight(), config.minCount(), config.maxCount()));
      } else if (event.getRandom().nextFloat() < 0.3f) {
        // Lower chance to spawn in non-preferred but valid biomes
        event
            .getSpawns()
            .addSpawn(
                config.category(),
                new MobSpawnSettings.SpawnerData(
                    entityType,
                    Math.max(1, config.weight() / 2), // Reduced weight
                    Math.max(1, config.minCount() - 1), // Reduced count
                    Math.max(1, config.maxCount() - 1) // Reduced count
                    ));
      }
    }
  }

  private static void registerMobSpawn(EntityType<?> entityType, MobSpawnConfig config) {
    MOB_SPAWN_CONFIGS.put(entityType, config);
  }

  /**
   * Configuration for mob spawning
   *
   * @param weight Spawn weight (relative to other mobs)
   * @param minCount Minimum number in a spawn group
   * @param maxCount Maximum number in a spawn group
   * @param category Mob category (determines spawn rules)
   * @param biomeTypes Preferred biome types (BiomeDictionary types as strings)
   * @param minSpawnProbability Minimum spawn probability (0-1)
   * @param maxSpawnProbability Maximum spawn probability (0-1)
   */
  public record MobSpawnConfig(
      int weight,
      int minCount,
      int maxCount,
      MobCategory category,
      Set<String> biomeTypes,
      float minSpawnProbability,
      float maxSpawnProbability) {
    public MobSpawnConfig {
      if (weight <= 0) throw new IllegalArgumentException("Weight must be positive");
      if (minCount <= 0 || maxCount <= 0)
        throw new IllegalArgumentException("Counts must be positive");
      if (minCount > maxCount)
        throw new IllegalArgumentException("minCount cannot be greater than maxCount");
      if (minSpawnProbability < 0
          || maxSpawnProbability > 1
          || minSpawnProbability > maxSpawnProbability) {
        throw new IllegalArgumentException("Invalid spawn probability range");
      }
    }
  }
}
