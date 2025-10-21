package com.netroaki.chex.block.aqua_mundus;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Registry class for Aqua Mundus blocks. */
public class AquaMundusBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CosmicHorizonsExpanded.MOD_ID);
  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, CosmicHorizonsExpanded.MOD_ID);

  // Vent Basalt
  public static final RegistryObject<Block> VENT_BASALT =
      registerBlock(
          "vent_basalt",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.BASALT)
                      .strength(1.5f, 6.0f)
                      .sound(SoundType.STONE)
                      .lightLevel(state -> 3) // Slight glow
                  ));

  // Manganese Nodule
  public static final RegistryObject<Block> MANGANESE_NODULE =
      registerBlock(
          "manganese_nodule",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                      .strength(3.0f, 3.0f)
                      .sound(SoundType.METAL)));

  // Luminous Kelp (Block)
  public static final RegistryObject<Block> LUMINOUS_KELP =
      registerBlock(
          "luminous_kelp",
          () ->
              new LuminousKelpBlock(
                  BlockBehaviour.Properties.copy(Blocks.KELP)
                      .strength(0.2f)
                      .sound(SoundType.WET_GRASS)
                      .lightLevel(state -> 10) // Bright glow
                      .noOcclusion()));

  // Luminous Kelp Plant
  public static final RegistryObject<Block> LUMINOUS_KELP_PLANT =
      BLOCKS.register(
          "luminous_kelp_plant",
          () ->
              new LuminousKelpPlantBlock(
                  BlockBehaviour.Properties.copy(Blocks.KELP_PLANT)
                      .lightLevel(state -> 10)
                      .noOcclusion()));

  // Ice Shelf Slab
  public static final RegistryObject<Block> ICE_SHELF_SLAB =
      registerBlock(
          "ice_shelf_slab",
          () ->
              new IceShelfSlabBlock(
                  BlockBehaviour.Properties.copy(Blocks.PACKED_ICE)
                      .strength(0.5f, 0.5f)
                      .friction(0.98f)
                      .sound(SoundType.GLASS)));

  // Helper method to register blocks with items
  private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn);
    return toReturn;
  }

  private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
    ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
  }

  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
    ITEMS.register(eventBus);
  }
}
