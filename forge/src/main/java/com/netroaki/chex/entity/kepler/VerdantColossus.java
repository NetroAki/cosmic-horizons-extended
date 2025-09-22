package com.netroaki.chex.entity.kepler;

import com.netroaki.chex.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class VerdantColossus extends Monster implements GeoEntity, PowerableMob {
    private static final EntityDataAccessor<Boolean> DATA_IS_ENRAGED = SynchedEntityData.defineId(VerdantColossus.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_PHASE = SynchedEntityData.defineId(VerdantColossus.class, EntityDataSerializers.INT);
    
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final BossEvent bossEvent = (BossEvent)(new BossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS));
    
    // Animation states
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.verdant_colossus.idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.verdant_colossus.walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("animation.verdant_colossus.attack");
    private static final RawAnimation ROAR_ANIM = RawAnimation.begin().thenPlay("animation.verdant_colossus.roar");
    
    public VerdantColossus(EntityType<? extends VerdantColossus> type, Level level) {
        super(type, level);
        this.xpReward = 1000;
        this.setPersistenceRequired();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new VerdantColossusMeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ENRAGED, false);
        this.entityData.define(DATA_ATTACK_PHASE, 0);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Enraged", this.isEnraged());
        pCompound.putInt("AttackPhase", this.getAttackPhase());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setEnraged(pCompound.getBoolean("Enraged"));
        this.setAttackPhase(pCompound.getInt("AttackPhase"));
    }
    
    @Override
    public void tick() {
        super.tick();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        
        if (!this.level().isClientSide) {
            // Update boss bar position
            this.bossEvent.setVisible(true);
            
            // Handle phase transitions
            float healthPercent = this.getHealth() / this.getMaxHealth();
            if (healthPercent < 0.3F && this.getAttackPhase() < 2) {
                this.setAttackPhase(2);
                this.setEnraged(true);
                this.level().broadcastEntityEvent(this, (byte) 4); // Roar animation
            } else if (healthPercent < 0.6F && this.getAttackPhase() < 1) {
                this.setAttackPhase(1);
                this.level().broadcastEntityEvent(this, (byte) 4); // Roar animation
            }
        }
    }
    
    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            // Roar animation
            this.triggerAnim("attack_controller", "roar");
        } else {
            super.handleEntityEvent(pId);
        }
    }
    
    // Getters and setters for synced data
    public boolean isEnraged() {
        return this.entityData.get(DATA_IS_ENRAGED);
    }
    
    public void setEnraged(boolean enraged) {
        this.entityData.set(DATA_IS_ENRAGED, enraged);
    }
    
    public int getAttackPhase() {
        return this.entityData.get(DATA_ATTACK_PHASE);
    }
    
    public void setAttackPhase(int phase) {
        this.entityData.set(DATA_ATTACK_PHASE, phase);
    }
    
    // Sound overrides
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WARDEN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WARDEN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.WARDEN_STEP, 0.15F, 1.0F);
    }
    
    // Boss bar implementation
    @Override
    public void startSeenByPlayer(ServerPlayer pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossEvent.addPlayer(pPlayer);
    }
    
    @Override
    public void stopSeenByPlayer(ServerPlayer pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossEvent.removePlayer(pPlayer);
    }
    
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        // Initialize boss bar
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }
    
    // GeoEntity implementation
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
            new AnimationController<>(this, "controller", 5, this::predicate)
                .triggerableAnim("roar", ROAR_ANIM)
                .triggerableAnim("attack", ATTACK_ANIM)
        );
    }
    
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event) {
        if (event.isMoving()) {
            return event.setAndContinue(WALK_ANIM);
        }
        return event.setAndContinue(IDLE_ANIM);
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    
    // Custom attack goal for the colossus
    static class VerdantColossusMeleeAttackGoal extends MeleeAttackGoal {
        private final VerdantColossus colossus;
        private int attackStep;
        private int attackTime;
        private int lastSeen;
        
        public VerdantColossusMeleeAttackGoal(VerdantColossus pColossus, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pColossus, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            this.colossus = pColossus;
        }
        
        @Override
        public void start() {
            super.start();
            this.attackStep = 0;
        }
        
        @Override
        public void stop() {
            super.stop();
            this.colossus.setAggressive(false);
            this.attackTime = 0;
        }
        
        @Override
        public void tick() {
            LivingEntity livingentity = this.colossus.getTarget();
            if (livingentity != null) {
                boolean canSee = this.colossus.getSensing().hasLineOfSight(livingentity);
                if (canSee) {
                    this.lastSeen = 0;
                } else {
                    ++this.lastSeen;
                }
                
                double d0 = this.colossus.distanceToSqr(livingentity);
                this.attackTime = Math.max(this.attackTime - 1, 0);
                
                if (d0 <= this.getAttackReachSqr(livingentity) && this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.colossus.swing(InteractionHand.MAIN_HAND);
                    this.colossus.doHurtTarget(livingentity);
                    
                    // Trigger attack animation
                    this.colossus.triggerAnim("attack_controller", "attack");
                    
                    // Special attacks based on phase
                    if (this.colossus.getAttackPhase() > 0) {
                        this.performSpecialAttack(livingentity);
                    }
                }
                
                this.colossus.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }
            
            super.tick();
        }
        
        private void performSpecialAttack(LivingEntity target) {
            // Implement special attacks based on phase
            if (this.colossus.getAttackPhase() >= 1) {
                // Phase 1: Root attack
                this.rootAttack(target);
            }
            
            if (this.colossus.getAttackPhase() >= 2) {
                // Phase 2: Spore cloud
                if (this.colossus.getRandom().nextFloat() < 0.3F) {
                    this.sporeCloudAttack();
                }
            }
        }
        
        private void rootAttack(LivingEntity target) {
            // Create roots around the target
            AABB aabb = target.getBoundingBox().inflate(3.0D, 0.0D, 3.0D);
            BlockPos.betweenClosedStream(aabb).forEach(pos -> {
                if (this.colossus.getRandom().nextFloat() < 0.3F) {
                    // Spawn temporary roots
                    // TODO: Implement root block placement
                }
            });
        }
        
        private void sporeCloudAttack() {
            // Create a cloud of spores
            // TODO: Implement spore cloud effect
        }
        
        @Override
        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return 4.0F + pAttackTarget.getBbWidth();
        }
    }
}
