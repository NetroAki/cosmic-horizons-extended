package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.block.BioluminescentLeavesBlock;
import com.netroaki.chex.block.FloatingCrystalClusterBlock;
import com.netroaki.chex.block.FloatingStoneBlock;
import com.netroaki.chex.registry.items.CHEXItems;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);

  // Floating Islands Blocks
  public static final RegistryObject<Block> FLOATING_STONE =
      registerBlock(
          "floating_stone",
          () ->
              new FloatingStoneBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.STONE)
                      .strength(1.5f, 6.0f)
                      .sound(SoundType.STONE)
                      .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> FLOATING_CRYSTAL_CLUSTER =
      registerBlock(
          "floating_crystal_cluster",
          () ->
              new FloatingCrystalClusterBlock(
                  7,
                  3,
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_CYAN)
                      .noOcclusion()
                      .instabreak()
                      .sound(SoundType.AMETHYST_CLUSTER)
                      .lightLevel(state -> 10)
                      .noCollission()));

  public static final RegistryObject<Block> FLOATING_LOG =
      registerBlock(
          "floating_log",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .lightLevel(state -> 3)));

  public static final RegistryObject<Block> FLOATING_LEAVES =
      registerBlock(
          "floating_leaves",
          () ->
              new BioluminescentLeavesBlock(
                  BlockBehaviour.Properties.copy(Blocks.AZALEA_LEAVES)
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .lightLevel(state -> 8)));

  public static final RegistryObject<Block> BIOLUMINESCENT_LOG =
      registerBlock(
          "bioluminescent_log",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.OAK_LOG)
                      .mapColor(MapColor.COLOR_BLUE)
                      .lightLevel(state -> 4)));

  public static final RegistryObject<Block> BIOLUMINESCENT_LEAVES =
      registerBlock(
          "bioluminescent_leaves",
          () ->
              new BioluminescentLeavesBlock(
                  BlockBehaviour.Properties.copy(Blocks.AZALEA_LEAVES)
                      .mapColor(MapColor.COLOR_BLUE)
                      .lightLevel(state -> 10)));

  public static final RegistryObject<Block> PANDORA_GRASS_BLOCK =
      registerBlock(
          "pandora_grass_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)
                      .mapColor(MapColor.COLOR_CYAN)
                      .lightLevel(state -> 2)));

  // Helper method to register blocks
  private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn);
    return toReturn;
  }

  // Helper method to register block items
  private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
    CHEXItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
  }

  // Register all blocks with the mod event bus
  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
