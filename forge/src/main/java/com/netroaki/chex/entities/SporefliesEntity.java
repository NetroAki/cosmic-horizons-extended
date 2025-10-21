package com.netroaki.chex.entities;

import javax.annotation.Nonnull;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SporefliesEntity extends AmbientCreature implements GeoEntity {

  private int sporeCooldown = 0;
  private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

  public SporefliesEntity(EntityType<? extends AmbientCreature> entityType, Level level) {
    super(entityType, level);
    this.setNoGravity(true);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 8.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.FLYING_SPEED, 0.4D);
  }

  // AzureLib animations
  private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
  private static final RawAnimation FLY = RawAnimation.begin().thenLoop("fly");

  @Override
  public void registerControllers(ControllerRegistrar controllers) {
    controllers.add(
        new AnimationController<>(
            this,
            "controller",
            5,
            state -> {
              // movement based animation swap
              return state.setAndContinue(this.getDeltaMovement().lengthSqr() > 0.001 ? FLY : IDLE);
            }));
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return cache;
  }

  @Override
  public void tick() {
    super.tick();

    // Add floating movement
    if (this.level().isClientSide) {
      this.setDeltaMovement(
          this.getDeltaMovement().add(0.0D, 0.02D * Math.sin(this.tickCount * 0.1D), 0.0D));

      // Add particle effects
      if (this.random.nextInt(3) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.SPORE_BLOSSOM_AIR,
                this.getX() + (this.random.nextDouble() - 0.5D) * 1.0D,
                this.getY() + this.random.nextDouble() * 1.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 1.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Spore effect on nearby players
    if (!this.level().isClientSide && this.sporeCooldown <= 0) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(3.0D))) {
        if (this.random.nextFloat() < 0.1F) {
          player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
          player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 40, 0, false, false));
          this.sporeCooldown = 100; // 5 second cooldown
          break;
        }
      }
    }

    if (this.sporeCooldown > 0) {
      this.sporeCooldown--;
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.BEE_LOOP;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.BEE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.BEE_DEATH;
  }

  @Override
  public boolean isNoGravity() {
    return true;
  }

  @Override
  public void travel(@Nonnull Vec3 travelVector) {
    if (this.isInWater()) {
      this.moveRelative(0.02F, travelVector);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
    } else {
      super.travel(travelVector);
    }
  }
}
