package com.netroaki.chex.entity.inferno;

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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AshCrawlerEntity extends InfernoEntity {
  private int burrowCooldown = 0;
  private boolean isBurrowed = false;

  public AshCrawlerEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 8;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return InfernoEntity.createAttributes()
        .add(Attributes.MAX_HEALTH, 25.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 4.0D)
        .add(Attributes.ARMOR, 3.0D)
        .add(Attributes.FOLLOW_RANGE, 20.0D);
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

    if (this.burrowCooldown > 0) {
      this.burrowCooldown--;
    } else if (this.random.nextInt(100) == 0 && this.isOnGround()) {
      this.tryBurrow();
    }

    // Move faster on ash blocks
    BlockState state = this.level().getBlockState(this.blockPosition().below());
    if (state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL)) {
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    } else {
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }
  }

  private void tryBurrow() {
    if (!this.level().isClientSide && this.random.nextFloat() < 0.1F) {
      this.isBurrowed = true;
      this.setInvisible(true);
      this.setInvulnerable(true);
      this.setNoAi(true);

      // Emerge after 3-8 seconds
      this.level()
          .scheduleTick(new BlockPos(this.blockPosition()), this, 60 + this.random.nextInt(100));
    }
  }

  @Override
  public void tickDespawn() {
    if (this.isBurrowed) {
      this.isBurrowed = false;
      this.setInvisible(false);
      this.setInvulnerable(false);
      this.setNoAi(false);
      this.burrowCooldown = 200 + this.random.nextInt(400);
    }
    super.tickDespawn();
  }

  @Override
  protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(source, looting, recentlyHit);
    // Drop cinder chitin (custom item, will be registered separately)
    this.spawnAtLocation(new ItemStack(Items.BLAZE_POWDER, 1 + this.random.nextInt(2 + looting)));
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.HOGLIN_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.HOGLIN_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.HOGLIN_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
  }
}
