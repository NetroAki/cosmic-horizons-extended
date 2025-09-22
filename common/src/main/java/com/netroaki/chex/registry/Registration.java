package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

public class Registration {
    private static final IEventBus MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    
    // Create DeferredRegisters
    public static final DeferredRegister<Biome> BIOMES = create(Registries.BIOME);
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = create(Registries.CONFIGURED_FEATURE);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = create(Registries.PLACED_FEATURE);
    
    // Initialize all registries
    public static void init() {
        // Register DeferredRegisters
        BIOMES.register(MOD_EVENT_BUS);
        CONFIGURED_FEATURES.register(MOD_EVENT_BUS);
        PLACED_FEATURES.register(MOD_EVENT_BUS);
        
        // Register other components
        ModBiomes.register();
    }
    
    // Helper method to create DeferredRegisters
    private static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registry) {
        return DeferredRegister.create(registry.location(), CHEX.MOD_ID);
    }
}
