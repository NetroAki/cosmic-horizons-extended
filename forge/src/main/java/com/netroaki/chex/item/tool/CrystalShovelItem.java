package com.netroaki.chex.item.tool;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class CrystalShovelItem extends ShovelItem {
  public CrystalShovelItem(
      Tier tier, float attackDamage, float attackSpeed, Properties properties) {
    super(tier, attackDamage, attackSpeed, properties);
  }
}
