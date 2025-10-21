package com.netroaki.chex.client;

import com.netroaki.chex.client.particle.*;
import com.netroaki.chex.particle.ArrakisParticleTypes;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleProviders {

  @SubscribeEvent
  public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    // Register all particle providers
    registerParticle(event, ArrakisParticleTypes.SAND_PARTICLE.get(), SandParticle.Provider::new);
    registerParticle(event, ArrakisParticleTypes.DUST_WHIRL.get(), DustWhirlParticle.Provider::new);
    registerParticle(event, ArrakisParticleTypes.HEAT_HAZE.get(), HeatHazeParticle.Provider::new);
  }

  private static <T extends ParticleType<?>> void registerParticle(
      RegisterParticleProvidersEvent event, T type, ParticleProvider<T> provider) {
    event.register(type, provider);
  }

  // Helper method to register particles with the particle engine directly (for programmatic
  // spawning)
  public static void registerParticleFactories(ParticleEngine engine) {
    engine.register(ArrakisParticleTypes.SAND_PARTICLE.get(), SandParticle.Provider::new);
    engine.register(ArrakisParticleTypes.DUST_WHIRL.get(), DustWhirlParticle.Provider::new);
    engine.register(ArrakisParticleTypes.HEAT_HAZE.get(), HeatHazeParticle.Provider::new);
  }
}
