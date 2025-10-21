package com.netroaki.chex.client;

import com.netroaki.chex.particles.SpiceParticle;
import com.netroaki.chex.registry.CHEXRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = "cosmic_horizons_extended",
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class ParticleHandler {

  @SubscribeEvent
  public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;

    // Register spice particle factory
    event.register(
        CHEXRegistries.PARTICLE_TYPES.getEntries().stream()
            .filter(entry -> entry.getId().getPath().equals("spice_particle"))
            .findFirst()
            .orElseThrow()
            .get(),
        SpiceParticle.Provider::new);
  }
}
