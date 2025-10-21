package com.netroaki.chex.client;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.client.entity.renderer.inferno.AshCrawlerRenderer;
import com.netroaki.chex.client.entity.renderer.inferno.FireWraithRenderer;
import com.netroaki.chex.client.entity.renderer.inferno.MagmaHopperRenderer;
import com.netroaki.chex.client.particle.ModParticles;
import com.netroaki.chex.client.renderer.entity.inferno.InfernalSovereignRenderer;
import com.netroaki.chex.client.sound.AquaMundusAmbientSoundHandler;
import com.netroaki.chex.client.sound.AquaMundusSounds;
import com.netroaki.chex.entity.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
    modid = CosmicHorizonsExpanded.MOD_ID,
    value = Dist.CLIENT,
    bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    // Register particle factories on the main thread
    event.enqueueWork(
        () -> {
          // Register entity renderers
          EntityRenderers.register(ModEntities.ASH_CRAWLER.get(), AshCrawlerRenderer::new);
          EntityRenderers.register(ModEntities.FIRE_WRAITH.get(), FireWraithRenderer::new);
          EntityRenderers.register(ModEntities.MAGMA_HOPPER.get(), MagmaHopperRenderer::new);
          EntityRenderers.register(
              ModEntities.INFERNAL_SOVEREIGN.get(), InfernalSovereignRenderer::new);

          // Initialize sound handlers
          AquaMundusSounds.register(CosmicHorizonsExpanded.MOD_EVENT_BUS);
          ModParticles.register();
        });
  }

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent event) {
    // Handle client-side ticking for ambient sounds
    if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
      AquaMundusAmbientSoundHandler.onClientTick();
    }
  }
}
