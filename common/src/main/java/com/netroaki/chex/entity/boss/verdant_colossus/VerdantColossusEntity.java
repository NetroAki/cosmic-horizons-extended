package com.netroaki.chex.entity.boss.verdant_colossus;

import com.netroaki.chex.entity.boss.verdant_colossus.ai.VerdantColossusGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class VerdantColossusEntity extends Monster {
  private int phase = 0;
  private int phaseTransitionTicks = 0;
  private static final int PHASE_TRANSITION_DURATION = 100;

  public VerdantColossusEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 1000;
    this.setHealth(this.getMaxHealth());
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 500.0D)
        .add(Attributes.ATTACK_DAMAGE, 10.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.FOLLOW_RANGE, 40.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new VerdantColossusGoal(this));
    this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void tick() {
    super.tick();

    if (this.phaseTransitionTicks > 0) {
      this.phaseTransitionTicks--;
      if (this.level().isClientSide) {
        spawnTransitionParticles();
      }
    }
  }

  private void spawnTransitionParticles() {
    for (int i = 0; i < 5; i++) {
      double d0 = this.getRandom().nextGaussian() * 0.2D;
      double d1 = this.getRandom().nextGaussian() * 0.2D;
      double d2 = this.getRandom().nextGaussian() * 0.2D;
      this.level()
          .addParticle(
              ParticleTypes.END_ROD,
              this.getX()
                  + (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F)
                  - this.getBbWidth(),
              this.getY() + 0.5D + (this.getRandom().nextFloat() * this.getBbHeight()),
              this.getZ()
                  + (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F)
                  - this.getBbWidth(),
              d0,
              d1,
              d2);
    }
  }

  public void triggerPhaseChange(int newPhase) {
    this.phase = newPhase;
    this.phaseTransitionTicks = PHASE_TRANSITION_DURATION;
    this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 10.0F, 0.5F);

    // Apply phase-specific buffs
    switch (newPhase) {
      case 1 -> {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15.0D);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
      }
      case 2 -> {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(20.0D);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(Attributes.ARMOR).setBaseValue(15.0D);
      }
    }
  }

  public int getPhase() {
    return phase;
  }

  public boolean isInTransition() {
    return phaseTransitionTicks > 0;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource source) {
    return SoundEvents.IRON_GOLEM_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.IRON_GOLEM_DEATH;
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }
}
