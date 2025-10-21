package com.netroaki.chex.world.gen.structure;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.world.structures.FrozenCathedralStructure;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHEStructures {
  public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
      DeferredRegister.create(Registries.STRUCTURE_TYPE, CosmicHorizonsExpanded.MOD_ID);

  public static final RegistryObject<StructureType<StellarAvatarArenaStructure>>
      STELLAR_AVATAR_ARENA =
          STRUCTURE_TYPES.register(
              "stellar_avatar_arena", () -> () -> StellarAvatarArenaStructure.CODEC);

  public static final ResourceKey<Structure> STELLAR_AVATAR_ARENA_KEY =
      ResourceKey.create(
          Registries.STRUCTURE,
          new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "stellar_avatar_arena"));

  public static final DeferredRegister<Structure> STRUCTURES =
      DeferredRegister.create(Registries.STRUCTURE, CosmicHorizonsExpanded.MOD_ID);

  public static final RegistryObject<Structure> STELLAR_AVATAR_ARENA_STRUCTURE =
      STRUCTURES.register(
          "stellar_avatar_arena",
          () ->
              new StellarAvatarArenaStructure(
                  new Structure.StructureSettings(
                      // Configure the structure to only generate in the Alpha Centauri dimension
                      // and with proper spacing/separation
                      new Structure.StructureSettings.StructureConfiguration(
                          (biome) ->
                              biome.is(
                                  ResourceKey.create(
                                      Registries.BIOME,
                                      new ResourceLocation(
                                          CosmicHorizonsExpanded.MOD_ID, "alpha_centauri_a"))),
                          new Structure.StructureSettings.OverrideableStructureSettings(
                              32, 8, 12345) // spacing, separation, salt
                          )),
                  // These will be set up in the datapack
                  null, // startPool
                  Optional.empty(), // startJigsawName
                  7, // size
                  HeightProvider.TRASEARCH, // startHeight
                  false, // useExpansionHack
                  Projection.RIGID, // projection
                  80 // maxDistanceFromCenter
                  ));

  public static final ResourceKey<Structure> FROZEN_CATHEDRAL_KEY =
      ResourceKey.create(
          Registries.STRUCTURE,
          new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "frozen_cathedral"));

  public static final RegistryObject<Structure> FROZEN_CATHEDRAL_STRUCTURE =
      STRUCTURES.register(
          "frozen_cathedral",
          () ->
              new FrozenCathedralStructure(
                  new Structure.StructureSettings(
                      // Configure the structure to only generate in the Crystalis dimension
                      new Structure.StructureSettings.StructureConfiguration(
                          (biome) ->
                              biome.is(
                                  ResourceKey.create(
                                      Registries.BIOME,
                                      new ResourceLocation(
                                          CosmicHorizonsExpanded.MOD_ID,
                                          "crystalis_glacial_peaks"))),
                          new Structure.StructureSettings.OverrideableStructureSettings(
                              64, 16, 54321) // spacing, separation, salt
                          )),
                  // These will be set up in the datapack
                  null, // startPool
                  Optional.empty(), // startJigsawName
                  10, // size
                  HeightProvider.TRASEARCH, // startHeight
                  false, // useExpansionHack
                  Projection.RIGID, // projection
                  100 // maxDistanceFromCenter
                  ));
}
