package com.netroaki.chex.registry.entities;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entities.CliffHunterEntity;
import com.netroaki.chex.entities.GlowbeastEntity;
import com.netroaki.chex.entities.SkyGrazerEntity;
import com.netroaki.chex.entities.SporefliesEntity;
import com.netroaki.chex.entities.boss.CliffHunterAlphaEntity;
import com.netroaki.chex.entities.boss.DeepSeaSirenEntity;
import com.netroaki.chex.entities.boss.MoltenBehemothEntity;
import com.netroaki.chex.entities.boss.SkySovereignEntity;
import com.netroaki.chex.entities.boss.SporeTyrantEntity;
import com.netroaki.chex.entities.boss.StormRocEntity;
import com.netroaki.chex.entities.boss.WorldheartAvatarEntity;
import com.netroaki.chex.entity.SporeCloudEntity;
import com.netroaki.chex.entity.Sporefly;
import com.netroaki.chex.entity.crystal.FloatingCrystalShard;
import com.netroaki.chex.entity.projectile.SporeCloudProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXEntities {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CHEX.MOD_ID);

  // Crystalis Entities
  public static final RegistryObject<EntityType<FloatingCrystalShard>> FLOATING_CRYSTAL =
      ENTITY_TYPES.register(
          "floating_crystal",
          () ->
              EntityType.Builder.<FloatingCrystalShard>of(
                      FloatingCrystalShard::new, MobCategory.AMBIENT)
                  .sized(0.5F, 0.5F)
                  .clientTrackingRange(6)
                  .updateInterval(2)
                  .build("floating_crystal"));

  // Pandora Fauna
  public static final RegistryObject<EntityType<GlowbeastEntity>> GLOWBEAST =
      ENTITY_TYPES.register(
          "glowbeast",
          () ->
              EntityType.Builder.of(GlowbeastEntity::new, MobCategory.CREATURE)
                  .sized(1.0F, 1.0F)
                  .clientTrackingRange(10)
                  .build("glowbeast"));

  public static final RegistryObject<EntityType<SporefliesEntity>> SPOREFLIES =
      ENTITY_TYPES.register(
          "sporeflies",
          () ->
              EntityType.Builder.of(SporefliesEntity::new, MobCategory.AMBIENT)
                  .sized(0.5F, 0.5F)
                  .clientTrackingRange(8)
                  .build("sporeflies"));

  public static final RegistryObject<EntityType<Sporefly>> SPOREFLY =
      ENTITY_TYPES.register(
          "sporefly",
          () ->
              EntityType.Builder.<Sporefly>of(Sporefly::new, MobCategory.AMBIENT)
                  .sized(0.75F, 0.75F)
                  .clientTrackingRange(8)
                  .build("sporefly"));

  public static final RegistryObject<EntityType<Sporefly>> SPORELING =
      ENTITY_TYPES.register(
          "sporeling",
          () ->
              EntityType.Builder.<Sporefly>of(Sporefly::new, MobCategory.AMBIENT)
                  .sized(0.6F, 0.6F)
                  .clientTrackingRange(8)
                  .build("sporeling"));

  public static final RegistryObject<EntityType<Sporefly>> ELITE_SPORELING =
      ENTITY_TYPES.register(
          "elite_sporeling",
          () ->
              EntityType.Builder.<Sporefly>of(Sporefly::new, MobCategory.MONSTER)
                  .sized(0.6F, 0.6F)
                  .clientTrackingRange(8)
                  .build("elite_sporeling"));

  public static final RegistryObject<EntityType<SporeCloudEntity>> SPORE_CLOUD =
      ENTITY_TYPES.register(
          "spore_cloud",
          () ->
              EntityType.Builder.<SporeCloudEntity>of(
                      (type, level) -> new SporeCloudEntity(type, level), MobCategory.MISC)
                  .sized(0.2F, 0.2F)
                  .clientTrackingRange(6)
                  .updateInterval(10)
                  .build("spore_cloud"));

  public static final RegistryObject<EntityType<SporeCloudProjectile>> SPORE_CLOUD_PROJECTILE =
      ENTITY_TYPES.register(
          "spore_cloud_projectile",
          () ->
              EntityType.Builder.<SporeCloudProjectile>of(
                      (type, level) -> new SporeCloudProjectile(type, level), MobCategory.MISC)
                  .sized(0.25F, 0.25F)
                  .clientTrackingRange(8)
                  .updateInterval(1)
                  .build("spore_cloud_projectile"));

  public static final RegistryObject<EntityType<SkyGrazerEntity>> SKY_GRAZER =
      ENTITY_TYPES.register(
          "sky_grazer",
          () ->
              EntityType.Builder.of(SkyGrazerEntity::new, MobCategory.CREATURE)
                  .sized(1.5F, 1.5F)
                  .clientTrackingRange(10)
                  .build("sky_grazer"));

  public static final RegistryObject<EntityType<CliffHunterEntity>> CLIFF_HUNTER =
      ENTITY_TYPES.register(
          "cliff_hunter",
          () ->
              EntityType.Builder.of(CliffHunterEntity::new, MobCategory.MONSTER)
                  .sized(1.2F, 2.0F)
                  .clientTrackingRange(8)
                  .build("cliff_hunter"));

  // Boss Entities
  public static final RegistryObject<EntityType<SporeTyrantEntity>> SPORE_TYRANT =
      ENTITY_TYPES.register(
          "spore_tyrant",
          () ->
              EntityType.Builder.of(SporeTyrantEntity::new, MobCategory.MONSTER)
                  .sized(2.0F, 3.0F)
                  .clientTrackingRange(12)
                  .build("spore_tyrant"));

  public static final RegistryObject<EntityType<CliffHunterAlphaEntity>> CLIFF_HUNTER_ALPHA =
      ENTITY_TYPES.register(
          "cliff_hunter_alpha",
          () ->
              EntityType.Builder.of(CliffHunterAlphaEntity::new, MobCategory.MONSTER)
                  .sized(1.5F, 2.5F)
                  .clientTrackingRange(10)
                  .build("cliff_hunter_alpha"));

  public static final RegistryObject<EntityType<DeepSeaSirenEntity>> DEEP_SEA_SIREN =
      ENTITY_TYPES.register(
          "deep_sea_siren",
          () ->
              EntityType.Builder.of(DeepSeaSirenEntity::new, MobCategory.MONSTER)
                  .sized(1.8F, 2.8F)
                  .clientTrackingRange(10)
                  .build("deep_sea_siren"));

  public static final RegistryObject<EntityType<MoltenBehemothEntity>> MOLTEN_BEHEMOTH =
      ENTITY_TYPES.register(
          "molten_behemoth",
          () ->
              EntityType.Builder.of(MoltenBehemothEntity::new, MobCategory.MONSTER)
                  .sized(2.5F, 3.5F)
                  .clientTrackingRange(10)
                  .build("molten_behemoth"));

  public static final RegistryObject<EntityType<StormRocEntity>> STORM_ROC =
      ENTITY_TYPES.register(
          "storm_roc",
          () ->
              EntityType.Builder.of(StormRocEntity::new, MobCategory.MONSTER)
                  .sized(3.5F, 2.5F)
                  .clientTrackingRange(12)
                  .build("storm_roc"));

  public static final RegistryObject<EntityType<SkySovereignEntity>> SKY_SOVEREIGN =
      ENTITY_TYPES.register(
          "sky_sovereign",
          () ->
              EntityType.Builder.of(SkySovereignEntity::new, MobCategory.MONSTER)
                  .sized(2.2F, 3.5F)
                  .clientTrackingRange(12)
                  .build("sky_sovereign"));

  public static final RegistryObject<EntityType<WorldheartAvatarEntity>> WORLDHEART_AVATAR =
      ENTITY_TYPES.register(
          "worldheart_avatar",
          () ->
              EntityType.Builder.of(WorldheartAvatarEntity::new, MobCategory.MONSTER)
                  .sized(3.0F, 5.0F)
                  .clientTrackingRange(15)
                  .build("worldheart_avatar"));
}
