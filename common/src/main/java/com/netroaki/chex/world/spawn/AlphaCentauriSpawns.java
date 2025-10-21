package com.netroaki.chex.world.spawn;

import com.netroaki.chex.entity.ModEntities;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AlphaCentauriSpawns {
  // Biome keys for Alpha Centauri A dimension
  private static final List<ResourceLocation> ALPHA_CENTAURI_BIOMES =
      Arrays.asList(
          new ResourceLocation("cosmic_horizons_extended", "photosphere_platforms"),
          new ResourceLocation("cosmic_horizons_extended", "corona_streams"));

  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onBiomeLoad(BiomeLoadingEvent event) {
    // Only add spawns to our dimension's biomes
    if (!isAlphaCentauriBiome(event.getName())) {
      return;
    }

    // Add spawns based on biome type
    if (event.getName().getPath().contains("photosphere")) {
      // Photosphere Platforms - More common for Plasma Wraiths and Drones
      addSpawn(event, ModEntities.PLASMA_WRAITH.get(), 35, 1, 2);
      addSpawn(event, ModEntities.SOLAR_ENGINEER_DRONE.get(), 20, 1, 1);
      addSpawn(event, ModEntities.FLARE_SPRITE.get(), 15, 1, 2);
    } else if (event.getName().getPath().contains("corona")) {
      // Corona Streams - More common for Flare Sprites
      addSpawn(event, ModEntities.FLARE_SPRITE.get(), 40, 1, 3);
      addSpawn(event, ModEntities.PLASMA_WRAITH.get(), 25, 1, 1);
      addSpawn(event, ModEntities.SOLAR_ENGINEER_DRONE.get(), 10, 1, 1);
    }
  }

  private static boolean isAlphaCentauriBiome(ResourceLocation biomeName) {
    return ALPHA_CENTAURI_BIOMES.contains(biomeName);
  }

  private static void addSpawn(
      BiomeLoadingEvent event, EntityType<?> entityType, int weight, int minCount, int maxCount) {
    event
        .getSpawns()
        .getSpawner(getSpawnCategory(entityType))
        .add(new MobSpawnSettings.SpawnerData(entityType, weight, minCount, maxCount));
  }

  private static MobCategory getSpawnCategory(EntityType<?> entityType) {
    if (entityType == ModEntities.SOLAR_ENGINEER_DRONE.get()) {
      return MobCategory.CREATURE; // Neutral/friendly mob
    }
    return MobCategory.MONSTER; // Hostile mobs
  }
}
