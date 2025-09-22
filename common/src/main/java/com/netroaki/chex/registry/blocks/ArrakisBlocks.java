package com.netroaki.chex.registry.blocks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.blocks.ArrakisBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.Function;

public class ArrakisBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, CHEX.MOD_ID);

    // Arrakite Sandstone Variants
    public static final RegistryObject<Block> ARRAKITE_SANDSTONE = registerBlock("arrakite_sandstone",
        () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.SAND)
            .strength(0.8f, 0.8f)
            .sound(SoundType.STONE)));
            
    public static final RegistryObject<Block> CHISELED_ARRAKITE_SANDSTONE = registerBlock("chiseled_arrakite_sandstone",
        () -> new Block(BlockBehaviour.Properties.copy(ARRAKITE_SANDSTONE.get())));
        
    public static final RegistryObject<Block> SMOOTH_ARRAKITE_SANDSTONE = registerBlock("smooth_arrakite_sandstone",
        () -> new Block(BlockBehaviour.Properties.copy(ARRAKITE_SANDSTONE.get())));
    
    // Spice Node
    public static final RegistryObject<Block> SPICE_NODE = registerBlock("spice_node",
        () -> new Block(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_ORANGE)
            .strength(0.5f)
            .lightLevel(state -> 5)
            .sound(SoundType.SAND)));
    
    // Crystalline Salt
    public static final RegistryObject<Block> CRYSTALLINE_SALT = registerBlock("crystalline_salt",
        () -> new Block(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.QUARTZ)
            .strength(0.5f)
            .friction(0.98f)
            .sound(SoundType.GLASS)));
    
    // Ash Stone
    public static final RegistryObject<Block> ASH_STONE = registerBlock("ash_stone",
        () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
            .strength(1.5f, 6.0f)
            .sound(SoundType.STONE)));
    
    // Dune Glass
    public static final RegistryObject<Block> DUNE_GLASS = registerBlock("dune_glass",
        () -> new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS, MaterialColor.SAND)
            .strength(0.3f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .isSuffocating((state, world, pos) -> false)
            .isViewBlocking((state, world, pos) -> false)));
    
    // Sietchrock
    public static final RegistryObject<Block> SIETCHROCK = registerBlock("sietchrock",
        () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN)
            .strength(2.0f, 10.0f)
            .sound(SoundType.STONE)));
    
    // Register all blocks
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, block1 -> new BlockItem(block1, new Item.Properties().tab(CHEX.CREATIVE_TAB)));
    }
    
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<T>, Item> item) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> item.apply(toReturn));
        return toReturn;
    }
    
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
