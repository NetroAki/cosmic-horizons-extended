package com.netroaki.chex.particle;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArrakisParticleTypes {
  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CosmicHorizonsExpanded.MOD_ID);

  // Dust particles for sandstorms and wind
  public static final RegistryObject<SimpleParticleType> SAND_PARTICLE =
      register("sand_particle", false);
  public static final RegistryObject<SimpleParticleType> DUST_WHIRL = register("dust_whirl", true);
  public static final RegistryObject<SimpleParticleType> HEAT_HAZE = register("heat_haze", true);

  private static RegistryObject<SimpleParticleType> register(String name, boolean alwaysShow) {
    return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
  }

  public static void register() {
    // TODO: Fix when register method is available
    // PARTICLE_TYPES.register("chex");
  }
}
