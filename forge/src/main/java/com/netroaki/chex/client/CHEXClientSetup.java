package com.netroaki.chex.client;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.model.entity.SporeTyrantModel;
import com.netroaki.chex.client.render.RingworldWallRenderer;
import com.netroaki.chex.client.render.ArrakisEffectsRenderer;
import com.netroaki.chex.client.render.SpiceBlowRenderer;
import com.netroaki.chex.client.renderer.entity.SporeTyrantRenderer;
import com.netroaki.chex.registry.CHEXBlockEntities;
import com.netroaki.chex.registry.CHEXEntities;
import com.netroaki.chex.registry.CHEXParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import com.netroaki.chex.client.renderer.dimension.LibrarySkyRenderer;
import com.netroaki.chex.registry.CHEXDimensions;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
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
        event.registerEntityRenderer(CHEXEntities.SPORE_TYRANT.get(), SporeTyrantRenderer::new);
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Register layer definitions for entity models
        event.registerLayerDefinition(SporeTyrantModel.LAYER_LOCATION, SporeTyrantModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Register client-side setup code here
        event.enqueueWork(() -> {
            // Any client-side initialization that needs to be done after mod loading
        });
    }
    
    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        // Register custom sky renderer for the Infinite Library dimension
        event.register(
            CHEXDimensions.INFINITE_LIBRARY_TYPE.location(),
            new LibrarySkyRenderer()
        );
    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        // This is a workaround for older Forge versions
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
        
        // Register particle factories
        particleEngine.register(CHEXParticleTypes.SAND_PARTICLE.get(),
            ArrakisEffectsRenderer.Factory::new);
            
        particleEngine.register(CHEXParticleTypes.SPICE_PARTICLE.get(),
            SpiceBlowRenderer.Factory::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        // Register particle providers (Forge 41.0.63+)
        event.register(CHEXParticleTypes.SAND_PARTICLE.get(),
            ArrakisEffectsRenderer.Factory::new);
            
        event.register(CHEXParticleTypes.SPICE_PARTICLE.get(),
            SpiceBlowRenderer.Factory::new);
    }
}
