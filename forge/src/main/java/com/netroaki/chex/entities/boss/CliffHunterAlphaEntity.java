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
import net.minecraft.world.phys.Vec3;

public class CliffHunterAlphaEntity extends Monster {

  private int leapCooldown = 0;
  private int phase = 1; // 1 = normal, 2 = enraged

  public CliffHunterAlphaEntity(EntityType<? extends Monster> entityType, Level level) {
    super(entityType, level);
    this.setNoAi(false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.8D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.2D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 16.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 300.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.4D)
        .add(Attributes.ATTACK_DAMAGE, 18.0D)
        .add(Attributes.FOLLOW_RANGE, 32.0D)
        .add(Attributes.ARMOR, 12.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D);
  }

  @Override
  public void tick() {
    super.tick();

    // Phase transition at 40% health
    if (this.getHealth() <= this.getMaxHealth() * 0.4F && phase == 1) {
      phase = 2;
      this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 2, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 1, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.JUMP, 999999, 1, false, false));
    }

    // Leap attack
    Entity target = this.getTarget();
    if (this.leapCooldown <= 0 && target != null && this.distanceTo(target) > 3.0D) {
      performLeapAttack();
      this.leapCooldown = phase == 2 ? 40 : 80; // Faster in enraged phase
    }

    if (this.leapCooldown > 0) {
      this.leapCooldown--;
    }

    // Particle effects
    if (this.level().isClientSide) {
      if (this.random.nextInt(4) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.ANGRY_VILLAGER,
                this.getX() + (this.random.nextDouble() - 0.5D) * 3.0D,
                this.getY() + this.random.nextDouble() * 2.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 3.0D,
                0.0D,
                0.0D,
                0.0D);
      }

      if (phase == 2 && this.random.nextInt(3) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.LAVA,
                this.getX() + (this.random.nextDouble() - 0.5D) * 2.0D,
                this.getY() + this.random.nextDouble() * 1.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Apply fear effects to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10.0D))) {
        if (player != null && this.random.nextFloat() < 0.1F) {
          player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 1, false, false));
          player.addEffect(
              new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false));
          player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
        }
      }
    }
  }

  private void performLeapAttack() {
    Entity target = this.getTarget();
    if (target == null) return;

    Vec3 targetPos = target.position();
    Vec3 currentPos = this.position();
    Vec3 direction = targetPos.subtract(currentPos).normalize();

    // Leap towards target
    this.setDeltaMovement(direction.x * 1.5D, 0.8D, direction.z * 1.5D);

    // Create impact particles
    if (this.level().isClientSide) {
      for (int i = 0; i < 15; i++) {
        this.level()
            .addParticle(
                ParticleTypes.CLOUD,
                this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D,
                this.getY(),
                this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Apply effects to nearby players on impact
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(5.0D))) {
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1, false, false));
      }
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.RAVAGER_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.RAVAGER_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.RAVAGER_DEATH;
  }

  @Override
  protected void dropCustomDeathLoot(
      @Nonnull DamageSource damageSource, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(damageSource, looting, recentlyHit);

    // Boss drops
    if (this.random.nextFloat() < 0.9F) {
      this.spawnAtLocation(Items.EMERALD, 5 + this.random.nextInt(4));
    }
    if (this.random.nextFloat() < 0.7F) {
      this.spawnAtLocation(Items.DIAMOND, 2 + this.random.nextInt(3));
    }
    if (this.random.nextFloat() < 0.5F) {
      this.spawnAtLocation(Items.NETHERITE_SCRAP, 2);
    }
    if (this.random.nextFloat() < 0.3F) {
      this.spawnAtLocation(Items.ENCHANTED_GOLDEN_APPLE, 1);
    }
    if (this.random.nextFloat() < 0.1F) {
      this.spawnAtLocation(Items.NETHERITE_INGOT, 1);
    }
  }

  @Override
  public boolean doHurtTarget(@Nonnull Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof Player) {
      // Apply fear effects on successful attack
      ((Player) target).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, false));
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, false, false));
      ((Player) target).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
    }
    return flag;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true; // Boss should not despawn
  }
}
