package com.netroaki.chex.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

/**
 * The Stellar Core is a rare drop from the Stellar Avatar boss. It's used in advanced photonic
 * technology and crafting.
 */
public class StellarCoreItem extends Item {
  public StellarCoreItem() {
    super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
  }
}
