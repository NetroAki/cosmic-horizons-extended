package com.netroaki.chex.client.particle;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CosmicHorizonsExpanded.MOD_ID);

  // Register particle types
  public static final RegistryObject<SimpleParticleType> BIOLUMINESCENT_SPARKLE =
      PARTICLE_TYPES.register("bioluminescent_sparkle", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> FALLING_ASH =
      PARTICLE_TYPES.register("falling_ash", () -> new SimpleParticleType(false));

  // Register particle factories
  @SubscribeEvent
  public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    event.register(
        BIOLUMINESCENT_SPARKLE.get(), spriteSet -> new BioluminescentParticle.Factory(spriteSet));

    event.register(FALLING_ASH.get(), spriteSet -> new AshParticle.Provider(spriteSet));
  }

  public static void register() {
    // Register with mod event bus
    CosmicHorizonsExpanded.MOD_EVENT_BUS.addListener(ModParticles::registerParticleFactories);
  }
}
