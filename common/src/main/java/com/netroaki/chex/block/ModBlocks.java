package com.netroaki.chex.block;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.block.flora.*;
import com.netroaki.chex.block.stormworld.ReformingCloudBlock;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CosmicHorizonsExpanded.MOD_ID);

  // Crystalis Flora
  public static final RegistryObject<Block> CRYSTAL_BLOOM =
      registerBlock(
          "crystal_bloom",
          () -> new CrystalBloomBlock(),
          new Item.Properties().rarity(net.minecraft.world.item.Rarity.UNCOMMON));

  public static final RegistryObject<Block> FROST_MOSS =
      registerBlock("frost_moss", FrostMossBlock::new, new Item.Properties());

  public static final RegistryObject<Block> FROSTCAP =
      registerBlock("frostcap", FrostcapBlock::new, new Item.Properties());

  public static final RegistryObject<Block> ICICLE_SPIRE =
      registerBlock("icicle_spire", IcicleSpireBlock::new, new Item.Properties());

  public static final RegistryObject<Block> PRESSURE_SPONGE =
      registerBlock("pressure_sponge", PressureSpongeBlock::new, new Item.Properties());

  // Stormworld Blocks
  public static final RegistryObject<Block> STORMSTONE =
      registerBlock(
          "stormstone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_GRAY)
                      .strength(2.0f, 6.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> STORMSTONE_CONDENSED =
      registerBlock(
          "stormstone_condensed",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(4.0f, 8.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> REFORMING_CLOUD =
      registerBlock(
          "reforming_cloud",
          () ->
              new ReformingCloudBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.SNOW)
                      .noOcclusion()
                      .strength(0.2f)
                      .friction(0.9f)),
          new Item.Properties());

  public static final RegistryObject<Block> ION_SPIRE =
      registerBlock(
          "ion_spire",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(3.0f, 9.0f)
                      .lightLevel(s -> 4)),
          new Item.Properties());

  public static final RegistryObject<Block> FULGURITE_GLASS =
      registerBlock(
          "fulgurite_glass",
          () ->
              new GlassBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.SAND)
                      .strength(0.3f)
                      .noOcclusion()
                      .lightLevel(s -> 1)),
          new Item.Properties());

  public static final RegistryObject<Block> CHARGED_CRYSTAL =
      registerBlock(
          "charged_crystal",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .strength(1.0f, 3.0f)
                      .lightLevel(s -> 12)),
          new Item.Properties());

  public static final RegistryObject<Block> METALLIC_HYDROGEN =
      registerBlock(
          "metallic_hydrogen",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_GRAY)
                      .strength(0.5f)
                      .friction(0.99f)
                      .lightLevel(s -> 7)),
          new Item.Properties());

  // Exotica Blocks
  public static final RegistryObject<Block> EXOTITE_BLOCK =
      registerBlock(
          "exotite_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_PURPLE)
                      .strength(5.0f, 10.0f)
                      .lightLevel(s -> 4)),
          new Item.Properties());

  public static final RegistryObject<Block> EXOTITE_ORE =
      registerBlock(
          "exotite_ore",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_PURPLE)
                      .strength(3.0f, 6.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> RESONANCE_SAND =
      registerBlock(
          "resonance_sand",
          () ->
              new FallingBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.SAND)
                      .strength(0.5f)
                      .friction(0.8f)),
          new Item.Properties());

  public static final RegistryObject<Block> PRISM_FLORA =
      registerBlock(
          "prism_flora",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .noOcclusion()
                      .instabreak()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)),
          new Item.Properties());

  // Torus World Blocks
  public static final RegistryObject<Block> TORIUM_ALLOY_BLOCK =
      registerBlock(
          "torium_alloy_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_GRAY)
                      .strength(6.0f, 12.0f)
                      .lightLevel(s -> 3)),
          new Item.Properties());

  public static final RegistryObject<Block> TORIUM_ORE =
      registerBlock(
          "torium_ore",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_GRAY)
                      .strength(3.0f, 6.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> RADIANT_PANEL =
      registerBlock(
          "radiant_panel",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_YELLOW)
                      .noOcclusion()
                      .strength(1.0f, 3.0f)
                      .lightLevel(s -> 15)),
          new Item.Properties());

  // Hollow World Blocks
  public static final RegistryObject<Block> HOLLOWSTONE =
      registerBlock(
          "hollowstone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f, 6.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> BIOLUME_MOSS =
      registerBlock(
          "biolume_moss",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_GREEN)
                      .strength(0.5f)
                      .lightLevel(s -> 7)),
          new Item.Properties());

  public static final RegistryObject<Block> VOIDSTONE =
      registerBlock(
          "voidstone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(3.0f, 8.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> VOID_CRYSTAL =
      registerBlock(
          "void_crystal",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_PURPLE)
                      .strength(1.0f, 3.0f)
                      .lightLevel(s -> 12)),
          new Item.Properties());

  public static final RegistryObject<Block> CRYSTAL_BARK =
      registerBlock(
          "crystal_bark",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .strength(1.5f, 3.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> PRISM_STONE =
      registerBlock(
          "prism_stone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_CYAN)
                      .strength(1.5f, 4.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> STALACTITE =
      registerBlock(
          "stalactite",
          () ->
              new Block(
                  BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0f, 2.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> RIVERBED_STONE =
      registerBlock(
          "riverbed_stone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.TERRACOTTA_BROWN)
                      .strength(1.2f, 3.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> GLOWSHROOM =
      registerBlock(
          "glowshroom",
          () ->
              new BushBlock(
                  BlockBehaviour.Properties.of()
                      .noCollission()
                      .instabreak()
                      .lightLevel(s -> 9)
                      .mapColor(MapColor.COLOR_LIGHT_GREEN)),
          new Item.Properties());

  public static final RegistryObject<Block> SPORE_REEDS =
      registerBlock(
          "spore_reeds",
          () ->
              new BushBlock(
                  BlockBehaviour.Properties.of()
                      .noCollission()
                      .instabreak()
                      .mapColor(MapColor.PLANT)),
          new Item.Properties());

  // Dyson Swarm Blocks
  public static final RegistryObject<Block> DYSON_PANEL =
      registerBlock(
          "dyson_panel",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(2.0f, 6.0f)
                      .lightLevel(s -> 4)),
          new Item.Properties());

  public static final RegistryObject<Block> DAMAGED_DYSON_PANEL =
      registerBlock(
          "damaged_dyson_panel",
          () ->
              new Block(
                  BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(1.5f, 4.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> NODE_FRAGMENT =
      registerBlock(
          "node_fragment",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_GRAY)
                      .strength(2.0f, 5.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> CIRCUIT_BLOCK =
      registerBlock(
          "circuit_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_CYAN)
                      .strength(1.8f, 4.0f)
                      .lightLevel(s -> 6)),
          new Item.Properties());

  public static final RegistryObject<Block> SCAFFOLD_STRUT =
      registerBlock(
          "scaffold_strut",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_GRAY)
                      .noOcclusion()
                      .strength(1.2f, 3.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> ROTARY_NODE =
      registerBlock(
          "rotary_node",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .strength(2.4f, 6.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> SHADOW_PANEL =
      registerBlock(
          "shadow_panel",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_BLACK)
                      .strength(2.0f, 6.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> IRRADIATED_STONE =
      registerBlock(
          "irradiated_stone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_PURPLE)
                      .strength(2.2f, 6.0f)
                      .lightLevel(s -> 7)),
          new Item.Properties());

  public static final RegistryObject<Block> RELAY_NODE =
      registerBlock(
          "relay_node",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_GRAY)
                      .strength(2.6f, 7.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> SIGNAL_STRUT =
      registerBlock(
          "signal_strut",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_GRAY)
                      .noOcclusion()
                      .strength(1.4f, 3.0f)),
          new Item.Properties());

  // Neutron Star Forge Blocks
  public static final RegistryObject<Block> ACCRETION_ROCK =
      registerBlock(
          "accretion_rock",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.TERRACOTTA_BROWN)
                      .strength(2.4f, 8.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> PLASMA_GLASS =
      registerBlock(
          "plasma_glass",
          () ->
              new GlassBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .noOcclusion()
                      .strength(0.6f, 1.0f)
                      .lightLevel(s -> 12)),
          new Item.Properties());

  public static final RegistryObject<Block> MAGNETITE_ALLOY_BLOCK =
      registerBlock(
          "magnetite_alloy_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_GRAY)
                      .strength(4.0f, 10.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> CHARGE_CRYSTAL =
      registerBlock(
          "charge_crystal",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .strength(1.2f, 3.0f)
                      .lightLevel(s -> 13)),
          new Item.Properties());

  public static final RegistryObject<Block> FORGE_ALLOY_BLOCK =
      registerBlock(
          "forge_alloy_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_ORANGE)
                      .strength(4.5f, 12.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> FURNACE_NODE =
      registerBlock(
          "furnace_node",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_RED)
                      .strength(3.0f, 9.0f)
                      .lightLevel(s -> 10)),
          new Item.Properties());

  public static final RegistryObject<Block> NEUTRON_STONE =
      registerBlock(
          "neutron_stone",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_GRAY)
                      .strength(3.2f, 10.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> STRANGELET_BLOCK =
      registerBlock(
          "strangelet_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_PURPLE)
                      .strength(3.8f, 11.0f)
                      .lightLevel(s -> 8)),
          new Item.Properties());

  public static final RegistryObject<Block> RADIATION_ALLOY =
      registerBlock(
          "radiation_alloy",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_GREEN)
                      .strength(4.2f, 12.0f)),
          new Item.Properties());

  public static final RegistryObject<Block> SHIELD_PANEL =
      registerBlock(
          "shield_panel",
          () ->
              new Block(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_CYAN)
                      .strength(2.8f, 8.0f)),
          new Item.Properties());

  // Helper method to register blocks with their items
  private static <T extends Block> RegistryObject<T> registerBlock(
      String name, Supplier<T> block, Item.Properties itemProperties) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn, itemProperties);
    return toReturn;
  }

  private static <T extends Block> void registerBlockItem(
      String name, RegistryObject<T> block, Item.Properties properties) {
    // TODO: Register block items when ModItems is implemented
    // ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
  }

  public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
