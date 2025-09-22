package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.biome.KeplerBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBiomes {
    // Biome Keys
    public static final ResourceKey<Biome> KEPLER_FOREST = registerKey("kepler_forest");
    public static final ResourceKey<Biome> KEPLER_RIVER = registerKey("kepler_river");
    public static final ResourceKey<Biome> KEPLER_PLAINS = registerKey("kepler_plains");

    private static ResourceKey<Biome> registerKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(CHEX.MOD_ID, name));
    }

    public static void register() {
        // Register biomes with the deferred register
        register(KEPLER_FOREST, () -> 
            KeplerBiomes.keplerForest(
                HolderGetter.direct(), // Placeholder - will be replaced by bootstrap
                HolderGetter.direct()  // Placeholder - will be replaced by bootstrap
            )
        );
        
        register(KEPLER_RIVER, () ->
            KeplerBiomes.keplerRiver(
                HolderGetter.direct(),
                HolderGetter.direct()
            )
        );
        
        register(KEPLER_PLAINS, () ->
            KeplerBiomes.keplerPlains(
                HolderGetter.direct(),
                HolderGetter.direct()
            )
        );
    }
    
    private static void register(ResourceKey<Biome> key, Supplier<Biome> biomeSupplier) {
        ModRegistries.BIOMES.register(key.location().getPath(), biomeSupplier);
    }
    
    public static void bootstrap(BootstapContext<Biome> context) {
        // This will be called with the proper context during data generation
        KeplerBiomes.bootstrap(context);
    }
}
