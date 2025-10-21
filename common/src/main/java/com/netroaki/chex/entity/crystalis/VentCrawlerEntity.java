package com.netroaki.chex.entity.crystalis;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class VentCrawlerEntity extends Spider {
  public VentCrawlerEntity(EntityType<? extends Spider> type, Level world) {
    super(type, world);
    this.xpReward = 8;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Spider.createAttributes()
        .add(Attributes.MAX_HEALTH, 25.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 4.0D)
        .add(Attributes.ARMOR, 3.0D)
        .add(Attributes.FOLLOW_RANGE, 24.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new FloatGoal(this));
    this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
    this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (super.doHurtTarget(target)) {
      if (target instanceof LivingEntity) {
        int i = 0;
        if (this.level().getDifficulty() == Difficulty.NORMAL) {
          i = 7;
        } else if (this.level().getDifficulty() == Difficulty.HARD) {
          i = 15;
        }

        if (i > 0) {
          ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0));
        }
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void tick() {
    super.tick();

    // Heal when in contact with lava or fire
    if (this.isInLava() || this.isOnFire()) {
      this.heal(0.1F);
    }
  }

  @Override
  public boolean fireImmune() {
    return true;
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.SPIDER_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.SPIDER_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.SPIDER_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
  }

  @Override
  public float getVoicePitch() {
    return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return source.isFire() || super.isInvulnerableTo(source);
  }
}
