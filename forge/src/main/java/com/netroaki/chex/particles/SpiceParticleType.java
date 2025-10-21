package com.netroaki.chex.particles;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SpiceParticleType {
  public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>>
      PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "chex");

  public static final RegistryObject<SimpleParticleType> SPICE_PARTICLE =
      PARTICLE_TYPES.register("spice_particle", () -> new SimpleParticleType(false));
}
