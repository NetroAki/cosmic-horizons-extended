package com.netroaki.chex.entity.aqua_mundus;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HydrothermalDroneEntity extends AquaticEntity {
  private static final EntityDataAccessor<Integer> HARVEST_COOLDOWN =
      SynchedEntityData.defineId(HydrothermalDroneEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> HAS_MINERALS =
      SynchedEntityData.defineId(HydrothermalDroneEntity.class, EntityDataSerializers.BOOLEAN);

  private BlockPos targetVentPos;
  private int scanCooldown = 0;
  private int harvestTime = 0;

  public HydrothermalDroneEntity(EntityType<? extends HydrothermalDroneEntity> type, Level level) {
    super(type, level);
    this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
    this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return AquaticEntity.createAttributes()
        .add(Attributes.MAX_HEALTH, 30.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.4D)
        .add(Attributes.ARMOR, 8.0D)
        .add(Attributes.ATTACK_DAMAGE, 6.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(HARVEST_COOLDOWN, 0);
    this.entityData.define(HAS_MINERALS, false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
    this.goalSelector.addGoal(1, new FindVentGoal(this));
    this.goalSelector.addGoal(2, new HarvestVentGoal(this));
    this.goalSelector.addGoal(3, new ReturnMineralsGoal(this));
    this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
    this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    this.targetSelector.addGoal(
        2,
        new NearestAttackableTargetGoal<>(
            this,
            Player.class,
            10,
            true,
            false,
            (entity) -> this.isCarryingMinerals() && !this.isTame()));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    if (!this.level().isClientSide) {
      // Update harvest cooldown
      if (this.getHarvestCooldown() > 0) {
        this.setHarvestCooldown(this.getHarvestCooldown() - 1);
      }

      // Scan for vents periodically
      if (this.scanCooldown-- <= 0 && this.targetVentPos == null) {
        this.scanCooldown = 200 + this.random.nextInt(200);
        this.findVent();
      }

      // Emit bubble particles when harvesting
      if (this.harvestTime > 0) {
        this.harvestTime--;
        if (this.harvestTime % 5 == 0) {
          this.level()
              .addPicle(
                  net.minecraft.core.particles.ParticleTypes.BUBBLE_COLUMN_UP,
                  this.getX() + (this.random.nextDouble() - 0.5) * 0.5,
                  this.getY() + 0.5,
                  this.getZ() + (this.random.nextDouble() - 0.5) * 0.5,
                  0,
                  0.1,
                  0);
        }
      }
    }
  }

  private void findVent() {
    if (this.getHarvestCooldown() > 0 || this.isCarryingMinerals()) {
      return;
    }

    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    int range = 16;

    for (int i = 0; i < 10; i++) {
      int x = this.blockPosition().getX() + this.random.nextInt(range * 2) - range;
      int y =
          Mth.clamp(
              this.blockPosition().getY() + this.random.nextInt(16) - 8,
              this.level().getMinBuildHeight() + 1,
              this.level().getMaxBuildHeight() - 1);
      int z = this.blockPosition().getZ() + this.random.nextInt(range * 2) - range;

      pos.set(x, y, z);

      if (this.isVent(pos)) {
        this.targetVentPos = pos.immutable();
        return;
      }
    }
  }

  private boolean isVent(BlockPos pos) {
    // Check if the block at pos is a vent block
    BlockState state = this.level().getBlockState(pos);
    // TODO: Replace with actual vent block check
    return state.is(Blocks.MAGMA_BLOCK) && this.level().getBlockState(pos.above()).is(Blocks.WATER);
  }

  public boolean isCarryingMinerals() {
    return this.entityData.get(HAS_MINERALS);
  }

  public void setCarryingMinerals(boolean carrying) {
    this.entityData.set(HAS_MINERALS, carrying);
  }

  public int getHarvestCooldown() {
    return this.entityData.get(HARVEST_COOLDOWN);
  }

  public void setHarvestCooldown(int cooldown) {
    this.entityData.set(HARVEST_COOLDOWN, cooldown);
  }

  public BlockPos getTargetVentPos() {
    return this.targetVentPos;
  }

  public void setTargetVentPos(BlockPos pos) {
    this.targetVentPos = pos != null ? pos.immutable() : null;
  }

  private void dropMinerals() {
    if (!this.level().isClientSide && this.isCarryingMinerals()) {
      ItemStack minerals =
          new ItemStack(
              Items.IRON_INGOT,
              1 + this.random.nextInt(3)); // TODO: Replace with custom mineral item
      this.spawnAtLocation(minerals);
      this.setCarryingMinerals(false);
      this.setHarvestCooldown(2400 + this.random.nextInt(1200)); // 2-3 minute cooldown
    }
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("HarvestCooldown", this.getHarvestCooldown());
    compound.putBoolean("HasMinerals", this.isCarryingMinerals());

    if (this.targetVentPos != null) {
      compound.putInt("VentX", this.targetVentPos.getX());
      compound.putInt("VentY", this.targetVentPos.getY());
      compound.putInt("VentZ", this.targetVentPos.getZ());
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setHarvestCooldown(compound.getInt("HarvestCooldown"));
    this.setCarryingMinerals(compound.getBoolean("HasMinerals"));

    if (compound.contains("VentX")) {
      this.targetVentPos =
          new BlockPos(
              compound.getInt("VentX"), compound.getInt("VentY"), compound.getInt("VentZ"));
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.CONDUIT_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.CONDUIT_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.CONDUIT_DEATH;
  }

  @Override
  public void die(DamageSource cause) {
    if (!this.level().isClientSide && this.isCarryingMinerals()) {
      this.dropMinerals();
    }
    super.die(cause);
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true;
  }

  @Override
  protected ItemStack getBucketItemStack() {
    return new ItemStack(Items.IRON_INGOT); // TODO: Replace with custom bucket
  }

  public static boolean checkDroneSpawnRules(
      EntityType<HydrothermalDroneEntity> type,
      LevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    return level.getBlockState(pos).is(Blocks.WATER)
        && level.getBlockState(pos.above()).is(Blocks.WATER)
        && pos.getY() < level.getSeaLevel() - 20; // Spawn in deeper waters
  }

  private static class FindVentGoal extends Goal {
    private final HydrothermalDroneEntity drone;
    private int timeToRecalcPath;

    public FindVentGoal(HydrothermalDroneEntity drone) {
      this.drone = drone;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
      return !this.drone.isCarryingMinerals()
          && this.drone.getHarvestCooldown() <= 0
          && this.drone.getTargetVentPos() != null;
    }

    @Override
    public boolean canContinueToUse() {
      return this.canUse()
          && this.drone.distanceToSqr(
                  this.drone.getTargetVentPos().getX() + 0.5,
                  this.drone.getTargetVentPos().getY() + 0.5,
                  this.drone.getTargetVentPos().getZ() + 0.5)
              > 4.0D;
    }

    @Override
    public void start() {
      this.timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
      if (--this.timeToRecalcPath <= 0) {
        this.timeToRecalcPath = 10;
        this.drone
            .getNavigation()
            .moveTo(
                this.drone.getTargetVentPos().getX() + 0.5,
                this.drone.getTargetVentPos().getY() + 0.5,
                this.drone.getTargetVentPos().getZ() + 0.5,
                1.0D);
      }
    }
  }

  private static class HarvestVentGoal extends Goal {
    private final HydrothermalDroneEntity drone;
    private int harvestTicks;

    public HarvestVentGoal(HydrothermalDroneEntity drone) {
      this.drone = drone;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      if (this.drone.isCarryingMinerals() || this.drone.getHarvestCooldown() > 0) {
        return false;
      }

      BlockPos ventPos = this.drone.getTargetVentPos();
      if (ventPos == null) {
        return false;
      }

      return this.drone.distanceToSqr(
              ventPos.getX() + 0.5, ventPos.getY() + 0.5, ventPos.getZ() + 0.5)
          <= 4.0D;
    }

    @Override
    public void start() {
      this.harvestTicks = 100 + this.drone.random.nextInt(100);
      this.drone.getNavigation().stop();
    }

    @Override
    public void stop() {
      this.drone.harvestTime = 0;
    }

    @Override
    public boolean canContinueToUse() {
      return this.harvestTicks > 0
          && !this.drone.isCarryingMinerals()
          && this.drone.getTargetVentPos() != null
          && this.drone
              .level()
              .getBlockState(this.drone.getTargetVentPos())
              .is(Blocks.MAGMA_BLOCK); // TODO: Replace with vent block
    }

    @Override
    public void tick() {
      this.harvestTicks--;
      this.drone.harvestTime = this.harvestTicks;

      if (this.harvestTicks <= 0) {
        this.drone.setCarryingMinerals(true);
        this.drone.setTargetVentPos(null);

        // Small chance to destroy the vent
        if (this.drone.random.nextFloat() < 0.1F) {
          this.drone
              .level()
              .setBlock(this.drone.getTargetVentPos(), Blocks.BASALT.defaultBlockState(), 3);
        }
      }
    }
  }

  private static class ReturnMineralsGoal extends Goal {
    private final HydrothermalDroneEntity drone;
    private BlockPos homePos;
    private int timeToRecalcPath;

    public ReturnMineralsGoal(HydrothermalDroneEntity drone) {
      this.drone = drone;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
      return this.drone.isCarryingMinerals();
    }

    @Override
    public void start() {
      this.timeToRecalcPath = 0;
      this.homePos = this.findSuitableReturnPos();
    }

    private BlockPos findSuitableReturnPos() {
      // Try to find a structure or suitable return point
      // For now, just return to spawn position
      BlockPos spawn = this.drone.level().getSharedSpawnPos();
      return new BlockPos(
          spawn.getX() + this.drone.random.nextInt(16) - 8,
          Mth.clamp(
              spawn.getY(),
              this.drone.level().getMinBuildHeight() + 1,
              this.drone.level().getSeaLevel() - 5),
          spawn.getZ() + this.drone.random.nextInt(16) - 8);
    }

    @Override
    public void tick() {
      if (--this.timeToRecalcPath <= 0) {
        this.timeToRecalcPath = 10;
        this.drone
            .getNavigation()
            .moveTo(
                this.homePos.getX() + 0.5,
                this.homePos.getY() + 0.5,
                this.homePos.getZ() + 0.5,
                1.0D);
      }

      // If close enough to home, drop minerals
      if (this.drone.distanceToSqr(
              this.homePos.getX() + 0.5, this.homePos.getY() + 0.5, this.homePos.getZ() + 0.5)
          < 9.0D) {
        this.drone.dropMinerals();
      }
    }
  }
}
