package com.netroaki.chex;

import com.mojang.logging.LogUtils;
import com.netroaki.chex.commands.ChexCommands;
import com.netroaki.chex.network.CHEXNetwork;
import com.netroaki.chex.registry.CHEXChunkGenerators;
import com.netroaki.chex.registry.CHEXRegistries;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.registry.RocketTiers;
import com.netroaki.chex.gt.GregTechBridge;
import com.netroaki.chex.integration.CosmicHorizonsIntegration;
import com.netroaki.chex.hooks.DimensionHooks;
import com.netroaki.chex.hooks.RingworldWrapHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CHEX.MOD_ID)
public class CHEX {
    public static final String MOD_ID = "cosmic_horizons_extended";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CHEX() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register our mod's event bus
        modEventBus.addListener(this::commonSetup);

        // Register registries
        CHEXRegistries.register(modEventBus);
        // Ensure block items are registered so recipes for those blocks resolve
        CHEXBlocks.registerBlockItems();
        CHEXChunkGenerators.CHUNK_GENERATORS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register hooks
        DimensionHooks.register();
        // Temporarily disable wrap teleports to reduce movement jank in ringworld
        // RingworldWrapHooks.register();

        // Register commands
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

        // Log mod initialization
        LOGGER.info("Cosmic Horizons Extended (CHEX) initializing...");

        // Check GTCEu availability
        if (GregTechBridge.isAvailable()) {
            LOGGER.info("GTCEu integration enabled - full mineral generation and progression features available");
        } else {
            LOGGER.info("GTCEu not detected - using fallback systems for mineral generation and progression");
        }

        // Check Cosmic Horizons availability
        if (CosmicHorizonsIntegration.isCosmicHorizonsLoaded()) {
            LOGGER.info(
                    "Cosmic Horizons integration enabled - full planet discovery and integration features available");
        } else if (CosmicHorizonsIntegration.isCosmosLoaded()) {
            LOGGER.info("Cosmos integration enabled - basic planet discovery features available");
        } else {
            LOGGER.info("No Cosmic Horizons mod detected - using standalone mode with CHEX planets only");
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("CHEX common setup starting...");

        // Initialize rocket tiers
        LOGGER.info("Initializing rocket tiers system...");
        RocketTiers.initialize();
        LOGGER.info("Rocket tiers system initialized with {} tiers", RocketTiers.values().length);

        // Initialize planet registry
        LOGGER.info("Initializing planet registry...");
        PlanetRegistry.initialize();

        // Initialize networking
        LOGGER.info("Initializing networking...");
        CHEXNetwork.register();

        LOGGER.info("CHEX common setup complete!");
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ChexCommands.register(event.getDispatcher());
        LOGGER.info("CHEX commands registered");
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}