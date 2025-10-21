package com.netroaki.chex.item;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.block.ModBlocks;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, CosmicHorizonsExpanded.MOD_ID);

  // Stellar Core - Drops from the Stellar Avatar boss
  public static final RegistryObject<Item> STELLAR_CORE =
      ITEMS.register("stellar_core", () -> new StellarCoreItem());

  // Aqua Mundus Armor Set
  public static final RegistryObject<Item> AQUA_MUNDUS_HELMET =
      ITEMS.register(
          "aqua_mundus_helmet",
          () ->
              new AquaMundusArmorItem(
                  ArmorItem.Type.HELMET,
                  new Item.Properties().stacksTo(1).durability(455).rarity(Rarity.RARE)));

  public static final RegistryObject<Item> AQUA_MUNDUS_CHESTPLATE =
      ITEMS.register(
          "aqua_mundus_chestplate",
          () ->
              new AquaMundusArmorItem(
                  ArmorItem.Type.CHESTPLATE,
                  new Item.Properties().stacksTo(1).durability(525).rarity(Rarity.RARE)));

  public static final RegistryObject<Item> AQUA_MUNDUS_LEGGINGS =
      ITEMS.register(
          "aqua_mundus_leggings",
          () ->
              new AquaMundusArmorItem(
                  ArmorItem.Type.LEGGINGS,
                  new Item.Properties().stacksTo(1).durability(500).rarity(Rarity.RARE)));

  public static final RegistryObject<Item> AQUA_MUNDUS_BOOTS =
      ITEMS.register(
          "aqua_mundus_boots",
          () ->
              new AquaMundusArmorItem(
                  ArmorItem.Type.BOOTS,
                  new Item.Properties().stacksTo(1).durability(430).rarity(Rarity.RARE)));

  // Cryo Monarch Drops
  public static final RegistryObject<Item> FROZEN_HEART =
      ITEMS.register(
          "frozen_heart",
          () ->
              new FrozenHeartItem(
                  new Item.Properties().stacksTo(1).durability(64).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> SHARDBLADE =
      ITEMS.register("shardblade", () -> new ShardbladeItem());

  public static final RegistryObject<Item> MONARCH_CORE =
      ITEMS.register("monarch_core", () -> new MonarchCoreItem());

  // Stormworld Boss Drops
  public static final RegistryObject<Item> STORMHEART =
      ITEMS.register(
          "stormheart", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> THUNDERMAUL =
      ITEMS.register(
          "thundermaul",
          () ->
              new SwordItem(
                  Tiers.NETHERITE,
                  6,
                  -3.2f,
                  new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> COLOSSUS_CORE =
      ITEMS.register(
          "colossus_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

  // Ringworld Boss Drops
  public static final RegistryObject<Item> PRIME_CORE =
      ITEMS.register(
          "prime_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> GUARDIANS_BLADE =
      ITEMS.register(
          "guardians_blade",
          () ->
              new SwordItem(
                  Tiers.NETHERITE,
                  7,
                  -3.1f,
                  new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> NANO_CIRCUIT_CORE =
      ITEMS.register(
          "nano_circuit_core",
          () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

  // Exotica Fauna Drops
  public static final RegistryObject<Item> PRISM_HIDE =
      ITEMS.register("prism_hide", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> SPECTRAL_FUR =
      ITEMS.register("spectral_fur", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> RESONANT_BONE =
      ITEMS.register(
          "resonant_bone", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> LIGHT_ESSENCE =
      ITEMS.register("light_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final RegistryObject<Item> FRACTAL_DUST =
      ITEMS.register("fractal_dust", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> PRISMATIC_WING =
      ITEMS.register("prismatic_wing", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

  // Torus World Drops
  public static final RegistryObject<Item> ALLOY_HORNS =
      ITEMS.register("alloy_horns", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> SOLAR_SCALES =
      ITEMS.register("solar_scales", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> CIRCUIT_FRAGMENTS =
      ITEMS.register(
          "circuit_fragments", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> PULSE_ESSENCE =
      ITEMS.register("pulse_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final RegistryObject<Item> NULL_ESSENCE =
      ITEMS.register("null_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

  // Torus Warden Boss Rewards
  public static final RegistryObject<Item> TORUS_CORE =
      ITEMS.register(
          "torus_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> WARDENS_RINGBLADE =
      ITEMS.register(
          "wardens_ringblade",
          () ->
              new SwordItem(
                  Tiers.NETHERITE,
                  7,
                  -3.0f,
                  new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> NULL_CIRCUIT_CORE =
      ITEMS.register(
          "null_circuit_core",
          () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

  // Exotica Boss Rewards
  public static final RegistryObject<Item> PRISM_CORE =
      ITEMS.register(
          "prism_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
  public static final RegistryObject<Item> RESONANT_CORE =
      ITEMS.register(
          "resonant_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
  public static final RegistryObject<Item> QUANTUM_CORE =
      ITEMS.register(
          "quantum_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
  public static final RegistryObject<Item> FRACTAL_CORE =
      ITEMS.register(
          "fractal_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
  public static final RegistryObject<Item> SERAPH_CORE =
      ITEMS.register(
          "seraph_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
  public static final RegistryObject<Item> EXOTIC_HEART =
      ITEMS.register(
          "exotic_heart", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
  public static final RegistryObject<Item> FRACTABLADE =
      ITEMS.register(
          "fractablade",
          () ->
              new SwordItem(
                  Tiers.NETHERITE,
                  7,
                  -3.0f,
                  new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
  public static final RegistryObject<Item> BREAKER_CORE =
      ITEMS.register(
          "breaker_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  // Hollow World Drops
  public static final RegistryObject<Item> SPORE_GLAND =
      ITEMS.register("spore_gland", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> BAT_OIL =
      ITEMS.register("bat_oil", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> VOID_ESSENCE =
      ITEMS.register("void_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final RegistryObject<Item> CHASM_SCALES =
      ITEMS.register("chasm_scales", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> CRYSTAL_DUST =
      ITEMS.register("crystal_dust", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> PRISM_PELT =
      ITEMS.register("prism_pelt", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> ROOT_SILK =
      ITEMS.register("root_silk", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> STALKER_FANG =
      ITEMS.register("stalker_fang", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> SERPENT_OIL =
      ITEMS.register("serpent_oil", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

  // Hollow Tyrant Boss Rewards
  public static final RegistryObject<Item> HOLLOW_HEART =
      ITEMS.register(
          "hollow_heart", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> TYRANT_FANGBLADE =
      ITEMS.register(
          "tyrant_fangblade",
          () ->
              new SwordItem(
                  Tiers.NETHERITE,
                  7,
                  -3.0f,
                  new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> TYRANT_CORE =
      ITEMS.register(
          "tyrant_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

  // Dyson Swarm Drops
  public static final RegistryObject<Item> PHOTON_SCALES =
      ITEMS.register(
          "photon_scales", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> PANEL_SHARDS =
      ITEMS.register("panel_shards", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> NODE_ESSENCE =
      ITEMS.register("node_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final RegistryObject<Item> CIRCUIT_PARTS =
      ITEMS.register(
          "circuit_parts", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> SCAFFOLD_SCALES =
      ITEMS.register(
          "scaffold_scales", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> DRONE_ALLOY =
      ITEMS.register("drone_alloy", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> RAD_ESSENCE =
      ITEMS.register("rad_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final RegistryObject<Item> HOUND_FANGS =
      ITEMS.register("hound_fangs", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> SIGNAL_DUST =
      ITEMS.register("signal_dust", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> RELAY_SHARDS =
      ITEMS.register("relay_shards", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

  // Dyson Apex Boss Rewards
  public static final RegistryObject<Item> APEX_CORE =
      ITEMS.register(
          "apex_core", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> DYSON_SPEAR =
      ITEMS.register(
          "dyson_spear",
          () ->
              new SwordItem(
                  Tiers.NETHERITE,
                  8,
                  -2.8f,
                  new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> QUANTUM_LENS =
      ITEMS.register(
          "quantum_lens", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

  // Neutron Star Forge Drops
  public static final RegistryObject<Item> PLASMA_ORGAN =
      ITEMS.register("plasma_organ", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> ACCRETED_CORE =
      ITEMS.register("accreted_core", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final RegistryObject<Item> MAGNETAR_SCALES =
      ITEMS.register(
          "magnetar_scales", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> STORM_FRAGMENT =
      ITEMS.register(
          "storm_fragment", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> ALLOY_CHIPS =
      ITEMS.register("alloy_chips", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> MOLTEN_ORGAN =
      ITEMS.register("molten_organ", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> GRAVITY_ORGAN =
      ITEMS.register("gravity_organ", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final RegistryObject<Item> LEECH_EXTRACT =
      ITEMS.register(
          "leech_extract", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> SHIELD_CIRCUIT =
      ITEMS.register(
          "shield_circuit", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
  public static final RegistryObject<Item> RADIANT_ESSENCE =
      ITEMS.register("radiant_essence", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

  // Forge Star Sovereign Boss Rewards
  public static final RegistryObject<Item> SOVEREIGN_HEART =
      ITEMS.register(
          "sovereign_heart", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> NEUTRON_EDGE =
      ITEMS.register(
          "neutron_edge",
          () ->
              new SwordItem(
                  Tiers.NETHERITE,
                  9,
                  -2.6f,
                  new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> FORGE_MATRIX_CORE =
      ITEMS.register(
          "forge_matrix_core",
          () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

  public static void register(IEventBus eventBus) {
    // Register block items first
    ModBlocks.register(eventBus);

    // Then register regular items
    ITEMS.register(eventBus);
  }
}
