package com.netroaki.chex;

import com.mojang.logging.LogUtils;
import com.netroaki.chex.block.crystalis.CrystalisBlocks;
import com.netroaki.chex.config.EntitySpawnConfig;
import com.netroaki.chex.effect.ModEffects;
import com.netroaki.chex.item.*;
import com.netroaki.chex.sound.ModSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(CosmicHorizonsExpanded.MOD_ID)
public class CosmicHorizonsExpanded {
  public static final String MOD_ID = "cosmic_horizons_extended";
  public static final Logger LOGGER = LogUtils.getLogger();

  public CosmicHorizonsExpanded() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register entity types
    // ModEntities.register(modEventBus); // TODO: Implement ModEntities

    // Register items and creative tabs
    ModItems.register(modEventBus);
    ModCreativeModeTabs.register(modEventBus);

    // Register sounds
    ModSounds.register(modEventBus);

    // Register effects
    ModEffects.register(modEventBus);

    // Register configurations
    ModLoadingContext.get()
        .registerConfig(
            ModConfig.Type.COMMON,
            EntitySpawnConfig.SPEC,
            "cosmic_horizons_extended-entities.toml");
    com.netroaki.chex.config.CHEXModConfig.register();

    // Register structure processors
    // ModStructureProcessors.register(modEventBus); // TODO: Implement
    // ModStructureProcessors

    // Register structure piece types
    // ModStructurePieceTypes.register(modEventBus); // TODO: Implement
    // ModStructurePieceTypes

    // Register structure types
    // CHEStructures.STRUCTURE_TYPES.register(modEventBus); // TODO: Implement
    // CHEStructures
    // CHEStructures.STRUCTURES.register(modEventBus); // TODO: Implement
    // CHEStructures

    // Register Aqua Mundus mechanics
    // AquaMundusMechanicsSetup.register(); // TODO: Implement
    // AquaMundusMechanicsSetup

    // Register Crystalis blocks
    CrystalisBlocks.register(modEventBus);

    // Register the setup method for mod loading
    modEventBus.addListener(this::setup);

    // Register structure placement
    modEventBus.addListener(this::registerStructurePlacements);

    // Register ourselves for server and other game events
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    // TODO: Configure structure placement when CHEStructures is implemented
    // event.enqueueWork(
    // () -> {
    // // Configure structure placement in biomes
    // StructureStep.Decoration structureDecoration =
    // StructureStep.Decoration.SURFACE_STRUCTURES;

    // // Register the structure in the Alpha Centauri A dimension
    // StructureManager.register(
    // CHEStructures.STELLAR_AVATAR_ARENA_KEY,
    // structureDecoration,
    // BiomeManager.getBiomes(
    // ResourceKey.create(
    // Registries.BIOME, new ResourceLocation(MOD_ID, "alpha_centauri_a")))
    // .toArray(Biome[]::new));
    // });

    LOGGER.info("Cosmic Horizons Expanded mod setup complete");
  }

  private void registerStructurePlacements(RegisterEvent event) {
    // TODO: Register structure placement when CHEStructures is implemented
    // event.register(
    // ForgeRegistries.Keys.BIOMES,
    // helper -> {
    // // Add structure to Alpha Centauri A biome
    // ResourceLocation alphaCentauriABiome = new ResourceLocation(MOD_ID,
    // "alpha_centauri_a");
    // helper.register(
    // alphaCentauriABiome,
    // builder -> {
    // builder.addStructure(
    // CHEStructures.STELLAR_AVATAR_ARENA_STRUCTURE.getHolder().get());
    // });
    // });
  }
}
