package com.netroaki.chex.world.eden;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.eden.entity.LumiflyEntity;
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
public class EdenEntities {
  public static final DeferredRegister<EntityType<?>> ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CHEX.MOD_ID);

  // Lumifly - A peaceful, glowing flying creature
  public static final RegistryObject<EntityType<LumiflyEntity>> LUMIFLY =
      ENTITIES.register(
          "lumifly",
          () ->
              EntityType.Builder.of(LumiflyEntity::new, MobCategory.AMBIENT)
                  .sized(0.7f, 0.6f)
                  .clientTrackingRange(8)
                  .build("lumifly"));

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(LUMIFLY.get(), LumiflyEntity.createAttributes().build());
  }

  @SubscribeEvent
  public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
    event.register(
        LUMIFLY.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LumiflyEntity::checkMobSpawnRules,
        SpawnPlacementRegisterEvent.Operation.OR);
  }
}
