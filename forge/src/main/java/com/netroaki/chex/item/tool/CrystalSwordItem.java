package com.netroaki.chex.item.tool;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class CrystalSwordItem extends SwordItem {
  public CrystalSwordItem(
      Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
    super(tier, attackDamageModifier, attackSpeedModifier, properties);
  }
}
