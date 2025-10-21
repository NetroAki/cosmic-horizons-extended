package com.netroaki.chex.entity.crystalis;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FrostHareEntity extends Rabbit {
  private static final Ingredient FOOD_ITEMS =
      Ingredient.of(Items.CARROT, Items.GOLDEN_CARROT, Items.DANDELION);

  public FrostHareEntity(EntityType<? extends Rabbit> type, Level level) {
    super(type, level);
    this.setSpeedModifier(0.0D);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 6.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.4D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.6D));
    this.goalSelector.addGoal(2, new BreedGoal(this, 0.8D));
    this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
    this.goalSelector.addGoal(5, new FleeSunGoal(this, 1.4D));
    this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
    this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.RABBIT_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.RABBIT_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.RABBIT_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.RABBIT_JUMP, 0.15F, 1.0F);
  }

  @Override
  public float getVoicePitch() {
    return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.9F;
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return FOOD_ITEMS.test(stack);
  }
}
