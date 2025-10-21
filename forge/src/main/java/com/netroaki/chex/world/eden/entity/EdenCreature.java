package com.netroaki.chex.world.eden.entity;

import net.minecraft.core.BlockPos;
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

public class EdenCreature extends Animal {
  private static final Ingredient FOOD_ITEMS =
      Ingredient.of(Items.GLOW_BERRIES, Items.SWEET_BERRIES);

  public EdenCreature(EntityType<? extends Animal> type, Level level) {
    super(type, level);
    this.setCanPickUpLoot(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 10.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.2F)
        .add(Attributes.FOLLOW_RANGE, 16.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, FOOD_ITEMS, false));
    this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
    this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.FOX_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.FOX_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.FOX_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.FOX_STEP, 0.15F, 1.0F);
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return FOOD_ITEMS.test(stack);
  }

  @Nullable
  @Override
  public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
    return null; // Implement in child classes
  }
}
