package com.netroaki.chex.item.material;

import com.netroaki.chex.registry.items.CHEXItems;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public enum CrystalArmorMaterial implements ArmorMaterial {
  CRYSTAL(
      "crystal",
      33,
      new int[] {3, 8, 6, 3},
      10,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      2.0f,
      0.0f,
      () -> Ingredient.of(CHEXItems.CRYSTAL_SHARD.get())),
  RARE_CRYSTAL(
      "rare_crystal",
      40,
      new int[] {4, 9, 7, 4},
      15,
      SoundEvents.ARMOR_EQUIP_NETHERITE,
      2.5f,
      0.1f,
      () -> Ingredient.of(CHEXItems.RARE_CRYSTAL_SHARD.get())),
  ENERGIZED_CRYSTAL(
      "energized_crystal",
      48,
      new int[] {5, 10, 8, 5},
      20,
      SoundEvents.ARMOR_EQUIP_NETHERITE,
      3.0f,
      0.2f,
      () -> Ingredient.of(CHEXItems.ENERGIZED_CRYSTAL_SHARD.get()));

  private static final int[] HEALTH_PER_SLOT = new int[] {13, 15, 16, 11};
  private final String name;
  private final int durabilityMultiplier;
  private final int[] slotProtections;
  private final int enchantmentValue;
  private final SoundEvent sound;
  private final float toughness;
  private final float knockbackResistance;
  private final LazyLoadedValue<Ingredient> repairIngredient;

  CrystalArmorMaterial(
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
    this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
  }

  @Override
  public int getDurabilityForType(ArmorItem.Type type) {
    return HEALTH_PER_SLOT[type.ordinal()] * this.durabilityMultiplier;
  }

  @Override
  public int getDefenseForType(ArmorItem.Type type) {
    return this.slotProtections[type.ordinal()];
  }

  @Override
  public int getEnchantmentValue() {
    return this.enchantmentValue;
  }

  @Override
  public SoundEvent getEquipSound() {
    return this.sound;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return this.repairIngredient.get();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public float getToughness() {
    return this.toughness;
  }

  @Override
  public float getKnockbackResistance() {
    return this.knockbackResistance;
  }
}
