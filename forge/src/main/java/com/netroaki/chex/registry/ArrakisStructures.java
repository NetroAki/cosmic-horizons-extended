package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.structure.ShaiHuludArenaStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ArrakisStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = 
        DeferredRegister.create(Registries.STRUCTURE_TYPE, CHEX.MOD_ID);
    
    // Structure Type Registrations
    public static final RegistryObject<StructureType<ShaiHuludArenaStructure>> SHAI_HULUD_ARENA = 
        STRUCTURE_TYPES.register("shai_hulud_arena", 
            () -> () -> ShaiHuludArenaStructure.CODEC);
    
    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPES.register(eventBus);
    }
}
