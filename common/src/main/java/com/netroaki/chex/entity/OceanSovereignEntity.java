package com.netroaki.chex.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class OceanSovereignEntity extends Monster {
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(OceanSovereignEntity.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_CHARGING = SynchedEntityData
            .defineId(OceanSovereignEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CHARGE_COOLDOWN = SynchedEntityData
            .defineId(OceanSovereignEntity.class, EntityDataSerializers.INT);

    private final BossEvent bossEvent = (BossEvent) (new BossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE,
            BossEvent.BossBarOverlay.PROGRESS));
    private int attackTimer;
    private int summonCooldown;

    public OceanSovereignEntity(EntityType<? extends OceanSovereignEntity> type, Level world) {
        super(type, world);
        this.moveControl = new OceanSovereignMoveControl(this);
        this.xpReward = 500;
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.ARMOR, 10.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new OceanSovereignChargeAttackGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, 0);
        this.entityData.define(IS_CHARGING, false);
        this.entityData.define(CHARGE_COOLDOWN, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Phase", this.getPhase());
        compound.putBoolean("Charging", this.isCharging());
        compound.putInt("ChargeCooldown", this.getChargeCooldown());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setPhase(compound.getInt("Phase"));
        this.setCharging(compound.getBoolean("Charging"));
        this.setChargeCooldown(compound.getInt("ChargeCooldown"));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            if (this.attackTimer > 0) {
                --this.attackTimer;
            }

            if (this.summonCooldown > 0) {
                --this.summonCooldown;
            }

            if (this.getChargeCooldown() > 0) {
                this.setChargeCooldown(this.getChargeCooldown() - 1);
            }

            // Phase transitions
            float healthPercent = this.getHealth() / this.getMaxHealth();
            if (healthPercent < 0.3f && this.getPhase() < 2) {
                this.setPhase(2);
            } else if (healthPercent < 0.6f && this.getPhase() < 1) {
                this.setPhase(1);
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
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
    public void remove(Entity.RemovalReason reason) {
        this.bossEvent.removeAllPlayers();
        super.remove(reason);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ELDER_GUARDIAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ELDER_GUARDIAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ELDER_GUARDIAN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        // No step sound as it's a floating entity
    }

    // Custom methods for Ocean Sovereign
    public int getPhase() {
        return this.entityData.get(PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(PHASE, phase);
        this.refreshDimensions();

        // Apply phase-specific modifications
        switch (phase) {
            case 1 -> {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12.0D);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
            }
            case 2 -> {
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(16.0D);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5D);
                this.getAttribute(Attributes.ARMOR).setBaseValue(15.0D);
            }
        }
    }

    public boolean isCharging() {
        return this.entityData.get(IS_CHARGING);
    }

    public void setCharging(boolean charging) {
        this.entityData.set(IS_CHARGING, charging);
    }

    public int getChargeCooldown() {
        return this.entityData.get(CHARGE_COOLDOWN);
    }

    public void setChargeCooldown(int cooldown) {
        this.entityData.set(CHARGE_COOLDOWN, cooldown);
    }

    // Custom AI Goals
    static class OceanSovereignMoveControl extends MoveControl {
        private final OceanSovereignEntity sovereign;
        private int floatDuration;

        public OceanSovereignMoveControl(OceanSovereignEntity entity) {
            super(entity);
            this.sovereign = entity;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.sovereign.getRandom().nextInt(5) + 2;
                    Vec3 vec3d = new Vec3(this.wantedX - this.sovereign.getX(),
                            this.wantedY - this.sovereign.getY(),
                            this.wantedZ - this.sovereign.getZ());
                    double d0 = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.canReach(vec3d, (int) Math.ceil(d0))) {
                        this.sovereign.setDeltaMovement(this.sovereign.getDeltaMovement().add(vec3d.scale(0.1D)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 vec, int steps) {
            // Add collision detection here if needed
            return true;
        }
    }

    static class OceanSovereignChargeAttackGoal extends Goal {
        private final OceanSovereignEntity sovereign;
        private int chargeTime;

        public OceanSovereignChargeAttackGoal(OceanSovereignEntity entity) {
            this.sovereign = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.sovereign.getTarget();
            return target != null && target.isAlive() &&
                    this.sovereign.distanceToSqr(target) < 100.0D &&
                    this.sovereign.getChargeCooldown() <= 0;
        }

        @Override
        public void start() {
            this.chargeTime = 0;
            this.sovereign.setCharging(true);
            this.sovereign.getNavigation().stop();
        }

        @Override
        public void stop() {
            this.sovereign.setCharging(false);
            this.sovereign.setChargeCooldown(100 + this.sovereign.getRandom().nextInt(100));
        }

        @Override
        public void tick() {
            LivingEntity target = this.sovereign.getTarget();
            if (target == null)
                return;

            this.sovereign.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (++this.chargeTime >= 20) {
                // Charge towards the target
                Vec3 vec3d = new Vec3(target.getX() - this.sovereign.getX(),
                        0.0D,
                        target.getZ() - this.sovereign.getZ())
                        .normalize()
                        .scale(2.0D);

                this.sovereign.setDeltaMovement(vec3d.x, 0.2D, vec3d.z);

                if (this.sovereign.distanceToSqr(target) < 4.0D) {
                    target.hurt(this.sovereign.damageSources().mobAttack(this.sovereign),
                            (float) this.sovereign.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5f);
                    this.stop();
                }

                if (this.chargeTime >= 40) {
                    this.stop();
                }
            }
        }
    }
}
