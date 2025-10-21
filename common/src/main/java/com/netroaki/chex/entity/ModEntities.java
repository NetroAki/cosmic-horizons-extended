package com.netroaki.chex.entity;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.entity.alpha_centauri.FlareSpriteEntity;
import com.netroaki.chex.entity.alpha_centauri.PlasmaWraithEntity;
import com.netroaki.chex.entity.alpha_centauri.SolarEngineerDrone;
import com.netroaki.chex.entity.boss.stellar_avatar.StellarAvatarEntity;
import com.netroaki.chex.entity.crystalis.AbyssDrifterEntity;
import com.netroaki.chex.entity.crystalis.CliffRaptorEntity;
import com.netroaki.chex.entity.crystalis.CryoMonarchEntity;
import com.netroaki.chex.entity.crystalis.CrystalGrazerEntity;
import com.netroaki.chex.entity.crystalis.FrostHareEntity;
import com.netroaki.chex.entity.crystalis.IceStalkerEntity;
import com.netroaki.chex.entity.crystalis.MistWraithEntity;
import com.netroaki.chex.entity.crystalis.PressureLeviathanEntity;
import com.netroaki.chex.entity.crystalis.ShardlingEntity;
import com.netroaki.chex.entity.crystalis.SnowGargoyleEntity;
import com.netroaki.chex.entity.crystalis.VentCrawlerEntity;
import com.netroaki.chex.entity.dyson.boss.DysonApexEntity;
import com.netroaki.chex.entity.exotica.boss.RealityBreakerEntity;
import com.netroaki.chex.entity.hollowworld.boss.HollowTyrantEntity;
import com.netroaki.chex.entity.inferno.AshCrawlerEntity;
import com.netroaki.chex.entity.inferno.FireWraithEntity;
import com.netroaki.chex.entity.inferno.InfernalSovereignEntity;
import com.netroaki.chex.entity.inferno.MagmaHopperEntity;
import com.netroaki.chex.entity.neutronforge.boss.ForgeStarSovereignEntity;
import com.netroaki.chex.entity.projectile.IceShardProjectile;
import com.netroaki.chex.entity.projectile.SolarFlareProjectile;
import com.netroaki.chex.entity.ringworld.SimpleWalkingMob;
import com.netroaki.chex.entity.ringworld.boss.GuardianPrimeEntity;
import com.netroaki.chex.entity.stormworld.SimpleFlyingMob;
import com.netroaki.chex.entity.stormworld.boss.StormlordColossusEntity;
import com.netroaki.chex.entity.torus.boss.TorusWardenEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
  public static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CosmicHorizonsExpanded.MOD_ID);

  // Alpha Centauri A Entities
  public static final RegistryObject<EntityType<PlasmaWraithEntity>> PLASMA_WRAITH =
      ENTITIES.register(
          "plasma_wraith",
          () ->
              EntityType.Builder.of(PlasmaWraithEntity::new, MobCategory.MONSTER)
                  .sized(0.8f, 2.4f)
                  .clientTrackingRange(8)
                  .build("plasma_wraith"));

  public static final RegistryObject<EntityType<FlareSpriteEntity>> FLARE_SPRITE =
      ENTITIES.register(
          "flare_sprite",
          () ->
              EntityType.Builder.of(FlareSpriteEntity::new, MobCategory.MONSTER)
                  .sized(0.6f, 0.6f)
                  .clientTrackingRange(8)
                  .build("flare_sprite"));

  public static final RegistryObject<EntityType<SolarEngineerDrone>> SOLAR_ENGINEER_DRONE =
      ENTITIES.register(
          "solar_engineer_drone",
          () ->
              EntityType.Builder.of(SolarEngineerDrone::new, MobCategory.CREATURE)
                  .sized(0.8f, 0.8f)
                  .clientTrackingRange(8)
                  .build("solar_engineer_drone"));

  // Inferno Prime Entities
  public static final RegistryObject<EntityType<AshCrawlerEntity>> ASH_CRAWLER =
      ENTITIES.register(
          "ash_crawler",
          () ->
              EntityType.Builder.of(AshCrawlerEntity::new, MobCategory.MONSTER)
                  .sized(1.2f, 0.9f)
                  .clientTrackingRange(8)
                  .fireImmune()
                  .build("ash_crawler"));

  public static final RegistryObject<EntityType<FireWraithEntity>> FIRE_WRAITH =
      ENTITIES.register(
          "fire_wraith",
          () ->
              EntityType.Builder.of(FireWraithEntity::new, MobCategory.MONSTER)
                  .sized(0.8f, 2.2f)
                  .clientTrackingRange(8)
                  .fireImmune()
                  .build("fire_wraith"));

  public static final RegistryObject<EntityType<MagmaHopperEntity>> MAGMA_HOPPER =
      ENTITIES.register(
          "magma_hopper",
          () ->
              EntityType.Builder.of(MagmaHopperEntity::new, MobCategory.MONSTER)
                  .sized(0.9f, 0.9f)
                  .clientTrackingRange(8)
                  .fireImmune()
                  .build("magma_hopper"));

  // Inferno Boss
  public static final RegistryObject<EntityType<InfernalSovereignEntity>> INFERNAL_SOVEREIGN =
      ENTITIES.register(
          "infernal_sovereign",
          () ->
              EntityType.Builder.of(InfernalSovereignEntity::new, MobCategory.MONSTER)
                  .sized(2.0f, 4.0f)
                  .clientTrackingRange(10)
                  .fireImmune()
                  .build("infernal_sovereign"));

  // Boss Entities
  public static final RegistryObject<EntityType<StellarAvatarEntity>> STELLAR_AVATAR =
      ENTITIES.register(
          "stellar_avatar",
          () ->
              EntityType.Builder.of(StellarAvatarEntity::new, MobCategory.MONSTER)
                  .sized(2.5f, 5.0f)
                  .clientTrackingRange(10)
                  .fireImmune()
                  .build("stellar_avatar"));

  // Boss Entities
  public static final RegistryObject<EntityType<OceanSovereignEntity>> OCEAN_SOVEREIGN =
      ENTITIES.register(
          "ocean_sovereign",
          () ->
              EntityType.Builder.of(OceanSovereignEntity::new, MobCategory.MONSTER)
                  .sized(3.0f, 8.0f) // Large size for a boss
                  .clientTrackingRange(10)
                  .fireImmune()
                  .build("ocean_sovereign"));

  // Crystalis Entities
  public static final RegistryObject<EntityType<CrystalGrazerEntity>> CRYSTAL_GRAZER =
      ENTITIES.register(
          "crystal_grazer",
          () ->
              EntityType.Builder.of(CrystalGrazerEntity::new, MobCategory.CREATURE)
                  .sized(1.4f, 1.2f)
                  .clientTrackingRange(10)
                  .build("crystal_grazer"));

  public static final RegistryObject<EntityType<ShardlingEntity>> SHARDLING =
      ENTITIES.register(
          "shardling",
          () ->
              EntityType.Builder.of(ShardlingEntity::new, MobCategory.MONSTER)
                  .sized(0.6f, 0.6f)
                  .clientTrackingRange(8)
                  .build("shardling"));

  public static final RegistryObject<EntityType<FrostHareEntity>> FROST_HARE =
      ENTITIES.register(
          "frost_hare",
          () ->
              EntityType.Builder.of(FrostHareEntity::new, MobCategory.CREATURE)
                  .sized(0.5f, 0.5f)
                  .clientTrackingRange(8)
                  .build("frost_hare"));

  public static final RegistryObject<EntityType<IceStalkerEntity>> ICE_STALKER =
      ENTITIES.register(
          "ice_stalker",
          () ->
              EntityType.Builder.of(IceStalkerEntity::new, MobCategory.MONSTER)
                  .sized(1.4f, 0.9f)
                  .clientTrackingRange(8)
                  .build("ice_stalker"));

  public static final RegistryObject<EntityType<MistWraithEntity>> MIST_WRAITH =
      ENTITIES.register(
          "mist_wraith",
          () ->
              EntityType.Builder.of(MistWraithEntity::new, MobCategory.MONSTER)
                  .sized(0.8f, 2.0f)
                  .clientTrackingRange(10)
                  .build("mist_wraith"));

  public static final RegistryObject<EntityType<VentCrawlerEntity>> VENT_CRAWLER =
      ENTITIES.register(
          "vent_crawler",
          () ->
              EntityType.Builder.of(VentCrawlerEntity::new, MobCategory.MONSTER)
                  .sized(1.4f, 0.9f)
                  .clientTrackingRange(8)
                  .fireResistant()
                  .build("vent_crawler"));

  public static final RegistryObject<EntityType<CliffRaptorEntity>> CLIFF_RAPTOR =
      ENTITIES.register(
          "cliff_raptor",
          () ->
              EntityType.Builder.of(CliffRaptorEntity::new, MobCategory.MONSTER)
                  .sized(1.2f, 1.8f)
                  .clientTrackingRange(10)
                  .build("cliff_raptor"));

  public static final RegistryObject<EntityType<SnowGargoyleEntity>> SNOW_GARGOYLE =
      ENTITIES.register(
          "snow_gargoyle",
          () ->
              EntityType.Builder.of(SnowGargoyleEntity::new, MobCategory.MONSTER)
                  .sized(1.0f, 2.2f)
                  .clientTrackingRange(10)
                  .build("snow_gargoyle"));

  public static final RegistryObject<EntityType<PressureLeviathanEntity>> PRESSURE_LEVIATHAN =
      ENTITIES.register(
          "pressure_leviathan",
          () ->
              EntityType.Builder.of(PressureLeviathanEntity::new, MobCategory.MONSTER)
                  .sized(4.0f, 2.0f)
                  .clientTrackingRange(10)
                  .build("pressure_leviathan"));

  public static final RegistryObject<EntityType<AbyssDrifterEntity>> ABYSS_DRIFTER =
      ENTITIES.register(
          "abyss_drifter",
          () ->
              EntityType.Builder.of(AbyssDrifterEntity::new, MobCategory.MONSTER)
                  .sized(0.8f, 2.4f)
                  .clientTrackingRange(10)
                  .build("abyss_drifter"));

  public static final RegistryObject<EntityType<CryoMonarchEntity>> CRYO_MONARCH =
      ENTITIES.register(
          "cryo_monarch",
          () ->
              EntityType.Builder.of(CryoMonarchEntity::new, MobCategory.MONSTER)
                  .sized(2.5f, 5.0f)
                  .clientTrackingRange(10)
                  .fireImmune()
                  .build("cryo_monarch"));

  public static final RegistryObject<EntityType<IceShardProjectile>> ICE_SHARD =
      ENTITIES.register(
          "ice_shard",
          () ->
              EntityType.Builder.<IceShardProjectile>of(IceShardProjectile::new, MobCategory.MISC)
                  .sized(0.25f, 0.25f)
                  .clientTrackingRange(4)
                  .updateInterval(20)
                  .build("ice_shard"));

  // Projectiles
  public static final RegistryObject<EntityType<SolarFlareProjectile>> SOLAR_FLARE =
      ENTITIES.register(
          "solar_flare",
          () ->
              EntityType.Builder.<SolarFlareProjectile>of(
                      SolarFlareProjectile::new, MobCategory.MISC)
                  .sized(0.5f, 0.5f)
                  .clientTrackingRange(4)
                  .updateInterval(10)
                  .build("solar_flare"));

  // Stormworld Boss
  public static final RegistryObject<EntityType<StormlordColossusEntity>> STORMLORD_COLOSSUS =
      ENTITIES.register(
          "stormlord_colossus",
          () ->
              EntityType.Builder.of(StormlordColossusEntity::new, MobCategory.MONSTER)
                  .sized(3.0f, 6.0f)
                  .clientTrackingRange(12)
                  .build("stormlord_colossus"));

  // Ringworld Boss
  public static final RegistryObject<EntityType<GuardianPrimeEntity>> GUARDIAN_PRIME =
      ENTITIES.register(
          "guardian_prime",
          () ->
              EntityType.Builder.of(GuardianPrimeEntity::new, MobCategory.MONSTER)
                  .sized(3.0f, 5.5f)
                  .clientTrackingRange(12)
                  .build("guardian_prime"));

  // Torus World Boss
  public static final RegistryObject<EntityType<TorusWardenEntity>> TORUS_WARDEN =
      ENTITIES.register(
          "torus_warden",
          () ->
              EntityType.Builder.of(TorusWardenEntity::new, MobCategory.MONSTER)
                  .sized(3.2f, 5.8f)
                  .clientTrackingRange(12)
                  .build("torus_warden"));

  // Exotica Bosses
  public static final RegistryObject<EntityType<RealityBreakerEntity>> REALITY_BREAKER =
      ENTITIES.register(
          "reality_breaker",
          () ->
              EntityType.Builder.of(RealityBreakerEntity::new, MobCategory.MONSTER)
                  .sized(3.0f, 5.0f)
                  .clientTrackingRange(12)
                  .build("reality_breaker"));

  // Hollow World Boss
  public static final RegistryObject<EntityType<HollowTyrantEntity>> HOLLOW_TYRANT =
      ENTITIES.register(
          "hollow_tyrant",
          () ->
              EntityType.Builder.of(HollowTyrantEntity::new, MobCategory.MONSTER)
                  .sized(3.2f, 5.2f)
                  .clientTrackingRange(12)
                  .build("hollow_tyrant"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> PRISM_COLOSSUS =
      ENTITIES.register(
          "prism_colossus",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.8f, 3.2f)
                  .clientTrackingRange(12)
                  .build("prism_colossus"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> DUNE_SIREN =
      ENTITIES.register(
          "dune_siren",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 2.2f)
                  .clientTrackingRange(12)
                  .build("dune_siren"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> QUANTUM_BEAST =
      ENTITIES.register(
          "quantum_beast",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.4f, 2.4f)
                  .clientTrackingRange(12)
                  .build("quantum_beast"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> FRACTAL_HORROR =
      ENTITIES.register(
          "fractal_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.0f, 2.6f)
                  .clientTrackingRange(12)
                  .build("fractal_horror"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> PRISM_SERAPH =
      ENTITIES.register(
          "prism_seraph",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 2.4f)
                  .clientTrackingRange(12)
                  .build("prism_seraph"));

  // Stormworld Entities (placeholder implementations using SimpleFlyingMob)
  public static final RegistryObject<EntityType<SimpleFlyingMob>> SKY_WHALE =
      ENTITIES.register(
          "sky_whale",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.CREATURE)
                  .sized(3.0f, 2.0f)
                  .clientTrackingRange(10)
                  .build("sky_whale"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> CLOUD_RAY =
      ENTITIES.register(
          "cloud_ray",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.CREATURE)
                  .sized(2.0f, 0.6f)
                  .clientTrackingRange(10)
                  .build("cloud_ray"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> STORM_DRAKE =
      ENTITIES.register(
          "storm_drake",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.6f)
                  .clientTrackingRange(10)
                  .build("storm_drake"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> GUST_WRAITH =
      ENTITIES.register(
          "gust_wraith",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(0.8f, 2.0f)
                  .clientTrackingRange(10)
                  .build("gust_wraith"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> FULMINATOR =
      ENTITIES.register(
          "fulminator",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 1.0f)
                  .clientTrackingRange(10)
                  .build("fulminator"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> SPARK_BEETLE =
      ENTITIES.register(
          "spark_beetle",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.CREATURE)
                  .sized(0.6f, 0.4f)
                  .clientTrackingRange(8)
                  .build("spark_beetle"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> EYE_SERPENT =
      ENTITIES.register(
          "eye_serpent",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.2f, 1.2f)
                  .clientTrackingRange(10)
                  .build("eye_serpent"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> CALM_SPIRIT =
      ENTITIES.register(
          "calm_spirit",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.AMBIENT)
                  .sized(0.6f, 1.0f)
                  .clientTrackingRange(8)
                  .build("calm_spirit"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> HYDROGEN_BEAST =
      ENTITIES.register(
          "hydrogen_beast",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(2.0f, 2.0f)
                  .clientTrackingRange(10)
                  .build("hydrogen_beast"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> PRESSURE_HORROR =
      ENTITIES.register(
          "pressure_horror",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.4f, 2.0f)
                  .clientTrackingRange(10)
                  .build("pressure_horror"));

  // Mini-boss placeholders
  public static final RegistryObject<EntityType<SimpleFlyingMob>> AERIAL_BEHEMOTH =
      ENTITIES.register(
          "aerial_behemoth",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 3.0f)
                  .clientTrackingRange(12)
                  .build("aerial_behemoth"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> TEMPEST_SERPENT =
      ENTITIES.register(
          "tempest_serpent",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(2.5f, 2.5f)
                  .clientTrackingRange(12)
                  .build("tempest_serpent"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> STORM_TITAN =
      ENTITIES.register(
          "storm_titan",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 4.0f)
                  .clientTrackingRange(12)
                  .build("storm_titan"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> CYCLONE_GUARDIAN =
      ENTITIES.register(
          "cyclone_guardian",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(2.5f, 3.0f)
                  .clientTrackingRange(12)
                  .build("cyclone_guardian"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> DEPTH_LEVIATHAN =
      ENTITIES.register(
          "depth_leviathan",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(3.5f, 3.0f)
                  .clientTrackingRange(12)
                  .build("depth_leviathan"));

  // Ringworld Entities (placeholder implementations using SimpleWalkingMob)
  public static final RegistryObject<EntityType<SimpleWalkingMob>> GLACIAL_GRAZER =
      ENTITIES.register(
          "glacial_grazer",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.6f, 1.6f)
                  .clientTrackingRange(10)
                  .build("glacial_grazer"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> POLAR_DRAKE =
      ENTITIES.register(
          "polar_drake",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 2.0f)
                  .clientTrackingRange(10)
                  .build("polar_drake"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SAND_STRIDER =
      ENTITIES.register(
          "sand_strider",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.4f, 1.2f)
                  .clientTrackingRange(10)
                  .build("sand_strider"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> DUST_PHANTOM =
      ENTITIES.register(
          "dust_phantom",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 2.0f)
                  .clientTrackingRange(10)
                  .build("dust_phantom"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> FLOAT_BEAST =
      ENTITIES.register(
          "float_beast",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(2.0f, 1.8f)
                  .clientTrackingRange(10)
                  .build("float_beast"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SKY_FLITTER =
      ENTITIES.register(
          "sky_flitter",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(0.8f, 0.8f)
                  .clientTrackingRange(8)
                  .build("sky_flitter"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SHADOW_HUNTER =
      ENTITIES.register(
          "shadow_hunter",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.4f, 1.6f)
                  .clientTrackingRange(10)
                  .build("shadow_hunter"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> FUNGAL_SHAMBLER =
      ENTITIES.register(
          "fungal_shambler",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.2f, 1.8f)
                  .clientTrackingRange(10)
                  .build("fungal_shambler"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SCAVENGER =
      ENTITIES.register(
          "scavenger",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.0f, 1.4f)
                  .clientTrackingRange(10)
                  .build("scavenger"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> HOVER_BEETLE =
      ENTITIES.register(
          "hover_beetle",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(0.9f, 0.6f)
                  .clientTrackingRange(8)
                  .build("hover_beetle"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SECURITY_DRONE =
      ENTITIES.register(
          "security_drone",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 1.0f)
                  .clientTrackingRange(10)
                  .build("security_drone"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> ALLOY_GUARDIAN =
      ENTITIES.register(
          "alloy_guardian",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 2.6f)
                  .clientTrackingRange(12)
                  .build("alloy_guardian"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> LIGHT_MOTH =
      ENTITIES.register(
          "light_moth",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.AMBIENT)
                  .sized(0.6f, 0.6f)
                  .clientTrackingRange(8)
                  .build("light_moth"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SOLAR_DRONE =
      ENTITIES.register(
          "solar_drone",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.0f, 1.0f)
                  .clientTrackingRange(10)
                  .build("solar_drone"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> WORKER_DRONE =
      ENTITIES.register(
          "worker_drone",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(0.9f, 0.9f)
                  .clientTrackingRange(10)
                  .build("worker_drone"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> REPAIR_SPIDER =
      ENTITIES.register(
          "repair_spider",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(0.9f, 0.5f)
                  .clientTrackingRange(8)
                  .build("repair_spider"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> ALLOY_SERPENT =
      ENTITIES.register(
          "alloy_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.4f)
                  .clientTrackingRange(10)
                  .build("alloy_serpent"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> EDGE_BEAST =
      ENTITIES.register(
          "edge_beast",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.4f, 2.0f)
                  .clientTrackingRange(12)
                  .build("edge_beast"));

  // Mini-boss placeholders
  public static final RegistryObject<EntityType<SimpleWalkingMob>> ICE_WARDEN =
      ENTITIES.register(
          "ice_warden",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.2f, 3.0f)
                  .clientTrackingRange(12)
                  .build("ice_warden"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> DUST_COLOSSUS =
      ENTITIES.register(
          "dust_colossus",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.6f, 3.2f)
                  .clientTrackingRange(12)
                  .build("dust_colossus"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> MEADOW_LEVIATHAN =
      ENTITIES.register(
          "meadow_leviathan",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 2.6f)
                  .clientTrackingRange(12)
                  .build("meadow_leviathan"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SHADOW_REVENANT =
      ENTITIES.register(
          "shadow_revenant",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.0f, 2.4f)
                  .clientTrackingRange(12)
                  .build("shadow_revenant"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> ARCOLOGY_WARLORD =
      ENTITIES.register(
          "arcology_warlord",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.4f, 2.8f)
                  .clientTrackingRange(12)
                  .build("arcology_warlord"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> NEON_SENTINEL =
      ENTITIES.register(
          "neon_sentinel",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.2f, 2.8f)
                  .clientTrackingRange(12)
                  .build("neon_sentinel"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> HELIOLITH =
      ENTITIES.register(
          "heliolith",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.0f, 2.6f)
                  .clientTrackingRange(12)
                  .build("heliolith"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TUNNEL_OVERSEER =
      ENTITIES.register(
          "tunnel_overseer",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.0f, 2.4f)
                  .clientTrackingRange(12)
                  .build("tunnel_overseer"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TRENCH_HORROR =
      ENTITIES.register(
          "trench_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.2f, 2.8f)
                  .clientTrackingRange(12)
                  .build("trench_horror"));

  // Exotica Fauna (placeholder implementations)
  public static final RegistryObject<EntityType<SimpleWalkingMob>> CHROMA_GRAZER =
      ENTITIES.register(
          "chroma_grazer",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.4f, 1.6f)
                  .clientTrackingRange(10)
                  .build("chroma_grazer"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SPECTRAL_HARE =
      ENTITIES.register(
          "spectral_hare",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(0.6f, 0.6f)
                  .clientTrackingRange(8)
                  .build("spectral_hare"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TONE_BEAST =
      ENTITIES.register(
          "tone_beast",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 2.0f)
                  .clientTrackingRange(10)
                  .build("tone_beast"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> SONIC_DRIFTER =
      ENTITIES.register(
          "sonic_drifter",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.AMBIENT)
                  .sized(0.8f, 0.8f)
                  .clientTrackingRange(10)
                  .build("sonic_drifter"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> PHASE_STALKER =
      ENTITIES.register(
          "phase_stalker",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.2f, 1.6f)
                  .clientTrackingRange(10)
                  .build("phase_stalker"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> ECHO_WISP =
      ENTITIES.register(
          "echo_wisp",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.AMBIENT)
                  .sized(0.5f, 0.5f)
                  .clientTrackingRange(8)
                  .build("echo_wisp"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> FRACTAL_CRAWLER =
      ENTITIES.register(
          "fractal_crawler",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 0.8f)
                  .clientTrackingRange(10)
                  .build("fractal_crawler"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> PATTERN_SERPENT =
      ENTITIES.register(
          "pattern_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.2f)
                  .clientTrackingRange(10)
                  .build("pattern_serpent"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> PRISM_SERPENT =
      ENTITIES.register(
          "prism_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.2f)
                  .clientTrackingRange(10)
                  .build("prism_serpent"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> RAINBOW_MANTIS =
      ENTITIES.register(
          "rainbow_mantis",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.4f, 1.8f)
                  .clientTrackingRange(10)
                  .build("rainbow_mantis"));

  // Torus World Entities (placeholder implementations)
  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_RIM_GRAZER =
      ENTITIES.register(
          "torus_rim_grazer",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.4f, 1.6f)
                  .clientTrackingRange(10)
                  .build("torus_rim_grazer"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> TORUS_LIGHT_OWL =
      ENTITIES.register(
          "torus_light_owl",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.AMBIENT)
                  .sized(0.8f, 0.8f)
                  .clientTrackingRange(10)
                  .build("torus_light_owl"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_SAND_STRIDER =
      ENTITIES.register(
          "torus_sand_strider",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.4f, 1.2f)
                  .clientTrackingRange(10)
                  .build("torus_sand_strider"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_SOLAR_STALKER =
      ENTITIES.register(
          "torus_solar_stalker",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.8f)
                  .clientTrackingRange(12)
                  .build("torus_solar_stalker"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_MAINTENANCE_DRONE =
      ENTITIES.register(
          "torus_maintenance_drone",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.CREATURE)
                  .sized(1.0f, 1.0f)
                  .clientTrackingRange(10)
                  .build("torus_maintenance_drone"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_ALLOY_SERPENT =
      ENTITIES.register(
          "torus_alloy_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.4f)
                  .clientTrackingRange(12)
                  .build("torus_alloy_serpent"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_RADIANT_BEAST =
      ENTITIES.register(
          "torus_radiant_beast",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.0f, 2.2f)
                  .clientTrackingRange(12)
                  .build("torus_radiant_beast"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> TORUS_PULSE_WRAITH =
      ENTITIES.register(
          "torus_pulse_wraith",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 1.4f)
                  .clientTrackingRange(12)
                  .build("torus_pulse_wraith"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_VOID_EEL =
      ENTITIES.register(
          "torus_void_eel",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 1.0f)
                  .clientTrackingRange(12)
                  .build("torus_void_eel"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> TORUS_NULL_PHANTOM =
      ENTITIES.register(
          "torus_null_phantom",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.2f, 1.2f)
                  .clientTrackingRange(12)
                  .build("torus_null_phantom"));

  // Torus World Mini-boss placeholders
  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_FOREST_GUARDIAN =
      ENTITIES.register(
          "torus_forest_guardian",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.4f, 3.0f)
                  .clientTrackingRange(12)
                  .build("torus_forest_guardian"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_DESERT_COLOSSUS =
      ENTITIES.register(
          "torus_desert_colossus",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 3.4f)
                  .clientTrackingRange(12)
                  .build("torus_desert_colossus"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_SPINE_OVERSEER =
      ENTITIES.register(
          "torus_spine_overseer",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.2f, 2.8f)
                  .clientTrackingRange(12)
                  .build("torus_spine_overseer"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_LUMINAL_TITAN =
      ENTITIES.register(
          "torus_luminal_titan",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.2f, 3.6f)
                  .clientTrackingRange(12)
                  .build("torus_luminal_titan"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> TORUS_EXOTIC_HORROR =
      ENTITIES.register(
          "torus_exotic_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 3.2f)
                  .clientTrackingRange(12)
                  .build("torus_exotic_horror"));

  // Hollow World Entities (placeholder implementations)
  public static final RegistryObject<EntityType<SimpleWalkingMob>> SPORE_BEAST =
      ENTITIES.register(
          "spore_beast",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.8f)
                  .clientTrackingRange(10)
                  .build("spore_beast"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> LUME_BAT =
      ENTITIES.register(
          "lume_bat",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.AMBIENT)
                  .sized(0.6f, 0.6f)
                  .clientTrackingRange(8)
                  .build("lume_bat"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> VOID_PHANTOM =
      ENTITIES.register(
          "void_phantom",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.2f, 1.2f)
                  .clientTrackingRange(12)
                  .build("void_phantom"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> CHASM_SERPENT =
      ENTITIES.register(
          "chasm_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 1.2f)
                  .clientTrackingRange(12)
                  .build("chasm_serpent"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> PRISM_STALKER =
      ENTITIES.register(
          "prism_stalker",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.4f, 1.6f)
                  .clientTrackingRange(12)
                  .build("prism_stalker"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> ROOT_SPIDER =
      ENTITIES.register(
          "root_spider",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.2f, 0.8f)
                  .clientTrackingRange(12)
                  .build("root_spider"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> HOLLOW_STALKER =
      ENTITIES.register(
          "hollow_stalker",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.8f)
                  .clientTrackingRange(12)
                  .build("hollow_stalker"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> RIVER_SERPENT =
      ENTITIES.register(
          "river_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 1.0f)
                  .clientTrackingRange(12)
                  .build("river_serpent"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> LUME_FISH =
      ENTITIES.register(
          "lume_fish",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.AMBIENT)
                  .sized(0.7f, 0.5f)
                  .clientTrackingRange(8)
                  .build("lume_fish"));

  // Hollow World Mini-boss placeholders
  public static final RegistryObject<EntityType<SimpleWalkingMob>> MYCELIUM_HORROR =
      ENTITIES.register(
          "mycelium_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.6f, 2.8f)
                  .clientTrackingRange(12)
                  .build("mycelium_horror"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> ABYSS_WYRM =
      ENTITIES.register(
          "abyss_wyrm",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 2.0f)
                  .clientTrackingRange(12)
                  .build("abyss_wyrm"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> CRYSTAL_TITAN =
      ENTITIES.register(
          "crystal_titan",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 3.4f)
                  .clientTrackingRange(12)
                  .build("crystal_titan"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> STALACTITE_HORROR =
      ENTITIES.register(
          "stalactite_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.4f, 3.0f)
                  .clientTrackingRange(12)
                  .build("stalactite_horror"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> RIVER_LEVIATHAN =
      ENTITIES.register(
          "river_leviathan",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.2f, 2.2f)
                  .clientTrackingRange(12)
                  .build("river_leviathan"));

  // Dyson Swarm Entities (placeholder implementations)
  public static final RegistryObject<EntityType<SimpleFlyingMob>> SOLAR_MOTH =
      ENTITIES.register(
          "solar_moth",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.AMBIENT)
                  .sized(0.8f, 0.6f)
                  .clientTrackingRange(10)
                  .build("solar_moth"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> PANEL_CRAWLER =
      ENTITIES.register(
          "panel_crawler",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 0.8f)
                  .clientTrackingRange(10)
                  .build("panel_crawler"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> NODE_WRAITH =
      ENTITIES.register(
          "node_wraith",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 1.0f)
                  .clientTrackingRange(12)
                  .build("node_wraith"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> REPAIR_DRONE =
      ENTITIES.register(
          "repair_drone",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.CREATURE)
                  .sized(0.9f, 0.9f)
                  .clientTrackingRange(10)
                  .build("repair_drone"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SCAFFOLD_SERPENT =
      ENTITIES.register(
          "scaffold_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 1.2f)
                  .clientTrackingRange(12)
                  .build("scaffold_serpent"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> ORB_DRONE =
      ENTITIES.register(
          "orb_drone",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.CREATURE)
                  .sized(0.8f, 0.8f)
                  .clientTrackingRange(10)
                  .build("orb_drone"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> RADIATION_WRAITH =
      ENTITIES.register(
          "radiation_wraith",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.1f, 1.1f)
                  .clientTrackingRange(12)
                  .build("radiation_wraith"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SHADOW_HOUND =
      ENTITIES.register(
          "shadow_hound",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.4f, 1.2f)
                  .clientTrackingRange(12)
                  .build("shadow_hound"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> SIGNAL_PHANTOM =
      ENTITIES.register(
          "signal_phantom",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.0f, 1.0f)
                  .clientTrackingRange(12)
                  .build("signal_phantom"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> RELAY_CONSTRUCT =
      ENTITIES.register(
          "relay_construct",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.8f)
                  .clientTrackingRange(12)
                  .build("relay_construct"));

  // Dyson Swarm Mini-bosses
  public static final RegistryObject<EntityType<SimpleWalkingMob>> SOLAR_WARDEN =
      ENTITIES.register(
          "solar_warden",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.6f, 3.0f)
                  .clientTrackingRange(12)
                  .build("solar_warden"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> NODE_HORROR =
      ENTITIES.register(
          "node_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.4f, 2.8f)
                  .clientTrackingRange(12)
                  .build("node_horror"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SCAFFOLD_TITAN =
      ENTITIES.register(
          "scaffold_titan",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 3.2f)
                  .clientTrackingRange(12)
                  .build("scaffold_titan"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> RADIANT_ABOMINATION =
      ENTITIES.register(
          "radiant_abomination",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.6f, 2.6f)
                  .clientTrackingRange(12)
                  .build("radiant_abomination"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SIGNAL_OVERLORD =
      ENTITIES.register(
          "signal_overlord",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.8f, 3.0f)
                  .clientTrackingRange(12)
                  .build("signal_overlord"));

  // Dyson Swarm Boss
  public static final RegistryObject<EntityType<DysonApexEntity>> DYSON_APEX =
      ENTITIES.register(
          "dyson_apex",
          () ->
              EntityType.Builder.of(DysonApexEntity::new, MobCategory.MONSTER)
                  .sized(3.4f, 5.6f)
                  .clientTrackingRange(12)
                  .build("dyson_apex"));

  // Neutron Star Forge Entities (placeholder implementations)
  public static final RegistryObject<EntityType<SimpleFlyingMob>> PLASMA_EEL =
      ENTITIES.register(
          "plasma_eel",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 0.8f)
                  .clientTrackingRange(12)
                  .build("plasma_eel"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> ACCRETION_BEAST =
      ENTITIES.register(
          "accretion_beast",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 1.6f)
                  .clientTrackingRange(12)
                  .build("accretion_beast"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> MAGNETAR_SERPENT =
      ENTITIES.register(
          "magnetar_serpent",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 1.2f)
                  .clientTrackingRange(12)
                  .build("magnetar_serpent"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> STORM_CONSTRUCT =
      ENTITIES.register(
          "storm_construct",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.2f, 1.2f)
                  .clientTrackingRange(12)
                  .build("storm_construct"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> FORGE_DRONE =
      ENTITIES.register(
          "forge_drone",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.CREATURE)
                  .sized(0.9f, 0.9f)
                  .clientTrackingRange(10)
                  .build("forge_drone"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SMELTER_HORROR =
      ENTITIES.register(
          "smelter_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.6f, 1.8f)
                  .clientTrackingRange(12)
                  .build("smelter_horror"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> GRAVITY_HORROR =
      ENTITIES.register(
          "gravity_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 2.2f)
                  .clientTrackingRange(12)
                  .build("gravity_horror"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> VOID_LEECH =
      ENTITIES.register(
          "void_leech",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(0.9f, 0.6f)
                  .clientTrackingRange(12)
                  .build("void_leech"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SHELTER_GUARDIAN =
      ENTITIES.register(
          "shelter_guardian",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(1.8f, 2.4f)
                  .clientTrackingRange(12)
                  .build("shelter_guardian"));

  public static final RegistryObject<EntityType<SimpleFlyingMob>> RADIANT_WRAITH =
      ENTITIES.register(
          "radiant_wraith",
          () ->
              EntityType.Builder.of(SimpleFlyingMob::new, MobCategory.MONSTER)
                  .sized(1.1f, 1.1f)
                  .clientTrackingRange(12)
                  .build("radiant_wraith"));

  // Neutron Star Forge Mini-bosses
  public static final RegistryObject<EntityType<SimpleWalkingMob>> ACCRETION_LEVIATHAN =
      ENTITIES.register(
          "accretion_leviathan",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.2f, 2.6f)
                  .clientTrackingRange(12)
                  .build("accretion_leviathan"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> MAGNETAR_COLOSSUS =
      ENTITIES.register(
          "magnetar_colossus",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 3.4f)
                  .clientTrackingRange(12)
                  .build("magnetar_colossus"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> FORGE_OVERSEER =
      ENTITIES.register(
          "forge_overseer",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.6f, 2.8f)
                  .clientTrackingRange(12)
                  .build("forge_overseer"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> GRAVITON_HORROR =
      ENTITIES.register(
          "graviton_horror",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(3.0f, 3.2f)
                  .clientTrackingRange(12)
                  .build("graviton_horror"));

  public static final RegistryObject<EntityType<SimpleWalkingMob>> SHELTER_SENTINEL =
      ENTITIES.register(
          "shelter_sentinel",
          () ->
              EntityType.Builder.of(SimpleWalkingMob::new, MobCategory.MONSTER)
                  .sized(2.4f, 3.0f)
                  .clientTrackingRange(12)
                  .build("shelter_sentinel"));

  // Neutron Forge Boss
  public static final RegistryObject<EntityType<ForgeStarSovereignEntity>> FORGE_STAR_SOVEREIGN =
      ENTITIES.register(
          "forge_star_sovereign",
          () ->
              EntityType.Builder.of(ForgeStarSovereignEntity::new, MobCategory.MONSTER)
                  .sized(3.6f, 5.8f)
                  .clientTrackingRange(12)
                  .build("forge_star_sovereign"));

  public static void register(IEventBus eventBus) {
    ENTITIES.register(eventBus);
  }
}
