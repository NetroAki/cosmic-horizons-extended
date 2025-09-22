package com.netroaki.chex.entity.alpha_centauri;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SolarEngineerDrone extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_REPAIRING = SynchedEntityData.defineId(SolarEngineerDrone.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ENERGY_LEVEL = SynchedEntityData.defineId(SolarEngineerDrone.class, EntityDataSerializers.INT);
    
    private int repairCooldown = 0;
    private BlockPos repairTarget = null;
    
    public SolarEngineerDrone(EntityType<? extends SolarEngineerDrone> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.xpReward = 10;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RepairNearbyGoal(this));
        this.goalSelector.addGoal(2, new SolarEngineerDroneAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_REPAIRING, false);
        this.entityData.define(DATA_ENERGY_LEVEL, 100);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Energy regeneration in sunlight
        if (this.level().isDay() && this.level().canSeeSky(this.blockPosition())) {
            this.setEnergyLevel(Math.min(100, this.getEnergyLevel() + 1));
        }
        
        // Repair cooldown
        if (this.repairCooldown > 0) {
            this.repairCooldown--;
        }
        
        // Visual effects
        if (this.level().isClientSide && this.random.nextInt(5) == 0) {
            this.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                    this.getRandomX(0.5D),
                    this.getRandomY() - 0.25D,
                    this.getRandomZ(0.5D),
                    (this.random.nextDouble() - 0.5D) * 0.1D,
                    (this.random.nextDouble() - 0.5D) * 0.1D,
                    (this.random.nextDouble() - 0.5D) * 0.1D);
        }
    }
    
    @Override
    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity) {
                // Apply a small shock effect
                target.setSecondsOnFire(2);
                
                // Consume energy on attack
                this.setEnergyLevel(this.getEnergyLevel() - 10);
                
                // Play electric shock sound
                this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 0.5F, 1.0F);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsRepairing", this.isRepairing());
        tag.putInt("EnergyLevel", this.getEnergyLevel());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setRepairing(tag.getBoolean("IsRepairing"));
        this.setEnergyLevel(tag.getInt("EnergyLevel"));
    }
    
    public boolean isRepairing() {
        return this.entityData.get(DATA_IS_REPAIRING);
    }
    
    public void setRepairing(boolean repairing) {
        this.entityData.set(DATA_IS_REPAIRING, repairing);
    }
    
    public int getEnergyLevel() {
        return this.entityData.get(DATA_ENERGY_LEVEL);
    }
    
    public void setEnergyLevel(int energy) {
        this.entityData.set(DATA_ENERGY_LEVEL, Math.max(0, Math.min(100, energy)));
    }
    
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        // Drop photonic components and other tech items
        this.spawnAtLocation(new ItemStack(Items.GLOWSTONE_DUST, 2 + this.random.nextInt(3)));
        if (this.random.nextFloat() < 0.3F + looting * 0.1F) {
            this.spawnAtLocation(new ItemStack(Items.GOLD_INGOT, 1));
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BEE_LOOP;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BEE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }
    
    @Override
    public boolean isNoGravity() {
        return true;
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.setEnergyLevel(50 + this.random.nextInt(50));
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    // Custom AI Goal for attacking
    static class SolarEngineerDroneAttackGoal extends MeleeAttackGoal {
        private final SolarEngineerDrone drone;
        private int seeTime;
        
        public SolarEngineerDroneAttackGoal(SolarEngineerDrone drone, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(drone, speedModifier, followingTargetEvenIfNotSeen);
            this.drone = drone;
        }
        
        @Override
        public void start() {
            super.start();
            this.seeTime = 0;
        }
        
        @Override
        public void tick() {
            LivingEntity target = this.drone.getTarget();
            if (target != null) {
                // Only attack if we have enough energy
                if (this.drone.getEnergyLevel() < 20) {
                    this.drone.setTarget(null);
                    return;
                }
                
                double distanceSq = this.drone.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
                boolean canSee = this.drone.getSensing().hasLineOfSight(target);
                
                if (canSee) {
                    this.seeTime++;
                } else {
                    this.seeTime = 0;
                }
                
                // Try to maintain a certain distance
                if (distanceSq < 4.0D) {
                    this.drone.getNavigation().stop();
                    this.drone.getMoveControl().setWantedPosition(
                            target.getX() + (this.drone.random.nextDouble() - 0.5) * 4.0,
                            target.getY() + 2.0,
                            target.getZ() + (this.drone.random.nextDouble() - 0.5) * 4.0,
                            1.0);
                } else {
                    this.drone.getNavigation().moveTo(target, this.speedModifier);
                }
                
                this.drone.getLookControl().setLookAt(target, 30.0F, 30.0F);
                
                // Only attack if we've seen the target for a bit
                if (this.seeTime >= 20) {
                    this.checkAndPerformAttack(target, distanceSq);
                }
            }
        }
    }
    
    // Custom AI Goal for repairing nearby structures
    static class RepairNearbyGoal extends Goal {
        private final SolarEngineerDrone drone;
        private int repairCooldown = 0;
        
        public RepairNearbyGoal(SolarEngineerDrone drone) {
            this.drone = drone;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            // Only repair if not in combat and has energy
            return this.drone.getTarget() == null && 
                   this.drone.getEnergyLevel() > 30 && 
                   this.drone.random.nextFloat() < 0.05F;
        }
        
        @Override
        public void start() {
            this.drone.setRepairing(true);
            this.repairCooldown = 20 + this.drone.random.nextInt(40);
            
            // Find a nearby block to repair (simplified - in a real mod you'd check for damaged blocks)
            BlockPos currentPos = this.drone.blockPosition();
            int range = 16;
            
            for (int i = 0; i < 10; i++) {
                BlockPos checkPos = currentPos.offset(
                    this.drone.random.nextInt(range * 2) - range,
                    this.drone.random.nextInt(8) - 4,
                    this.drone.random.nextInt(range * 2) - range
                );
                
                // In a real implementation, you'd check if the block at checkPos needs repair
                // For now, just pick a random position
                this.drone.repairTarget = checkPos;
                this.drone.getNavigation().moveTo(checkPos.getX() + 0.5, checkPos.getY() + 1, checkPos.getZ() + 0.5, 1.0);
                break;
            }
        }
        
        @Override
        public void tick() {
            if (this.repairCooldown > 0) {
                this.repairCooldown--;
                
                // Visual effect while repairing
                if (this.drone.repairTarget != null && this.drone.random.nextFloat() < 0.2F) {
                    double x = this.drone.repairTarget.getX() + 0.5 + (this.drone.random.nextDouble() - 0.5);
                    double y = this.drone.repairTarget.getY() + 0.5 + (this.drone.random.nextDouble() - 0.5);
                    double z = this.drone.repairTarget.getZ() + 0.5 + (this.drone.random.nextDouble() - 0.5);
                    
                    this.drone.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                            x, y, z,
                            (this.drone.random.nextDouble() - 0.5) * 0.1,
                            (this.drone.random.nextDouble() - 0.5) * 0.1,
                            (this.drone.random.nextDouble() - 0.5) * 0.1);
                }
                
                // Consume energy while repairing
                if (this.drone.tickCount % 20 == 0) {
                    this.drone.setEnergyLevel(this.drone.getEnergyLevel() - 2);
                }
            } else {
                this.drone.setRepairing(false);
                this.drone.repairTarget = null;
            }
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.repairCooldown > 0 && 
                   this.drone.getEnergyLevel() > 10 && 
                   this.drone.getTarget() == null;
        }
        
        @Override
        public void stop() {
            this.drone.setRepairing(false);
            this.drone.repairTarget = null;
        }
    }
}
