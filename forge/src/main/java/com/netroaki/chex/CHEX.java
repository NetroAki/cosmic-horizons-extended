package com.netroaki.chex;

import com.mojang.logging.LogUtils;
import com.netroaki.chex.commands.ChexCommands;
import com.netroaki.chex.crafting.RecipeConditionTier;
import com.netroaki.chex.debug.LagProfiler;
import com.netroaki.chex.discovery.PlanetDiscovery;
import com.netroaki.chex.gt.GregTechBridge;
import com.netroaki.chex.gt.GtBridge;
import com.netroaki.chex.hooks.DimensionHooks;
import com.netroaki.chex.integration.CosmicHorizonsIntegration;
import com.netroaki.chex.integration.KubeJSIntegration;
import com.netroaki.chex.network.CHEXNetwork;
import com.netroaki.chex.registry.CHEXChunkGenerators;
import com.netroaki.chex.registry.CHEXBlocks;
import com.netroaki.chex.registry.CHEXDimensions;
import com.netroaki.chex.registry.CHEXItems;
import com.netroaki.chex.menu.CHEXMenuTypes;
import com.netroaki.chex.network.LibraryBookUpdatePacket;
import com.netroaki.chex.registry.CHEXRegistries;
import com.netroaki.chex.registry.CHEXStructures;
import com.netroaki.chex.registry.LibraryStructureRegistry;
import com.netroaki.chex.world.eden.EdenBiomeRegistry;
import com.netroaki.chex.world.eden.EdenDimension;
import com.netroaki.chex.world.eden.EdenEntities;
import com.netroaki.chex.world.eden.EdenGardenAbilities;
import com.netroaki.chex.world.eden.EdenGardenAccess;
import com.netroaki.chex.world.eden.EdenGardenProgression;
import com.netroaki.chex.world.library.LibraryDimension;
import com.netroaki.chex.world.library.LibraryDimensionProvider;
import com.netroaki.chex.registry.FuelRegistry;
import com.netroaki.chex.registry.NoduleDesigns;
import com.netroaki.chex.config.PandoraMobSpawnsConfig;
import com.netroaki.chex.config.PandoraHazardsConfig;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.registry.biomes.CHEXBiomes;
import com.netroaki.chex.registry.block_entity.CHEXBlockEntities;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import com.netroaki.chex.suits.SuitItems;
import com.netroaki.chex.suits.SuitTiers;
import com.netroaki.chex.travel.TravelGraph;
import com.netroaki.chex.quest.QuestManager;
import com.netroaki.chex.quest.arrakis.ArrakisQuests;
import com.netroaki.chex.quest.QuestEventHandler;
import com.netroaki.chex.worldgen.CHEXBiomeModifiers;
import com.netroaki.chex.worldgen.MineralGenerationRegistry;
import com.netroaki.chex.world.hazards.PandoraHazardManager;
import com.netroaki.chex.world.ambience.PandoraAmbienceManager;
import com.netroaki.chex.sound.ArrakisSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CHEX.MOD_ID)
public class CHEX {
  public static final String MOD_ID = "cosmic_horizons_extended";
  public static final Logger LOGGER = LogUtils.getLogger();
  private static final GtBridge GT_BRIDGE = new GregTechBridge();

  public CHEX() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register registries
    CHEXRegistries.register(modEventBus);
    CHEXBlockEntities.register(modEventBus);
    EntityInit.register(modEventBus);
    EdenDimension.register();
    EdenEntities.ENTITIES.register(modEventBus);
    LibraryStructureRegistry.STRUCTURE_TYPES.register(modEventBus);
    
    // Register Eden's Garden progression system
    modEventBus.register(EdenGardenAccess.class);
    modEventBus.register(EdenGardenAbilities.class);
    modEventBus.register(EdenGardenProgression.class);
    
    // Register Infinite Library dimension
    modEventBus.addListener(LibraryDimension::register);
    CHEXEffects.EFFECTS.register(modEventBus);
    com.netroaki.chex.config.CHEXConfig.register();
    PandoraMobSpawnsConfig.register();
    PandoraHazardsConfig.register();
    
