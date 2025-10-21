package com.netroaki.chex.client;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.render.entity.SandEmperorRenderer;
import com.netroaki.chex.client.render.entity.SandwormJuvenileRenderer;
import com.netroaki.chex.client.render.entity.SpiceGathererRenderer;
import com.netroaki.chex.client.render.entity.StormHawkRenderer;
import com.netroaki.chex.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class EntityRenderers {
  @SubscribeEvent
  public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    // Register entity renderers
    event.registerEntityRenderer(EntityInit.SANDWORM_JUVENILE.get(), SandwormJuvenileRenderer::new);
    event.registerEntityRenderer(EntityInit.STORM_HAWK.get(), StormHawkRenderer::new);
    event.registerEntityRenderer(EntityInit.SPICE_GATHERER.get(), SpiceGathererRenderer::new);
    event.registerEntityRenderer(EntityInit.SAND_EMPEROR.get(), SandEmperorRenderer::new);
  }
}
