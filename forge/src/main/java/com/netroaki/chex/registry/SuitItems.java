package com.netroaki.chex.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;

public final class SuitItems {

  // Suit Tier 1 - Basic Space Suit
  public static final ArmorMaterial BASIC_SUIT_MATERIAL =
      new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 130;
            case LEGGINGS -> 150;
            case CHESTPLATE -> 160;
            case HELMET -> 55;
          };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 2;
            case LEGGINGS -> 5;
            case CHESTPLATE -> 6;
            case HELMET -> 2;
          };
        }

        @Override
        public int getEnchantmentValue() {
          return 9;
        }

        @Override
        public SoundEvent getEquipSound() {
          return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
          return Ingredient.of(net.minecraft.world.item.Items.LEATHER);
        }

        @Override
        public String getName() {
          return "basic_suit";
        }

        @Override
        public float getToughness() {
          return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
          return 0.0F;
        }
      };

  public static final RegistryObject<Item> BASIC_SUIT_HELMET =
      CHEXRegistries.ITEMS.register(
          "basic_suit_helmet",
          () ->
              new ArmorItem(
                  BASIC_SUIT_MATERIAL,
                  ArmorItem.Type.HELMET,
                  new Item.Properties().rarity(Rarity.COMMON)));

  public static final RegistryObject<Item> BASIC_SUIT_CHESTPLATE =
      CHEXRegistries.ITEMS.register(
          "basic_suit_chestplate",
          () ->
              new ArmorItem(
                  BASIC_SUIT_MATERIAL,
                  ArmorItem.Type.CHESTPLATE,
                  new Item.Properties().rarity(Rarity.COMMON)));

  public static final RegistryObject<Item> BASIC_SUIT_LEGGINGS =
      CHEXRegistries.ITEMS.register(
          "basic_suit_leggings",
          () ->
              new ArmorItem(
                  BASIC_SUIT_MATERIAL,
                  ArmorItem.Type.LEGGINGS,
                  new Item.Properties().rarity(Rarity.COMMON)));

  public static final RegistryObject<Item> BASIC_SUIT_BOOTS =
      CHEXRegistries.ITEMS.register(
          "basic_suit_boots",
          () ->
              new ArmorItem(
                  BASIC_SUIT_MATERIAL,
                  ArmorItem.Type.BOOTS,
                  new Item.Properties().rarity(Rarity.COMMON)));

  // Suit Tier 2 - Advanced Space Suit
  public static final ArmorMaterial ADVANCED_SUIT_MATERIAL =
      new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 195;
            case LEGGINGS -> 225;
            case CHESTPLATE -> 240;
            case HELMET -> 165;
          };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 3;
            case LEGGINGS -> 6;
            case CHESTPLATE -> 8;
            case HELMET -> 3;
          };
        }

        @Override
        public int getEnchantmentValue() {
          return 10;
        }

        @Override
        public SoundEvent getEquipSound() {
          return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public Ingredient getRepairIngredient() {
          return Ingredient.of(net.minecraft.world.item.Items.IRON_INGOT);
        }

        @Override
        public String getName() {
          return "advanced_suit";
        }

        @Override
        public float getToughness() {
          return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
          return 0.0F;
        }
      };

  public static final RegistryObject<Item> ADVANCED_SUIT_HELMET =
      CHEXRegistries.ITEMS.register(
          "advanced_suit_helmet",
          () ->
              new ArmorItem(
                  ADVANCED_SUIT_MATERIAL,
                  ArmorItem.Type.HELMET,
                  new Item.Properties().rarity(Rarity.UNCOMMON)));

  public static final RegistryObject<Item> ADVANCED_SUIT_CHESTPLATE =
      CHEXRegistries.ITEMS.register(
          "advanced_suit_chestplate",
          () ->
              new ArmorItem(
                  ADVANCED_SUIT_MATERIAL,
                  ArmorItem.Type.CHESTPLATE,
                  new Item.Properties().rarity(Rarity.UNCOMMON)));

  public static final RegistryObject<Item> ADVANCED_SUIT_LEGGINGS =
      CHEXRegistries.ITEMS.register(
          "advanced_suit_leggings",
          () ->
              new ArmorItem(
                  ADVANCED_SUIT_MATERIAL,
                  ArmorItem.Type.LEGGINGS,
                  new Item.Properties().rarity(Rarity.UNCOMMON)));

  public static final RegistryObject<Item> ADVANCED_SUIT_BOOTS =
      CHEXRegistries.ITEMS.register(
          "advanced_suit_boots",
          () ->
              new ArmorItem(
                  ADVANCED_SUIT_MATERIAL,
                  ArmorItem.Type.BOOTS,
                  new Item.Properties().rarity(Rarity.UNCOMMON)));

  // Suit Tier 3 - Heavy Duty Space Suit
  public static final ArmorMaterial HEAVY_DUTY_SUIT_MATERIAL =
      new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 260;
            case LEGGINGS -> 300;
            case CHESTPLATE -> 320;
            case HELMET -> 220;
          };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 3;
            case LEGGINGS -> 8;
            case CHESTPLATE -> 10;
            case HELMET -> 3;
          };
        }

        @Override
        public int getEnchantmentValue() {
          return 12;
        }

        @Override
        public SoundEvent getEquipSound() {
          return SoundEvents.ARMOR_EQUIP_DIAMOND;
        }

        @Override
        public Ingredient getRepairIngredient() {
          return Ingredient.of(net.minecraft.world.item.Items.DIAMOND);
        }

        @Override
        public String getName() {
          return "heavy_duty_suit";
        }

        @Override
        public float getToughness() {
          return 2.0F;
        }

        @Override
        public float getKnockbackResistance() {
          return 0.0F;
        }
      };

  public static final RegistryObject<Item> HEAVY_DUTY_SUIT_HELMET =
      CHEXRegistries.ITEMS.register(
          "heavy_duty_suit_helmet",
          () ->
              new ArmorItem(
                  HEAVY_DUTY_SUIT_MATERIAL,
                  ArmorItem.Type.HELMET,
                  new Item.Properties().rarity(Rarity.RARE)));

  public static final RegistryObject<Item> HEAVY_DUTY_SUIT_CHESTPLATE =
      CHEXRegistries.ITEMS.register(
          "heavy_duty_suit_chestplate",
          () ->
              new ArmorItem(
                  HEAVY_DUTY_SUIT_MATERIAL,
                  ArmorItem.Type.CHESTPLATE,
                  new Item.Properties().rarity(Rarity.RARE)));

  public static final RegistryObject<Item> HEAVY_DUTY_SUIT_LEGGINGS =
      CHEXRegistries.ITEMS.register(
          "heavy_duty_suit_leggings",
          () ->
              new ArmorItem(
                  HEAVY_DUTY_SUIT_MATERIAL,
                  ArmorItem.Type.LEGGINGS,
                  new Item.Properties().rarity(Rarity.RARE)));

  public static final RegistryObject<Item> HEAVY_DUTY_SUIT_BOOTS =
      CHEXRegistries.ITEMS.register(
          "heavy_duty_suit_boots",
          () ->
              new ArmorItem(
                  HEAVY_DUTY_SUIT_MATERIAL,
                  ArmorItem.Type.BOOTS,
                  new Item.Properties().rarity(Rarity.RARE)));

  // Suit Tier 4 - Exotic Environment Suit
  public static final ArmorMaterial EXOTIC_SUIT_MATERIAL =
      new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 325;
            case LEGGINGS -> 375;
            case CHESTPLATE -> 400;
            case HELMET -> 275;
          };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 3;
            case LEGGINGS -> 6;
            case CHESTPLATE -> 8;
            case HELMET -> 3;
          };
        }

        @Override
        public int getEnchantmentValue() {
          return 15;
        }

        @Override
        public SoundEvent getEquipSound() {
          return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public Ingredient getRepairIngredient() {
          return Ingredient.of(net.minecraft.world.item.Items.NETHERITE_INGOT);
        }

        @Override
        public String getName() {
          return "exotic_suit";
        }

        @Override
        public float getToughness() {
          return 3.0F;
        }

        @Override
        public float getKnockbackResistance() {
          return 0.1F;
        }
      };

  public static final RegistryObject<Item> EXOTIC_SUIT_HELMET =
      CHEXRegistries.ITEMS.register(
          "exotic_suit_helmet",
          () ->
              new ArmorItem(
                  EXOTIC_SUIT_MATERIAL,
                  ArmorItem.Type.HELMET,
                  new Item.Properties().rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> EXOTIC_SUIT_CHESTPLATE =
      CHEXRegistries.ITEMS.register(
          "exotic_suit_chestplate",
          () ->
              new ArmorItem(
                  EXOTIC_SUIT_MATERIAL,
                  ArmorItem.Type.CHESTPLATE,
                  new Item.Properties().rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> EXOTIC_SUIT_LEGGINGS =
      CHEXRegistries.ITEMS.register(
          "exotic_suit_leggings",
          () ->
              new ArmorItem(
                  EXOTIC_SUIT_MATERIAL,
                  ArmorItem.Type.LEGGINGS,
                  new Item.Properties().rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> EXOTIC_SUIT_BOOTS =
      CHEXRegistries.ITEMS.register(
          "exotic_suit_boots",
          () ->
              new ArmorItem(
                  EXOTIC_SUIT_MATERIAL,
                  ArmorItem.Type.BOOTS,
                  new Item.Properties().rarity(Rarity.EPIC)));

  // Suit Tier 5 - Ultimate Space Suit
  public static final ArmorMaterial ULTIMATE_SUIT_MATERIAL =
      new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 390;
            case LEGGINGS -> 450;
            case CHESTPLATE -> 480;
            case HELMET -> 330;
          };
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
          return switch (type) {
            case BOOTS -> 4;
            case LEGGINGS -> 7;
            case CHESTPLATE -> 9;
            case HELMET -> 4;
          };
        }

        @Override
        public int getEnchantmentValue() {
          return 20;
        }

        @Override
        public SoundEvent getEquipSound() {
          return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public Ingredient getRepairIngredient() {
          return Ingredient.of(net.minecraft.world.item.Items.NETHERITE_INGOT);
        }

        @Override
        public String getName() {
          return "ultimate_suit";
        }

        @Override
        public float getToughness() {
          return 4.0F;
        }

        @Override
        public float getKnockbackResistance() {
          return 0.2F;
        }
      };

  public static final RegistryObject<Item> ULTIMATE_SUIT_HELMET =
      CHEXRegistries.ITEMS.register(
          "ultimate_suit_helmet",
          () ->
              new ArmorItem(
                  ULTIMATE_SUIT_MATERIAL,
                  ArmorItem.Type.HELMET,
                  new Item.Properties().rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> ULTIMATE_SUIT_CHESTPLATE =
      CHEXRegistries.ITEMS.register(
          "ultimate_suit_chestplate",
          () ->
              new ArmorItem(
                  ULTIMATE_SUIT_MATERIAL,
                  ArmorItem.Type.CHESTPLATE,
                  new Item.Properties().rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> ULTIMATE_SUIT_LEGGINGS =
      CHEXRegistries.ITEMS.register(
          "ultimate_suit_leggings",
          () ->
              new ArmorItem(
                  ULTIMATE_SUIT_MATERIAL,
                  ArmorItem.Type.LEGGINGS,
                  new Item.Properties().rarity(Rarity.EPIC)));

  public static final RegistryObject<Item> ULTIMATE_SUIT_BOOTS =
      CHEXRegistries.ITEMS.register(
          "ultimate_suit_boots",
          () ->
              new ArmorItem(
                  ULTIMATE_SUIT_MATERIAL,
                  ArmorItem.Type.BOOTS,
                  new Item.Properties().rarity(Rarity.EPIC)));

  private SuitItems() {}
}
