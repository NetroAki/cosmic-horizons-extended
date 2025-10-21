package com.netroaki.chex.entity.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;

/** Base type for lightweight mob abilities. */
public abstract class MobAbility {
  protected final Mob mob;

  protected MobAbility(Mob mob) {
    this.mob = mob;
  }

  /** Called every tick from the owning mob. */
  public void tick() {}

  /** Persist ability data to the given tag. */
  public void save(CompoundTag tag) {}

  /** Restore ability data from the given tag. */
  public void load(CompoundTag tag) {}
}
