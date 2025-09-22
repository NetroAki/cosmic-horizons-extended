package com.netroaki.chex.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Sandworm extends ArrakisCreature {
    private static final EntityDataAccessor<Boolean> DATA_IS_BURROWED = SynchedEntityData.defineId(Sandworm.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_EMERGE_COOLDOWN = SynchedEntityData.defineId(Sandworm.class, EntityDataSerializers.INT);
    
    private int burrowTime;
    private int emergeTime;
    
    public Sandworm(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0F);
        this.xpReward = 20;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 150.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            .add(Attributes.ATTACK_DAMAGE, 10.0D)
            .add(Attributes.ARMOR, 8.0D)
            .add(Attributes.ATTACK_KNOCKBACK, 2.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SandwormMeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new SandwormBurrowGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_BURROWED, false);
        this.entityData.define(DATA_EMERGE_COOLDOWN, 0);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsBurrowed", this.isBurrowed());
        tag.putInt("EmergeCooldown", this.getEmergeCooldown());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBurrowed(tag.getBoolean("IsBurrowed"));
        this.setEmergeCooldown(tag.getInt("EmergeCooldown"));
    }
    
    public boolean isBurrowed() {
        return this.entityData.get(DATA_IS_BURROWED);
    }
    
    public void setBurrowed(boolean burrowed) {
        this.entityData.set(DATA_IS_BURROWED, burrowed);
    }
    
    public int getEmergeCooldown() {
        return this.entityData.get(DATA_EMERGE_COOLDOWN);
    }
    
    public void setEmergeCooldown(int cooldown) {
        this.entityData.set(DATA_EMERGE_COOLDOWN, cooldown);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            if (this.getEmergeCooldown() > 0) {
                this.setEmergeCooldown(this.getEmergeCooldown() - 1);
            }
            
            if (this.isBurrowed()) {
                this.burrowTime++;
                if (this.burrowTime > 200) {
                    this.setBurrowed(false);
                    this.burrowTime = 0;
                    this.setEmergeCooldown(100 + this.random.nextInt(100));
                }
                
                // Spawn sand particles when burrowed
                if (this.tickCount % 5 == 0) {
                    this.spawnSandParticles();
                }
            } else {
                this.emergeTime++;
                if (this.emergeTime > 100 && this.random.nextFloat() < 0.1F) {
                    this.setBurrowed(true);
                    this.emergeTime = 0;
                }
            }
            
            // Push entities away when emerging
            if (!this.isBurrowed() && this.tickCount % 5 == 0) {
                this.pushEntities();
            }
        }
    }
    
    private void spawnSandParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = this.position();
            serverLevel.sendParticles(
                ParticleTypes.FALLING_DUST,
                pos.x, pos.y, pos.z,
                20,
                this.getBbWidth() * 0.5, 0.1, this.getBbWidth() * 0.5,
                0.2D
            );
        }
    }
    
    private void pushEntities() {
        this.level().getEntitiesOfClass(
            Entity.class,
            this.getBoundingBox().inflate(2.0D),
            e -> e != this && e.isAlive() && e.isPickable()
        ).forEach(entity -> {
            double d0 = entity.getX() - this.getX();
            double d1 = entity.getZ() - this.getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.1D);
            entity.push(d0 / d2 * 0.4D, 0.2D, d1 / d2 * 0.4D);
            
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.hurt(this.damageSources().mobAttack(this), 5.0F);
            }
        });
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return this.isBurrowed() || super.isInvulnerableTo(source);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WARDEN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WARDEN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WARDEN_STEP, 1.0F, 0.5F);
    }
    
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null; // Sandworms don't breed
    }
    
    @Override
    protected boolean getTemptFood() {
        return false; // Not tempted by food
    }
    
    // Custom AI Goals
    static class SandwormMeleeAttackGoal extends MeleeAttackGoal {
        public SandwormMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }
        
        @Override
        public boolean canUse() {
            return !((Sandworm)this.mob).isBurrowed() && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !((Sandworm)this.mob).isBurrowed() && super.canContinueToUse();
        }
    }
    
    static class SandwormBurrowGoal extends Goal {
        private final Sandworm worm;
        
        public SandwormBurrowGoal(Sandworm worm) {
            this.worm = worm;
        }
        
        @Override
        public boolean canUse() {
            return worm.getEmergeCooldown() <= 0 && worm.random.nextFloat() < 0.02F;
        }
        
        @Override
        public void start() {
            this.worm.setBurrowed(true);
        }
    }
}
