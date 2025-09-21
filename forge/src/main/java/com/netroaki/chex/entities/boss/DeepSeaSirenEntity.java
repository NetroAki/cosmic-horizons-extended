package com.netroaki.chex.entities.boss;

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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class DeepSeaSirenEntity extends Monster {

  private int songCooldown = 0;
  private int phase = 1; // 1 = normal, 2 = enraged

  public DeepSeaSirenEntity(EntityType<? extends Monster> entityType, Level level) {
    super(entityType, level);
    this.setNoAi(false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.6D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 20.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 250.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.35D)
        .add(Attributes.ATTACK_DAMAGE, 15.0D)
        .add(Attributes.FOLLOW_RANGE, 28.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
  }

  @Override
  public void tick() {
    super.tick();

    // Phase transition at 45% health
    if (this.getHealth() <= this.getMaxHealth() * 0.45F && phase == 1) {
      phase = 2;
      this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 1, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 0, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 999999, 0, false, false));
    }

    // Song attack
    if (this.songCooldown <= 0 && this.getTarget() != null) {
      performSongAttack();
      this.songCooldown = phase == 2 ? 80 : 140; // Faster in enraged phase
    }

    if (this.songCooldown > 0) {
      this.songCooldown--;
    }

    // Particle effects
    if (this.level().isClientSide) {
      if (this.random.nextInt(5) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.BUBBLE,
                this.getX() + (this.random.nextDouble() - 0.5D) * 3.0D,
                this.getY() + this.random.nextDouble() * 2.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 3.0D,
                0.0D,
                0.1D,
                0.0D);
      }

      if (phase == 2 && this.random.nextInt(3) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.END_ROD,
                this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D,
                this.getY() + this.random.nextDouble() * 3.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Apply siren effects to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(12.0D))) {
        if (this.random.nextFloat() < 0.12F) {
          player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0, false, false));
          player.addEffect(
              new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false));
          player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, false, false));
        }
      }
    }
  }

  private void performSongAttack() {
    if (this.level().isClientSide) return;

    // Create song wave effect
    for (int i = 0; i < 30; i++) {
      double angle = (i / 30.0) * 2 * Math.PI;
      double x = this.getX() + Math.cos(angle) * 8.0D;
      double z = this.getZ() + Math.sin(angle) * 8.0D;
      double y = this.getY() + this.random.nextDouble() * 2.0D;

      this.level().addParticle(ParticleTypes.BUBBLE, x, y, z, 0.0D, 0.1D, 0.0D);
    }

    // Apply song effects to nearby players
    for (Player player :
        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10.0D))) {
      player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 1, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 250, 1, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 150, 0, false, false));
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ELDER_GUARDIAN_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.ELDER_GUARDIAN_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ELDER_GUARDIAN_DEATH;
  }

  @Override
  protected void dropCustomDeathLoot(
      @Nonnull DamageSource damageSource, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(damageSource, looting, recentlyHit);

    // Boss drops
    if (this.random.nextFloat() < 0.85F) {
      this.spawnAtLocation(Items.EMERALD, 4 + this.random.nextInt(3));
    }
    if (this.random.nextFloat() < 0.65F) {
      this.spawnAtLocation(Items.DIAMOND, 2 + this.random.nextInt(2));
    }
    if (this.random.nextFloat() < 0.45F) {
      this.spawnAtLocation(Items.NETHERITE_SCRAP, 1 + this.random.nextInt(2));
    }
    if (this.random.nextFloat() < 0.25F) {
      this.spawnAtLocation(Items.ENCHANTED_GOLDEN_APPLE, 1);
    }
    if (this.random.nextFloat() < 0.15F) {
      this.spawnAtLocation(Items.HEART_OF_THE_SEA, 1);
    }
  }

  @Override
  public boolean doHurtTarget(@Nonnull Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof Player) {
      // Apply siren effects on successful attack
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false));
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, false, false));
    }
    return flag;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true; // Boss should not despawn
  }
}