    // Register hazard and ambience systems
    modEventBus.register(PandoraHazardManager.class);
    modEventBus.register(PandoraAmbienceManager.class);
    
    // Register Arrakis sounds
    ArrakisSounds.register(modEventBus);
    
    CHEXBlocks.register(modEventBus);
    CHEXBlocks.registerBlockItems();
    CHEXChunkGenerators.CHUNK_GENERATORS.register(modEventBus);
    CHEXBiomeModifiers.register(modEventBus);
    // CHEXDensityFunctions.DENSITY_FUNCTIONS.register(modEventBus); // Temporarily
    // disabled
    SuitItems.ITEMS.register(modEventBus);
    CHEXSoundEvents.SOUND_EVENTS.register(modEventBus);
    com.netroaki.chex.init.SoundInit.SOUND_EVENTS.register(modEventBus);

    // Register the setup method for mod loading
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::registerBiomes);

    // Register ourselves for server and other game events we are interested in
    MinecraftForge.EVENT_BUS.register(this);

    // Register hooks
    DimensionHooks.register();
    // Disable chunk mirroring hook for stability during testing
    // com.netroaki.chex.hooks.RingworldChunkMirrorHooks.register();

    // Auto-teleport on login disabled per request
    // Datapack generator now handles water; disable drain hook
    // RingworldDrainWaterHooks.register();
    // Disable runtime band/wall hooks; handled in custom generator
    // com.netroaki.chex.hooks.RingworldBandHooks.register();

    // Register commands
    MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStart);
    MinecraftForge.EVENT_BUS.addListener(this::onServerStopping);

    // Log mod initialization
    LOGGER.info("Cosmic Horizons Extended (CHEX) initializing...");

    // Check GTCEu availability
    if (gt().isAvailable()) {
      LOGGER.info(
          "GTCEu integration enabled - full mineral generation and progression features available");
    } else {
      LOGGER.info(
          "GTCEu not detected - using fallback systems for mineral generation and progression");
    }

    // Check Cosmic Horizons availability
    if (CosmicHorizonsIntegration.isCosmicHorizonsLoaded()) {
      LOGGER.info(
          "Cosmic Horizons integration enabled - full planet discovery and integration features"
              + " available");
    } else if (CosmicHorizonsIntegration.isCosmosLoaded()) {
      LOGGER.info("Cosmos integration enabled - basic planet discovery features available");
    } else {
      LOGGER.info("No Cosmic Horizons mod detected - using standalone mode with CHEX planets only");
    }
  }

  public static GtBridge gt() {
    return GT_BRIDGE;
  }

  private void commonSetup(final FMLCommonSetupEvent event) {
    LOGGER.info("CHEX common setup starting...");

    // Initialize nodule tiers
    LOGGER.info("Initializing nodule tiers system...");
    NoduleTiers.initialize();
    LOGGER.info("Nodule tiers system initialized with {} tiers", NoduleTiers.values().length);

    // Initialize suit tiers
    LOGGER.info("Initializing suit tiers system...");
    SuitTiers.initialize();
    LOGGER.info("Suit tiers system initialized with {} tiers", SuitTiers.values().length);

    // Initialize fuel registry
    LOGGER.info("Initializing fuel registry...");
    FuelRegistry.initialize();
    LOGGER.info("Fuel registry initialized");

    // Initialize planet registry
    LOGGER.info("Initializing planet registry...");
    PlanetRegistry.initialize();

    // Initialize travel graph
    LOGGER.info("Initializing travel graph...");
    TravelGraph.loadFromConfigOrDefaults();
    LOGGER.info("Travel graph initialized");

    // Initialize hardcoded nodule material themes
    LOGGER.info("Initializing nodule material themes...");
    NoduleDesigns.initializeDefaults();

    // Load mineral distributions from configuration
    event.enqueueWork(MineralGenerationRegistry::reload);

    // Register spawn placements for Arrakis entities
    event.enqueueWork(() -> {
        LOGGER.info("Registering Arrakis entity spawn placements...");
        com.netroaki.chex.world.spawning.ArrakisSpawnPlacements.registerSpawnPlacements();
    });

    // Initialize networking
    LOGGER.info("Initializing networking...");
    CHEXNetwork.register();
    
    // Initialize Sand Core fuel handler
    LOGGER.info("Initializing Sand Core fuel handler...");
    com.netroaki.chex.item.arrakis.SandCoreFuelHandler.register();
    LOGGER.info("Sand Core fuel handler initialized");

    // Register packet handlers
    event.enqueueWork(() -> {
      int packetId = 0;
      CHEXNetwork.INSTANCE.registerMessage(
          packetId++,
          LibraryBookUpdatePacket.class,
          LibraryBookUpdatePacket::encode,
          LibraryBookUpdatePacket::new,
          LibraryBookUpdatePacket::handle
      );
    });

    // Initialize KubeJS integration
    LOGGER.info("Initializing KubeJS integration...");
    KubeJSIntegration.initialize();

    // Initialize GTCEu integration if available
    if (gt().isAvailable()) {
      gt().initialize();
    }

    // Initialize quest system
    event.enqueueWork(() -> {
      // Register Arrakis quests
      ArrakisQuests.registerQuests(event);

      LOGGER.info("Quest system initialized");
    });

    // Initialize TerraBlender regions
    LOGGER.info("Initializing TerraBlender regions...");
    event.enqueueWork(
        () -> {
          CHEXBiomes.registerRegions();
        });
    LOGGER.info("TerraBlender regions initialized");

    LOGGER.info("CHEX common setup complete!");
  }

  private void registerCommands(RegisterCommandsEvent event) {
    ChexCommands.register(event.getDispatcher());
    LOGGER.info("CHEX commands registered");
  }

  private void onServerAboutToStart(ServerAboutToStartEvent event) {
    if (!LagProfiler.isRunning()) {
      boolean started = LagProfiler.start(20);
      if (started) {
        LOGGER.info("CHEX LagProfiler auto-started (20ms period)");
      }
    }

    // Initialize the travel graph
    TravelGraph.INSTANCE.initialize(event.getServer());

    // Initialize planet discovery system
    PlanetDiscovery.initialize(event.getServer());

    // Initialize mineral generation
    MineralGenerationRegistry.initialize(event.getServer());

    // Initialize quest system data
    QuestManager.init(event.getServer().overworld().getDataStorage());

    // Dimension registry sanity check vs PlanetRegistry
    var server = event.getServer();
    // Discover and write CH planets snapshot to config
    PlanetDiscovery.discoverAndWrite(server);
    var dimRegistryOpt = server.registryAccess().registry(Registries.DIMENSION);
    if (dimRegistryOpt.isPresent()) {
      var dimRegistry = dimRegistryOpt.get();
      var planetIds = com.netroaki.chex.registry.PlanetRegistry.getAllPlanets().keySet();

      // Planets missing dimension json
      var missing =
          planetIds.stream()
              .filter(id -> !dimRegistry.containsKey(ResourceKey.create(Registries.DIMENSION, id)))
              .toList();
      if (!missing.isEmpty()) {
        LOGGER.warn("[CHEX] Planets with missing dimensions ({}):", missing.size());
        missing.forEach(id -> LOGGER.warn(" - {}", id));
      } else {
        LOGGER.info("[CHEX] All planet dimensions present ({}).", planetIds.size());
      }

      // CHEX dimensions not referenced by PlanetRegistry (stale)
      var stale =
          dimRegistry.registryKeySet().stream()
              .filter(k -> k.location().getNamespace().equals(MOD_ID))
              .map(k -> k.location())
              .filter(loc -> !planetIds.contains(loc))
              .toList();
      if (!stale.isEmpty()) {
        LOGGER.warn(
            "[CHEX] Dimensions present but not referenced by PlanetRegistry (stale: {}):",
            stale.size());
        stale.forEach(loc -> LOGGER.warn(" - {}", loc));
      }
    } else {
      LOGGER.warn("[CHEX] Dimension registry not available during ServerAboutToStart");
    }
  }

  private void onServerStopping(ServerStoppingEvent event) {
    if (LagProfiler.isRunning()) {
      LagProfiler.stop(null);
      LOGGER.info("CHEX LagProfiler stopped on server shutdown");
    }
  }

  public static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
  }
}
