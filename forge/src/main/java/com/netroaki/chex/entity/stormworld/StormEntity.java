package com.netroaki.chex.entity.stormworld;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class StormEntity extends Monster {
  private boolean stormPowered;

  protected StormEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
  }

  public boolean isStormPowered() {
    return stormPowered;
  }

  protected void setStormPowered(boolean powered) {
    if (this.stormPowered != powered) {
      this.stormPowered = powered;
      this.onStormPowerChange(powered);
    }
  }

  protected void onStormPowerChange(boolean isPowered) {}
}
