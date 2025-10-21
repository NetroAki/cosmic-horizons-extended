package com.netroaki.chex.world.eden.features.flora;

import com.netroaki.chex.registry.CHEXBlocks;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EdenFloraBlocks {
  public static final DeferredRegister<Block> BLOCKS = CHEXBlocks.BLOCKS;
  public static final DeferredRegister<Item> ITEMS = CHEXBlocks.ITEMS;

  // Celestial Bloom - Glowing plant that emits light and drops glowstone
  public static final RegistryObject<Block> CELESTIAL_BLOOM =
      registerWithItem(
          "celestial_bloom",
          () ->
              new CelestialBloomBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_PURPLE)
                      .noCollission()
                      .randomTicks()
                      .instabreak()
                      .sound(SoundType.GRASS)
                      .lightLevel(
                          state -> state.getValue(EdenPlantBlock.AGE) * 3) // Brighter as it grows
                  ));

  // More plant types can be added here following the same pattern

  private static <T extends Block> RegistryObject<T> registerWithItem(
      String name, Supplier<T> blockSupplier) {
    RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
    ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    return block;
  }

  public static void register() {
    // Static initializer will register all blocks
  }
}
