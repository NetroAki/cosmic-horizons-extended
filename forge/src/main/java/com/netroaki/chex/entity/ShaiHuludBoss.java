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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.BossEvent;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ShaiHuludBoss extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_BURROWED = SynchedEntityData.defineId(ShaiHuludBoss.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(ShaiHuludBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_COOLDOWN = SynchedEntityData.defineId(ShaiHuludBoss.class, EntityDataSerializers.INT);
    
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS));
    private int burrowTime;
    private int emergeTime;
    private int phaseTransitionTimer;
    
    public ShaiHuludBoss(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 1000;
        this.setPersistenceRequired();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 1000.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            .add(Attributes.ATTACK_DAMAGE, 20.0D)
            .add(Attributes.ARMOR, 15.0D)
            .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
            .add(Attributes.FOLLOW_RANGE, 64.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ShaiHuludMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new ShaiHuludBurrowGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_BURROWED, false);
        this.entityData.define(DATA_PHASE, 1);
        this.entityData.define(DATA_ATTACK_COOLDOWN, 0);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsBurrowed", this.isBurrowed());
        tag.putInt("Phase", this.getPhase());
        tag.putInt("AttackCooldown", this.getAttackCooldown());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBurrowed(tag.getBoolean("IsBurrowed"));
        this.setPhase(tag.getInt("Phase"));
        this.setAttackCooldown(tag.getInt("AttackCooldown"));
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
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            
            // Handle attack cooldown
            if (this.getAttackCooldown() > 0) {
                this.setAttackCooldown(this.getAttackCooldown() - 1);
            }
            
            // Handle burrow/emerge cycle
            if (this.isBurrowed()) {
                this.burrowTime++;
                if (this.burrowTime > 100) {
                    this.setBurrowed(false);
                    this.burrowTime = 0;
                    this.performEmergingAttack();
                }
                
                // Spawn sand particles
                if (this.tickCount % 5 == 0) {
                    this.spawnSandParticles();
                }
            } else {
                this.emergeTime++;
                if (this.emergeTime > 200 && this.random.nextFloat() < 0.05F) {
                    this.setBurrowed(true);
                    this.emergeTime = 0;
                }
            }
            
            // Handle phase transitions
            if (this.phaseTransitionTimer > 0) {
                this.phaseTransitionTimer--;
                if (this.phaseTransitionTimer == 0) {
                    this.completePhaseTransition();
                }
            }
            
            // Check for phase transitions based on health
            this.checkPhaseTransition();
        }
    }
    
    private void checkPhaseTransition() {
        float healthRatio = this.getHealth() / this.getMaxHealth();
        int targetPhase = 1;
        
        if (healthRatio < 0.3F) {
            targetPhase = 3;
        } else if (healthRatio < 0.7F) {
            targetPhase = 2;
        }
        
        if (targetPhase > this.getPhase() && this.phaseTransitionTimer == 0) {
            this.initiatePhaseTransition(targetPhase);
        }
    }
    
    private void initiatePhaseTransition(int newPhase) {
        this.phaseTransitionTimer = 100; // 5 second transition
        this.setPhase(newPhase);
        this.setBurrowed(true);
        this.heal(100.0F * newPhase); // Heal during phase transition
        
        // Play sound and effects
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 60 + newPhase);
        }
    }
    
    private void completePhaseTransition() {
        // Apply phase-specific buffs
        switch (this.getPhase()) {
            case 2 -> {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(30.0D);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
            }
            case 3 -> {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(40.0D);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
                this.getAttribute(Attributes.ARMOR).setBaseValue(20.0D);
            }
        }
    }
    
    private void performEmergingAttack() {
        if (this.level().isClientSide) return;
        
        // Create a shockwave effect
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(10.0D))) {
            if (entity instanceof LivingEntity && !(entity instanceof ShaiHuludBoss)) {
                entity.hurt(this.damageSources().mobAttack(this), 10.0F * this.getPhase());
                
                // Launch entities into the air
                double dx = entity.getX() - this.getX();
                double dz = entity.getZ() - this.getZ();
                double distance = Math.sqrt(dx * dx + dz * dz);
                
                if (distance > 0.0D) {
                    double power = 1.5D * this.getPhase();
                    entity.push(
                        (dx / distance) * power,
                        0.5D * this.getPhase(),
                        (dz / distance) * power
                    );
                }
            }
        }
    }
    
    private void spawnSandParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 20; i++) {
                double offsetX = this.random.nextGaussian() * 2.0D;
                double offsetY = this.random.nextGaussian() * 0.5D;
                double offsetZ = this.random.nextGaussian() * 2.0D;
                
                serverLevel.sendParticles(
                    ParticleTypes.FALLING_DUST,
                    this.getX() + offsetX,
                    this.getY() + offsetY,
                    this.getZ() + offsetZ,
                    1, 0, 0, 0, 0.1D
                );
            }
        }
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return this.isBurrowed() || super.isInvulnerableTo(source);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isBurrowed();
    }
    
    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_GROWL;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_DRAGON_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WARDEN_STEP, 2.0F, 0.5F);
    }
    
    // Getters and setters for synced data
    public boolean isBurrowed() {
        return this.entityData.get(DATA_IS_BURROWED);
    }
    
    public void setBurrowed(boolean burrowed) {
        this.entityData.set(DATA_IS_BURROWED, burrowed);
    }
    
    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }
    
    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
    }
    
    public int getAttackCooldown() {
        return this.entityData.get(DATA_ATTACK_COOLDOWN);
    }
    
    public void setAttackCooldown(int cooldown) {
        this.entityData.set(DATA_ATTACK_COOLDOWN, cooldown);
    }
    
    // Custom AI Goals
    static class ShaiHuludMeleeAttackGoal extends MeleeAttackGoal {
        private final ShaiHuludBoss boss;
        
        public ShaiHuludMeleeAttackGoal(ShaiHuludBoss boss, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(boss, speedModifier, followingTargetEvenIfNotSeen);
            this.boss = boss;
        }
        
        @Override
        public boolean canUse() {
            return !boss.isBurrowed() && super.canUse();
        }
        
        @Override
        public boolean canContinueToUse() {
            return !boss.isBurrowed() && super.canContinueToUse();
        }
        
        @Override
        protected void checkAndPerformAttack(LivingEntity target, double distanceSquared) {
            double attackReach = this.getAttackReachSqr(target);
            if (distanceSquared <= attackReach && this.getTicksUntilNextAttack() <= 0) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                
                // Special attack based on phase
                if (boss.getPhase() >= 2) {
                    // Phase 2+ can do an area attack
                    boss.performEmergingAttack();
                }
            }
        }
    }
    
    static class ShaiHuludBurrowGoal extends Goal {
        private final ShaiHuludBoss boss;
        private int burrowCooldown;
        
        public ShaiHuludBurrowGoal(ShaiHuludBoss boss) {
            this.boss = boss;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            if (this.burrowCooldown > 0) {
                this.burrowCooldown--;
                return false;
            }
            
            if (boss.getTarget() == null || boss.isBurrowed()) {
                return false;
            }
            
            // Random chance to burrow, more likely in later phases
            float chance = 0.02F * boss.getPhase();
            return boss.random.nextFloat() < chance;
        }
        
        @Override
        public void start() {
            boss.setBurrowed(true);
            this.burrowCooldown = 100 + boss.random.nextInt(100);
        }
        
        @Override
        public boolean canContinueToUse() {
            return boss.isBurrowed();
        }
    }
}
