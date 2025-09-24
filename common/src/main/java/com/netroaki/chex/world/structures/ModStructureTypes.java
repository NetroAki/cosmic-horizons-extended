package com.netroaki.chex.world.structures;

import com.mojang.serialization.Codec;
import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureTypes {
        public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister
                        .create(Registries.STRUCTURE_TYPE, CosmicHorizonsExpanded.MOD_ID);

        public static final RegistryObject<StructureType<SolarCollectorStructure>> SOLAR_COLLECTOR = register(
                        "solar_collector", SolarCollectorStructure.CODEC);

        public static final RegistryObject<StructureType<FluxPylonStructure>> FLUX_PYLON = register("flux_pylon",
                        FluxPylonStructure.CODEC);

        public static final RegistryObject<StructureType<CoronalLoopStructure>> CORONAL_LOOP = register("coronal_loop",
                        CoronalLoopStructure.CODEC);

        public static final RegistryObject<StructureType<OceanSovereignArenaStructure>> OCEAN_SOVEREIGN_ARENA = register(
                        "ocean_sovereign_arena", OceanSovereignArenaStructure.CODEC);

        private static <T extends Structure> RegistryObject<StructureType<T>> register(String name, Codec<T> codec) {
                return STRUCTURE_TYPES.register(name, () -> explicitStructureTypeTyping(codec));
        }

        // Helper method to create structure type
        private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> codec) {
                return () -> codec;
        }
}
