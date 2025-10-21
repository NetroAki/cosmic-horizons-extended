package com.netroaki.chex.registry.particle;

import com.netroaki.chex.CHEX;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXParticles {
  public static final DeferredRegister<ParticleType<?>> PARTICLES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CHEX.MOD_ID);

  public static final RegistryObject<SimpleParticleType> WIND =
      PARTICLES.register("wind", () -> new SimpleParticleType(false));
}
