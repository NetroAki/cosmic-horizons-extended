package com.netroaki.chex.registry.blocks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.blocks.flora.DesertShrubBlock;
import com.netroaki.chex.blocks.flora.IceReedBlock;
import com.netroaki.chex.blocks.flora.SpiceCactusBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.Function;

public class ArrakisFloraBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, CHEX.MOD_ID);

    // Spice Cactus
    public static final RegistryObject<Block> SPICE_CACTUS = registerBlock("spice_cactus",
        () -> new SpiceCactusBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .randomTicks()
            .strength(0.4F)
            .sound(SoundType.WOOL)
            .pushReaction(PushReaction.DESTROY)),
        block -> new BlockItem(block, new Item.Properties().tab(CHEX.CREATIVE_TAB)));
    
    // Ice Reeds
    public static final RegistryObject<Block> ICE_REEDS = registerBlock("ice_reeds",
        () -> new IceReedBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .randomTicks()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)),
        block -> new BlockItem(block, new Item.Properties().tab(CHEX.CREATIVE_TAB)));
    
    // Desert Shrub
    public static final RegistryObject<Block> DESERT_SHRUB = registerBlock("desert_shrub",
        () -> new DesertShrubBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_YELLOW)
            .randomTicks()
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)),
        block -> new BlockItem(block, new Item.Properties().tab(CHEX.CREATIVE_TAB)));
    
    // Register all blocks
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, b -> new BlockItem(b, new Item.Properties().tab(CHEX.CREATIVE_TAB)));
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
