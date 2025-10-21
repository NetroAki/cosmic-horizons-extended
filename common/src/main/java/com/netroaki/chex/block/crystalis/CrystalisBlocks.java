package com.netroaki.chex.block.crystalis;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrystalisBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CosmicHorizonsExpanded.MOD_ID);

  // Cryostone Variants
  public static final RegistryObject<Block> CRYOSTONE =
      registerBlock(
          "cryostone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.ICE)
                      .strength(1.5f, 6.0f)
                      .friction(0.98f)
                      .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> CRACKED_CRYOSTONE =
      registerBlock(
          "cracked_cryostone",
          () -> new Block(BlockBehaviour.Properties.copy(CRYOSTONE.get()).strength(1.2f, 5.0f)));

  public static final RegistryObject<Block> MOSSY_CRYOSTONE =
      registerBlock(
          "mossy_cryostone",
          () -> new Block(BlockBehaviour.Properties.copy(CRYOSTONE.get()).friction(0.99f)));

  public static final RegistryObject<Block> PERMAFROST =
      registerBlock(
          "permafrost",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.SNOW)
                      .strength(0.5f, 0.5f)
                      .friction(0.98f)));

  // Glacial Glass
  public static final RegistryObject<Block> GLACIAL_GLASS =
      registerBlock(
          "glacial_glass",
          () ->
              new IceBlock(
                  BlockBehaviour.Properties.copy(Blocks.ICE)
                      .lightLevel(state -> 5)
                      .friction(0.98f)
                      .noOcclusion()));

  // Low-Traction Slippery Ice (hazard)
  public static final RegistryObject<Block> SLIPPERY_ICE =
      registerBlock(
          "slippery_ice",
          () ->
              new SlipperyIceBlock(
                  BlockBehaviour.Properties.copy(Blocks.ICE)
                      .friction(0.989f)
                      .strength(0.5f)
                      .noOcclusion()));

  // Cryo Geyser
  public static final RegistryObject<Block> CRYO_GEYSER =
      registerBlock(
          "cryo_geyser",
          () ->
              new CryoGeyserBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.ICE)
                      .strength(2.0f, 3.0f)
                      .friction(0.98f)
                      .lightLevel(state -> state.getValue(CryoGeyserBlock.ACTIVE) ? 5 : 0)
                      .randomTicks()
                      .requiresCorrectToolForDrops()));

  // Geyser Stone & Frozen Vents
  public static final RegistryObject<Block> GEYSER_STONE =
      registerBlock(
          "geyser_stone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .strength(1.8f, 6.0f)
                      .requiresCorrectToolForDrops()));

  public static final RegistryObject<Block> FROZEN_VENT =
      registerBlock(
          "frozen_vent",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(GEYSER_STONE.get())
                      .lightLevel(state -> 3)
                      .emissiveRendering((state, level, pos) -> true)));

  // Pressure Crystals
  public static final RegistryObject<Block> PRESSURE_CRYSTAL_BLOCK =
      registerBlock(
          "pressure_crystal_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.DIAMOND)
                      .strength(3.0f, 12.0f)
                      .lightLevel(state -> 7)
                      .friction(0.99f)
                      .requiresCorrectToolForDrops()));

  // Prism Ice Spires
  public static final RegistryObject<Block> PRISM_ICE =
      registerBlock(
          "prism_ice",
          () ->
              new IceBlock(
                  BlockBehaviour.Properties.copy(Blocks.PACKED_ICE)
                      .lightLevel(state -> 3)
                      .friction(0.98f)
                      .noOcclusion()));

  // Crystal Lattice
  public static final RegistryObject<Block> CRYSTAL_LATTICE =
      registerBlock(
          "crystal_lattice",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.QUARTZ)
                      .strength(0.3f, 0.3f)
                      .lightLevel(state -> 11)
                      .noOcclusion()));

  // Helper method to register blocks and their items
  private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn);
    return toReturn;
  }

  private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
    // TODO: Register block items when CHEXItems is implemented
    // com.netroaki.chex.item.CHEXItems.ITEMS.register(
    // name,
    // () -> new BlockItem(
    // block.get(), new
    // Item.Properties().tab(com.netroaki.chex.item.CHEXItems.CHEX_TAB)));
  }

  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
