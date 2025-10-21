package com.netroaki.chex.client;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.renderer.RingworldWallRenderer;
import com.netroaki.chex.registry.CHEXBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Client-side setup for CHEX */
@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class CHEXClientSetup {

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    // Register block entity renderers
    event.registerBlockEntityRenderer(
        CHEXBlockEntities.RINGWORLD_WALL.get(), RingworldWallRenderer::new);

    // Register entity renderers
    // TODO: Re-enable when SporeTyrant entity is implemented
    // event.registerEntityRenderer(CHEXEntities.SPORE_TYRANT.get(),
    // SporeTyrantRenderer::new);
  }

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    // Register layer definitions for entity models
    // TODO: Re-enable when SporeTyrantModel is properly implemented
    // event.registerLayerDefinition(
    // SporeTyrantModel.LAYER_LOCATION, SporeTyrantModel::createBodyLayer);
  }

  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    // Register client-side setup code here
    event.enqueueWork(
        () -> {
          // Any client-side initialization that needs to be done after mod loading
        });
  }

  @SubscribeEvent
  public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
    // Register Pandora special effects (matches dimension_type effects id)
    event.register(
        new net.minecraft.resources.ResourceLocation(com.netroaki.chex.CHEX.MOD_ID, "pandora"),
        PandoraSkyEffects.INSTANCE);

    // Register Arrakis special effects (harsh desert sky)
    event.register(
        new net.minecraft.resources.ResourceLocation(com.netroaki.chex.CHEX.MOD_ID, "arrakis"),
        ArrakisSkyEffects.INSTANCE);
  }

  @SubscribeEvent
  public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    // Register particle factories
    // TODO: Fix particle registration when particle types are properly implemented
    // event.register(CHEXParticleTypes.SAND_PARTICLE.get(),
    // ArrakisEffectsRenderer.Factory::new);
    // event.register(CHEXParticleTypes.SPICE_PARTICLE.get(),
    // SpiceBlowRenderer.Factory::new);
  }
}
