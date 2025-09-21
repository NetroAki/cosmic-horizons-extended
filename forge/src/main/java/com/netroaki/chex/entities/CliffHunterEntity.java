package com.netroaki.chex.entities;

import javax.annotation.Nonnull;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CliffHunterEntity extends Monster {

  public CliffHunterEntity(EntityType<? extends Monster> entityType, Level level) {
    super(entityType, level);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 40.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.35D)
        .add(Attributes.ATTACK_DAMAGE, 6.0D)
        .add(Attributes.FOLLOW_RANGE, 16.0D)
        .add(Attributes.ARMOR, 2.0D);
  }

  @Override
  public void tick() {
    super.tick();

    // Add particle effects for aggressive behavior
    if (this.level().isClientSide && this.random.nextInt(10) == 0) {
      this.level()
          .addParticle(
              ParticleTypes.ANGRY_VILLAGER,
              this.getX() + (this.random.nextDouble() - 0.5D) * 2.0D,
              this.getY() + this.random.nextDouble() * 2.0D,
              this.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D,
              0.0D,
              0.0D,
              0.0D);
    }

    // Apply effects to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(6.0D))) {
        if (this.random.nextFloat() < 0.1F) {
          player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, false));
          player.addEffect(
              new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, false, false));
        }
      }
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.PILLAGER_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.PILLAGER_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.PILLAGER_DEATH;
  }

  @Override
  protected void dropCustomDeathLoot(
      @Nonnull DamageSource damageSource, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(damageSource, looting, recentlyHit);

    // Drop cliff hunter specific items
    if (this.random.nextFloat() < 0.3F) {
      this.spawnAtLocation(Items.IRON_INGOT, 1);
    }
    if (this.random.nextFloat() < 0.1F) {
      this.spawnAtLocation(Items.EMERALD, 1);
    }
  }

  @Override
  public boolean doHurtTarget(@Nonnull Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof Player) {
      // Apply additional effects on successful attack
      ((Player) target).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
    }
    return flag;
  }
}
