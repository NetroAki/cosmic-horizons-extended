package com.netroaki.chex.entities;

import com.netroaki.chex.registry.entities.CHEXEntities;
import javax.annotation.Nonnull;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SkyGrazerEntity extends Animal {

  public SkyGrazerEntity(EntityType<? extends Animal> entityType, Level level) {
    super(entityType, level);
    this.setNoGravity(true);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.of(Items.WHEAT), false));
    this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
    this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 30.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.FLYING_SPEED, 0.5D)
        .add(Attributes.ATTACK_DAMAGE, 1.0D);
  }

  @Override
  public void tick() {
    super.tick();

    // Add floating movement and particle effects
    if (this.level().isClientSide) {
      // Gentle floating motion
      this.setDeltaMovement(
          this.getDeltaMovement().add(0.0D, 0.01D * Math.sin(this.tickCount * 0.05D), 0.0D));

      // Add cloud-like particles
      if (this.random.nextInt(8) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.CLOUD,
                this.getX() + (this.random.nextDouble() - 0.5D) * 3.0D,
                this.getY() + this.random.nextDouble() * 2.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 3.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Apply levitation effect to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(8.0D))) {
        if (this.random.nextFloat() < 0.05F) {
          player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0, false, false));
        }
      }
    }
  }

  @Override
  public boolean isFood(@Nonnull ItemStack stack) {
    return stack.is(Items.WHEAT) || stack.is(Items.HAY_BLOCK);
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.PHANTOM_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.PHANTOM_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.PHANTOM_DEATH;
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

  @Override
  public boolean canBreed() {
    return super.canBreed() && this.getAge() == 0;
  }

  @Override
  public SkyGrazerEntity getBreedOffspring(
      @Nonnull ServerLevel level, @Nonnull AgeableMob otherParent) {
    return CHEXEntities.SKY_GRAZER.get().create(level);
  }
}
