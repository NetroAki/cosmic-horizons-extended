package com.netroaki.chex.world.stormworld.entity;

import com.netroaki.chex.registry.entities.CHEXEntities;
import com.netroaki.chex.world.stormworld.weather.StormworldWeatherManager;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "chex", bus = Mod.EventBusSubscriber.Bus.MOD)
public class StormworldSpawns {

  // Spawn weights for each mob type
  private static final Map<EntityType<? extends Mob>, StormworldSpawnData> SPAWN_DATA =
      new HashMap<>();

  // Biome tags for spawn restrictions
  private static final TagKey<Biome> STORMCALLER_BIOMES =
      TagKey.create(Registries.BIOME, new ResourceLocation("chex", "stormcaller_biomes"));
  private static final TagKey<Biome> WINDRIDER_BIOMES =
      TagKey.create(Registries.BIOME, new ResourceLocation("chex", "windrider_biomes"));
  private static final TagKey<Biome> STATIC_JELLY_BIOMES =
      TagKey.create(Registries.BIOME, new ResourceLocation("chex", "static_jelly_biomes"));

  // Register spawn data for our mobs
  static {
    // Stormcaller - rare, spawns in open areas during storms
    SPAWN_DATA.put(
        CHEXEntities.STORMCALLER.get(),
        new StormworldSpawnData(30, 1, 2, 0.7f, 0.8f, STORMCALLER_BIOMES));

    // Windrider - common in high altitude, spawns in small groups
    SPAWN_DATA.put(
        CHEXEntities.WINDRIDER.get(),
        new StormworldSpawnData(60, 1, 3, 0.5f, 0.6f, WINDRIDER_BIOMES));

    // Static Jelly - uncommon, spawns near electrical features
    SPAWN_DATA.put(
        CHEXEntities.STATIC_JELLY.get(),
        new StormworldSpawnData(45, 1, 1, 0.6f, 0.3f, STATIC_JELLY_BIOMES));
  }

  @SubscribeEvent
  public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
    // Configure spawn placement rules for each mob
    event.register(
        CHEXEntities.STORMCALLER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        StormworldSpawns::checkStormSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        CHEXEntities.WINDRIDER.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        StormworldSpawns::checkFlyingSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);

    event.register(
        CHEXEntities.STATIC_JELLY.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        StormworldSpawns::checkJellySpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);
  }

  // Custom spawn predicate for Stormcaller
  private static boolean checkStormSpawnRules(
      EntityType<? extends Mob> type,
      ServerLevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    // Only spawn during storms or in stormy biomes
    if (StormworldWeatherManager.isStormActive(level)) {
      return true;
    }

    // Check for stormy biomes
    Holder<Biome> biome = level.getBiome(pos);
    return biome.is(STORMCALLER_BIOMES) && random.nextFloat() < 0.1f;
  }

  // Custom spawn predicate for Windrider
  private static boolean checkFlyingSpawnRules(
      EntityType<? extends Mob> type,
      ServerLevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    // Check for high altitude or windy conditions
    if (pos.getY() > 100 || level.canSeeSky(pos)) {
      return true;
    }

    // Check for windy biomes
    Holder<Biome> biome = level.getBiome(pos);
    return biome.is(WINDRIDER_BIOMES) && random.nextFloat() < 0.2f;
  }

  // Custom spawn predicate for Static Jelly
  private static boolean checkJellySpawnRules(
      EntityType<? extends Mob> type,
      ServerLevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    // Check for electrical features or stormy weather
    if (StormworldWeatherManager.isStormActive(level) || isNearElectricalFeature(level, pos)) {
      return true;
    }

    // Check for jelly biomes
    Holder<Biome> biome = level.getBiome(pos);
    return biome.is(STATIC_JELLY_BIOMES) && random.nextFloat() < 0.15f;
  }

  // Check if position is near an electrical feature
  private static boolean isNearElectricalFeature(ServerLevelAccessor level, BlockPos pos) {
    // TODO: Implement check for nearby electrical features
    return false;
  }

  // Register entity attributes
  @SubscribeEvent
  public static void onAttributeCreate(EntityAttributeCreationEvent event) {
    event.put(CHEXEntities.STORMCALLER.get(), StormcallerEntity.createAttributes().build());
    event.put(CHEXEntities.WINDRIDER.get(), WindriderEntity.createAttributes().build());
    event.put(CHEXEntities.STATIC_JELLY.get(), StaticJellyEntity.createAttributes().build());
  }

  // Data class to hold spawn configuration
  private static class StormworldSpawnData {
    public final int weight;
    public final int minCount;
    public final int maxCount;
    public final float baseSpawnChance;
    public final float stormSpawnChance;
    public final TagKey<Biome> biomeTag;

    public StormworldSpawnData(
        int weight,
        int minCount,
        int maxCount,
        float baseSpawnChance,
        float stormSpawnChance,
        TagKey<Biome> biomeTag) {
      this.weight = weight;
      this.minCount = minCount;
      this.maxCount = maxCount;
      this.baseSpawnChance = baseSpawnChance;
      this.stormSpawnChance = stormSpawnChance;
      this.biomeTag = biomeTag;
    }
  }
}
