package com.netroaki.chex.entity.inferno;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MagmaHopperEntity extends InfernoEntity {
  private int jumpCooldown = 0;
  private int explosionPower = 2;

  public MagmaHopperEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 10;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return InfernoEntity.createAttributes()
        .add(Attributes.MAX_HEALTH, 15.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 3.0D)
        .add(Attributes.ARMOR, 1.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
        .add(Attributes.FOLLOW_RANGE, 16.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.4F));
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void tick() {
    super.tick();

    // Handle jump cooldown
    if (this.jumpCooldown > 0) {
      this.jumpCooldown--;
    }

    // Visual effects
    if (this.level().isClientSide) {
      for (int i = 0; i < 2; ++i) {
        this.level()
            .addParticle(
                ParticleTypes.FLAME,
                this.getRandomX(0.5D),
                this.getRandomY() - 0.25D,
                this.getRandomZ(0.5D),
                (this.random.nextDouble() - 0.5D) * 0.1D,
                0.05D,
                (this.random.nextDouble() - 0.5D) * 0.1D);
      }
    }

    // Jump attack logic
    if (!this.level().isClientSide && this.getTarget() != null && this.jumpCooldown <= 0) {
      if (this.distanceToSqr(this.getTarget()) < 16.0D && this.random.nextFloat() < 0.1F) {
        this.performJumpAttack();
      }
    }
  }

  private void performJumpAttack() {
    if (this.getTarget() == null) return;

    // Calculate direction to target
    Vec3 targetPos = this.getTarget().position();
    Vec3 direction = targetPos.subtract(this.position()).normalize();

    // Launch towards target
    this.setDeltaMovement(
        direction.x * 1.5D, 0.8D + this.random.nextDouble() * 0.4D, direction.z * 1.5D);

    // Set cooldown
    this.jumpCooldown = 40 + this.random.nextInt(40);

    // Play sound
    this.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F);
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    boolean result = super.doHurtTarget(target);

    if (result && this.random.nextFloat() < 0.3F) {
      // Small explosion on hit
      if (!this.level().isClientSide) {
        this.level()
            .explode(
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                this.explosionPower,
                Level.ExplosionInteraction.MOB);

        // Apply fire resistance to self
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false));

        // Die after exploding
        this.discard();
      }
    }

    return result;
  }

  @Override
  protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(source, looting, recentlyHit);
    // Drop both cinder chitin and volcanic essence
    if (this.random.nextFloat() < 0.5F + looting * 0.1F) {
      this.spawnAtLocation(
          new ItemStack(Items.BLAZE_POWDER, 1 + this.random.nextInt(1 + looting / 2)));
    }
    if (this.random.nextFloat() < 0.3F + looting * 0.1F) {
      this.spawnAtLocation(new ItemStack(Items.BLAZE_ROD, 1));
    }
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.MAGMA_CUBE_SQUISH_SMALL;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.MAGMA_CUBE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.MAGMA_CUBE_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.MAGMA_CUBE_JUMP, 0.15F, 1.0F);
  }
}
