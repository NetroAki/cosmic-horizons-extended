package com.netroaki.chex.registry.items;

import com.netroaki.chex.CHEX;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public enum CHEXArmorMaterials implements ArmorMaterial {
  SPACE_SUIT_T1(
      "space_suit_t1",
      15,
      new int[] {2, 5, 6, 2},
      9,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      0.0f,
      0.0f,
      () -> Ingredient.of(net.minecraft.world.item.Items.LEATHER)),
  SPACE_SUIT_T2(
      "space_suit_t2",
      20,
      new int[] {3, 6, 7, 3},
      12,
      SoundEvents.ARMOR_EQUIP_IRON,
      0.0f,
      0.0f,
      () -> Ingredient.of(net.minecraft.world.item.Items.IRON_INGOT)),
  SPACE_SUIT_T3(
      "space_suit_t3",
      25,
      new int[] {4, 7, 8, 4},
      15,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      1.0f,
      0.0f,
      () -> Ingredient.of(net.minecraft.world.item.Items.DIAMOND)),
  SPACE_SUIT_T4(
      "space_suit_t4",
      30,
      new int[] {5, 8, 9, 5},
      18,
      SoundEvents.ARMOR_EQUIP_NETHERITE,
      2.0f,
      0.1f,
      () -> Ingredient.of(net.minecraft.world.item.Items.NETHERITE_INGOT)),
  SPACE_SUIT_T5(
      "space_suit_t5",
      35,
      new int[] {6, 9, 10, 6},
      21,
      SoundEvents.ARMOR_EQUIP_NETHERITE,
      3.0f,
      0.2f,
      () -> Ingredient.of(net.minecraft.world.item.Items.NETHERITE_INGOT));

  private final String name;
  private final int durabilityMultiplier;
  private final int[] slotProtections;
  private final int enchantmentValue;
  private final SoundEvent sound;
  private final float toughness;
  private final float knockbackResistance;
  private final Supplier<Ingredient> repairIngredient;

  CHEXArmorMaterials(
      String name,
      int durabilityMultiplier,
      int[] slotProtections,
      int enchantmentValue,
      SoundEvent sound,
      float toughness,
      float knockbackResistance,
      Supplier<Ingredient> repairIngredient) {
    this.name = name;
    this.durabilityMultiplier = durabilityMultiplier;
    this.slotProtections = slotProtections;
    this.enchantmentValue = enchantmentValue;
    this.sound = sound;
    this.toughness = toughness;
    this.knockbackResistance = knockbackResistance;
    this.repairIngredient = repairIngredient;
  }

  @Override
  public int getDurabilityForType(ArmorItem.Type type) {
    return switch (type) {
      case BOOTS -> 13 * durabilityMultiplier;
      case LEGGINGS -> 15 * durabilityMultiplier;
      case CHESTPLATE -> 16 * durabilityMultiplier;
      case HELMET -> 11 * durabilityMultiplier;
    };
  }

  @Override
  public int getDefenseForType(ArmorItem.Type type) {
    return switch (type) {
      case BOOTS -> slotProtections[0];
      case LEGGINGS -> slotProtections[1];
      case CHESTPLATE -> slotProtections[2];
      case HELMET -> slotProtections[3];
    };
  }

  @Override
  public int getEnchantmentValue() {
    return enchantmentValue;
  }

  @Override
  public SoundEvent getEquipSound() {
    return sound;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return repairIngredient.get();
  }

  @Override
  public String getName() {
    return CHEX.MOD_ID + ":" + name;
  }

  @Override
  public float getToughness() {
    return toughness;
  }

  @Override
  public float getKnockbackResistance() {
    return knockbackResistance;
  }
}
