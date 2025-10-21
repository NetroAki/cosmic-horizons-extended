package com.netroaki.chex.block.inferno;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InfernoBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CosmicHorizonsExpanded.MOD_ID);

  // Magma Geyser Blocks
  public static final RegistryObject<Block> MAGMA_GEYSER =
      registerBlock(
          "magma_geyser",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(3.5f, 10.0f)
                      .lightLevel(state -> 7)
                      .requiresCorrectToolForDrops()));

  // Obsidian Flora
  public static final RegistryObject<Block> OBSIDIAN_SPIKE =
      registerBlock(
          "obsidian_spike",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)
                      .strength(25.0f, 1200.0f)
                      .lightLevel(state -> 3)));

  public static final RegistryObject<Block> OBSIDIAN_THORN_VINES =
      registerBlock(
          "obsidian_thorn_vines",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.TWISTING_VINES)
                      .strength(20.0f, 1000.0f)
                      .lightLevel(state -> 2)));

  // Ash Blocks
  public static final RegistryObject<Block> COMPACTED_ASH =
      registerBlock(
          "compacted_ash",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(0.5f, 0.5f)
                      .friction(0.8f)));

  public static final RegistryObject<Block> ASHEN_SOIL =
      registerBlock(
          "ashen_soil",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(0.7f, 0.7f)
                      .friction(0.9f)));

  // Helper method to register blocks with items
  private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn);
    return toReturn;
  }

  private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
    DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, CosmicHorizonsExpanded.MOD_ID);
    ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
  }

  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
