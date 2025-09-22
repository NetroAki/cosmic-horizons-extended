package com.netroaki.chex.entity;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.aqua.LuminfishEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CHEX.MOD_ID);

    // Aqua Mundus Entities
    public static final RegistryObject<EntityType<LuminfishEntity>> LUMINFISH = ENTITIES.register("luminfish",
        () -> EntityType.Builder.of(LuminfishEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.5F, 0.3F)
            .clientTrackingRange(4)
            .build("luminfish")
    );

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(LUMINFISH.get(), LuminfishEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
            LUMINFISH.get(),
            SpawnPlacements.Type.IN_WATER,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            LuminfishEntity::checkLuminfishSpawnRules,
            SpawnPlacementRegisterEvent.Operation.OR
        );
    }
}
