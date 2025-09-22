package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.Sandworm;
import com.netroaki.chex.entity.FremenVillager;
import com.netroaki.chex.entity.StormHawk;
import com.netroaki.chex.entity.ShaiHuludBoss;
import com.netroaki.chex.entity.alpha_centauri.SolarEngineerDrone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CHEX.MOD_ID);

    // Sandworm (Juvenile)
    public static final RegistryObject<EntityType<Sandworm>> SANDWORM = ENTITIES.register("sandworm",
        () -> EntityType.Builder.of(Sandworm::new, MobCategory.CREATURE)
            .sized(1.5f, 0.9f)
            .clientTrackingRange(8)
            .build(new ResourceLocation(CHEX.MOD_ID, "sandworm").toString()));

    // Fremen Villager (Spice Gatherer)
    public static final RegistryObject<EntityType<FremenVillager>> FREMEN_VILLAGER = ENTITIES.register("fremen_villager",
        () -> EntityType.Builder.of(FremenVillager::new, MobCategory.CREATURE)
            .sized(0.6f, 1.95f)
            .clientTrackingRange(10)
            .build(new ResourceLocation(CHEX.MOD_ID, "fremen_villager").toString()));

    // Storm Hawk
    public static final RegistryObject<EntityType<StormHawk>> STORM_HAWK = ENTITIES.register("storm_hawk",
        () -> EntityType.Builder.of(StormHawk::new, MobCategory.AMBIENT)
            .sized(0.9f, 0.8f)
            .clientTrackingRange(10)
            .build(new ResourceLocation(CHEX.MOD_ID, "storm_hawk").toString()));
            
    // Boss Entities
    public static final RegistryObject<EntityType<ShaiHuludBoss>> SHAI_HULUD = ENTITIES.register("shai_hulud",
        () -> EntityType.Builder.of(ShaiHuludBoss::new, MobCategory.MONSTER)
            .sized(3.0f, 6.0f)
            .clientTrackingRange(12)
            .fireImmune()
            .build(new ResourceLocation(CHEX.MOD_ID, "shai_hulud").toString()));
            
    // Alpha Centauri Entities
    public static final RegistryObject<EntityType<SolarEngineerDrone>> SOLAR_ENGINEER_DRONE = ENTITIES.register("solar_engineer_drone",
        () -> EntityType.Builder.of(SolarEngineerDrone::new, MobCategory.MONSTER)
            .sized(0.8f, 0.6f)
            .clientTrackingRange(8)
            .fireImmune()
            .build(new ResourceLocation(CHEX.MOD_ID, "solar_engineer_drone").toString()));

    // Verdant Colossus
    public static final RegistryObject<EntityType<VerdantColossus>> VERDANT_COLOSSUS = ENTITIES.register("verdant_colossus",
        () -> EntityType.Builder.of(VerdantColossus::new, MobCategory.MONSTER)
            .sized(2.8f, 5.8f)  // Large size for a boss
            .clientTrackingRange(12)
            .fireImmune()
            .updateInterval(2)
            .build(new ResourceLocation(CHEX.MOD_ID, "verdant_colossus").toString()));
}
