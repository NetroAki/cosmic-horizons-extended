package com.netroaki.chex.client;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.ring.client.ArcSceneryRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    value = Dist.CLIENT,
    bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RingClient {
  @SubscribeEvent
  public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(
        com.netroaki.chex.registry.CHEXBlockEntities.ARC_SCENERY.get(), ArcSceneryRenderer::new);
  }

  @SubscribeEvent
  public static void onRegisterSky(RegisterDimensionSpecialEffectsEvent event) {
    // Register a simple tinted sky for Aurelia
    event.register(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "aurelia_ringworld"),
        AureliaSkyEffects.INSTANCE);
  }
}
