package com.netroaki.chex.world;

import static com.netroaki.chex.CHEX.MOD_ID;

import com.netroaki.chex.entity.arrakis.SandwormJuvenileEntity;
import com.netroaki.chex.entity.arrakis.SpiceGathererEntity;
import com.netroaki.chex.entity.arrakis.StormHawkEntity;
import com.netroaki.chex.init.EntityInit;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnPlacements {

  public static void registerSpawnPlacements() {
    // Sandworm Juvenile - spawns on solid blocks
    SpawnPlacements.register(
        EntityInit.SANDWORM_JUVENILE.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        SandwormJuvenileEntity::checkMobSpawnRules);

    // Storm Hawk - spawns in air
    SpawnPlacements.register(
        EntityInit.STORM_HAWK.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        StormHawkEntity::checkMobSpawnRules);

    // Spice Gatherer - spawns like animals
    SpawnPlacements.register(
        EntityInit.SPICE_GATHERER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        SpiceGathererEntity::checkMobSpawnRules);
  }

  @SubscribeEvent
  public static void onBiomeLoad(BiomeLoadingEvent event) {
    if (event.getName() == null) return;

    // Only add spawns to Arrakis dimension biomes
    if (event.getName().getNamespace().equals(MOD_ID)
        && event.getName().getPath().contains("arrakis")) {

      // Sandworm Juvenile - common in deserts
      if (event.getName().getPath().contains("desert")) {
        event
            .getSpawns()
            .addSpawn(
                MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(
                    EntityInit.SANDWORM_JUVENILE.get(),
                    4, // Weight (spawn probability)
                    1, // Min group size
                    2 // Max group size
                    ));
      }

      // Storm Hawk - spawns in high altitude areas
      if (event.getName().getPath().contains("highlands")
          || event.getName().getPath().contains("cliffs")) {
        event
            .getSpawns()
            .addSpawn(
                MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(
                    EntityInit.STORM_HAWK.get(),
                    3, // Weight
                    1, // Min group size
                    3 // Max group size
                    ));
      }

      // Spice Gatherer - spawns near spice fields
      if (event.getName().getPath().contains("spice")) {
        event
            .getSpawns()
            .addSpawn(
                MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(
                    EntityInit.SPICE_GATHERER.get(),
                    6, // Weight
                    1, // Min group size
                    3 // Max group size
                    ));
      }
    }
  }
}
