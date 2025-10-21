package com.netroaki.chex.registry.blocks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.blocks.HollowWorldBlock;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HollowWorldBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);

  // Glowstone Fungi
  public static final RegistryObject<Block> GLOWSTONE_FUNGI =
      registerBlock(
          "glowstone_fungi",
          () ->
              new HollowWorldBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLUE)
                      .lightLevel(state -> 15)
                      .strength(0.2f)
                      .sound(SoundType.FUNGUS)
                      .noOcclusion()));

  // Void Stone
  public static final RegistryObject<Block> VOID_STONE =
      registerBlock(
          "void_stone",
          () ->
              new HollowWorldBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(3.0f, 10.0f)
                      .sound(SoundType.STONE)
                      .lightLevel(state -> 3)
                      .noOcclusion()));

  // Stalactite Variants
  public static final RegistryObject<Block> STALACTITE =
      registerBlock(
          "stalactite",
          () ->
              new HollowWorldBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.STONE)
                      .strength(1.5f, 3.0f)
                      .sound(SoundType.CALCITE)
                      .noOcclusion()
                      .pushReaction(PushReaction.DESTROY)));

  // River Biolum Flora
  public static final RegistryObject<Block> LUMINOUS_REED =
      registerBlock(
          "luminous_reed",
          () ->
              new HollowWorldBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_CYAN)
                      .lightLevel(state -> 12)
                      .strength(0.0f)
                      .sound(SoundType.WET_GRASS)
                      .noCollission()));

  // Crystal Formations
  public static final RegistryObject<Block> CRYSTAL_FORMATION =
      registerBlock(
          "crystal_formation",
          () ->
              new HollowWorldBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_PURPLE)
                      .lightLevel(state -> 8)
                      .strength(2.0f, 4.0f)
                      .sound(SoundType.AMETHYST)
                      .noOcclusion()));

  private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn);
    return toReturn;
  }

  private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
    // Block items will be registered separately
  }

  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
