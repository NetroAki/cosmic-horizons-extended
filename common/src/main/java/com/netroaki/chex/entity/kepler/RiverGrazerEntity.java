package com.netroaki.chex.entity.kepler;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class RiverGrazerEntity extends Animal {
  public RiverGrazerEntity(EntityType<? extends Animal> type, Level level) {
    super(type, level);
  }

  @Override
  public boolean isFood(net.minecraft.world.item.ItemStack stack) {
    return false;
  }
}
