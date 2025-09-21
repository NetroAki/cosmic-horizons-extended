package com.netroaki.chex.registry.blocks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.blocks.RingworldWallBlock;
import com.netroaki.chex.registry.fluids.CHEXFluids;
import com.netroaki.chex.ring.block.ArcSceneryBlock;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXBlocks {

  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, CHEX.MOD_ID);

  private static final List<RegistryObject<Block>> FALLBACK_ORE_BLOCKS = new ArrayList<>();

  // Rocket Assembly and Fuel Processing Blocks
  public static final RegistryObject<Block> ROCKET_ASSEMBLY_TABLE =
      BLOCKS.register(
          "rocket_assembly_table",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.ANVIL).strength(3.5f)));

  public static final RegistryObject<Block> FUEL_REFINERY =
      BLOCKS.register(
          "fuel_refinery",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(5.0f)));

  // Arc scenery anchor (client-rendered ring arc)
  public static final RegistryObject<Block> ARC_SCENERY =
      BLOCKS.register(
          "arc_scenery",
          () -> new ArcSceneryBlock(BlockBehaviour.Properties.of().noOcclusion().noCollission()));

  // Placeholder surface blocks per-dimension/biome
  public static final RegistryObject<Block> ARRAKIS_SAND =
      BLOCKS.register(
          "arrakis_sand",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND).strength(0.5f)));

  public static final RegistryObject<Block> INFERNO_STONE =
      BLOCKS.register(
          "inferno_stone",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(1.5f)));

  public static final RegistryObject<Block> CRYSTALIS_CRYSTAL =
      BLOCKS.register(
          "crystalis_crystal",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(1.0f)));

  public static final RegistryObject<Block> AQUA_MUNDUS_STONE =
      BLOCKS.register(
          "aqua_mundus_stone",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.PRISMARINE).strength(1.0f)));

  public static final RegistryObject<Block> PANDORA_GRASS =
      BLOCKS.register(
          "pandora_grass",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).strength(0.6f)));

  public static final RegistryObject<Block> NEUTRON_STAR_BASALT =
      BLOCKS.register(
          "neutron_star_basalt",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).strength(2.0f)));

  // Secondary placeholders per world
  public static final RegistryObject<Block> ARRAKIS_ROCK =
      BLOCKS.register(
          "arrakis_rock",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(0.8f)));
  public static final RegistryObject<Block> INFERNO_ASH =
      BLOCKS.register(
          "inferno_ash",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERRACK).strength(0.6f)));
  public static final RegistryObject<Block> CRYSTALIS_CLEAR =
      BLOCKS.register(
          "crystalis_clear",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(0.3f)));
  public static final RegistryObject<Block> AQUA_DARK_PRISM =
      BLOCKS.register(
          "aqua_dark_prism",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.DARK_PRISMARINE).strength(1.2f)));
  public static final RegistryObject<Block> PANDORA_BLOOM =
      BLOCKS.register(
          "pandora_bloom",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).strength(0.5f)));
  public static final RegistryObject<Block> NEUTRON_STAR_PLATE =
      BLOCKS.register(
          "neutron_star_plate",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).strength(2.5f)));

  // Arrakis base blocks
  public static final RegistryObject<Block> ARRAKITE_SANDSTONE =
      BLOCKS.register(
          "arrakite_sandstone",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.2f)));
  public static final RegistryObject<Block> ARRAKITE_SANDSTONE_CUT =
      BLOCKS.register(
          "arrakite_sandstone_cut",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.CUT_SANDSTONE).strength(1.2f)));
  public static final RegistryObject<Block> ARRAKITE_SANDSTONE_CHISELED =
      BLOCKS.register(
          "arrakite_sandstone_chiseled",
          () ->
              new Block(BlockBehaviour.Properties.copy(Blocks.CHISELED_SANDSTONE).strength(1.2f)));
  public static final RegistryObject<Block> ARRAKITE_SANDSTONE_SMOOTH =
      BLOCKS.register(
          "arrakite_sandstone_smooth",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.SMOOTH_SANDSTONE).strength(1.2f)));
  public static final RegistryObject<Block> ARRAKIS_SALT =
      BLOCKS.register(
          "arrakis_salt",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.CALCITE).strength(0.8f)));
  public static final RegistryObject<Block> SPICE_NODE =
      BLOCKS.register(
          "spice_node",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE)
                      .strength(1.0f)
                      .lightLevel(s -> 7)));

  // Pandora base blocks
  public static final RegistryObject<Block> PANDORITE_STONE =
      BLOCKS.register(
          "pandorite_stone",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.5f)));
  public static final RegistryObject<Block> PANDORITE_COBBLED =
      BLOCKS.register(
          "pandorite_cobbled",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE).strength(1.5f)));
  public static final RegistryObject<Block> PANDORITE_BRICKS =
      BLOCKS.register(
          "pandorite_bricks",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).strength(1.5f)));
  public static final RegistryObject<Block> PANDORITE_MOSSY =
      BLOCKS.register(
          "pandorite_mossy",
          () ->
              new Block(BlockBehaviour.Properties.copy(Blocks.MOSSY_STONE_BRICKS).strength(1.5f)));
  public static final RegistryObject<Block> PANDORITE_POLISHED =
      BLOCKS.register(
          "pandorite_polished",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.POLISHED_ANDESITE).strength(1.5f)));
  public static final RegistryObject<Block> SPORE_SOIL =
      BLOCKS.register(
          "spore_soil",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).strength(0.5f)));
  public static final RegistryObject<Block> BIOLUME_MOSS =
      BLOCKS.register(
          "biolume_moss",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK)
                      .strength(0.4f)
                      .lightLevel(s -> 7)));
  public static final RegistryObject<Block> LUMICORAL_BLOCK =
      BLOCKS.register(
          "lumicoral_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.PRISMARINE)
                      .strength(0.6f)
                      .lightLevel(s -> 10)));

  // Fallback ore blocks for GTCEu-less deployments
  public static final RegistryObject<Block> FALLBACK_BAUXITE_ORE = registerFallbackOre("bauxite");
  public static final RegistryObject<Block> FALLBACK_ILMENITE_ORE = registerFallbackOre("ilmenite");
  public static final RegistryObject<Block> FALLBACK_LEAD_ORE = registerFallbackOre("lead");
  public static final RegistryObject<Block> FALLBACK_SILVER_ORE = registerFallbackOre("silver");
  public static final RegistryObject<Block> FALLBACK_LITHIUM_ORE = registerFallbackOre("lithium");
  public static final RegistryObject<Block> FALLBACK_TITANIUM_ORE = registerFallbackOre("titanium");
  public static final RegistryObject<Block> FALLBACK_VANADIUM_ORE = registerFallbackOre("vanadium");
  public static final RegistryObject<Block> FALLBACK_TUNGSTEN_ORE = registerFallbackOre("tungsten");
  public static final RegistryObject<Block> FALLBACK_MOLYBDENUM_ORE =
      registerFallbackOre("molybdenum");
  public static final RegistryObject<Block> FALLBACK_CHROMIUM_ORE = registerFallbackOre("chromium");
  public static final RegistryObject<Block> FALLBACK_MAGNESIUM_ORE =
      registerFallbackOre("magnesium");
  public static final RegistryObject<Block> FALLBACK_PLATINUM_ORE = registerFallbackOre("platinum");
  public static final RegistryObject<Block> FALLBACK_IRIDIUM_ORE = registerFallbackOre("iridium");
  public static final RegistryObject<Block> FALLBACK_PALLADIUM_ORE =
      registerFallbackOre("palladium");
  public static final RegistryObject<Block> FALLBACK_NIOBIUM_ORE = registerFallbackOre("niobium");
  public static final RegistryObject<Block> FALLBACK_TANTALUM_ORE = registerFallbackOre("tantalum");
  public static final RegistryObject<Block> FALLBACK_URANIUM_ORE = registerFallbackOre("uranium");
  public static final RegistryObject<Block> FALLBACK_OSMIUM_ORE = registerFallbackOre("osmium");
  public static final RegistryObject<Block> FALLBACK_BERYLLIUM_ORE =
      registerFallbackOre("beryllium");
  public static final RegistryObject<Block> FALLBACK_FLUORITE_ORE = registerFallbackOre("fluorite");
  public static final RegistryObject<Block> FALLBACK_RUBY_ORE = registerFallbackOre("ruby");
  public static final RegistryObject<Block> FALLBACK_SAPPHIRE_ORE = registerFallbackOre("sapphire");
  public static final RegistryObject<Block> FALLBACK_DRACONIUM_ORE =
      registerFallbackOre("draconium");
  public static final RegistryObject<Block> FALLBACK_AWAKENED_DRACONIUM_ORE =
      registerFallbackOre("awakened_draconium");

  // Ringworld wall blocks
  public static final RegistryObject<Block> RINGWORLD_WALL =
      BLOCKS.register(
          "ringworld_wall",
          () ->
              new RingworldWallBlock(
                  BlockBehaviour.Properties.of()
                      .noOcclusion()
                      .noCollission()
                      .strength(-1.0f, 3600000.0f)
                      .lightLevel(state -> 15)));

  // Fluid blocks
  public static final RegistryObject<LiquidBlock> KEROSENE_FLUID_BLOCK =
      BLOCKS.register(
          "kerosene_fluid_block",
          () ->
              new LiquidBlock(
                  () -> CHEXFluids.KEROSENE.get(), BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> RP1_FLUID_BLOCK =
      BLOCKS.register(
          "rp1_fluid_block",
          () ->
              new LiquidBlock(
                  () -> CHEXFluids.RP1.get(), BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> LOX_FLUID_BLOCK =
      BLOCKS.register(
          "lox_fluid_block",
          () ->
              new LiquidBlock(
                  () -> CHEXFluids.LOX.get(), BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> LH2_FLUID_BLOCK =
      BLOCKS.register(
          "lh2_fluid_block",
          () ->
              new LiquidBlock(
                  () -> CHEXFluids.LH2.get(), BlockBehaviour.Properties.copy(Blocks.WATER)));

  // Aurelia ringworld wall - unbreakable, black
  public static final RegistryObject<Block> AURELIA_WALL =
      BLOCKS.register(
          "aurelia_wall", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK)));

  // Aurelia alien flora
  public static final RegistryObject<Block> AURELIAN_LOG =
      BLOCKS.register(
          "aurelian_log",
          () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
  public static final RegistryObject<Block> AURELIAN_LEAVES =
      BLOCKS.register(
          "aurelian_leaves",
          () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
  public static final RegistryObject<Block> AURELIAN_GRASS =
      BLOCKS.register(
          "aurelian_grass",
          () ->
              new Block(BlockBehaviour.Properties.copy(Blocks.GRASS).noCollission().instabreak()));

  private static RegistryObject<Block> registerFallbackOre(String material) {
    RegistryObject<Block> block =
        BLOCKS.register(
            "fallback_" + material + "_ore",
            () ->
                new DropExperienceBlock(
                    BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                        .strength(3.0f)
                        .requiresCorrectToolForDrops(),
                    UniformInt.of(1, 3)));
    FALLBACK_ORE_BLOCKS.add(block);
    return block;
  }

  // Register block items
  public static void registerBlockItems() {
    registerBlockItem(ROCKET_ASSEMBLY_TABLE);
    registerBlockItem(FUEL_REFINERY);
    registerBlockItem(AURELIA_WALL);
    registerBlockItem(ARC_SCENERY);
    registerBlockItem(ARRAKIS_SAND);
    registerBlockItem(INFERNO_STONE);
    registerBlockItem(CRYSTALIS_CRYSTAL);
    registerBlockItem(AQUA_MUNDUS_STONE);
    registerBlockItem(PANDORA_GRASS);
    registerBlockItem(NEUTRON_STAR_BASALT);
    registerBlockItem(ARRAKIS_ROCK);
    registerBlockItem(INFERNO_ASH);
    registerBlockItem(CRYSTALIS_CLEAR);
    registerBlockItem(AQUA_DARK_PRISM);
    registerBlockItem(PANDORA_BLOOM);
    registerBlockItem(NEUTRON_STAR_PLATE);
    registerBlockItem(ARRAKITE_SANDSTONE);
    registerBlockItem(ARRAKITE_SANDSTONE_CUT);
    registerBlockItem(ARRAKITE_SANDSTONE_CHISELED);
    registerBlockItem(ARRAKITE_SANDSTONE_SMOOTH);
    registerBlockItem(ARRAKIS_SALT);
    registerBlockItem(SPICE_NODE);
    registerBlockItem(PANDORITE_STONE);
    registerBlockItem(PANDORITE_COBBLED);
    registerBlockItem(PANDORITE_BRICKS);
    registerBlockItem(PANDORITE_MOSSY);
    registerBlockItem(PANDORITE_POLISHED);
    registerBlockItem(SPORE_SOIL);
    registerBlockItem(BIOLUME_MOSS);
    registerBlockItem(LUMICORAL_BLOCK);
    registerBlockItem(AURELIAN_LOG);
    registerBlockItem(AURELIAN_LEAVES);
    registerBlockItem(AURELIAN_GRASS);
    FALLBACK_ORE_BLOCKS.forEach(CHEXBlocks::registerBlockItem);
  }

  private static void registerBlockItem(RegistryObject<Block> block) {
    com.netroaki.chex.registry.items.CHEXItems.ITEMS.register(
        block.getId().getPath(),
        () ->
            new BlockItem(
                java.util.Objects.requireNonNull(
                    net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(block.getId())),
                new Item.Properties()));
  }
}
