package com.netroaki.chex.entity.boss;

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
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
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

/**
 * The Spore Tyrant is a powerful fungal boss that inhabits the deepest parts of Pandora's fungal
 * forests. It uses spore-based attacks and can summon minions to aid in battle.
 */
public class SporeTyrant extends Monster implements PowerableMob {
  private static final EntityDataAccessor<Integer> PHASE =
      SynchedEntityData.defineId(SporeTyrant.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> CHARGING =
      SynchedEntityData.defineId(SporeTyrant.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> SPORE_BURST_COOLDOWN =
      SynchedEntityData.defineId(SporeTyrant.class, EntityDataSerializers.INT);

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

  public SporeTyrant(EntityType<? extends SporeTyrant> type, Level level) {
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
        }
      }

      // Update cooldowns
      if (this.spawnMinionCooldown > 0) {
        this.spawnMinionCooldown--;
      }

      if (this.chargeCooldown > 0) {
        this.chargeCooldown--;
      }

      // Update spore burst cooldown
      if (this.getSporeBurstCooldown() > 0) {
        this.setSporeBurstCooldown(this.getSporeBurstCooldown() - 1);
      }

      // Phase transitions based on health
      if (this.getHealth() < this.getMaxHealth() * 0.66f && this.getPhase() == 1) {
        this.transitionToPhase(2);
      } else if (this.getHealth() < this.getMaxHealth() * 0.33f && this.getPhase() == 2) {
        this.transitionToPhase(3);
      }

      // Spore particle effects
      if (this.tickCount % 5 == 0) {
        this.spawnSporeParticles();
      }
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
        }
        case 3 -> {
          this.bossEvent.setName(this.getDisplayName().copy().append(" - Final Phase"));
          this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(20.0D);
          this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
          this.getAttribute(Attributes.ARMOR).setBaseValue(15.0D);
        }
      }
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
    private final SporeTyrant tyrant;
    private int burstTicks = 0;

    public SporeBurstGoal(SporeTyrant tyrant) {
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
    private final SporeTyrant tyrant;
    private int chargeTime;

    public ChargeAttackGoal(SporeTyrant tyrant) {
      this.tyrant = tyrant;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      if (tyrant.chargeCooldown > 0 || tyrant.getTarget() == null) {
        return false;
      }

      // Only charge if target is somewhat far away
      double distance = tyrant.distanceToSqr(tyrant.getTarget());
      return distance > 16.0D && distance < 100.0D && tyrant.random.nextFloat() < 0.1F;
    }

    @Override
    public void start() {
      this.chargeTime = 0;
      tyrant.setCharging(true);
      tyrant.getNavigation().stop();
    }

    @Override
    public void tick() {
      this.chargeTime++;
      LivingEntity target = tyrant.getTarget();

      if (target != null) {
        // Look at target
        tyrant.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Charge after a short windup
        if (this.chargeTime >= 10) {
          // Calculate direction to target
          Vec3 direction =
              target
                  .position()
                  .subtract(tyrant.position())
                  .normalize()
                  .scale(tyrant.getPhase() * 0.5 + 0.5);

          // Apply velocity
          tyrant.setDeltaMovement(direction);

          // Spawn charge particles
          if (tyrant.level() instanceof ServerLevel level) {
            for (int i = 0; i < 5; i++) {
              level.sendParticles(
                  ParticleTypes.CLOUD,
                  tyrant.getX(),
                  tyrant.getY() + 1.0,
                  tyrant.getZ(),
                  1,
                  0.5,
                  0.5,
                  0.5,
                  0.1);
            }
          }
        }
      }

      // End charge after a short time
      if (this.chargeTime >= 30) {
        tyrant.chargeCooldown = 100 + tyrant.random.nextInt(100);
      }
    }

    @Override
    public boolean canContinueToUse() {
      return this.chargeTime < 30 && tyrant.getTarget() != null && !tyrant.getNavigation().isDone();
    }

    @Override
    public void stop() {
      tyrant.setCharging(false);
    }
  }

  static class SummonMinionsGoal extends Goal {
    private final SporeTyrant tyrant;
    private int summonCooldown;

    public SummonMinionsGoal(SporeTyrant tyrant) {
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
      List<Entity> nearbyEntities =
          tyrant
              .level()
              .getEntities(
                  tyrant,
                  searchArea,
                  e ->
                      e.getType() == CHEXEntities.SPOREFLY.get()
                          || e.getType() == CHEXEntities.GLOWBEAST.get());

      return nearbyEntities.size() < maxMinions && tyrant.random.nextFloat() < 0.05F;
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
          BlockPos.MutableBlockPos pos =
              new BlockPos.MutableBlockPos((int) x, (int) tyrant.getY(), (int) z);
          while (level.isEmptyBlock(pos) && pos.getY() > level.getMinBuildHeight()) {
            pos.move(Direction.DOWN);
          }

          // Create minion (50% chance for each type)
          Entity minion;
          if (tyrant.random.nextBoolean()) {
            minion = CHEXEntities.SPOREFLY.get().create(level);
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
