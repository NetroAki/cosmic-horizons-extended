package com.netroaki.chex.entity.alpha_centauri;

import com.netroaki.chex.registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FlareSprite extends Monster {
    private int flarePower = 0;
    
    public FlareSprite(EntityType<? extends FlareSprite> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
        this.xpReward = 8;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.FLYING_SPEED, 0.6D)
            .add(Attributes.MOVEMENT_SPEED, 0.4D)
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            .add(Attributes.FOLLOW_RANGE, 48.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(2, new FlareSpriteMoveRandomGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        
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
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            // Visual effects
            if (this.tickCount % 2 == 0) {
                ((ServerLevel)this.level()).sendParticles(
                    ParticleTypes.FLAME,
                    this.getX() + (this.random.nextDouble() - 0.5D) * 0.5D,
                    this.getY() + this.random.nextDouble() * 1.5D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * 0.5D,
                    1, 0, 0, 0, 0.02D);
                
                ((ServerLevel)this.level()).sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    this.getX(), this.getY() + 0.5D, this.getZ(),
                    1, 0.1D, 0.1D, 0.1D, 0.02D);
            }
            
            // Gain power during solar flares
            if (this.tickCount % 20 == 0 && AlphaCentauriHazardHandler.isFlareActive()) {
                this.flarePower = Math.min(100, this.flarePower + 5);
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D + this.flarePower * 0.05D);
            }
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // More vulnerable to water
        if (source == this.damageSources().drown()) {
            return super.hurt(source, amount * 2.0F);
        }
        // Resistant to fire and energy
        if (source.isFire() || source == this.damageSources().magic()) {
            return super.hurt(source, amount * 0.5F);
        }
        return super.hurt(source, amount);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("FlarePower", this.flarePower);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.flarePower = compound.getInt("FlarePower");
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
    
    // Custom AI Goal for erratic flying movement
    static class FlareSpriteMoveRandomGoal extends Goal {
        private final FlareSprite sprite;
        
        public FlareSpriteMoveRandomGoal(FlareSprite sprite) {
            this.sprite = sprite;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }
        
        @Override
        public boolean canUse() {
            return sprite.getTarget() == null && (sprite.horizontalCollision || sprite.getRandom().nextFloat() < 0.05F);
        }
        
        @Override
        public void tick() {
            double d0 = sprite.getX() + (sprite.getRandom().nextFloat() * 2.0F - 1.0F) * 4.0F;
            double d1 = sprite.getY() + (sprite.getRandom().nextFloat() * 2.0F - 1.0F) * 4.0F;
            double d2 = sprite.getZ() + (sprite.getRandom().nextFloat() * 2.0F - 1.0F) * 4.0F;
            
            // Keep within bounds
            d1 = Mth.clamp(d1, sprite.level().getMinBuildHeight() + 4, sprite.level().getMaxBuildHeight() - 4);
            
            if (sprite.distanceToSqr(d0, d1, d2) > 1.0D) {
                sprite.getNavigation().moveTo(d0, d1, d2, 1.0D);
            }
        }
    }
}
