package com.netroaki.chex.entity.boss;

import com.netroaki.chex.entity.SporeCloudEntity;
import com.netroaki.chex.entity.projectile.SporeCloudProjectile;
import com.netroaki.chex.registry.entities.CHEXEntities;
import com.netroaki.chex.registry.items.CHEXItems;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SporeTyrantEntity extends Monster implements PowerableMob {
  private static final EntityDataAccessor<Integer> PHASE =
      SynchedEntityData.defineId(SporeTyrantEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> CHARGING =
      SynchedEntityData.defineId(SporeTyrantEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> SPORE_BURST_COOLDOWN =
      SynchedEntityData.defineId(SporeTyrantEntity.class, EntityDataSerializers.INT);

  private final ServerBossEvent bossEvent =
      (ServerBossEvent)
          (new ServerBossEvent(
              this.getDisplayName(),
              BossEvent.BossBarColor.PURPLE,
              BossEvent.BossBarOverlay.PROGRESS));
  private int spawnMinionCooldown = 0;
  private int chargeCooldown = 0;
  private int phaseTransitionTicks = 0;
  private BlockPos arenaCenter = null;

  public SporeTyrantEntity(EntityType<? extends SporeTyrantEntity> type, Level level) {
    super(type, level);
    this.moveControl = new FlyingMoveControl(this, 20, true);
    this.xpReward = 500;
    this.setNoGravity(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 500.0D)
        .add(Attributes.FOLLOW_RANGE, 40.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 10.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.FLYING_SPEED, 0.6D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(PHASE, 1);
    this.entityData.define(CHARGING, false);
    this.entityData.define(SPORE_BURST_COOLDOWN, 0);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new SporeBurstGoal(this));
    this.goalSelector.addGoal(2, new ChargeAttackGoal(this));
    this.goalSelector.addGoal(3, new SummonMinionsGoal(this));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  protected PathNavigation createNavigation(Level level) {
    FlyingPathNavigation navigation =
        new FlyingPathNavigation(this, level) {
          public boolean isStableDestination(BlockPos pos) {
            return !this.level.getBlockState(pos.below()).isAir();
          }
        };
    navigation.setCanOpenDoors(false);
    navigation.setCanFloat(true);
    navigation.setCanPassDoors(true);
    return navigation;
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level().isClientSide) {
      // Update boss bar
      this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

      // Handle phase transitions
      if (this.phaseTransitionTicks > 0) {
        this.phaseTransitionTicks--;
        if (this.phaseTransitionTicks == 0) {
          this.onPhaseTransitionComplete();
        } else {
          // During transition, hover and be invulnerable
          this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
          this.setInvulnerable(true);
          return;
        }
      } else {
        this.setInvulnerable(false);
      }

      // Update cooldowns
      if (this.spawnMinionCooldown > 0) {
        this.spawnMinionCooldown--;
      } else if (this.getTarget() != null && this.random.nextFloat() < 0.02f) {
        // Chance to summon minions when off cooldown
        this.spawnMinionCooldown = 200 + this.random.nextInt(100);
        this.summonMinions();
      }

      if (this.chargeCooldown > 0) {
        this.chargeCooldown--;
      }

      // Update spore burst cooldown
      if (this.getSporeBurstCooldown() > 0) {
        this.setSporeBurstCooldown(this.getSporeBurstCooldown() - 1);
      } else if (this.getTarget() != null && this.random.nextFloat() < 0.05f) {
        // Chance to use spore burst when off cooldown
        this.setSporeBurstCooldown(100 + this.random.nextInt(50));
        this.performSporeBurst(this.getTarget());
      }

      // Phase transitions based on health
      if (this.getHealth() < this.getMaxHealth() * 0.66f && this.getPhase() == 1) {
        this.transitionToPhase(2);
      } else if (this.getHealth() < this.getMaxHealth() * 0.33f && this.getPhase() == 2) {
        this.transitionToPhase(3);
      }

      // Special abilities based on phase
      if (this.tickCount % 20 == 0) {
        this.updatePhaseAbilities();
      }

      // Spore particle effects - more intense in later phases
      int particleCount = this.getPhase();
      for (int i = 0; i < particleCount; i++) {
        this.spawnSporeParticles();
      }

      // Arena awareness - keep the boss in the arena
      this.enforceArenaBoundaries();
    }
  }

  private void spawnSporeParticles() {
    if (this.level() instanceof ServerLevel serverLevel) {
      for (int i = 0; i < 3; i++) {
        double offsetX = (this.random.nextDouble() - 0.5) * 2.0;
        double offsetY = this.random.nextDouble() * 2.0;
        double offsetZ = (this.random.nextDouble() - 0.5) * 2.0;

        serverLevel.sendParticles(
            ParticleTypes.END_ROD,
            this.getX() + offsetX,
            this.getY() + offsetY,
            this.getZ() + offsetZ,
            1,
            0,
            0,
            0,
            0.05);
      }
    }
  }

  private void transitionToPhase(int newPhase) {
    if (this.getPhase() != newPhase) {
      this.entityData.set(PHASE, newPhase);
      this.phaseTransitionTicks = 40; // 2 seconds of transition time

      // Play transition sound
      this.level()
          .playSound(
              null,
              this.blockPosition(),
              SoundEvents.WARDEN_AGITATED,
              this.getSoundSource(),
              5.0F,
              0.8F + this.random.nextFloat() * 0.4F);

      // Apply phase-specific effects
      switch (newPhase) {
        case 2 -> {
          this.bossEvent.setName(this.getDisplayName().copy().append(" - Phase 2"));
          this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(15.0D);
          this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
          // Spawn some minions when entering phase 2
          this.summonMinions(3);
        }
        case 3 -> {
          this.bossEvent.setName(this.getDisplayName().copy().append(" - Final Phase"));
          this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(20.0D);
          this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
          this.getAttribute(Attributes.ARMOR).setBaseValue(15.0D);
          // Spawn elite minions when entering final phase
          this.summonMinions(5);
        }
      }
    }
  }

  private void updatePhaseAbilities() {
    int phase = this.getPhase();

    // Phase 2: More aggressive minion spawning and spore bursts
    if (phase >= 2) {
      if (this.random.nextFloat() < 0.1f) {
        // Chance to summon additional minions
        this.summonMinions(1 + this.random.nextInt(phase));
      }

      // More frequent spore bursts in phase 2+
      if (this.getSporeBurstCooldown() <= 0 && this.random.nextFloat() < 0.15f) {
        this.setSporeBurstCooldown(80 + this.random.nextInt(40));
        this.performSporeBurst(this.getTarget());
      }
    }

    // Phase 3: Additional abilities
    if (phase >= 3) {
      // Chance to heal from nearby spore clouds
      if (this.random.nextFloat() < 0.1f) {
        this.healFromSpores();
      }

      // More aggressive charge attacks
      if (this.chargeCooldown <= 0 && this.random.nextFloat() < 0.2f) {
        this.chargeCooldown = 80 + this.random.nextInt(40);
        // The ChargeAttackGoal will handle the actual charging
      }
    }
  }

  private void healFromSpores() {
    if (this.level() instanceof ServerLevel level) {
      // Find nearby spore clouds
      AABB area = this.getBoundingBox().inflate(8.0);
      List<SporeCloudEntity> clouds =
          level.getEntitiesOfClass(
              SporeCloudEntity.class, area, cloud -> cloud.distanceToSqr(this) < 64.0);

      if (!clouds.isEmpty()) {
        // Heal for each nearby cloud
        float healAmount = 2.0f * clouds.size();
        this.heal(healAmount);

        // Visual effect
        for (SporeCloudEntity cloud : clouds) {
          // Create healing particles moving from cloud to boss
          Vec3 startPos = cloud.position();
          Vec3 endPos = this.position();
          Vec3 direction = endPos.subtract(startPos).normalize();

          for (int i = 0; i < 5; i++) {
            double progress = i / 5.0;
            Vec3 particlePos =
                startPos.add(
                    direction.x * progress, direction.y * progress, direction.z * progress);

            level.sendParticles(
                ParticleTypes.HEART, particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0, 0);
          }

          // Remove the consumed cloud
          cloud.discard();
        }

        // Play sound
        level.playSound(
            null,
            this.blockPosition(),
            SoundEvents.EVOKER_PREPARE_SUMMON,
            this.getSoundSource(),
            1.0f,
            1.5f);
      }
    }
  }

  private void summonMinions() {
    this.summonMinions(2 + this.random.nextInt(3));
  }

  private void summonMinions(int count) {
    if (this.level().isClientSide) return;

    ServerLevel level = (ServerLevel) this.level();
    int phase = this.getPhase();

    for (int i = 0; i < count; i++) {
      // Calculate spawn position in a circle around the boss
      double angle = this.random.nextDouble() * 2 * Math.PI;
      double distance = 3.0 + this.random.nextDouble() * 3.0;
      double x = this.getX() + Math.cos(angle) * distance;
      double z = this.getZ() + Math.sin(angle) * distance;

      // Find a suitable Y position
      BlockPos.MutableBlockPos pos =
          new BlockPos.MutableBlockPos((int) x, (int) this.getY(), (int) z);

      // Move up or down to find air
      while (!level.getBlockState(pos).isAir() && pos.getY() < this.getY() + 3) {
        pos.move(Direction.UP);
      }

      // Spawn the minion
      EntityType<?> minionType =
          phase >= 3 && this.random.nextFloat() < 0.3f
              ? CHEXEntities.ELITE_SPORELING.get()
              : CHEXEntities.SPORELING.get();

      Entity minion = minionType.create(level);
      if (minion != null) {
        minion.moveTo(
            pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, this.random.nextFloat() * 360.0f, 0.0f);

        // Make minions target the boss's target
        if (minion instanceof Mob mob) {
          mob.setTarget(this.getTarget());
          // Make minions not despawn
          mob.setPersistenceRequired();
        }

        level.addFreshEntity(minion);

        // Spawn effect
        level.sendParticles(
            ParticleTypes.POOF,
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            10,
            0.5,
            0.5,
            0.5,
            0.1);
      }
    }

    // Play summon sound
    level.playSound(
        null,
        this.blockPosition(),
        SoundEvents.EVOKER_PREPARE_SUMMON,
        this.getSoundSource(),
        1.0f,
        0.8f + this.random.nextFloat() * 0.4f);
  }

  private void enforceArenaBoundaries() {
    if (this.arenaCenter == null) return;

    double maxDistance = 32.0; // Arena radius
    double distanceSq =
        this.distanceToSqr(
            this.arenaCenter.getX() + 0.5, this.arenaCenter.getY(), this.arenaCenter.getZ() + 0.5);

    if (distanceSq > maxDistance * maxDistance) {
      // Push back towards arena center
      Vec3 toCenter =
          new Vec3(
                  this.arenaCenter.getX() + 0.5 - this.getX(),
                  0,
                  this.arenaCenter.getZ() + 0.5 - this.getZ())
              .normalize()
              .scale(0.5);

      this.setDeltaMovement(toCenter);

      // Visual effect
      if (this.tickCount % 5 == 0) {
        this.level()
            .addParticle(
                ParticleTypes.CRIT,
                this.getX(),
                this.getY() + 1.0,
                this.getZ(),
                toCenter.x * 0.1,
                0.1,
                toCenter.z * 0.1);
      }
    }
  }

  private void performSporeBurst(LivingEntity target) {
    if (target == null || this.level().isClientSide) return;

    ServerLevel level = (ServerLevel) this.level();
    int phase = this.getPhase();

    // Play sound
    level.playSound(
        null,
        this.blockPosition(),
        SoundEvents.DRAGON_FIREBALL_EXPLODE,
        this.getSoundSource(),
        2.0f,
        0.8f + this.random.nextFloat() * 0.4f);

    // Create multiple projectiles in a spread
    int projectileCount = 3 + phase; // More projectiles in later phases

    for (int i = 0; i < projectileCount; i++) {
      // Calculate direction with some spread
      Vec3 direction =
          target
              .position()
              .add(0, target.getBbHeight() * 0.5, 0)
              .subtract(this.position())
              .normalize()
              .add(
                  (this.random.nextDouble() - 0.5) * 0.5,
                  (this.random.nextDouble() - 0.3) * 0.5,
                  (this.random.nextDouble() - 0.5) * 0.5)
              .normalize();

      // Create and launch the projectile
      SporeCloudProjectile projectile = new SporeCloudProjectile(level, this, direction);
      projectile.setPos(this.getX(), this.getEyeY() - 0.5, this.getZ());
      projectile.shoot(direction.x, direction.y, direction.z, 0.8f, 1.0f);
      level.addFreshEntity(projectile);
    }

    // Visual effect at boss
    for (int i = 0; i < 10; i++) {
      level.sendParticles(
          ParticleTypes.SPORE_BLOSSOM_AIR,
          this.getX(),
          this.getY() + 1.0,
          this.getZ(),
          5,
          0.5,
          1.0,
          0.5,
          0.1);
    }
  }

  private void onPhaseTransitionComplete() {
    // Spawn particles and effects when transition completes
    if (this.level() instanceof ServerLevel serverLevel) {
      for (int i = 0; i < 20; i++) {
        double angle = (i / 20.0) * 2 * Math.PI;
        double x = this.getX() + Math.cos(angle) * 3.0;
        double z = this.getZ() + Math.sin(angle) * 3.0;

        serverLevel.sendParticles(
            ParticleTypes.END_ROD, x, this.getY() + 1.0, z, 5, 0.5, 0.5, 0.5, 0.1);
      }
    }
  }

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

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt("Phase", this.getPhase());
    tag.putInt("PhaseTransitionTicks", this.phaseTransitionTicks);
    tag.putInt("SpawnMinionCooldown", this.spawnMinionCooldown);
    tag.putInt("ChargeCooldown", this.chargeCooldown);
    tag.putInt("SporeBurstCooldown", this.getSporeBurstCooldown());

    if (this.arenaCenter != null) {
      tag.putInt("ArenaCenterX", this.arenaCenter.getX());
      tag.putInt("ArenaCenterY", this.arenaCenter.getY());
      tag.putInt("ArenaCenterZ", this.arenaCenter.getZ());
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    if (tag.contains("Phase")) {
      this.entityData.set(PHASE, tag.getInt("Phase"));
    }
    this.phaseTransitionTicks = tag.getInt("PhaseTransitionTicks");
    this.spawnMinionCooldown = tag.getInt("SpawnMinionCooldown");
    this.chargeCooldown = tag.getInt("ChargeCooldown");
    this.setSporeBurstCooldown(tag.getInt("SporeBurstCooldown"));

    if (tag.contains("ArenaCenterX")) {
      this.arenaCenter =
          new BlockPos(
              tag.getInt("ArenaCenterX"), tag.getInt("ArenaCenterY"), tag.getInt("ArenaCenterZ"));
    }

    // Update boss bar based on phase
    if (this.getPhase() >= 2) {
      this.bossEvent.setName(
          this.getDisplayName()
              .copy()
              .append(this.getPhase() == 2 ? " - Phase 2" : " - Final Phase"));
    }
  }

  @Override
  public void checkDespawn() {
    if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
      this.discard();
    } else {
      super.checkDespawn();
    }
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }

  @Override
  protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(source, looting, recentlyHit);
    // Drop special loot here
    this.spawnAtLocation(new ItemStack(CHEXItems.SPORE_TYRANT_HEART.get()));
  }

  @Override
  public void setCustomName(@Nullable net.minecraft.network.chat.Component name) {
    super.setCustomName(name);
    this.bossEvent.setName(this.getDisplayName());
  }

  // Getters and setters
  public int getPhase() {
    return this.entityData.get(PHASE);
  }

  public boolean isCharging() {
    return this.entityData.get(CHARGING);
  }

  public void setCharging(boolean charging) {
    this.entityData.set(CHARGING, charging);
  }

  public int getSporeBurstCooldown() {
    return this.entityData.get(SPORE_BURST_COOLDOWN);
  }

  public void setSporeBurstCooldown(int cooldown) {
    this.entityData.set(SPORE_BURST_COOLDOWN, cooldown);
  }

  public void setArenaCenter(BlockPos pos) {
    this.arenaCenter = pos;
  }

  public BlockPos getArenaCenter() {
    return this.arenaCenter != null ? this.arenaCenter : this.blockPosition();
  }

  @Override
  public boolean isPowered() {
    return this.getPhase() >= 2;
  }

  // Custom AI Goals
  static class SporeBurstGoal extends Goal {
    private final SporeTyrantEntity tyrant;
    private int burstTicks = 0;

    public SporeBurstGoal(SporeTyrantEntity tyrant) {
      this.tyrant = tyrant;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      if (tyrant.getTarget() == null || tyrant.getSporeBurstCooldown() > 0) {
        return false;
      }

      // Check if target is in range for spore burst
      double distance = tyrant.distanceToSqr(tyrant.getTarget());
      return distance <= 64.0D && distance >= 9.0D && tyrant.random.nextFloat() < 0.1F;
    }

    @Override
    public void start() {
      this.burstTicks = 0;
      tyrant.getNavigation().stop();
    }

    @Override
    public void tick() {
      this.burstTicks++;
      LivingEntity target = tyrant.getTarget();

      if (target != null) {
        // Look at target
        tyrant.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Perform spore burst
        if (this.burstTicks == 10) {
          this.performSporeBurst(target);
        }

        // End the attack after a short delay
        if (this.burstTicks >= 20) {
          tyrant.setSporeBurstCooldown(100 + tyrant.random.nextInt(50));
        }
      }
    }

    private void performSporeBurst(LivingEntity target) {
      if (tyrant.level() instanceof ServerLevel level) {
        // Play sound
        level.playSound(
            null,
            tyrant.blockPosition(),
            SoundEvents.ENDER_DRAGON_GROWL,
            tyrant.getSoundSource(),
            2.0F,
            0.8F + tyrant.random.nextFloat() * 0.4F);

        // Spawn spore cloud projectiles
        int projectiles = 3 + tyrant.random.nextInt(tyrant.getPhase());
        for (int i = 0; i < projectiles; i++) {
          // Calculate direction to target with some spread
          Vec3 toTarget =
              target
                  .position()
                  .subtract(tyrant.position())
                  .normalize()
                  .add(
                      (tyrant.random.nextDouble() - 0.5) * 0.5,
                      (tyrant.random.nextDouble() - 0.5) * 0.5,
                      (tyrant.random.nextDouble() - 0.5) * 0.5)
                  .normalize();

          // Create and spawn projectile
          // Note: You'll need to create a SporeCloudProjectile class
          // SporeCloudProjectile projectile = new SporeCloudProjectile(level, tyrant, toTarget);
          // projectile.setPos(tyrant.getX(), tyrant.getEyeY() - 0.5, tyrant.getZ());
          // level.addFreshEntity(projectile);
        }
      }
    }

    @Override
    public boolean canContinueToUse() {
      return this.burstTicks < 20 && tyrant.getTarget() != null && !tyrant.getNavigation().isDone();
    }

    @Override
    public void stop() {
      tyrant.setCharging(false);
    }
  }

  static class ChargeAttackGoal extends Goal {
    private final SporeTyrantEntity tyrant;
    private int chargeTime;
    private Vec3 chargeDirection;
    private int chargeTicks;
    private static final int MAX_CHARGE_TIME = 40;
    private static final int MAX_CHARGE_TICKS = 30;
    private static final double CHARGE_SPEED = 2.0;
    private static final double CHARGE_RANGE = 12.0;
    private static final double CHARGE_DAMAGE = 15.0;
    private static final double KNOCKBACK_STRENGTH = 1.5;
    private boolean hasPlayedChargeSound = false;

    public ChargeAttackGoal(SporeTyrantEntity tyrant) {
      this.tyrant = tyrant;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      if (tyrant.chargeCooldown > 0 || tyrant.getTarget() == null || tyrant.getPhase() < 2) {
        return false;
      }

      // Only charge if target is within range but not too close
      double distance = tyrant.distanceToSqr(tyrant.getTarget());
      return distance > 16.0D && distance < 100.0D && tyrant.random.nextFloat() < 0.1F;
    }

    @Override
    public void start() {
      this.chargeTime = 0;
      this.chargeTicks = 0;
      this.chargeDirection = null;
      this.hasPlayedChargeSound = false;
      tyrant.setCharging(true);
      tyrant.getNavigation().stop();

      // Play windup sound
      tyrant
          .level()
          .playSound(
              null,
              tyrant.blockPosition(),
              SoundEvents.RAID_HORN.value(),
              SoundSource.HOSTILE,
              1.5F,
              0.7F);
    }

    @Override
    public void tick() {
      LivingEntity target = tyrant.getTarget();
      if (target == null) {
        return;
      }

      // Face the target during windup
      tyrant.getLookControl().setLookAt(target, 30.0F, 30.0F);

      if (chargeDirection == null) {
        // Windup phase
        chargeTime++;

        // Play windup particles
        if (tyrant.level() instanceof ServerLevel serverLevel) {
          for (int i = 0; i < 3; i++) {
            double offsetX = (tyrant.random.nextDouble() - 0.5) * 2.0;
            double offsetZ = (tyrant.random.nextDouble() - 0.5) * 2.0;

            serverLevel.sendParticles(
                ParticleTypes.CRIT,
                tyrant.getX() + offsetX,
                tyrant.getY() + 1.0,
                tyrant.getZ() + offsetZ,
                1,
                0,
                0,
                0,
                0.1);
          }
        }

        // Start charging after windup
        if (chargeTime >= MAX_CHARGE_TIME) {
          chargeDirection =
              target.position().subtract(tyrant.position()).normalize().scale(CHARGE_SPEED);

          // Play charge sound
          tyrant
              .level()
              .playSound(
                  null,
                  tyrant.blockPosition(),
                  SoundEvents.RAVAGER_ROAR,
                  SoundSource.HOSTILE,
                  1.5F,
                  0.8F);
          this.hasPlayedChargeSound = true;
        }
      } else {
        // Charging phase
        chargeTicks++;

        // Move in the charge direction
        tyrant.setDeltaMovement(chargeDirection);

        // Check for collisions
        AABB hitbox = tyrant.getBoundingBox().inflate(1.0, 0.25, 1.0);
        List<LivingEntity> entities =
            tyrant
                .level()
                .getEntitiesOfClass(
                    LivingEntity.class,
                    hitbox,
                    e -> e != tyrant && e.isAlive() && e.isAttackable());

        // Damage entities in the way
        for (LivingEntity entity : entities) {
          if (entity.hurt(tyrant.damageSources().mobAttack(tyrant), (float) CHARGE_DAMAGE)) {
            // Apply knockback
            Vec3 knockback = chargeDirection.normalize().scale(KNOCKBACK_STRENGTH);
            entity.push(knockback.x, 0.4, knockback.z);

            // Apply effects based on phase
            if (tyrant.getPhase() >= 2) {
              entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
            }
            if (tyrant.getPhase() >= 3) {
              entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
            }
          }
        }

        // Create charge particles
        if (tyrant.level() instanceof ServerLevel serverLevel) {
          for (int i = 0; i < 3; i++) {
            double offsetX = (tyrant.random.nextDouble() - 0.5) * 2.0;
            double offsetZ = (tyrant.random.nextDouble() - 0.5) * 2.0;

            serverLevel.sendParticles(
                ParticleTypes.CRIT,
                tyrant.getX() + offsetX,
                tyrant.getY() + 0.5,
                tyrant.getZ() + offsetZ,
                1,
                0,
                0,
                0,
                0.1);
          }
        }

        // Stop charging after max ticks or hitting a wall
        if (chargeTicks >= MAX_CHARGE_TICKS || tyrant.horizontalCollision) {
          stop();
        }
      }
    }

    @Override
    public boolean canContinueToUse() {
      return chargeTicks < MAX_CHARGE_TICKS
          && tyrant.getTarget() != null
          && !tyrant.horizontalCollision
          && (chargeDirection == null || chargeTicks < MAX_CHARGE_TICKS);
    }

    @Override
    public void stop() {
      tyrant.setCharging(false);
      tyrant.chargeCooldown = 100 + tyrant.random.nextInt(50);

      // Create impact effect when stopping
      if (tyrant.level() instanceof ServerLevel serverLevel) {
        serverLevel.sendParticles(
            ParticleTypes.EXPLOSION,
            tyrant.getX(),
            tyrant.getY(),
            tyrant.getZ(),
            10,
            1.0,
            0.5,
            1.0,
            0.2);

        if (this.hasPlayedChargeSound) {
          serverLevel.playSound(
              null,
              tyrant.blockPosition(),
              SoundEvents.GENERIC_EXPLODE,
              SoundSource.HOSTILE,
              1.0F,
              0.8F + tyrant.random.nextFloat() * 0.4F);
        }
      }
    }
  }

  static class SummonMinionsGoal extends Goal {
    private final SporeTyrantEntity tyrant;
    private int summonCooldown;

    public SummonMinionsGoal(SporeTyrantEntity tyrant) {
      this.tyrant = tyrant;
      this.summonCooldown = 100 + tyrant.random.nextInt(100);
    }

    @Override
    public boolean canUse() {
      if (tyrant.spawnMinionCooldown > 0 || tyrant.getTarget() == null) {
        return false;
      }

      // Only summon if there are few minions nearby
      int maxMinions = 3 + tyrant.getPhase();
      AABB searchArea = tyrant.getBoundingBox().inflate(20.0D);
      long nearbyMinions =
          tyrant
              .level()
              .getEntities(
                  tyrant,
                  searchArea,
                  e ->
                      e.getType() == CHEXEntities.SPOREFLIES.get()
                          || e.getType() == CHEXEntities.GLOWBEAST.get())
              .size();

      return nearbyMinions < maxMinions && tyrant.random.nextFloat() < 0.05F;
    }

    @Override
    public void start() {
      this.summonCooldown = 40 + tyrant.random.nextInt(20);
      tyrant.getNavigation().stop();

      // Play summoning sound
      tyrant
          .level()
          .playSound(
              null,
              tyrant.blockPosition(),
              SoundEvents.EVOKER_PREPARE_SUMMON,
              tyrant.getSoundSource(),
              2.0F,
              0.8F + tyrant.random.nextFloat() * 0.4F);
    }

    @Override
    public void tick() {
      this.summonCooldown--;

      // Spawn minions after a delay
      if (this.summonCooldown <= 0) {
        this.summonMinions();
        tyrant.spawnMinionCooldown = 200 + tyrant.random.nextInt(200);
      }
    }

    private void summonMinions() {
      if (tyrant.level() instanceof ServerLevel level) {
        int minionCount = 2 + tyrant.random.nextInt(tyrant.getPhase() + 1);

        for (int i = 0; i < minionCount; i++) {
          // Calculate spawn position in a circle around the boss
          double angle = (i / (double) minionCount) * 2 * Math.PI;
          double distance = 3.0 + tyrant.random.nextDouble() * 2.0;
          double x = tyrant.getX() + Math.cos(angle) * distance;
          double z = tyrant.getZ() + Math.sin(angle) * distance;

          // Find a suitable Y position
          BlockPos pos = new BlockPos((int) x, (int) tyrant.getY(), (int) z);
          while (level.isEmptyBlock(pos) && pos.getY() > level.getMinBuildHeight()) {
            pos = pos.below();
          }

          // Create minion (50% chance for each type)
          Entity minion;
          if (tyrant.random.nextBoolean()) {
            minion = CHEXEntities.SPOREFLIES.get().create(level);
          } else {
            minion = CHEXEntities.GLOWBEAST.get().create(level);
          }

          if (minion != null) {
            minion.moveTo(
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                tyrant.random.nextFloat() * 360.0F,
                0.0F);

            // Set minion properties
            if (minion instanceof Mob mob) {
              mob.finalizeSpawn(
                  level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
              mob.setTarget(tyrant.getTarget());
            }

            // Spawn minion with particle effects
            level.addFreshEntity(minion);
            level.sendParticles(
                ParticleTypes.POOF,
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                20,
                0.5,
                0.5,
                0.5,
                0.1);
          }
        }

        // Play summon complete sound
        level.playSound(
            null,
            tyrant.blockPosition(),
            SoundEvents.EVOKER_CAST_SPELL,
            tyrant.getSoundSource(),
            2.0F,
            0.8F + tyrant.random.nextFloat() * 0.4F);
      }
    }

    @Override
    public boolean canContinueToUse() {
      return this.summonCooldown > 0;
    }

    @Override
    public void stop() {
      tyrant.spawnMinionCooldown = 200 + tyrant.random.nextInt(200);
    }
  }
}
