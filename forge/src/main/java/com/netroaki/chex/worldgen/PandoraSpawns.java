package com.netroaki.chex.worldgen;

import com.netroaki.chex.registry.entities.CHEXEntities;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "chex")
public class PandoraSpawns {

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        // Only add spawns to Pandora biomes
        if (event.getName() == null || !event.getName().getNamespace().equals("chex")) {
            return;
        }

        // Glowbeast - Spawns in Bioluminescent Forest
        if (event.getName().getPath().contains("bioluminescent_forest")) {
            event.getSpawns().addSpawn(
                MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(
                    CHEXEntities.GLOWBEAST.get(),
                    10,  // Weight (spawn frequency)
                    2,   // Min group size
                    4    // Max group size
                )
            );
            
            // Sporeflies - Spawn around Glowbeasts
            event.getSpawns().addSpawn(
                MobCategory.AMBIENT,
                new MobSpawnSettings.SpawnerData(
                    CHEXEntities.SPOREFLIES.get(),
                    15,  // Higher weight as they're ambient
                    3,   // Min group size
                    6    // Max group size
                )
            );
        }

        // Sky Grazer - Spawns in Floating Islands
        if (event.getName().getPath().contains("floating_islands")) {
            event.getSpawns().addSpawn(
                MobCategory.CREATURE,
                new MobSpawnSettings.SpawnerData(
                    CHEXEntities.SKY_GRAZER.get(),
                    8,   // Weight
                    1,   // Min group size
                    3    // Max group size
                )
            );
        }

        // Cliff Hunter - Spawns in Floating Mountains
        if (event.getName().getPath().contains("floating_mountains")) {
            event.getSpawns().addSpawn(
                MobCategory.MONSTER,
                new MobSpawnSettings.SpawnerData(
                    CHEXEntities.CLIFF_HUNTER.get(),
                    6,   // Weight (rarer than passive mobs)
                    1,   // Min group size
                    2    // Max group size
                )
            );
        }
    }

    // Register spawn placements
    public static void registerSpawnPlacements() {
        // Glowbeast - Spawns on solid ground
        SpawnPlacements.register(
            CHEXEntities.GLOWBEAST.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> 
                world.getBlockState(pos.below()).isSolidRender(world, pos.below())
        );

        // Sporeflies - Can spawn in air near glowbeasts
        SpawnPlacements.register(
            CHEXEntities.SPOREFLIES.get(),
            SpawnPlacements.Type.NO_RESTRICTIONS,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> true
        );

        // Sky Grazer - Spawns on solid blocks in floating islands
        SpawnPlacements.register(
            CHEXEntities.SKY_GRAZER.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> 
                world.getBlockState(pos.below()).isSolidRender(world, pos.below())
        );

        // Cliff Hunter - Spawns on solid blocks in mountains
        SpawnPlacements.register(
            CHEXEntities.CLIFF_HUNTER.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> {
                // Only spawn on solid blocks with air above, and not too close to the ground
                return world.getBlockState(pos.below()).isSolidRender(world, pos.below()) &&
                       world.getBlockState(pos).isAir() &&
                       world.getBlockState(pos.above()).isAir() &&
                       pos.getY() > 80;  // Only spawn high up in the mountains
            }
        );
    }
}
