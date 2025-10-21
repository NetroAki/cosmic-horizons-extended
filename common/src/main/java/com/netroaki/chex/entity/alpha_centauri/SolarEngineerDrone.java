package com.netroaki.chex.entity.alpha_centauri;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class SolarEngineerDrone extends Animal {
  private static final EntityDataAccessor<Boolean> DATA_HAS_RESOURCE =
      SynchedEntityData.defineId(SolarEngineerDrone.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> DATA_REPAIR_TIME =
      SynchedEntityData.defineId(SolarEngineerDrone.class, EntityDataSerializers.INT);

  private static final Ingredient TEMPTATION_ITEMS =
      Ingredient.of(Items.IRON_INGOT, Items.GOLD_INGOT);
  private int resourceCooldown = 0;
  private UUID lastInteractedPlayer = null;

  public SolarEngineerDrone(EntityType<? extends Animal> type, Level level) {
    super(type, level);
    this.moveControl = new FlyingMoveControl(this, 20, true);
    this.setNoGravity(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.FLYING_SPEED, 0.4D)
        .add(Attributes.MOVEMENT_SPEED, 0.2D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
    this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, TEMPTATION_ITEMS, false));
    this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_HAS_RESOURCE, true);
    this.entityData.define(DATA_REPAIR_TIME, 0);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.resourceCooldown > 0) {
      this.resourceCooldown--;
      if (this.resourceCooldown <= 0 && !this.hasResource()) {
        this.setHasResource(true);
        this.setRepairTime(0);
      }
    }

    if (this.getRepairTime() > 0) {
      this.setRepairTime(this.getRepairTime() - 1);
      if (this.getRepairTime() <= 0 && !this.hasResource()) {
        this.setHasResource(true);

        // Visual effect when resource is ready
        if (!this.level().isClientSide) {
          for (int i = 0; i < 5; ++i) {
            ((ServerLevel) this.level())
                .sendParticles(
                    net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK,
                    this.getRandomX(0.5D),
                    this.getRandomY(),
                    this.getRandomZ(0.5D),
                    3,
                    0.2D,
                    0.2D,
                    0.2D,
                    0.1D);
          }
        }
      }
    }

    // Gentle floating animation
    if (this.level().isClientSide) {
      this.setYRot(this.getYRot() + 0.5F);
      this.yBodyRot = this.getYRot();

      // Subtle hovering effect
      this.setDeltaMovement(this.getDeltaMovement().multiply(0.9D, 0.9D, 0.9D));
      this.setDeltaMovement(
          this.getDeltaMovement().add(0.0D, Math.sin(this.tickCount * 0.1D) * 0.002D, 0.0D));
    }
  }

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    ItemStack itemstack = player.getItemInHand(hand);

    if (this.hasResource() && TEMPTATION_ITEMS.test(itemstack)) {
      // Player is trying to trade
      if (!this.level().isClientSide) {
        // Give player photonic core
        player.addItem(new ItemStack(Items.PRISMARINE_CRYSTALS, 1 + this.random.nextInt(3)));

        // Consume the resource and start cooldown
        this.setHasResource(false);
        this.resourceCooldown = 6000 + this.random.nextInt(6000); // 5-10 minutes
        this.setRepairTime(100 + this.random.nextInt(100));

        // Play sound
        this.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);

        // Visual effect
        for (int i = 0; i < 8; ++i) {
          ((ServerLevel) this.level())
              .sendParticles(
                  net.minecraft.core.particles.ParticleTypes.END_ROD,
                  this.getRandomX(0.5D),
                  this.getRandomY(),
                  this.getRandomZ(0.5D),
                  1,
                  0.1D,
                  0.1D,
                  0.1D,
                  0.05D);
        }

        // Remember last player who interacted for potential achievements/tracking
        this.lastInteractedPlayer = player.getUUID();

        return InteractionResult.SUCCESS;
      }

      return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    return super.mobInteract(player, hand);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setHasResource(compound.getBoolean("HasResource"));
    this.resourceCooldown = compound.getInt("ResourceCooldown");
    this.setRepairTime(compound.getInt("RepairTime"));
    if (compound.hasUUID("LastInteractedPlayer")) {
      this.lastInteractedPlayer = compound.getUUID("LastInteractedPlayer");
    }
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("HasResource", this.hasResource());
    compound.putInt("ResourceCooldown", this.resourceCooldown);
    compound.putInt("RepairTime", this.getRepairTime());
    if (this.lastInteractedPlayer != null) {
      compound.putUUID("LastInteractedPlayer", this.lastInteractedPlayer);
    }
  }

  @Override
  protected PathNavigation createNavigation(Level level) {
    FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
    flyingpathnavigation.setCanOpenDoors(false);
    flyingpathnavigation.setCanFloat(true);
    flyingpathnavigation.setCanPassDoors(true);
    return flyingpathnavigation;
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENDER_DRONE_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.ENDER_DRONE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENDER_DRONE_DEATH;
  }

  // Getters and setters
  public boolean hasResource() {
    return this.entityData.get(DATA_HAS_RESOURCE);
  }

  public void setHasResource(boolean hasResource) {
    this.entityData.set(DATA_HAS_RESOURCE, hasResource);
  }

  public int getRepairTime() {
    return this.entityData.get(DATA_REPAIR_TIME);
  }

  public void setRepairTime(int repairTime) {
    this.entityData.set(DATA_REPAIR_TIME, repairTime);
  }

  @Override
  public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
    return null; // Not breedable
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return TEMPTATION_ITEMS.test(stack);
  }

  @Override
  public boolean isFlying() {
    return true;
  }
}
