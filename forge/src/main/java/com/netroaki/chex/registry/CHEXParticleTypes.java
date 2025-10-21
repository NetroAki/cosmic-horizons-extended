package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEXParticleTypes {
  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(Registries.PARTICLE_TYPE, CHEX.MOD_ID);

  // Sand particle for sandstorms and wind effects
  public static final RegistryObject<SimpleParticleType> SAND_PARTICLE =
      PARTICLE_TYPES.register("sand_particle", () -> new SimpleParticleType(false));

  // Heat wave particle for heat distortion effects
  public static final RegistryObject<SimpleParticleType> HEAT_WAVE =
      PARTICLE_TYPES.register("heat_wave", () -> new SimpleParticleType(false));

  // Spice particle for spice blow events
  public static final RegistryObject<SimpleParticleType> SPICE_PARTICLE =
      PARTICLE_TYPES.register("spice_particle", () -> new SimpleParticleType(true));

  public static void register(IEventBus eventBus) {
    PARTICLE_TYPES.register(eventBus);
  }
}
