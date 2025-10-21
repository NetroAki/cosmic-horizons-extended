package com.netroaki.chex.item.material;

import com.netroaki.chex.registry.items.CHEXItems;
import java.util.function.Supplier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public enum CrystalToolMaterial implements Tier {
  // Durability, mining speed, attack damage, mining level, enchantability, repair
  // material
  CRYSTAL(1561, 8.0F, 3.0F, 3, 15, () -> Ingredient.of(Items.AMETHYST_SHARD)),
  RARE_CRYSTAL(2031, 9.0F, 4.0F, 4, 18, () -> Ingredient.of(CHEXItems.RARE_CRYSTAL_SHARD.get())),
  ENERGIZED_CRYSTAL(
      2501, 10.0F, 5.0F, 5, 22, () -> Ingredient.of(CHEXItems.ENERGIZED_CRYSTAL_SHARD.get()));

  private final int maxUses;
  private final float efficiency;
  private final float attackDamage;
  private final int harvestLevel;
  private final int enchantability;
  private final LazyLoadedValue<Ingredient> repairMaterial;
  private final TagKey<Block> tag;

  CrystalToolMaterial(
      int maxUses,
      float efficiency,
      float attackDamage,
      int harvestLevel,
      int enchantability,
      Supplier<Ingredient> repairMaterial) {
    this.maxUses = maxUses;
    this.efficiency = efficiency;
    this.attackDamage = attackDamage;
    this.harvestLevel = harvestLevel;
    this.enchantability = enchantability;
    this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
    this.tag =
        switch (this) {
          case CRYSTAL -> BlockTags.NEEDS_IRON_TOOL;
          case RARE_CRYSTAL -> BlockTags.NEEDS_DIAMOND_TOOL;
          case ENERGIZED_CRYSTAL -> Tags.Blocks.NEEDS_NETHERITE_TOOL;
        };
  }

  @Override
  public int getUses() {
    return this.maxUses;
  }

  @Override
  public float getSpeed() {
    return this.efficiency;
  }

  @Override
  public float getAttackDamageBonus() {
    return this.attackDamage;
  }

  @Override
  public int getLevel() {
    return this.harvestLevel;
  }

  @Override
  public int getEnchantmentValue() {
    return this.enchantability;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return this.repairMaterial.get();
  }

  @Override
  public TagKey<Block> getTag() {
    return this.tag;
  }
}
