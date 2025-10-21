package com.netroaki.chex.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Configuration for mob spawns in the Pandora dimension. This allows server admins to customize
 * spawn rates and behaviors.
 */
public class PandoraMobSpawnsConfig {
  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  private static final Map<EntityType<?>, SpawnConfig> CONFIGS = new HashMap<>();

  // Default spawn configurations
  static {
    // Glowbeast defaults
    registerSpawnConfig(
        "glowbeast",
        new SpawnConfig(
            2, // weight
            1, // minCount
            1, // maxCount
            MobCategory.CREATURE,
            0.3f, // minSpawnChance
            1.0f, // maxSpawnChance
            new String[] {"FOREST", "JUNGLE"} // Preferred biome types
            ));

    // Sporefly defaults
    registerSpawnConfig(
        "sporefly",
        new SpawnConfig(
            8, // weight
            2, // minCount
            6, // maxCount
            MobCategory.AMBIENT,
            0.4f, // minSpawnChance
            0.8f, // maxSpawnChance
            new String[] {"FOREST", "SWAMP"} // Preferred biome types
            ));
  }

  public static final ForgeConfigSpec SPEC = BUILDER.build();

  public static void register() {
    // Commented out until configuration system is properly implemented
    // ModLoadingContext.get()
    // .registerConfig(ModConfig.Type.COMMON, SPEC, "chex-pandora-mob-spawns.toml");
  }

  public static SpawnConfig getSpawnConfig(EntityType<?> entityType) {
    return CONFIGS.getOrDefault(entityType, SpawnConfig.DEFAULT);
  }

  private static void registerSpawnConfig(String name, SpawnConfig defaults) {
    // Commented out until configuration system is properly implemented
    // String configKey = name.toLowerCase().replace('_', '-');
    //
    // ForgeConfigSpec.IntValue weight =
    // BUILDER
    // .comment("Spawn weight for " + name + " in Pandora (higher = more common)")
    // .defineInRange("spawns." + configKey + ".weight", defaults.weight, 1, 100);
    //
    // ForgeConfigSpec.IntValue minCount =
    // BUILDER
    // .comment("Minimum number of " + name + " in a spawn group in Pandora")
    // .defineInRange("spawns." + configKey + ".min_count", defaults.minCount, 1,
    // 10);
    //
    // ForgeConfigSpec.IntValue maxCount =
    // BUILDER
    // .comment("Maximum number of " + name + " in a spawn group in Pandora")
    // .defineInRange("spawns." + configKey + ".max_count", defaults.maxCount, 1,
    // 20);
    //
    // ForgeConfigSpec.EnumValue<MobCategory> category =
    // BUILDER
    // .comment("Spawn category for " + name + " in Pandora")
    // .defineEnum("spawns." + configKey + ".category", defaults.category);
    //
    // ForgeConfigSpec.DoubleValue minSpawnChance =
    // BUILDER
    // .comment("Minimum spawn chance for " + name + " in preferred biomes (0.0 to
    // 1.0)")
    // .defineInRange(
    // "spawns." + configKey + ".min_spawn_chance", defaults.minSpawnChance, 0.0,
    // 1.0);
    //
    // ForgeConfigSpec.DoubleValue maxSpawnChance =
    // BUILDER
    // .comment("Maximum spawn chance for " + name + " in preferred biomes (0.0 to
    // 1.0)")
    // .defineInRange(
    // "spawns." + configKey + ".max_spawn_chance", defaults.maxSpawnChance, 0.0,
    // 1.0);
    //
    // // Get the entity type from the registry
    // ResourceLocation entityId = ResourceLocation.parse("chex:" + name);
    // EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityId);
    //
    // if (entityType != null) {
    // CONFIGS.put(
    // entityType,
    // new SpawnConfig(
    // weight::get,
    // minCount::get,
    // maxCount::get,
    // () -> category.get(),
    // () -> minSpawnChance.get().floatValue(),
    // () -> maxSpawnChance.get().floatValue(),
    // defaults.biomeTypes));
    // }
  }

  public static class SpawnConfig {
    public static final SpawnConfig DEFAULT =
        new SpawnConfig(
            () -> 1,
            () -> 1,
            () -> 1,
            () -> MobCategory.CREATURE,
            () -> 0.1f,
            () -> 0.3f,
            new String[] {"NONE"});

    private final Supplier<Integer> weight;
    private final Supplier<Integer> minCount;
    private final Supplier<Integer> maxCount;
    private final Supplier<MobCategory> category;
    private final Supplier<Float> minSpawnChance;
    private final Supplier<Float> maxSpawnChance;
    private final String[] biomeTypes;

    public SpawnConfig(
        int weight,
        int minCount,
        int maxCount,
        MobCategory category,
        float minSpawnChance,
        float maxSpawnChance,
        String[] biomeTypes) {
      this(
          () -> weight,
          () -> minCount,
          () -> maxCount,
          () -> category,
          () -> minSpawnChance,
          () -> maxSpawnChance,
          biomeTypes);
    }

    public SpawnConfig(
        Supplier<Integer> weight,
        Supplier<Integer> minCount,
        Supplier<Integer> maxCount,
        Supplier<MobCategory> category,
        Supplier<Float> minSpawnChance,
        Supplier<Float> maxSpawnChance,
        String[] biomeTypes) {
      this.weight = weight;
      this.minCount = minCount;
      this.maxCount = maxCount;
      this.category = category;
      this.minSpawnChance = minSpawnChance;
      this.maxSpawnChance = maxSpawnChance;
      this.biomeTypes = biomeTypes;
    }

    public int weight() {
      return weight.get();
    }

    public int minCount() {
      return minCount.get();
    }

    public int maxCount() {
      return maxCount.get();
    }

    public MobCategory category() {
      return category.get();
    }

    public float minSpawnChance() {
      return minSpawnChance.get();
    }

    public float maxSpawnChance() {
      return maxSpawnChance.get();
    }

    public String[] biomeTypes() {
      return biomeTypes;
    }
  }
}
