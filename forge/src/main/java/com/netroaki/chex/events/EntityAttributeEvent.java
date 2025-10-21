package com.netroaki.chex.events;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entities.SporefliesEntity;
import com.netroaki.chex.entities.boss.SporeTyrantEntity;
import com.netroaki.chex.entities.boss.WorldheartAvatarEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributeEvent {

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.SPOREFLIES.get(),
        SporefliesEntity.createAttributes().build());

    // Boss entities
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.SPORE_TYRANT.get(),
        SporeTyrantEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.WORLDHEART_AVATAR.get(),
        WorldheartAvatarEntity.createAttributes().build());
  }
}
