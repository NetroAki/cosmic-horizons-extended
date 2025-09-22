package com.netroaki.chex;

import com.mojang.logging.LogUtils;
import com.netroaki.chex.commands.ChexCommands;
import com.netroaki.chex.commands.TierCommand;
import com.netroaki.chex.events.PlayerEvents;
import com.netroaki.chex.crafting.RecipeConditionTier;
import com.netroaki.chex.debug.LagProfiler;
import com.netroaki.chex.discovery.PlanetDiscovery;
import com.netroaki.chex.gt.GregTechBridge;
import com.netroaki.chex.gt.GtBridge;
import com.netroaki.chex.hooks.DimensionHooks;
import com.netroaki.chex.integration.CosmicHorizonsIntegration;
import com.netroaki.chex.integration.KubeJSIntegration;
import com.netroaki.chex.network.CHEXNetwork;
import com.netroaki.chex.config.FuelConfig;
import com.netroaki.chex.registry.CHEXChunkGenerators;
import com.netroaki.chex.registry.CHEXEffects;
import com.netroaki.chex.registry.ArrakisEntities;
import com.netroaki.chex.registry.ArrakisFeatures;
import com.netroaki.chex.registry.ArrakisItems;
import com.netroaki.chex.registry.ArrakisPlacements;
import com.netroaki.chex.registry.CHEXRegistries;
import com.netroaki.chex.registry.FuelRegistry;
import com.netroaki.chex.registry.NoduleDesigns;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.registry.biomes.CHEXBiomes;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import com.netroaki.chex.suits.SuitItems;
import com.netroaki.chex.suits.SuitTiers;
import com.netroaki.chex.travel.TravelGraph;
import com.netroaki.chex.worldgen.CHEXBiomeModifiers;
import com.netroaki.chex.worldgen.MineralGenerationRegistry;
import com.netroaki.chex.worldgen.PandoraSpawns;
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

    // Register our mod's event bus
    modEventBus.addListener(this::commonSetup);

    // Register configurations
    com.netroaki.chex.config.CHEXConfig.register();
    FuelConfig.register();
    
    // Register registries
    CHEXRegistries.register(modEventBus);
    CHEXEffects.EFFECTS.register(modEventBus);
    ArrakisEntities.ENTITIES.register(modEventBus);
    ArrakisFeatures.FEATURES.register(modEventBus);
    ArrakisItems.ITEMS.register(modEventBus);
    ArrakisPlacements.PLACEMENTS.register(modEventBus);
    // Register biomes
    CHEXBiomes.BIOMES.register(modEventBus);
    // Ensure block items are registered so recipes for those blocks resolve
    CHEXBlocks.registerBlockItems();
    CHEXChunkGenerators.CHUNK_GENERATORS.register(modEventBus);
    CHEXBiomeModifiers.register(modEventBus);
    // CHEXDensityFunctions.DENSITY_FUNCTIONS.register(modEventBus); // Temporarily
    // disabled
    SuitItems.ITEMS.register(modEventBus);

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

    // Register event handlers
    MinecraftForge.EVENT_BUS.register(PlayerEvents.class);
    
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
          "GTCEu not found - using fallback systems. Some features may be limited.");
    }

    // Initialize fuel registry after configs are loaded
    FuelRegistry.initialize();

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

    // Initialize networking
    LOGGER.info("Initializing networking...");
    CHEXNetwork.register();

    // Register recipe condition serializer
    CraftingHelper.register(new RecipeConditionTier.Serializer());

    // Initialize KubeJS integration
    LOGGER.info("Initializing KubeJS integration...");
    KubeJSIntegration.register();
    
    // Initialize GTCEu integration
    LOGGER.info("Initializing GTCEu integration...");
    com.netroaki.chex.integration.gtceu.GTCEuIntegrationHandler.init();
    
    // Register Arrakis content
    LOGGER.info("Registering Arrakis content...");
    com.netroaki.chex.registry.blocks.ArrakisBlocks.register(eventBus);
    com.netroaki.chex.registry.blocks.ArrakisFloraBlocks.register(eventBus);
    com.netroaki.chex.registry.entities.ArrakisEntities.ENTITIES.register(eventBus);

    // Initialize TerraBlender regions
    LOGGER.info("Initializing TerraBlender regions...");
    event.enqueueWork(() -> {
        CHEXBiomes.registerRegions();
        
        // Register Arrakis entity spawns
        SpawnPlacements.register(ArrakisEntities.SPICE_GATHERER.get(), 
            SpawnPlacements.Type.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            (entityType, level, spawnType, pos, random) -> 
                level.getBlockState(pos.below()).isSolid() && 
                level.getRawBrightness(pos, 0) > 8
        );
        
        SpawnPlacements.register(ArrakisEntities.JUVENILE_SANDWORM.get(), 
            SpawnPlacements.Type.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            JuvenileSandwormEntity::checkSandwormSpawnRules
        );
        
        SpawnPlacements.register(ArrakisEntities.STORM_HAWK.get(), 
            SpawnPlacements.Type.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING, 
            StormHawkEntity::checkStormHawkSpawnRules
        );
    });
    LOGGER.info("TerraBlender regions and entity spawns initialized");
    
    // Register spawn placements for Pandora fauna
    LOGGER.info("Registering Pandora fauna spawn placements...");
    event.enqueueWork(PandoraSpawns::registerSpawnPlacements);
    LOGGER.info("Pandora fauna spawn placements registered");

    LOGGER.info("CHEX common setup complete!");
  }

  private void registerCommands(RegisterCommandsEvent event) {
    ChexCommands.register(event.getDispatcher());
    TierCommand.register(event.getDispatcher());
    LOGGER.info("CHEX commands registered");
  }

  private void onServerAboutToStart(ServerAboutToStartEvent event) {
    if (!LagProfiler.isRunning()) {
      boolean started = LagProfiler.start(20);
      if (started) {
        LOGGER.info("CHEX LagProfiler auto-started (20ms period)");
      }
    }

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
