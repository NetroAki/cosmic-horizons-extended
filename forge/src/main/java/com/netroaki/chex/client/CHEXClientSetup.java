package com.netroaki.chex.client;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.renderer.RingworldWallRenderer;
import com.netroaki.chex.registry.CHEXBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Client-side setup for CHEX */
@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class CHEXClientSetup {

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(
        CHEXBlockEntities.RINGWORLD_WALL.get(), RingworldWallRenderer::new);
  }
}
