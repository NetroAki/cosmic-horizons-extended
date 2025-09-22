package com.netroaki.chex.client;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.render.SolarEngineerDroneRenderer;
import com.netroaki.chex.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register entity renderers
            EntityRenderers.register(ModEntities.SOLAR_ENGINEER_DRONE.get(), SolarEngineerDroneRenderer::new);
            EntityRenderers.register(ModEntities.VERDANT_COLOSSUS.get(), VerdantColossusRenderer::new);
            EntityRenderers.register(ModEntities.LUMINFISH.get(), LuminfishRenderer::new);
            
            // Add more entity renderers here as needed
        });
    }
}
