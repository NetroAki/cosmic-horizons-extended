package com.netroaki.chex.registry.blocks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.block.arrakis.DesertShrubBlock;
import com.netroaki.chex.block.arrakis.IceReedsBlock;
import com.netroaki.chex.block.arrakis.SpiceCactusBlock;
import com.netroaki.chex.block.crystal.CrystalBlock;
import com.netroaki.chex.block.crystal.CrystalCoreOreBlock;
import com.netroaki.chex.blocks.RingworldWallBlock;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
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

  // Crystalis Blocks
  public static final RegistryObject<Block> CRYSTAL_BLOCK =
      BLOCKS.register(
          "crystal_block",
          () ->
              new CrystalBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_CYAN)
                      .instrument(NoteBlockInstrument.HAT)
                      .requiresCorrectToolForDrops()
                      .strength(1.5F, 6.0F)
                      .lightLevel(state -> 5)
                      .noOcclusion()));

  // Crystal Core Ores
  public static final RegistryObject<Block> CRYSTAL_CORE_ORE =
      BLOCKS.register("crystal_core_ore", () -> new CrystalCoreOreBlock(1, 3));

  public static final RegistryObject<Block> DEEPSLATE_CRYSTAL_CORE_ORE =
      BLOCKS.register("deepslate_crystal_core_ore", () -> new CrystalCoreOreBlock(2, 4));

  public static final RegistryObject<Block> END_CRYSTAL_CORE_ORE =
      BLOCKS.register("end_crystal_core_ore", () -> new CrystalCoreOreBlock(3, 5));

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

  public static final RegistryObject<Block> PANDORITE_STONE =
      BLOCKS.register(
          "pandorite_stone",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(1.3f)));

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
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.MOSSY_STONE_BRICKS).strength(1.5f)));

  public static final RegistryObject<Block> PANDORITE_POLISHED =
      BLOCKS.register(
          "pandorite_polished",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.POLISHED_ANDESITE).strength(1.5f)));

  // Kepler-452b Blocks (temperate Earth-like planet)
  public static final RegistryObject<Block> KEPLER_WOOD_LOG =
      BLOCKS.register(
          "kepler_wood_log",
          () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));

  public static final RegistryObject<Block> KEPLER_WOOD_LEAVES =
      BLOCKS.register(
          "kepler_wood_leaves",
          () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

  public static final RegistryObject<Block> KEPLER_MOSS =
      BLOCKS.register(
          "kepler_moss",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET).noCollission().instabreak()));

  public static final RegistryObject<Block> KEPLER_VINES =
      BLOCKS.register(
          "kepler_vines",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.VINE).noCollission().instabreak()));

  public static final RegistryObject<Block> KEPLER_BLOSSOM =
      BLOCKS.register(
          "kepler_blossom",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.FLOWERING_AZALEA_LEAVES)
                      .lightLevel(state -> 3)));

  // Aqua Mundus Blocks (ocean world)
  public static final RegistryObject<Block> AQUA_VENT_BASALT =
      BLOCKS.register(
          "aqua_vent_basalt",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.BASALT)
                      .strength(1.2f)
                      .lightLevel(state -> 5)));

  public static final RegistryObject<Block> AQUA_MANGANESE_NODULE =
      BLOCKS.register(
          "aqua_manganese_nodule",
          () ->
              new DropExperienceBlock(
                  BlockBehaviour.Properties.copy(Blocks.IRON_ORE).strength(2.0f),
                  UniformInt.of(2, 5)));

  public static final RegistryObject<Block> AQUA_LUMINOUS_KELP =
      BLOCKS.register(
          "aqua_luminous_kelp",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.KELP).lightLevel(state -> 8)));

  public static final RegistryObject<Block> AQUA_ICE_SHELF_SLAB =
      BLOCKS.register(
          "aqua_ice_shelf_slab",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.PACKED_ICE).strength(0.8f)));

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

  // Spice Mine blocks
  public static final RegistryObject<Block> SPICE_DEPOSIT =
      BLOCKS.register(
          "spice_deposit",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)
                      .strength(1.2f)
                      .lightLevel(s -> 4)
                      .sound(SoundType.AMETHYST)));

  public static final RegistryObject<Block> CRYSTALLINE_SALT_CLUSTER =
      BLOCKS.register(
          "crystalline_salt_cluster",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK)
                      .strength(0.8f)
                      .sound(SoundType.AMETHYST_CLUSTER)));

  // Sietch Stronghold blocks
  public static final RegistryObject<Block> SIETCH_STONE =
      BLOCKS.register(
          "sietch_stone",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).strength(1.8f)));

  public static final RegistryObject<Block> SIETCH_STONE_BRICKS =
      BLOCKS.register(
          "sietch_stone_bricks",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).strength(2.0f)));

  public static final RegistryObject<Block> SIETCH_STONE_CHISELED =
      BLOCKS.register(
          "sietch_stone_chiseled",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.CHISELED_STONE_BRICKS).strength(2.0f)));

  // Stormlands blocks
  // Arrakis Flora
  public static final RegistryObject<Block> SPICE_CACTUS =
      BLOCKS.register(
          "spice_cactus",
          () ->
              new SpiceCactusBlock(
                  BlockBehaviour.Properties.copy(Blocks.CACTUS)
                      .sound(SoundType.WOOL) // Softer sound than normal cactus
                      .lightLevel(state -> state.getValue(SpiceCactusBlock.AGE) == 3 ? 3 : 0)));

  public static final RegistryObject<Block> ICE_REEDS =
      BLOCKS.register(
          "ice_reeds",
          () ->
              new IceReedsBlock(
                  BlockBehaviour.Properties.copy(Blocks.SUGAR_CANE)
                      .sound(SoundType.GLASS) // Icy sound
                      .lightLevel(state -> 2) // Subtle glow
                  ));

  public static final RegistryObject<Block> DESERT_SHRUB =
      BLOCKS.register(
          "desert_shrub",
          () ->
              new DesertShrubBlock(
                  BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH)
                      .sound(SoundType.GRASS)
                      .noOcclusion()));

  // Stormlands blocks
  public static final RegistryObject<Block> STORMGLASS =
      BLOCKS.register(
          "stormglass",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.GLASS)
                      .strength(0.8f)
                      .sound(SoundType.GLASS)
                      .isValidSpawn((state, world, pos, type) -> false)));

  public static final RegistryObject<Block> HARDENED_DUNE_SAND =
      BLOCKS.register(
          "hardened_dune_sand",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).strength(1.5f)));

  public static final RegistryObject<Block> SANDSTORM_GLASS =
      BLOCKS.register(
          "sandstorm_glass",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.TINTED_GLASS)
                      .strength(1.0f)
                      .isValidSpawn((state, world, pos, type) -> false)));

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

  // Crystal-clad Pandorite variants
  public static final RegistryObject<Block> CRYSTAL_CLAD_PANDORITE =
      BLOCKS.register(
          "crystal_clad_pandorite",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)
                      .strength(2.0f, 6.0f)
                      .lightLevel(s -> 4)));

  public static final RegistryObject<Block> CRYSTAL_CLAD_PANDORITE_BRICKS =
      BLOCKS.register(
          "crystal_clad_pandorite_bricks",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)
                      .strength(2.0f, 6.0f)
                      .lightLevel(s -> 4)));

  public static final RegistryObject<Block> CRYSTAL_CLAD_PANDORITE_POLISHED =
      BLOCKS.register(
          "crystal_clad_pandorite_polished",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK)
                      .strength(2.0f, 6.0f)
                      .lightLevel(s -> 4)));

  // Volcanic blocks for Pandora's volcanic wasteland
  public static final RegistryObject<Block> VOLCANIC_BASALT =
      BLOCKS.register(
          "volcanic_basalt",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.BASALT).strength(1.5f, 6.0f)));

  public static final RegistryObject<Block> VOLCANIC_ASH =
      BLOCKS.register(
          "volcanic_ash",
          () -> new Block(BlockBehaviour.Properties.copy(Blocks.SOUL_SAND).strength(0.5f)));

  public static final RegistryObject<Block> OBSIDIAN_GLASS =
      BLOCKS.register(
          "obsidian_glass",
          () ->
              new Block(
                  BlockBehaviour.Properties.copy(Blocks.GLASS)
                      .strength(3.0f, 1200.0f)
                      .noOcclusion()
                      .isValidSpawn((state, world, pos, type) -> false)
                      .isRedstoneConductor((state, world, pos) -> false)
                      .isSuffocating((state, world, pos) -> false)
                      .isViewBlocking((state, world, pos) -> false)));

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
                  () -> com.netroaki.chex.registry.fluids.CHEXFluids.KEROSENE.get(),
                  BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> RP1_FLUID_BLOCK =
      BLOCKS.register(
          "rp1_fluid_block",
          () ->
              new LiquidBlock(
                  () -> com.netroaki.chex.registry.fluids.CHEXFluids.RP1.get(),
                  BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> LOX_FLUID_BLOCK =
      BLOCKS.register(
          "lox_fluid_block",
          () ->
              new LiquidBlock(
                  () -> com.netroaki.chex.registry.fluids.CHEXFluids.LOX.get(),
                  BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> LH2_FLUID_BLOCK =
      BLOCKS.register(
          "lh2_fluid_block",
          () ->
              new LiquidBlock(
                  () -> com.netroaki.chex.registry.fluids.CHEXFluids.LH2.get(),
                  BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> DT_MIX_FLUID_BLOCK =
      BLOCKS.register(
          "dt_mix_fluid_block",
          () ->
              new LiquidBlock(
                  () -> com.netroaki.chex.registry.fluids.CHEXFluids.DT_MIX.get(),
                  BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> HE3_BLEND_FLUID_BLOCK =
      BLOCKS.register(
          "he3_blend_fluid_block",
          () ->
              new LiquidBlock(
                  () -> com.netroaki.chex.registry.fluids.CHEXFluids.HE3_BLEND.get(),
                  BlockBehaviour.Properties.copy(Blocks.WATER)));

  public static final RegistryObject<LiquidBlock> EXOTIC_MIX_FLUID_BLOCK =
      BLOCKS.register(
          "exotic_mix_fluid_block",
          () ->
              new LiquidBlock(
                  () -> com.netroaki.chex.registry.fluids.CHEXFluids.EXOTIC_MIX.get(),
                  BlockBehaviour.Properties.copy(Blocks.WATER)));

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
      registerBlockItem(PANDORITE_STONE);
      registerBlockItem(PANDORITE_COBBLED);
      registerBlockItem(PANDORITE_BRICKS);
      registerBlockItem(PANDORITE_MOSSY);
      registerBlockItem(PANDORITE_POLISHED);
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
    registerBlockItem(SPICE_DEPOSIT);
    registerBlockItem(CRYSTALLINE_SALT_CLUSTER);
    registerBlockItem(SIETCH_STONE);
    registerBlockItem(SIETCH_STONE_BRICKS);
    registerBlockItem(SIETCH_STONE_CHISELED);
    registerBlockItem(STORMGLASS);
    registerBlockItem(HARDENED_DUNE_SAND);
    registerBlockItem(SANDSTORM_GLASS);

    // Arrakis Flora
    registerBlockItem(SPICE_CACTUS);
    registerBlockItem(ICE_REEDS);
    registerBlockItem(DESERT_SHRUB);
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

    // Kepler-452b
    registerBlockItem(KEPLER_WOOD_LOG);
    registerBlockItem(KEPLER_WOOD_LEAVES);
    registerBlockItem(KEPLER_MOSS);
    registerBlockItem(KEPLER_VINES);
    registerBlockItem(KEPLER_BLOSSOM);

    // Aqua Mundus
    registerBlockItem(AQUA_VENT_BASALT);
    registerBlockItem(AQUA_MANGANESE_NODULE);
    registerBlockItem(AQUA_LUMINOUS_KELP);
    registerBlockItem(AQUA_ICE_SHELF_SLAB);

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
