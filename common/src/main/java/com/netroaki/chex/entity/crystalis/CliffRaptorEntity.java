package com.netroaki.chex.entity.crystalis;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CliffRaptorEntity extends Monster {
  public CliffRaptorEntity(EntityType<? extends Monster> type, Level world) {
    super(type, world);
    this.xpReward = 10;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 35.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.35D)
        .add(Attributes.ATTACK_DAMAGE, 6.0D)
        .add(Attributes.ARMOR, 3.0D)
        .add(Attributes.FOLLOW_RANGE, 32.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4D, true));
    this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
    this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, true));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Make the raptor more likely to jump when moving
    if (this.horizontalCollision && net.minecraftforge.common.ForgeHooks.onLivingJump(this)) {
      this.jumpFromGround();
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && this.getRandom().nextInt(3) == 0) {
      // 33% chance to perform a knockback attack
      if (target instanceof LivingEntity) {
        double d0 = this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        double d1 = d0 * 0.5D + this.random.nextInt((int) Math.ceil(d0) + 1);
        ((LivingEntity) target)
            .knockback(0.4F, this.getX() - target.getX(), this.getZ() - target.getZ());
      }
    }
    return flag;
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.RAVAGER_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.RAVAGER_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.RAVAGER_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
  }

  @Override
  public float getVoicePitch() {
    return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return source.is(DamageTypeTags.IS_FREEZE) || super.isInvulnerableTo(source);
  }
}
