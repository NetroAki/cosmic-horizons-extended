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

public class MoltenBehemothEntity extends Monster {

  private int lavaBurstCooldown = 0;
  private int phase = 1; // 1 = normal, 2 = enraged

  public MoltenBehemothEntity(EntityType<? extends Monster> entityType, Level level) {
    super(entityType, level);
    this.setNoAi(false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 2.0D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 18.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 400.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 20.0D)
        .add(Attributes.FOLLOW_RANGE, 20.0D)
        .add(Attributes.ARMOR, 15.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
  }

  @Override
  public void tick() {
    super.tick();

    // Phase transition at 35% health
    if (this.getHealth() <= this.getMaxHealth() * 0.35F && phase == 1) {
      phase = 2;
      this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 2, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 1, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 999999, 0, false, false));
    }

    // Lava burst attack
    if (this.lavaBurstCooldown <= 0 && this.getTarget() != null) {
      performLavaBurst();
      this.lavaBurstCooldown = phase == 2 ? 100 : 180; // Faster in enraged phase
    }

    if (this.lavaBurstCooldown > 0) {
      this.lavaBurstCooldown--;
    }

    // Particle effects
    if (this.level().isClientSide) {
      if (this.random.nextInt(2) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.LAVA,
                this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D,
                this.getY() + this.random.nextDouble() * 2.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D,
                0.0D,
                0.0D,
                0.0D);
      }

      if (phase == 2 && this.random.nextInt(2) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.FLAME,
                this.getX() + (this.random.nextDouble() - 0.5D) * 3.0D,
                this.getY() + this.random.nextDouble() * 1.5D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 3.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Apply heat effects to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(6.0D))) {
        if (this.random.nextFloat() < 0.2F) {
          player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, false, false));
          player.addEffect(
              new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, false, false));
        }
      }
    }
  }

  private void performLavaBurst() {
    if (this.level().isClientSide) return;

    // Create lava burst effect
    for (int i = 0; i < 25; i++) {
      double angle = (i / 25.0) * 2 * Math.PI;
      double x = this.getX() + Math.cos(angle) * 6.0D;
      double z = this.getZ() + Math.sin(angle) * 6.0D;
      double y = this.getY() + this.random.nextDouble() * 1.0D;

      this.level().addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    // Apply heat effects to nearby players
    for (Player player :
        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(8.0D))) {
      player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 150, 1, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, false));
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.BLAZE_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.BLAZE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.BLAZE_DEATH;
  }

  @Override
  protected void dropCustomDeathLoot(
      @Nonnull DamageSource damageSource, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(damageSource, looting, recentlyHit);

    // Boss drops
    if (this.random.nextFloat() < 0.9F) {
      this.spawnAtLocation(Items.EMERALD, 6 + this.random.nextInt(4));
    }
    if (this.random.nextFloat() < 0.8F) {
      this.spawnAtLocation(Items.DIAMOND, 3 + this.random.nextInt(3));
    }
    if (this.random.nextFloat() < 0.6F) {
      this.spawnAtLocation(Items.NETHERITE_SCRAP, 2 + this.random.nextInt(2));
    }
    if (this.random.nextFloat() < 0.4F) {
      this.spawnAtLocation(Items.ENCHANTED_GOLDEN_APPLE, 1);
    }
    if (this.random.nextFloat() < 0.2F) {
      this.spawnAtLocation(Items.NETHERITE_INGOT, 1);
    }
    if (this.random.nextFloat() < 0.1F) {
      this.spawnAtLocation(Items.NETHERITE_BLOCK, 1);
    }
  }

  @Override
  public boolean doHurtTarget(@Nonnull Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof Player) {
      // Apply heat effects on successful attack
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120, 0, false, false));
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false));
    }
    return flag;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true; // Boss should not despawn
  }
}
