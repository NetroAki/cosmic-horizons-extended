package com.netroaki.chex.events;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entities.CliffHunterEntity;
import com.netroaki.chex.entities.GlowbeastEntity;
import com.netroaki.chex.entities.SkyGrazerEntity;
import com.netroaki.chex.entities.SporefliesEntity;
import com.netroaki.chex.entities.boss.CliffHunterAlphaEntity;
import com.netroaki.chex.entities.boss.DeepSeaSirenEntity;
import com.netroaki.chex.entities.boss.MoltenBehemothEntity;
import com.netroaki.chex.entities.boss.SkySovereignEntity;
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
        com.netroaki.chex.registry.entities.CHEXEntities.GLOWBEAST.get(),
        GlowbeastEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.SPOREFLIES.get(),
        SporefliesEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.SKY_GRAZER.get(),
        SkyGrazerEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.CLIFF_HUNTER.get(),
        CliffHunterEntity.createAttributes().build());

    // Boss entities
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.SPORE_TYRANT.get(),
        SporeTyrantEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.CLIFF_HUNTER_ALPHA.get(),
        CliffHunterAlphaEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.DEEP_SEA_SIREN.get(),
        DeepSeaSirenEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.MOLTEN_BEHEMOTH.get(),
        MoltenBehemothEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.SKY_SOVEREIGN.get(),
        SkySovereignEntity.createAttributes().build());
    event.put(
        com.netroaki.chex.registry.entities.CHEXEntities.WORLDHEART_AVATAR.get(),
        WorldheartAvatarEntity.createAttributes().build());
  }
}
