package com.netroaki.chex.entity.inferno;

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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FireWraithEntity extends InfernoEntity {
  private int phaseCooldown = 0;
  private boolean isPhasing = false;

  public FireWraithEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 12;
    this.noPhysics = true;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return InfernoEntity.createAttributes()
        .add(Attributes.MAX_HEIGHT, 2.0F)
        .add(Attributes.MAX_HEALTH, 30.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 5.0D)
        .add(Attributes.ARMOR, 1.0D)
        .add(Attributes.FOLLOW_RANGE, 24.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
    this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void tick() {
    super.tick();

    // Handle phasing ability
    if (this.phaseCooldown > 0) {
      this.phaseCooldown--;

      if (this.isPhasing && this.phaseCooldown <= 0) {
        this.isPhasing = false;
        this.setNoAi(false);
        this.phaseCooldown = 100 + this.random.nextInt(100);
      }
    } else if (this.getTarget() != null && this.random.nextFloat() < 0.02F) {
      this.triggerPhase();
    }

    // Leave fire trail
    if (this.tickCount % 5 == 0 && !this.level().isClientSide) {
      BlockPos pos = this.blockPosition();
      if (this.level().isEmptyBlock(pos)
          && this.level().getBlockState(pos.below()).isSolidRender(this.level(), pos.below())) {
        this.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
      }
    }

    // Float slightly above ground
    if (!this.isPhasing) {
      BlockPos pos = this.blockPosition();
      if (!this.level().getBlockState(pos.below()).isAir()) {
        this.setNoGravity(true);
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));

        if (this.getY() - pos.getY() < 0.2) {
          this.setPos(this.getX(), pos.getY() + 0.2, this.getZ());
        }
      }
    }
  }

  private void triggerPhase() {
    this.isPhasing = true;
    this.phaseCooldown = 20 + this.random.nextInt(40);
    this.setNoAi(true);

    // Teleport behind target if possible
    if (this.getTarget() != null) {
      Vec3 targetPos = this.getTarget().position();
      Vec3 lookVec = this.getTarget().getLookAngle().normalize();
      Vec3 teleportPos = targetPos.add(lookVec.scale(-2.0));

      // Ensure we don't teleport into blocks
      BlockPos pos = new BlockPos((int) teleportPos.x, (int) teleportPos.y, (int) teleportPos.z);
      while (pos.getY() > 0 && !this.level().getBlockState(pos).isAir()) {
        pos = pos.above();
      }

      this.teleportTo(teleportPos.x, pos.getY(), teleportPos.z);
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (super.doHurtTarget(target)) {
      if (target instanceof LivingEntity) {
        // Set target on fire
        target.setSecondsOnFire(5);

        // Apply slowness
        ((LivingEntity) target)
            .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, false));
      }
      return true;
    }
    return false;
  }

  @Override
  protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(source, looting, recentlyHit);
    // Drop volcanic essence (custom item, will be registered separately)
    this.spawnAtLocation(new ItemStack(Items.BLAZE_ROD, 1 + this.random.nextInt(1 + looting / 2)));
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.BLAZE_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.BLAZE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.BLAZE_DEATH;
  }
}
