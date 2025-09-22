package com.netroaki.chex.block;

import com.netroaki.chex.CHEX;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AquaBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);
    
    // Vent Basalt - A dark, porous rock found near hydrothermal vents
    public static final RegistryObject<Block> VENT_BASALT = registerBlock("vent_basalt",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .requiresCorrectToolForDrops()
            .strength(1.5F, 6.0F)
            .sound(SoundType.BASALT)
            .lightLevel(state -> 3) // Slight glow
    ));
    
    // Polished Vent Basalt - Smoother variant for building
    public static final RegistryObject<Block> POLISHED_VENT_BASALT = registerBlock("polished_vent_basalt",
        () -> new Block(BlockBehaviour.Properties.copy(VENT_BASALT.get())
            .strength(2.0F, 6.0F)
    ));
    
    // Manganese Nodule - Rare mineral deposit
    public static final RegistryObject<Block> MANGANESE_NODULE = registerBlock("manganese_nodule",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 3.0F)
            .sound(SoundType.DEEPSLATE)
            .pushReaction(PushReaction.DESTROY)
    ));
    
    // Luminous Kelp - Glowing underwater plant
    public static final RegistryObject<Block> LUMINOUS_KELP = registerBlock("luminous_kelp",
        () -> new KelpPlantBlock(BlockBehaviour.Properties.copy(Blocks.KELP_PLANT)
            .lightLevel(state -> 8)
            .noOcclusion()
            .noCollission()
            .instabreak()
            .sound(SoundType.WET_GRASS)
    ));
    
    public static final RegistryObject<LiquidBlock> LUMINOUS_KELP_FLUID = BLOCKS.register("luminous_kelp_fluid",
        () -> new LiquidBlock(ModFluids.SOURCE_LUMINOUS_KELP, BlockBehaviour.Properties.copy(Blocks.WATER)
            .lightLevel(state -> 10)
            .noCollission()
            .strength(100.0F)
            .noLootTable()
    ));
    
    // Ice Shelf - Thick, durable ice
    public static final RegistryObject<Block> ICE_SHELF = registerBlock("ice_shelf",
        () -> new IceBlock(BlockBehaviour.Properties.copy(Blocks.BLUE_ICE)
            .friction(0.989F)
            .mapColor(MapColor.ICE)
            .strength(0.5F)
    ));
    
    // Ice Shelf Slab
    public static final RegistryObject<SlabBlock> ICE_SHELF_SLAB = registerBlock("ice_shelf_slab",
        () -> new SlabBlock(BlockBehaviour.Properties.copy(ICE_SHELF.get()))
    );
    
    // Coral Formation - Decorative coral block
    public static final RegistryObject<Block> CORAL_FORMATION = registerBlock("coral_formation",
        () -> new CoralPlantBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PINK)
            .noOcclusion()
            .instabreak()
            .sound(SoundType.WET_GRASS)
            .lightLevel(state -> 4)
    ));
    
    // Helper method to register blocks with items
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), 
            new Item.Properties()
                .tab(CHEX.TAB)
        ));
    }
    
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
