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

public class WorldheartAvatarEntity extends Monster {

  private int worldPulseCooldown = 0;
  private int phase = 1; // 1 = normal, 2 = enraged, 3 = final

  public WorldheartAvatarEntity(EntityType<? extends Monster> entityType, Level level) {
    super(entityType, level);
    this.setNoAi(false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2.2D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 500.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 25.0D)
        .add(Attributes.FOLLOW_RANGE, 40.0D)
        .add(Attributes.ARMOR, 20.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
  }

  @Override
  public void tick() {
    super.tick();

    // Phase transitions
    if (this.getHealth() <= this.getMaxHealth() * 0.6F && phase == 1) {
      phase = 2;
      this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 1, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 0, false, false));
    }

    if (this.getHealth() <= this.getMaxHealth() * 0.3F && phase == 2) {
      phase = 3;
      this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 2, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 1, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 999999, 0, false, false));
    }

    // World pulse attack
    if (this.worldPulseCooldown <= 0 && this.getTarget() != null) {
      performWorldPulse();
      this.worldPulseCooldown = phase == 3 ? 60 : phase == 2 ? 100 : 150; // Faster in higher phases
    }

    if (this.worldPulseCooldown > 0) {
      this.worldPulseCooldown--;
    }

    // Particle effects
    if (this.level().isClientSide) {
      if (this.random.nextInt(2) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.END_ROD,
                this.getX() + (this.random.nextDouble() - 0.5D) * 6.0D,
                this.getY() + this.random.nextDouble() * 4.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 6.0D,
                0.0D,
                0.0D,
                0.0D);
      }

      if (phase >= 2 && this.random.nextInt(3) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.ENCHANT,
                this.getX() + (this.random.nextDouble() - 0.5D) * 5.0D,
                this.getY() + this.random.nextDouble() * 3.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 5.0D,
                0.0D,
                0.0D,
                0.0D);
      }

      if (phase == 3 && this.random.nextInt(2) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.ANGRY_VILLAGER,
                this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D,
                this.getY() + this.random.nextDouble() * 2.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Apply world effects to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0D))) {
        if (this.random.nextFloat() < 0.05F) {
          player.addEffect(
              new MobEffectInstance(MobEffects.WEAKNESS, 100, phase - 1, false, false));
          player.addEffect(
              new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, phase - 1, false, false));
          player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
        }
      }
    }
  }

  private void performWorldPulse() {
    if (this.level().isClientSide) return;

    // Create world pulse effect
    for (int i = 0; i < 50; i++) {
      double angle = (i / 50.0) * 2 * Math.PI;
      double x = this.getX() + Math.cos(angle) * 12.0D;
      double z = this.getZ() + Math.sin(angle) * 12.0D;
      double y = this.getY() + this.random.nextDouble() * 4.0D;

      this.level().addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    // Apply world effects to nearby players
    for (Player player :
        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(15.0D))) {
      player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, phase, false, false));
      player.addEffect(
          new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 250, phase, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 150, 0, false, false));
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.WITHER_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.WITHER_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.WITHER_DEATH;
  }

  @Override
  protected void dropCustomDeathLoot(
      @Nonnull DamageSource damageSource, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(damageSource, looting, recentlyHit);

    // Ultimate boss drops
    if (this.random.nextFloat() < 0.95F) {
      this.spawnAtLocation(Items.EMERALD, 10 + this.random.nextInt(8));
    }
    if (this.random.nextFloat() < 0.9F) {
      this.spawnAtLocation(Items.DIAMOND, 6 + this.random.nextInt(5));
    }
    if (this.random.nextFloat() < 0.8F) {
      this.spawnAtLocation(Items.NETHERITE_SCRAP, 5 + this.random.nextInt(4));
    }
    if (this.random.nextFloat() < 0.7F) {
      this.spawnAtLocation(Items.ENCHANTED_GOLDEN_APPLE, 2 + this.random.nextInt(2));
    }
    if (this.random.nextFloat() < 0.5F) {
      this.spawnAtLocation(Items.NETHERITE_INGOT, 2 + this.random.nextInt(2));
    }
    if (this.random.nextFloat() < 0.3F) {
      this.spawnAtLocation(Items.NETHERITE_BLOCK, 1);
    }
    if (this.random.nextFloat() < 0.1F) {
      this.spawnAtLocation(Items.NETHER_STAR, 1);
    }
  }

  @Override
  public boolean doHurtTarget(@Nonnull Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof Player) {
      // Apply world effects on successful attack
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, phase, false, false));
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, phase, false, false));
      ((Player) target).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0, false, false));
    }
    return flag;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true; // Boss should not despawn
  }
}
