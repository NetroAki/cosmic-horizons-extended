package com.netroaki.chex.entity.arrakis;

import com.netroaki.chex.registry.CHEXItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class StormHawkEntity extends ArrakisCreatureEntity implements FlyingAnimal {
    private static final EntityDataAccessor<Boolean> IS_DIVING = SynchedEntityData.defineId(StormHawkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_PERCHED = SynchedEntityData.defineId(StormHawkEntity.class, EntityDataSerializers.BOOLEAN);
    
    private BlockPos perchPos;
    private int perchTime;
    private int diveCooldown;
    private int flapTime;
    
    public StormHawkEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new StormHawkLookControl(this);
        this.setNoGravity(true);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 25.0D)
            .add(Attributes.FLYING_SPEED, 0.6D)
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            .add(Attributes.ATTACK_DAMAGE, 4.0D)
            .add(Attributes.FOLLOW_RANGE, 48.0D)
            .add(Attributes.ATTACK_KNOCKBACK, 0.5D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new StormHawkMeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(2, new StormHawkDiveGoal(this));
        this.goalSelector.addGoal(3, new StormHawkWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new StormHawkPerchGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_DIVING, false);
        this.entityData.define(IS_PERCHED, false);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsDiving", this.isDiving());
        tag.putBoolean("IsPerched", this.isPerched());
        tag.putInt("PerchTime", this.perchTime);
        tag.putInt("DiveCooldown", this.diveCooldown);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setDiving(tag.getBoolean("IsDiving"));
        this.setPerched(tag.getBoolean("IsPerched"));
        this.perchTime = tag.getInt("PerchTime");
        this.diveCooldown = tag.getInt("DiveCooldown");
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.setPerched(this.random.nextFloat() < 0.3F);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        
        if (this.level().isClientSide) {
            // Flap wings
            if (this.flapTime > 0) {
                this.flapTime--;
            }
            
            // Add wing particles
            if (!this.isPerched() && this.random.nextInt(5) == 0) {
                for(int i = 0; i < 2; ++i) {
                    this.level().addParticle(
                        ParticleTypes.CLOUD,
                        this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                        this.getY() + this.random.nextDouble() * this.getBbHeight() - 0.25D,
                        this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                        0.0D, -0.1D, 0.0D
                    );
                }
            }
        } else {
            // Server-side logic
            if (this.isPerched()) {
                this.perchTime++;
                
                // Take off after a while
                if (this.perchTime > 200 + this.random.nextInt(600)) {
                    this.setPerched(false);
                    this.perchTime = 0;
                }
            } else {
                // Decrease dive cooldown
                if (this.diveCooldown > 0) {
                    this.diveCooldown--;
                }
                
                // Check for dive opportunities
                if (this.getTarget() != null && this.diveCooldown <= 0 && this.random.nextFloat() < 0.01F) {
                    this.setDiving(true);
                }
            }
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isDiving()) {
            amount *= 1.5F; // Take extra damage while diving
        }
        
        boolean flag = super.hurt(source, amount);
        
        if (flag && this.isPerched()) {
            this.setPerched(false);
        }
        
        return flag;
    }
    
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isPerched()) {
            this.setDeltaMovement(Vec3.ZERO);
            return;
        }
        
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                this.moveRelative(this.getSpeed(), travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.91D));
            }
        }
        
        this.calculateEntityAnimation(false);
    }
    
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean playerKill) {
        super.dropCustomDeathLoot(source, looting, playerKill);
        
        // Drop feathers
        int featherCount = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        this.spawnAtLocation(new ItemStack(CHEXItems.STORM_HAWK_FEATHER.get(), featherCount));
        
        // Drop talons
        if (this.random.nextFloat() < 0.3F + looting * 0.1F) {
            this.spawnAtLocation(new ItemStack(CHEXItems.TALON.get()));
        }
    }
    
    public boolean isDiving() {
        return this.entityData.get(IS_DIVING);
    }
    
    public void setDiving(boolean diving) {
        this.entityData.set(IS_DIVING, diving);
    }
    
    public boolean isPerched() {
        return this.entityData.get(IS_PERCHED);
    }
    
    public void setPerched(boolean perched) {
        boolean wasPerched = this.isPerched();
        this.entityData.set(IS_PERCHED, perched);
        
        if (perched && !wasPerched) {
            this.setNoGravity(true);
            this.setDeltaMovement(Vec3.ZERO);
            this.perchTime = 0;
        } else if (!perched && wasPerched) {
            this.setNoGravity(false);
        }
    }
    
    @Nullable
    public BlockPos getPerchPos() {
        return this.perchPos;
    }
    
    public void setPerchPos(@Nullable BlockPos pos) {
        this.perchPos = pos;
    }
    
    public void onDiveComplete() {
        this.setDiving(false);
        this.diveCooldown = 100 + this.random.nextInt(200);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PARROT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }
    
    @Override
    public boolean isFlying() {
        return !this.isPerched();
    }
    
    // Custom AI Goals
    
    private static class StormHawkMeleeAttackGoal extends MeleeAttackGoal {
        private final StormHawkEntity hawk;
        
        public StormHawkMeleeAttackGoal(StormHawkEntity hawk, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(hawk, speedModifier, followingTargetEvenIfNotSeen);
            this.hawk = hawk;
        }
        
        @Override
        public boolean canUse() {
            return !this.hawk.isPerched() && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.hawk.isPerched() && super.canContinueToUse();
        }
    }
    
    private static class StormHawkDiveGoal extends Goal {
        private final StormHawkEntity hawk;
        private int diveTime;
        
        public StormHawkDiveGoal(StormHawkEntity hawk) {
            this.hawk = hawk;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return this.hawk.isDiving() && this.hawk.getTarget() != null && !this.hawk.isPerched();
        }
        
        @Override
        public boolean canContinueToUse() {
            return this.diveTime > 0 && this.hawk.getTarget() != null && !this.hawk.isPerched();
        }
        
        @Override
        public void start() {
            this.diveTime = 20 + this.hawk.random.nextInt(40);
            this.hawk.getNavigation().stop();
        }
        
        @Override
        public void stop() {
            this.hawk.onDiveComplete();
            this.diveTime = 0;
        }
        
        @Override
        public void tick() {
            if (--this.diveTime <= 0) {
                return;
            }
            
            LivingEntity target = this.hawk.getTarget();
            if (target == null) {
                return;
            }
            
            // Calculate dive vector
            Vec3 diveVec = new Vec3(
                target.getX() - this.hawk.getX(),
                target.getY(0.5D) - this.hawk.getY(),
                target.getZ() - this.hawk.getZ()
            ).normalize().scale(1.5D);
            
            // Apply movement
            this.hawk.setDeltaMovement(diveVec);
            
            // Look at target
            double d0 = target.getX() - this.hawk.getX();
            double d1 = target.getZ() - this.hawk.getZ();
            this.hawk.setYRot(-((float)Mth.atan2(d0, d1)) * (180F / (float)Math.PI));
            this.hawk.yBodyRot = this.hawk.getYRot();
            
            // Check for hit
            if (this.hawk.getBoundingBox().inflate(1.0D).intersects(target.getBoundingBox())) {
                this.hawk.doHurtTarget(target);
                this.stop();
            }
        }
    }
    
    private static class StormHawkWanderGoal extends Goal {
        private final StormHawkEntity hawk;
        private final double speedModifier;
        
        public StormHawkWanderGoal(StormHawkEntity hawk, double speedModifier) {
            this.hawk = hawk;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return !this.hawk.isPerched() && 
                   !this.hawk.isDiving() && 
                   this.hawk.getTarget() == null && 
                   this.hawk.getNavigation().isDone();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.hawk.isPerched() && 
                   !this.hawk.isDiving() && 
                   this.hawk.getTarget() == null && 
                   !this.hawk.getNavigation().isDone();
        }
        
        @Override
        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                this.hawk.getNavigation().moveTo(
                    this.hawk.getNavigation().createPath(
                        new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z), 1), 
                    this.speedModifier
                );
            }
        }
        
        @Nullable
        private Vec3 findPos() {
            Vec3 vec32;
            if (this.hawk.isInWater()) {
                vec32 = WaterRandomPos.getPos(this.hawk, 15, 15);
            } else if (this.hawk.isOnGround()) {
                vec32 = LandRandomPos.getPos(this.hawk, 15, 7, this.hawk.getY() + 4);
            } else {
                BlockPos blockpos = this.hawk.blockPosition();
                vec32 = AirAndWaterRandomPos.getPos(this.hawk, 15, 7, -2, this.hawk.getY() + 4, Math.PI / 2, 8, 3);
            }
            
            return vec32 == null ? HoverRandomPos.getPos(this.hawk, 8, 4, this.hawk.getX(), this.hawk.getZ(), Math.PI / 2, 3, 1) : vec32;
        }
    }
    
    private static class StormHawkPerchGoal extends Goal {
        private final StormHawkEntity hawk;
        private BlockPos targetPerch;
        private int timeToRecalcPath;
        private int timeTryingToReachPerch;
        
        public StormHawkPerchGoal(StormHawkEntity hawk) {
            this.hawk = hawk;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            if (this.hawk.isPerched() || this.hawk.isDiving() || this.hawk.getTarget() != null) {
                return false;
            }
            
            if (this.hawk.random.nextFloat() < 0.1F) {
                this.targetPerch = this.findPerch();
                return this.targetPerch != null;
            }
            
            return false;
        }
        
        @Override
        public boolean canContinueToUse() {
            return !this.hawk.getNavigation().isDone() && 
                   this.timeTryingToReachPerch < 100 && 
                   !this.hawk.isPerched() && 
                   this.targetPerch != null && 
                   !this.hawk.level().isEmptyBlock(this.targetPerch);
        }
        
        @Override
        public void start() {
            this.timeTryingToReachPerch = 0;
            this.timeToRecalcPath = 0;
            this.hawk.setPerchPos(this.targetPerch);
            this.moveToPerch();
        }
        
        @Override
        public void stop() {
            if (this.hawk.distanceToSqr(Vec3.atCenterOf(this.targetPerch)) < 4.0D) {
                this.hawk.setPerched(true);
            }
            
            this.hawk.setPerchPos(null);
            this.hawk.getNavigation().stop();
        }
        
        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                this.moveToPerch();
            }
            
            this.timeTryingToReachPerch++;
        }
        
        private void moveToPerch() {
            if (this.targetPerch != null) {
                this.hawk.getNavigation().moveTo(
                    this.hawk.getNavigation().createPath(this.targetPerch, 1), 1.0D
                );
            }
        }
        
        @Nullable
        private BlockPos findPerch() {
            BlockPos blockpos = this.hawk.blockPosition();
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            
            for(int i = 0; i < 10; ++i) {
                int j = blockpos.getX() + this.hawk.random.nextInt(20) - 10;
                int k = blockpos.getY() + this.hawk.random.nextInt(6) - 3;
                int l = blockpos.getZ() + this.hawk.random.nextInt(20) - 10;
                
                if (k > this.hawk.level().getMinBuildHeight() + 2) {
                    mutablePos.set(j, k, l);
                    if (this.isValidPerch(this.hawk.level(), mutablePos)) {
                        return mutablePos.immutable();
                    }
                }
            }
            
            return null;
        }
        
        private boolean isValidPerch(Level level, BlockPos pos) {
            return !level.isEmptyBlock(pos) && 
                   level.isEmptyBlock(pos.above()) && 
                   level.isEmptyBlock(pos.above(2)) &&
                   level.getBlockState(pos).isCollisionShapeFullBlock(level, pos);
        }
    }
    
    private static class StormHawkLookControl extends LookControl {
        public StormHawkLookControl(Mob mob) {
            super(mob);
        }
        
        @Override
        public void tick() {
            if (!((StormHawkEntity)this.mob).isPerched()) {
                super.tick();
            }
        }
    }
    
    public static boolean checkStormHawkSpawnRules(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return pos.getY() > level.getSeaLevel() && 
               level.canSeeSky(pos) && 
               (level.getBlockState(pos.below()).isSolid() || level.getBlockState(pos.below(2)).isSolid());
    }
}
