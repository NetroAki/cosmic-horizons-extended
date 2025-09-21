package com.netroaki.chex.registry;

import com.mojang.serialization.Codec;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.DysonShellGenerator;
import com.netroaki.chex.worldgen.HollowWorldGenerator;
import com.netroaki.chex.worldgen.StripRingGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEXChunkGenerators {
  public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS =
      DeferredRegister.create(Registries.CHUNK_GENERATOR, CHEX.MOD_ID);

  // Removed: custom ringworld generator; using vanilla noise via datapack
  // public static final RegistryObject<Codec<? extends ChunkGenerator>>
  // HALO_RINGWORLD = CHUNK_GENERATORS
  // .register("aurelia_ringworld", () -> HaloRingworldGenerator.CODEC);

  public static final RegistryObject<Codec<? extends ChunkGenerator>> DYSON_SHELL =
      CHUNK_GENERATORS.register("dyson_shell", () -> DysonShellGenerator.CODEC);

  public static final RegistryObject<Codec<? extends ChunkGenerator>> HOLLOW_WORLD =
      CHUNK_GENERATORS.register("hollow_world", () -> HollowWorldGenerator.CODEC);

  public static final RegistryObject<Codec<? extends ChunkGenerator>> STRIP_RING =
      CHUNK_GENERATORS.register("strip_ring", () -> StripRingGenerator.CODEC);
}
