package com.netroaki.chex.entity.boss;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.entity.boss.verdant_colossus.VerdantColossusEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BossEntities {
  public static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CosmicHorizonsExpanded.MOD_ID);

  // Verdant Colossus - Kepler-452b Boss
  public static final RegistryObject<EntityType<VerdantColossusEntity>> VERDANT_COLOSSUS =
      register(
          "verdant_colossus",
          EntityType.Builder.of(VerdantColossusEntity::new, MobCategory.MONSTER)
              .sized(2.5f, 6.0f)
              .clientTrackingRange(10)
              .fireImmune());

  private static <T extends net.minecraft.world.entity.Entity>
      RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
    return ENTITIES.register(name, () -> builder.build(CosmicHorizonsExpanded.MOD_ID + ":" + name));
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(VERDANT_COLOSSUS.get(), VerdantColossusEntity.createAttributes().build());
  }
}
