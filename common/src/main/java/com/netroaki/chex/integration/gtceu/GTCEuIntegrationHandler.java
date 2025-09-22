package com.netroaki.chex.integration.gtceu;

import com.netroaki.chex.CHEX;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Handles initialization of GTCEu integration.
 */
public class GTCEuIntegrationHandler {
    private static boolean initialized = false;
    private static boolean gtceuLoaded = false;
    
    /**
     * Initialize GTCEu integration if the mod is loaded.
     */
    public static void init() {
        if (initialized) {
            return;
        }
        
        gtceuLoaded = ModList.get().isLoaded("gtceu");
        
        if (gtceuLoaded) {
            try {
                // Register our setup method to be called during mod initialization
                FMLJavaModLoadingContext.get().getModEventBus().addListener(GTCEuIntegrationHandler::onCommonSetup);
                CHEX.LOGGER.info("GTCEu integration will be initialized");
            } catch (Exception e) {
                CHEX.LOGGER.error("Failed to set up GTCEu integration", e);
                gtceuLoaded = false;
            }
        } else {
            CHEX.LOGGER.info("GTCEu not found, using fallback implementations");
        }
        
        initialized = true;
    }
    
    private static void onCommonSetup(FMLCommonSetupEvent event) {
        if (!gtceuLoaded) {
            return;
        }
        
        event.enqueueWork(() -> {
            try {
                // Initialize GTCEu integration
                PandoraGTCEuIntegration.init();
                CHEX.LOGGER.info("GTCEu integration initialized successfully");
            } catch (Exception e) {
                CHEX.LOGGER.error("Failed to initialize GTCEu integration", e);
                gtceuLoaded = false;
            }
        });
    }
    
    /**
     * @return True if GTCEu is loaded and initialized
     */
    public static boolean isGTCEuLoaded() {
        return gtceuLoaded;
    }
    
    /**
     * Get the GTCEu compatibility interface.
     * @return The GTCEu compatibility interface
     */
    public static GTCEuCompat getCompat() {
        return GTCEuCompat.INSTANCE;
    }
}
