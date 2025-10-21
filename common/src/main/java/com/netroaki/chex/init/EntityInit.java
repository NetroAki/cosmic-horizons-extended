package com.netroaki.chex.init;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {
  public static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CosmicHorizonsExpanded.MOD_ID);

  // TODO: Implement entity registrations when entity classes are available
  // // Arrakis Fauna
  // public static final RegistryObject<EntityType<SandwormJuvenileEntity>> SANDWORM_JUVENILE =
  //     ENTITIES.register(
  //         "sandworm_juvenile",
  //         () ->
  //             EntityType.Builder.of(SandwormJuvenileEntity::new, MobCategory.MONSTER)
  //                 .sized(1.5f, 0.8f)
  //                 .clientTrackingRange(8)
  //                 .build("sandworm_juvenile"));

  // public static final RegistryObject<EntityType<StormHawkEntity>> STORM_HAWK =
  //     ENTITIES.register(
  //         "storm_hawk",
  //         () ->
  //             EntityType.Builder.of(StormHawkEntity::new, MobCategory.MONSTER)
  //                 .sized(0.9f, 0.8f)
  //                 .clientTrackingRange(10)
  //                 .build("storm_hawk"));

  // public static final RegistryObject<EntityType<SpiceGathererEntity>> SPICE_GATHERER =
  //     ENTITIES.register(
  //         "spice_gatherer",
  //         () ->
  //             EntityType.Builder.of(SpiceGathererEntity::new, MobCategory.CREATURE)
  //                 .sized(0.6f, 1.8f)
  //                 .clientTrackingRange(10)
  //                 .build("spice_gatherer"));

  // // Arrakis Boss
  // public static final RegistryObject<EntityType<SandEmperorEntity>> SAND_EMPEROR =
  //     ENTITIES.register(
  //         "sand_emperor",
  //         () ->
  //             EntityType.Builder.of(SandEmperorEntity::new, MobCategory.MONSTER)
  //                 .sized(2.5f, 5.0f)
  //                 .clientTrackingRange(10)
  //                 .fireImmune()
  //                 .build("sand_emperor"));

  public static void register(IEventBus eventBus) {
    ENTITIES.register(eventBus);
  }
}
