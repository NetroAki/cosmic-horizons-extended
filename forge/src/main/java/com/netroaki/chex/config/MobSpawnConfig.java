package com.netroaki.chex.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.ForgeConfigSpec;

public class MobSpawnConfig {
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
            1.0f // maxSpawnChance
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
            0.8f // maxSpawnChance
            ));
  }

  public static final ForgeConfigSpec SPEC = BUILDER.build();

  public static void register() {
    // Commented out until configuration system is properly implemented
    // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC,
    // "chex-mob-spawns.toml");
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
    // .comment("Spawn weight for " + name + " (higher = more common)")
    // .defineInRange("spawns." + configKey + ".weight", defaults.weight, 1, 100);
    //
    // ForgeConfigSpec.IntValue minCount =
    // BUILDER
    // .comment("Minimum number of " + name + " in a spawn group")
    // .defineInRange("spawns." + configKey + ".min_count", defaults.minCount, 1,
    // 10);
    //
    // ForgeConfigSpec.IntValue maxCount =
    // BUILDER
    // .comment("Maximum number of " + name + " in a spawn group")
    // .defineInRange("spawns." + configKey + ".max_count", defaults.maxCount, 1,
    // 20);
    //
    // ForgeConfigSpec.EnumValue<MobCategory> category =
    // BUILDER
    // .comment("Spawn category for " + name)
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
    // () -> maxSpawnChance.get().floatValue()));
    // }
  }

  public static class SpawnConfig {
    public static final SpawnConfig DEFAULT =
        new SpawnConfig(
            () -> 1, () -> 1, () -> 1, () -> MobCategory.CREATURE, () -> 0.1f, () -> 0.3f);

    private final Supplier<Integer> weight;
    private final Supplier<Integer> minCount;
    private final Supplier<Integer> maxCount;
    private final Supplier<MobCategory> category;
    private final Supplier<Float> minSpawnChance;
    private final Supplier<Float> maxSpawnChance;

    public SpawnConfig(
        int weight,
        int minCount,
        int maxCount,
        MobCategory category,
        float minSpawnChance,
        float maxSpawnChance) {
      this(
          () -> weight,
          () -> minCount,
          () -> maxCount,
          () -> category,
          () -> minSpawnChance,
          () -> maxSpawnChance);
    }

    public SpawnConfig(
        Supplier<Integer> weight,
        Supplier<Integer> minCount,
        Supplier<Integer> maxCount,
        Supplier<MobCategory> category,
        Supplier<Float> minSpawnChance,
        Supplier<Float> maxSpawnChance) {
      this.weight = weight;
      this.minCount = minCount;
      this.maxCount = maxCount;
      this.category = category;
      this.minSpawnChance = minSpawnChance;
      this.maxSpawnChance = maxSpawnChance;
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
  }
}
