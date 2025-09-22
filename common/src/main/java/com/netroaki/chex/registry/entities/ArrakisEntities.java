package com.netroaki.chex.registry.entities;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.arrakis.JuvenileSandwormEntity;
import com.netroaki.chex.entity.arrakis.SpiceGathererEntity;
import com.netroaki.chex.entity.arrakis.StormHawkEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArrakisEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CHEX.MOD_ID);

    // Entity Registrations
    public static final RegistryObject<EntityType<SpiceGathererEntity>> SPICE_GATHERER = ENTITIES.register("spice_gatherer",
        () -> EntityType.Builder.of(SpiceGathererEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(10)
            .build("spice_gatherer"));

    public static final RegistryObject<EntityType<JuvenileSandwormEntity>> JUVENILE_SANDWORM = ENTITIES.register("juvenile_sandworm",
        () -> EntityType.Builder.of(JuvenileSandwormEntity::new, MobCategory.MONSTER)
            .sized(1.4F, 0.7F)
            .clientTrackingRange(8)
            .build("juvenile_sandworm"));

    public static final RegistryObject<EntityType<StormHawkEntity>> STORM_HAWK = ENTITIES.register("storm_hawk",
        () -> EntityType.Builder.of(StormHawkEntity::new, MobCategory.CREATURE)
            .sized(0.9F, 0.95F)
            .clientTrackingRange(10)
            .build("storm_hawk"));

    // Register entity attributes
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SPICE_GATHERER.get(), SpiceGathererEntity.createAttributes().build());
        event.put(JUVENILE_SANDWORM.get(), JuvenileSandwormEntity.createAttributes().build());
        event.put(STORM_HAWK.get(), StormHawkEntity.createAttributes().build());
    }

    // Register spawn placements
    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
            SPICE_GATHERER.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, level, spawnType, pos, random) -> 
                level.getBlockState(pos.below()).isSolid() && 
                level.getRawBrightness(pos, 0) > 8,
            SpawnPlacementRegisterEvent.Operation.OR
        );

        event.register(
            JUVENILE_SANDWORM.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            JuvenileSandwormEntity::checkSandwormSpawnRules,
            SpawnPlacementRegisterEvent.Operation.OR
        );

        event.register(
            STORM_HAWK.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING,
            StormHawkEntity::checkStormHawkSpawnRules,
            SpawnPlacementRegisterEvent.Operation.OR
        );
    }
}
