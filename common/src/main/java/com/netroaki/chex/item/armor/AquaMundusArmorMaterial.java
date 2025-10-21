package com.netroaki.chex.item.armor;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public enum AquaMundusArmorMaterial implements ArmorMaterial {
  AQUA_MUNDUS(
      "aqua_mundus",
      35,
      new int[] {3, 6, 8, 3},
      15,
      SoundEvents.ARMOR_EQUIP_TURTLE,
      2.5F,
      0.1F,
      () -> Ingredient.of(Items.PRISMARINE_CRYSTALS));

  private static final int[] HEALTH_PER_SLOT = new int[] {13, 15, 16, 11};
  private final String name;
  private final int durabilityMultiplier;
  private final int[] slotProtections;
  private final int enchantmentValue;
  private final SoundEvent sound;
  private final float toughness;
  private final float knockbackResistance;
  private final LazyLoadedValue<Ingredient> repairIngredient;

  AquaMundusArmorMaterial(
      String name,
      int durabilityMultiplier,
      int[] slotProtections,
      int enchantmentValue,
      SoundEvent sound,
      float toughness,
      float knockbackResistance,
      Supplier<Ingredient> repairIngredient) {
    this.name = CosmicHorizonsExpanded.MOD_ID + ":" + name;
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
