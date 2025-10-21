package com.netroaki.chex.entity.arrakis;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SpiceGathererEntity extends AbstractVillager {
  public SpiceGathererEntity(EntityType<? extends AbstractVillager> type, Level level) {
    super(type, level);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 30.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.35D)
        .add(Attributes.ATTACK_DAMAGE, 3.0D)
        .add(Attributes.FOLLOW_RANGE, 32.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
    this.goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));
    this.goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.35D));
    this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.35D));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, true));
  }

  @Override
  protected void updateTrades() {
    // TODO: Implement custom trades related to spice and Arrakis resources
  }

  @Override
  public boolean canBeLeashed(Player player) {
    return false;
  }
}
