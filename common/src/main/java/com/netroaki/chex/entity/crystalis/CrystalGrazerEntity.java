package com.netroaki.chex.entity.crystalis;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrystalGrazerEntity extends Animal {
  private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT);
  private int eatAnimationTick;
  private EatBlockGoal eatBlockGoal;

  public CrystalGrazerEntity(EntityType<? extends Animal> entityType, Level level) {
    super(entityType, level);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.2D)
        .add(Attributes.ATTACK_DAMAGE, 2.0D);
  }

  @Override
  protected void registerGoals() {
    this.eatBlockGoal = new EatBlockGoal(this);
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, FOOD_ITEMS, false));
    this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
    this.goalSelector.addGoal(5, this.eatBlockGoal);
    this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
  }

  @Override
  public void aiStep() {
    if (this.level().isClientSide) {
      this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
    }
    super.aiStep();
  }

  @Override
  public void handleEntityEvent(byte id) {
    if (id == 10) {
      this.eatAnimationTick = 40;
    } else {
      super.handleEntityEvent(id);
    }
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.COW_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.COW_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.COW_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
  }

  @Override
  public float getVoicePitch() {
    return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F;
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return FOOD_ITEMS.test(stack);
  }

  @Override
  public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
    return null; // Will be implemented in a future update
  }
}
