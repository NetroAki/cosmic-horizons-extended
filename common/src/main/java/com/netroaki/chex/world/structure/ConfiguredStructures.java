package com.netroaki.chex.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

import java.util.List;

public class ConfiguredStructures {
    public static final ResourceKey<Structure> COLOSSUS_ARENA_KEY = createKey("colossus_arena");
    public static final ResourceKey<StructureSet> COLOSSUS_ARENA_SET = createSetKey("colossus_arena");

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation("chex", name));
    }
    
    private static ResourceKey<StructureSet> createSetKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation("chex", name));
    }

    public static void bootstrap(BootstapContext<Structure> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        
        // Configure the Colossus Arena structure
        context.register(COLOSSUS_ARENA_KEY, new ColossusArenaStructure(
            new Structure.StructureSettings(
                biomeRegistry.getOrThrow(BiomeTags.IS_FOREST), // Spawn in forest biomes
                // Add structure spacing and separation
                // This means the structure will attempt to generate every 32 chunks (512 blocks) with a minimum of 8 chunks (128 blocks) between structures
                // This is just an example; adjust as needed
                List.of(), // Empty list means no additional biomes
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.BEARD_THIN
            ),
            ConstantHeight.of(Heightmap.Types.WORLD_SURFACE_WG), // Spawn at surface level
            32 // Size of the structure in chunks
        ));
    }

    public static void bootstrapSet(BootstapContext<StructureSet> context) {
        // Get the structure we want to add to the set
        HolderGetter<Structure> structureRegistry = context.lookup(Registries.STRUCTURE);
        
        // Register the structure set for the Colossus Arena
        context.register(COLOSSUS_ARENA_SET, new StructureSet(
            structureRegistry.getOrThrow(COLOSSUS_ARENA_KEY),
            // This is the structure placement configuration
            // It determines how the structure is placed in the world
            new RandomSpreadStructurePlacement(
                32, // spacing (in chunks) between attempts to generate
                8,  // separation (in chunks) between structures
                RandomSpreadType.LINEAR,
                12345 // salt for the random number generator
            )
        ));
    }
}
