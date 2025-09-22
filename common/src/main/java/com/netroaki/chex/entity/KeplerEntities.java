package com.netroaki.chex.entity;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.kepler.MeadowFlutterwingEntity;
import com.netroaki.chex.entity.kepler.RiverGrazerEntity;
import com.netroaki.chex.entity.kepler.ScrubStalkerEntity;
import com.netroaki.chex.entity.kepler.VerdantColossusEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeplerEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CHEX.MOD_ID);

    // Entity Registrations
    public static final RegistryObject<EntityType<RiverGrazerEntity>> RIVER_GRAZER = ENTITIES.register("river_grazer",
        () -> EntityType.Builder.of(RiverGrazerEntity::new, MobCategory.CREATURE)
            .sized(1.4f, 1.6f)
            .clientTrackingRange(10)
            .build("river_grazer"));

    public static final RegistryObject<EntityType<MeadowFlutterwingEntity>> MEADOW_FLUTTERWING = ENTITIES.register("meadow_flutterwing",
        () -> EntityType.Builder.of(MeadowFlutterwingEntity::new, MobCategory.AMBIENT)
            .sized(0.6f, 0.6f)
            .clientTrackingRange(8)
            .build("meadow_flutterwing"));

    public static final RegistryObject<EntityType<ScrubStalkerEntity>> SCRUB_STALKER = ENTITIES.register("scrub_stalker",
        () -> EntityType.Builder.of(ScrubStalkerEntity::new, MobCategory.MONSTER)
            .sized(1.2f, 1.5f)
            .clientTrackingRange(10)
            .build("scrub_stalker"));
            
    public static final RegistryObject<EntityType<VerdantColossusEntity>> VERDANT_COLOSSUS = ENTITIES.register("verdant_colossus",
        () -> EntityType.Builder.of(VerdantColossusEntity::new, MobCategory.MONSTER)
            .sized(2.5f, 5.0f)
            .clientTrackingRange(12)
            .fireImmune()
            .build("verdant_colossus"));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(RIVER_GRAZER.get(), RiverGrazerEntity.createAttributes().build());
        event.put(MEADOW_FLUTTERWING.get(), MeadowFlutterwingEntity.createAttributes().build());
        event.put(SCRUB_STALKER.get(), ScrubStalkerEntity.createAttributes().build());
        event.put(VERDANT_COLOSSUS.get(), VerdantColossusEntity.createAttributes().build());
    }
}
