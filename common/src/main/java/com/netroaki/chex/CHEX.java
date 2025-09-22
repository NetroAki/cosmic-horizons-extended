package com.netroaki.chex;

import com.mojang.logging.LogUtils;
import com.netroaki.chex.registry.*;
import com.netroaki.chex.world.structure.ConfiguredStructures;
import com.netroaki.chex.world.structure.StructureRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(CHEX.MOD_ID)
public class CHEX {
    public static final String MOD_ID = "chex";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CHEX() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Initialize registries
        ModRegistries.init();
        
        // Register structure pieces and types
        StructureRegistry.register(modEventBus);
        
        // Register ourselves for server and other game events
        MinecraftForge.EVENT_BUS.register(this);
        
        // Register the setup method for mod loading
        modEventBus.addListener(this::commonSetup);
        
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Cosmic Horizons Expanded initializing...");
        
        // Configure structure sets
        event.enqueueWork(() -> {
            ConfiguredStructures.registerConfiguredStructures();
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Add items to creative tabs here
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // event.accept(ModItems.EXAMPLE_ITEM.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Cosmic Horizons Expanded server setup complete");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void onRegister(RegisterEvent event) {
            // Register structure type
            event.register(Registries.STRUCTURE_TYPE, helper -> {
                helper.register(ResourceKey.create(Registries.STRUCTURE_TYPE, 
                    new ResourceLocation(MOD_ID, "colossus_arena")), 
                    StructureRegistry.COLOSSUS_ARENA.get());
            });
            
            // Register biomes
            event.register(Registries.BIOME, helper -> {
                ModBiomes.bootstrap(helper);
            });
            
            // Register configured features
            event.register(Registries.CONFIGURED_FEATURE, helper -> {
                ModConfiguredFeatures.bootstrap(helper);
            });
            
            // Register placed features
            event.register(Registries.PLACED_FEATURE, helper -> {
                ModConfiguredFeatures.bootstrapPlacements(helper);
            });
        }
    }
}
