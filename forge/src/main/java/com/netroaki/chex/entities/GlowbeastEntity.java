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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class GlowbeastEntity extends Animal {

  public GlowbeastEntity(EntityType<? extends Animal> entityType, Level level) {
    super(entityType, level);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(
        3, new TemptGoal(this, 1.1D, Ingredient.of(Items.GLOW_BERRIES), false));
    this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
    this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 2.0D);
  }

  @Override
  public void tick() {
    super.tick();

    // Add glowing particle effects
    if (this.level().isClientSide && this.random.nextInt(5) == 0) {
      this.level()
          .addParticle(
              ParticleTypes.END_ROD,
              this.getX() + (this.random.nextDouble() - 0.5D) * 2.0D,
              this.getY() + this.random.nextDouble() * 2.0D,
              this.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D,
              0.0D,
              0.0D,
              0.0D);
    }
  }

  @Override
  public boolean isFood(@Nonnull ItemStack stack) {
    return stack.is(Items.GLOW_BERRIES);
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.GLOW_SQUID_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.GLOW_SQUID_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.GLOW_SQUID_DEATH;
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Apply glowing effect to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(5.0D))) {
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0, false, false));
      }
    }
  }

  @Override
  public boolean canBreed() {
    return super.canBreed() && this.getAge() == 0;
  }

  @Override
  public GlowbeastEntity getBreedOffspring(
      @Nonnull ServerLevel level, @Nonnull AgeableMob otherParent) {
    return CHEXEntities.GLOWBEAST.get().create(level);
  }
}
