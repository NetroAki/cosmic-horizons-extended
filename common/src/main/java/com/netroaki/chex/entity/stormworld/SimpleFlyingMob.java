package com.netroaki.chex.entity.stormworld;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class SimpleFlyingMob extends PathfinderMob {
  public SimpleFlyingMob(EntityType<? extends SimpleFlyingMob> type, Level level) {
    super(type, level);
    this.moveControl = new FlyingMoveControl(this, 10, true);
    this.setNoGravity(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.FLYING_SPEED, 0.35D)
        .add(Attributes.FOLLOW_RANGE, 24.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  @Override
  protected FlyingPathNavigation createNavigation(Level level) {
    FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
    nav.setCanOpenDoors(false);
    nav.setCanPassDoors(true);
    nav.setCanFloat(true);
    return nav;
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level,
      DifficultyInstance difficulty,
      MobSpawnType reason,
      @Nullable SpawnGroupData spawnData,
      @Nullable net.minecraft.nbt.CompoundTag tag) {
    this.setNoGravity(true);
    return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
  }

  @Override
  public boolean fireImmune() {
    return true; // Stormworld entities often resist environmental damage
  }

  @Override
  public boolean causeFallDamage(
      float fallDistance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
    return false;
  }
}
