package com.netroaki.chex.item.tool;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;

public class CrystalHoeItem extends HoeItem {
  public CrystalHoeItem(
      Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
    super(tier, attackDamageModifier, attackSpeedModifier, properties);
  }
}
