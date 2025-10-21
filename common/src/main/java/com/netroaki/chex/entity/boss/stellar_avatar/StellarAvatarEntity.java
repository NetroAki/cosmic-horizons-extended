package com.netroaki.chex.entity.boss.stellar_avatar;

import com.netroaki.chex.entity.boss.stellar_avatar.ai.StellarAvatarMeleeAttackGoal;
import com.netroaki.chex.entity.boss.stellar_avatar.ai.StellarAvatarPhaseGoal;
import com.netroaki.chex.entity.boss.stellar_avatar.ai.StellarAvatarRandomLookGoal;
import com.netroaki.chex.entity.boss.stellar_avatar.ai.StellarAvatarRangedAttackGoal;
import com.netroaki.chex.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class StellarAvatarEntity extends Monster {
  private static final EntityDataAccessor<Integer> DATA_PHASE =
      SynchedEntityData.defineId(StellarAvatarEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> DATA_SHIELDED =
      SynchedEntityData.defineId(StellarAvatarEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> DATA_ATTACK_TIMER =
      SynchedEntityData.defineId(StellarAvatarEntity.class, EntityDataSerializers.INT);

  private final ServerBossEvent bossEvent =
      (ServerBossEvent)
          (new ServerBossEvent(
              this.getDisplayName(),
              BossEvent.BossBarColor.RED,
              BossEvent.BossBarOverlay.PROGRESS));
  private int phaseTransitionTimer = 0;
  private int attackCooldown = 0;
  private int invulnerableTicks = 0;

  public StellarAvatarEntity(EntityType<? extends StellarAvatarEntity> type, Level level) {
    super(type, level);
    this.xpReward = 1000;
    this.setHealth(this.getMaxHealth());
    this.bossEvent.setVisible(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 1000.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 10.0D)
        .add(Attributes.FOLLOW_RANGE, 50.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
  }

  @Override
  protected void registerGoals() {
    // Main AI goals
    this.goalSelector.addGoal(1, new StellarAvatarPhaseGoal(this));
    this.goalSelector.addGoal(2, new StellarAvatarMeleeAttackGoal(this, 1.0D, false));
    this.goalSelector.addGoal(3, new StellarAvatarRangedAttackGoal(this, 1.0D, 40, 20.0F));
    this.goalSelector.addGoal(4, new StellarAvatarRandomLookGoal(this));

    // Target selection
    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(
        2,
        new NearestAttackableTargetGoal<>(
            this,
            Mob.class,
            10,
            true,
            false,
            entity -> entity instanceof Enemy && !(entity instanceof StellarAvatarEntity)));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_PHASE, 0);
    this.entityData.define(DATA_SHIELDED, false);
    this.entityData.define(DATA_ATTACK_TIMER, 0);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("Phase", this.getPhase());
    compound.putBoolean("Shielded", this.isShielded());
    compound.putInt("PhaseTransitionTimer", this.phaseTransitionTimer);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setPhase(compound.getInt("Phase"));
    this.setShielded(compound.getBoolean("Shielded"));
    this.phaseTransitionTimer = compound.getInt("PhaseTransitionTimer");
  }

  @Override
  public void tick() {
    super.tick();
    this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

    // Handle phase transitions and other per-tick logic
    if (!this.level().isClientSide) {
      this.updatePhase();
    }

    // Client-side effects
    if (this.level().isClientSide) {
      this.spawnParticles();
      this.updateAnimations();
    }
  }

  protected void updatePhase() {
    // Handle phase transitions
    if (this.phaseTransitionTimer > 0) {
      this.phaseTransitionTimer--;
      if (this.phaseTransitionTimer <= 0) {
        this.completePhaseTransition();
      }
      return;
    }

    // Handle attack cooldown
    if (this.attackCooldown > 0) {
      this.attackCooldown--;
    }

    // Handle invulnerability frames
    if (this.invulnerableTicks > 0) {
      this.invulnerableTicks--;
    }

    // Phase-specific behavior
    this.tickPhase();
  }

  protected void tickPhase() {
    switch (this.getPhase()) {
      case 0 -> this.tickPhase1();
      case 1 -> this.tickPhase2();
      case 2 -> this.tickPhase3();
    }
  }

  protected void tickPhase1() {
    // Solar Flare phase behavior
    if (this.tickCount % 100 == 0) {
      this.performSolarFlareAttack();
    }
  }

  protected void tickPhase2() {
    // Coronal Ejection phase behavior
    if (this.tickCount % 80 == 0) {
      this.performCoronalEjection();
    }
  }

  protected void tickPhase3() {
    // Stellar Core phase behavior
    if (this.tickCount % 60 == 0) {
      this.performNovaStrike();
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return ModSounds.STELLAR_AVATAR_AMBIENT.get();
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return ModSounds.STELLAR_AVATAR_HURT.get();
  }

  @Override
  protected SoundEvent getDeathSound() {
    return ModSounds.STELLAR_AVATAR_DEATH.get();
  }

  @Override
  protected float getSoundVolume() {
    return 2.0F;
  }

  @Override
  public float getVoicePitch() {
    return 0.8F + (this.random.nextFloat() * 0.4F);
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState blockIn) {
    // No step sound for floating boss
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (this.isInvulnerableTo(source) || this.invulnerableTicks > 0) {
      return false;
    }

    if (this.isShielded() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
      // Visual effect when hitting shield
      this.level()
          .addParticle(
              net.minecraft.core.particles.ParticleTypes.FLAME,
              this.getX(),
              this.getY() + this.getBbHeight() * 0.5,
              this.getZ(),
              0.0D,
              0.1D,
              0.0D);
      return false;
    }

    boolean hurt = super.hurt(source, amount);

    if (hurt) {
      // Check for phase transition
      this.checkPhaseTransition();
      // Set invulnerability frames
      this.invulnerableTicks = 10;
    }

    return hurt;
  }

  protected void checkPhaseTransition() {
    float healthRatio = this.getHealth() / this.getMaxHealth();

    if (this.getPhase() == 0 && healthRatio < 0.66F) {
      this.startPhaseTransition(1);
    } else if (this.getPhase() == 1 && healthRatio < 0.33F) {
      this.startPhaseTransition(2);
    }
  }

  protected void startPhaseTransition(int newPhase) {
    this.phaseTransitionTimer = 100; // 5 seconds at 20 ticks per second
    this.setShielded(true);
    this.bossEvent.setColor(BossEvent.BossBarColor.YELLOW);
    this.bossEvent.setName(this.getDisplayName().copy().append(" - Phase " + (newPhase + 1)));

    // Play transition sound
    this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 10.0F, 0.5F);
  }

  protected void completePhaseTransition() {
    int newPhase = this.getPhase() + 1;
    this.setPhase(newPhase);
    this.setShielded(false);

    // Heal a bit on phase transition
    this.heal(this.getMaxHealth() * 0.1F);

    // Update boss bar color based on phase
    BossEvent.BossBarColor color =
        switch (newPhase) {
          case 1 -> BossEvent.BossBarColor.BLUE;
          case 2 -> BossEvent.BossBarColor.PURPLE;
          default -> BossEvent.BossBarColor.RED;
        };
    this.bossEvent.setColor(color);

    // Play phase transition complete sound
    if (!this.level().isClientSide) {
      this.level()
          .playSound(
              null,
              this.getX(),
              this.getY(),
              this.getZ(),
              ModSounds.STELLAR_AVATAR_PHASE_CHANGE.get(),
              SoundSource.HOSTILE,
              2.0F,
              1.0F + (this.random.nextFloat() * 0.2F) - 0.1F);
    }
  }

  // Phase-specific attack methods
  protected void performSolarFlareAttack() {
    if (this.level().isClientSide) return;

    // Create a ring of fire around the boss
    int points = 12;
    double radius = 5.0D;
    double angleIncrement = (2 * Math.PI) / points;

    for (int i = 0; i < points; i++) {
      double angle = i * angleIncrement;
      double x = this.getX() + Math.cos(angle) * radius;
      double z = this.getZ() + Math.sin(angle) * radius;

      // Spawn fire charge entity
      // TODO: Implement custom projectile
    }
  }

  protected void performCoronalEjection() {
    if (this.level().isClientSide) return;

    // Play attack sound
    this.level()
        .playSound(
            null,
            this.getX(),
            this.getY(),
            this.getZ(),
            ModSounds.STELLAR_AVATAR_ATTACK_CORONAL.get(),
            SoundSource.HOSTILE,
            2.5F,
            0.7F + (this.random.nextFloat() * 0.3F));

    // Implement coronal ejection attack
    // TODO: Implement coronal ejection attack
  }

  protected void performNovaStrike() {
    if (this.level().isClientSide) return;

    // Play attack sound
    this.level()
        .playSound(
            null,
            this.getX(),
            this.getY(),
            this.getZ(),
            ModSounds.STELLAR_AVATAR_ATTACK_NOVA.get(),
            SoundSource.HOSTILE,
            3.0F,
            0.6F + (this.random.nextFloat() * 0.2F));

    // Massive AOE attack
    // Implement nova strike attack
  }

  private void spawnParticles() {
    // Base ambient particles
    if (this.tickCount % 2 == 0) {
      for (int i = 0; i < 3; i++) {
        double offsetX = (this.random.nextDouble() - 0.5) * 2.0 * this.getBbWidth();
        double offsetY = this.random.nextDouble() * this.getBbHeight();
        double offsetZ = (this.random.nextDouble() - 0.5) * 2.0 * this.getBbWidth();

        this.level()
            .addParticle(
                ParticleTypes.FLAME,
                this.getX() + offsetX,
                this.getY() + offsetY,
                this.getZ() + offsetZ,
                0,
                0,
                0);

        // Add phase-specific particles
        switch (this.getPhase()) {
          case 1 -> this.spawnPhase1Particles(offsetX, offsetY, offsetZ);
          case 2 -> this.spawnPhase2Particles(offsetX, offsetY, offsetZ);
          case 3 -> this.spawnPhase3Particles(offsetX, offsetY, offsetZ);
        }
      }
    }

    // Phase transition effects
    if (this.phaseTransitionTimer > 0) {
      this.spawnTransitionParticles();
    }
  }

  private void spawnPhase1Particles(double offsetX, double offsetY, double offsetZ) {
    // Yellow-orange flame particles for phase 1
    if (this.random.nextFloat() < 0.3f) {
      this.level()
          .addParticle(
              ParticleTypes.FLAME,
              this.getX() + offsetX,
              this.getY() + offsetY,
              this.getZ() + offsetZ,
              (this.random.nextDouble() - 0.5) * 0.1,
              (this.random.nextDouble() - 0.5) * 0.1,
              (this.random.nextDouble() - 0.5) * 0.1);
    }
  }

  private void spawnPhase2Particles(double offsetX, double offsetY, double offsetZ) {
    // Red-orange particles with occasional smoke for phase 2
    if (this.random.nextFloat() < 0.4f) {
      this.level()
          .addParticle(
              this.random.nextBoolean() ? ParticleTypes.LAVA : ParticleTypes.FLAME,
              this.getX() + offsetX,
              this.getY() + offsetY,
              this.getZ() + offsetZ,
              0,
              0.1,
              0);
    }
  }

  private void spawnPhase3Particles(double offsetX, double offsetY, double offsetZ) {
    // White-hot particles with smoke and explosion effects for phase 3
    if (this.random.nextFloat() < 0.5f) {
      this.level()
          .addParticle(
              this.random.nextBoolean() ? ParticleTypes.FLASH : ParticleTypes.SOUL_FIRE_FLAME,
              this.getX() + offsetX,
              this.getY() + offsetY,
              this.getZ() + offsetZ,
              (this.random.nextDouble() - 0.5) * 0.2,
              (this.random.nextDouble() - 0.5) * 0.2,
              (this.random.nextDouble() - 0.5) * 0.2);
    }
  }

  private void spawnTransitionParticles() {
    // Create a ring of particles during phase transitions
    int particles = 20;
    double radius = this.getBbWidth() * 1.5;
    double height = this.getBbHeight() * 1.2;

    for (int i = 0; i < particles; i++) {
      double angle = (2 * Math.PI * i) / particles;
      double x = this.getX() + Math.cos(angle) * radius;
      double z = this.getZ() + Math.sin(angle) * radius;

      this.level().addParticle(ParticleTypes.FLASH, x, this.getY() + height, z, 0, 0.1, 0);
    }
  }

  private void updateAnimations() {
    // Update any client-side animations
    // This will be called every tick on the client side
  }

  // Getters and setters
  public int getPhase() {
    return this.entityData.get(DATA_PHASE);
  }

  public void setPhase(int phase) {
    this.entityData.set(DATA_PHASE, phase);
  }

  public boolean isShielded() {
    return this.entityData.get(DATA_SHIELDED);
  }

  protected void setShielded(boolean shielded) {
    boolean wasShielded = this.isShielded();
    this.entityData.set(DATA_SHIELDED, shielded);

    // Play shield sound effect when shield state changes
    if (!this.level().isClientSide && wasShielded != shielded) {
      if (shielded) {
        this.level()
            .playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                ModSounds.STELLAR_AVATAR_SHIELD_ACTIVATE.get(),
                SoundSource.HOSTILE,
                2.0F,
                1.0F);
      } else {
        this.level()
            .playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                ModSounds.STELLAR_AVATAR_SHIELD_BREAK.get(),
                SoundSource.HOSTILE,
                2.0F,
                0.8F);
      }
    }
  }

  public int getAttackTimer() {
    return this.entityData.get(DATA_ATTACK_TIMER);
  }

  public void setAttackTimer(int timer) {
    this.entityData.set(DATA_ATTACK_TIMER, timer);
  }

  // Boss bar handling
  @Override
  public void startSeenByPlayer(ServerPlayer player) {
    super.startSeenByPlayer(player);
    this.bossEvent.addPlayer(player);
  }

  @Override
  public void stopSeenByPlayer(ServerPlayer player) {
    super.stopSeenByPlayer(player);
    this.bossEvent.removePlayer(player);
  }
}
