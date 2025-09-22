package com.netroaki.chex.entity.alpha_centauri;

import com.netroaki.chex.registry.ModEntities;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class PlasmaWraith extends Monster {
    private static final EntityDataAccessor<Integer> DATA_CHARGE_LEVEL = SynchedEntityData.defineId(PlasmaWraith.class, EntityDataSerializers.INT);
    private int chargeCooldown = 0;
    
    public PlasmaWraith(EntityType<? extends PlasmaWraith> type, Level level) {
        super(type, level);
        this.xpReward = 15;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 60.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.ATTACK_DAMAGE, 6.0D)
            .add(Attributes.ARMOR, 4.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PlasmaChargeGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CHARGE_LEVEL, 0);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            // Update charge cooldown
            if (chargeCooldown > 0) {
                chargeCooldown--;
            }
            
            // Visual effects
            if (this.tickCount % 5 == 0) {
                ((ServerLevel)this.level()).sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    this.getX(), this.getY() + 1.0D, this.getZ(),
                    2, 0.2D, 0.2D, 0.2D, 0.0D);
            }
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // 25% chance to absorb energy attacks
        if (source.isFire() && this.random.nextFloat() < 0.25F) {
            this.heal(amount * 0.5F);
            return false;
        }
        return super.hurt(source, amount);
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BLAZE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ChargeCooldown", this.chargeCooldown);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.chargeCooldown = compound.getInt("ChargeCooldown");
    }
    
    // Custom AI Goal for plasma charging attack
    static class PlasmaChargeGoal extends Goal {
        private final PlasmaWraith wraith;
        private int chargeTime;
        
        public PlasmaChargeGoal(PlasmaWraith wraith) {
            this.wraith = wraith;
        }
        
        @Override
        public boolean canUse() {
            LivingEntity target = this.wraith.getTarget();
            return target != null && target.isAlive() && this.wraith.chargeCooldown <= 0;
        }
        
        @Override
        public void start() {
            this.chargeTime = 0;
            this.wraith.getNavigation().stop();
            this.wraith.getLookControl().setLookAt(this.wraith.getTarget(), 180.0F, 0.0F);
        }
        
        @Override
        public void tick() {
            this.chargeTime++;
            LivingEntity target = this.wraith.getTarget();
            
            if (target != null) {
                // Look at target while charging
                this.wraith.getLookControl().setLookAt(target, 30.0F, 30.0F);
                
                if (this.chargeTime == 20) {
                    // Launch plasma ball
                    Vec3 vec3 = this.wraith.getViewVector(1.0F);
                    double d2 = target.getX() - (this.wraith.getX() + vec3.x * 4.0D);
                    double d3 = target.getY(0.5D) - (0.5D + this.wraith.getY(0.5D));
                    double d4 = target.getZ() - (this.wraith.getZ() + vec3.z * 4.0D);
                    
                    PlasmaBallEntity plasma = new PlasmaBallEntity(ModEntities.PLASMA_BALL.get(), this.wraith, 
                        d2, d3, d4, this.wraith.level());
                    plasma.setPos(this.wraith.getX() + vec3.x * 2.0D, 
                        this.wraith.getY(0.5D) + 0.5D, 
                        this.wraith.getZ() + vec3.z * 2.0D);
                    this.wraith.level().addFreshEntity(plasma);
                    
                    this.wraith.chargeCooldown = 100 + this.wraith.random.nextInt(50);
                }
            }
            
            if (this.chargeTime >= 40) {
                this.wraith.chargeCooldown = 100;
            }
        }
        
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
