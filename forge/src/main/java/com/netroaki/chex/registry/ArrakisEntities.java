package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.FremenWarrior;
import com.netroaki.chex.entity.FremenVillager;
import com.netroaki.chex.entity.Sandworm;
import com.netroaki.chex.entity.ShaiHuludBoss;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
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
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CHEX.MOD_ID);

    // Entity Type Registrations
    public static final RegistryObject<EntityType<Sandworm>> SANDWORM = ENTITY_TYPES.register("sandworm",
        () -> EntityType.Builder.of(Sandworm::new, MobCategory.MONSTER)
            .sized(2.5F, 1.5F)
            .clientTrackingRange(8)
            .build("sandworm")
    );

    public static final RegistryObject<EntityType<FremenWarrior>> FREMEN_WARRIOR = ENTITY_TYPES.register("fremen_warrior",
        () -> EntityType.Builder.of(FremenWarrior::new, MobCategory.CREATURE)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(10)
            .build("fremen_warrior")
    );
    
    public static final RegistryObject<EntityType<ShaiHuludBoss>> SHAI_HULUD = ENTITY_TYPES.register("shai_hulud",
        () -> EntityType.Builder.of(ShaiHuludBoss::new, MobCategory.MONSTER)
            .sized(3.0F, 2.5F)
            .clientTrackingRange(12)
            .build("shai_hulud")
    );
    
    public static final RegistryObject<EntityType<FremenVillager>> FREMEN_VILLAGER = ENTITY_TYPES.register("fremen_villager",
        () -> EntityType.Builder.of(FremenVillager::new, MobCategory.MISC)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(10)
            .build("fremen_villager")
    );

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SANDWORM.get(), Sandworm.createAttributes().build());
        event.put(FREMEN_WARRIOR.get(), FremenWarrior.createAttributes().build());
        event.put(SHAI_HULUD.get(), ShaiHuludBoss.createAttributes().build());
        event.put(FREMEN_VILLAGER.get(), FremenVillager.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        // Sandworm spawns on surface in desert biomes
        event.register(
            SANDWORM.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> 
                world.getBlockState(pos.below()).is(BlockTags.SAND) && 
                world.canSeeSky(pos) &&
                pos.getY() > 60,
            SpawnPlacementRegisterEvent.Operation.AND
        );

        // Fremen Warrior spawns in caves and on surface in desert biomes
        event.register(
            FREMEN_WARRIOR.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> 
                world.getBlockState(pos.below()).is(BlockTags.SAND) &&
                (world.canSeeSky(pos) || pos.getY() < 60),
            SpawnPlacementRegisterEvent.Operation.AND
        );
        
        // Shai-Hulud spawns only through structure generation
        event.register(
            SHAI_HULUD.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> false, // Disable natural spawning
            SpawnPlacementRegisterEvent.Operation.AND
        );
        
        // Fremen Villager spawns only through structure generation
        event.register(
            FREMEN_VILLAGER.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (entityType, world, reason, pos, random) -> false, // Disable natural spawning
            SpawnPlacementRegisterEvent.Operation.AND
        );
    }
}
