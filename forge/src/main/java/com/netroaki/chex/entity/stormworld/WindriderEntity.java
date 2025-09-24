package com.netroaki.chex.entity.stormworld;

import com.netroaki.chex.world.stormworld.weather.StormworldWeatherManager;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class WindriderEntity extends StormEntity implements FlyingAnimal {
    private static final EntityDataAccessor<Boolean> DATA_IS_GLIDING = SynchedEntityData.defineId(WindriderEntity.class,
            EntityDataSerializers.BOOLEAN);

    private float flapSpeed = 1.0f;
    private float nextFlap = 1.0f;
    private int gustCooldown = 0;
    private Vec3 windDirection = Vec3.ZERO;

    public WindriderEntity(EntityType<? extends StormEntity> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.xpReward = 12;
    }

    public static AttributeSupplier.Builder createWindriderAttributes() {
        return createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new WindriderMeleeAttackGoal(this, 1.4D, true));
        this.goalSelector.addGoal(1, new WindriderWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new GustAttackGoal(this));
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_GLIDING, false);
    }

    @Override
    public void tick() {
        super.tick();

        // Update gliding state based on movement
        if (!this.level().isClientSide) {
            Vec3 motion = this.getDeltaMovement();
            boolean shouldGlide = motion.horizontalDistanceSqr() > 0.01D && this.isFlying();
            if (this.isGliding() != shouldGlide) {
                this.setGliding(shouldGlide);
            }

            // Update wind direction from weather system
            if (this.tickCount % 20 == 0) {
                this.windDirection = StormworldWeatherManager.getWindDirection(this.blockPosition(),
                        this.level().getGameTime());

                // Apply wind force when gliding
                if (this.isGliding()) {
                    double windStrength = StormworldWeatherManager.isStormActive() ? 0.2D : 0.05D;
                    this.setDeltaMovement(this.getDeltaMovement().add(
                            this.windDirection.x * windStrength,
                            this.windDirection.y * windStrength,
                            this.windDirection.z * windStrength));
                }
            }

            // Update gust cooldown
            if (this.gustCooldown > 0) {
                this.gustCooldown--;
            }
        }

        // Wing flapping animation
        if (this.isFlying() && !this.onGround()) {
            this.flapSpeed = Mth.lerp(0.1F, this.flapSpeed, 1.0F);
        } else {
            this.flapSpeed = Mth.lerp(0.1F, this.flapSpeed, 0.0F);
        }

        // Visual effects
        if (this.level().isClientSide && this.isGliding()) {
            this.spawnGlideParticles();
        }
    }

    private void spawnGlideParticles() {
        if (this.random.nextInt(5) == 0) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth();
            double d1 = this.getY() + this.random.nextDouble() * this.getBbHeight() * 0.5;
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth();

            this.level().addParticle(
                    ParticleTypes.CLOUD,
                    d0, d1, d2,
                    0, -0.1, 0);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Apply slow falling when not flapping
        if (this.isFlying() && this.getDeltaMovement().y < 0.0D && !this.isGliding()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }
    }

    public boolean isGliding() {
        return this.entityData.get(DATA_IS_GLIDING);
    }

    public void setGliding(boolean gliding) {
        this.entityData.set(DATA_IS_GLIDING, gliding);
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    public boolean canGustAttack() {
        return this.gustCooldown <= 0 && this.getTarget() != null &&
                this.distanceToSqr(this.getTarget()) < 16.0D && this.random.nextFloat() < 0.1F;
    }

    public void performGustAttack() {
        if (this.level() instanceof ServerLevel serverLevel) {
            // Create a gust of wind that knocks back entities
            AABB aabb = this.getBoundingBox().inflate(5.0D, 2.0D, 5.0D);
            for (Entity entity : serverLevel.getEntities(this, aabb)) {
                if (entity instanceof LivingEntity && entity.isPushable()) {
                    Vec3 pushVec = entity.position().subtract(this.position())
                            .normalize()
                            .add(0.0D, 0.4D, 0.0D)
                            .normalize()
                            .scale(1.5D);

                    entity.push(pushVec.x, pushVec.y, pushVec.z);

                    if (entity instanceof Player player) {
                        player.hurtMarked = true;
                    }

                    // Add particles
                    for (int i = 0; i < 10; i++) {
                        double d0 = entity.getX() + (this.random.nextDouble() - 0.5) * entity.getBbWidth();
                        double d1 = entity.getY() + this.random.nextDouble() * entity.getBbHeight();
                        double d2 = entity.getZ() + (this.random.nextDouble() - 0.5) * entity.getBbWidth();

                        serverLevel.sendParticles(
                                ParticleTypes.CLOUD,
                                d0, d1, d2,
                                1, 0, 0, 0, 0.1);
                    }
                }
            }

            // Play sound
            serverLevel.playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENDER_DRAGON_FLAP,
                    this.getSoundSource(),
                    1.0F,
                    0.8F + this.random.nextFloat() * 0.4F);

            // Set cooldown (shorter during storms)
            this.gustCooldown = StormworldWeatherManager.isStormActive() ? 60 : 120;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Gliding", this.isGliding());
        tag.putInt("GustCooldown", this.gustCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setGliding(tag.getBoolean("Gliding"));
        this.gustCooldown = tag.getInt("GustCooldown");
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false; // Immune to fall damage
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // No fall damage
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    // Custom AI Goals

    static class WindriderWanderGoal extends Goal {
        private final WindriderEntity windrider;
        private double x;
        private double y;
        private double z;
        private final double speedModifier;
        private int tryTicks;
        private boolean stopped;

        public WindriderWanderGoal(WindriderEntity windrider, double speedModifier) {
            this.windrider = windrider;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.windrider.getRandom().nextInt(10) != 0) {
                return false;
            } else if (this.windrider.getTarget() != null) {
                return false;
            } else if (!this.windrider.getMoveControl().hasWanted()) {
                return true;
            } else {
                return this.windrider.getRandom().nextInt(30) == 0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.windrider.getNavigation().isDone() && !this.stopped;
        }

        @Override
        public void start() {
            double d0 = this.windrider.getX() + (this.windrider.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F;
            double d1 = Mth.clamp(
                    this.windrider.getY() + (this.windrider.getRandom().nextInt(16) - 8),
                    this.windrider.level().getMinBuildHeight() + 8,
                    this.windrider.level().getMinBuildHeight() + this.windrider.level().getLogicalHeight() - 8);
            double d2 = this.windrider.getZ() + (this.windrider.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F;

            // Try to find a position in the direction of the wind
            if (this.windrider.isGliding() && this.windrider.windDirection.lengthSqr() > 0.1D) {
                Vec3 windDir = this.windrider.windDirection.normalize().scale(16.0D);
                d0 = this.windrider.getX() + windDir.x;
                d2 = this.windrider.getZ() + windDir.z;
            }

            if (this.windrider.level().getBlockState(BlockPos.containing(d0, d1, d2).below()).isAir()) {
                // Try to find a position above ground
                for (int i = 0; i < 10; i++) {
                    BlockPos pos = BlockPos.containing(d0, d1 - i, d2);
                    if (!this.windrider.level().getBlockState(pos).isAir()) {
                        d1 = pos.getY() + 3.0D + this.windrider.getRandom().nextInt(4);
                        break;
                    }
                }
            }

            this.x = d0;
            this.y = d1;
            this.z = d2;
            this.tryTicks = 0;
            this.stopped = false;
        }

        @Override
        public void tick() {
            if (--this.tryTicks <= 0) {
                this.tryTicks = 10 + this.windrider.getRandom().nextInt(10);

                // If we're gliding, try to find a position in the wind direction
                if (this.windrider.isGliding() && this.windrider.windDirection.lengthSqr() > 0.1D) {
                    Vec3 windDir = this.windrider.windDirection.normalize().scale(16.0D);
                    this.x = this.windrider.getX() + windDir.x;
                    this.y = this.windrider.getY() + windDir.y;
                    this.z = this.windrider.getZ() + windDir.z;
                }

                if (!this.windrider.getNavigation().moveTo(this.x, this.y, this.z, this.speedModifier)) {
                    this.tryTicks += 15;
                }
            }
        }

        @Override
        public void stop() {
            this.stopped = true;
        }
    }

    static class WindriderMeleeAttackGoal extends Goal {
        protected final WindriderEntity mob;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private long lastCanUseCheck;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;

        public WindriderMeleeAttackGoal(WindriderEntity mob, double speedModifier, boolean followWhenNotSeen) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.followingTargetEvenIfNotSeen = followWhenNotSeen;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            long gameTime = this.mob.level().getGameTime();
            if (gameTime - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = gameTime;
                LivingEntity target = this.mob.getTarget();

                if (target == null) {
                    return false;
                } else if (!target.isAlive()) {
                    return false;
                } else {
                    if (canPenalize) {
                        if (--this.ticksUntilNextPathRecalculation <= 0) {
                            this.path = this.mob.getNavigation().createPath(target, 0);
                            this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                            return this.path != null;
                        } else {
                            return true;
                        }
                    }

                    this.path = this.mob.getNavigation().createPath(target, 0);
                    if (this.path != null) {
                        return true;
                    } else {
                        return this.getAttackReachSqr(target) >= this.mob.distanceToSqr(target.getX(),
                                target.getBoundingBox().minY, target.getZ());
                    }
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            } else if (!this.mob.isWithinRestriction(target.blockPosition())) {
                return false;
            } else {
                return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
            }
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            LivingEntity target = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
                this.mob.setTarget(null);
            }

            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null)
                return;

            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distanceSq = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);

            if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(target))
                    && this.ticksUntilNextPathRecalculation <= 0
                    && (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D
                            || target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D
                            || this.mob.getRandom().nextFloat() < 0.05F)) {

                this.pathedTargetX = target.getX();
                this.pathedTargetY = target.getY();
                this.pathedTargetZ = target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);

                if (this.canPenalize) {
                    this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                    if (this.mob.getNavigation().getPath() != null) {
                        Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                        if (finalPathPoint != null
                                && target.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }

                if (distanceSq > 1024.0D) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (distanceSq > 256.0D) {
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if (!this.mob.getNavigation().moveTo(target, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(target, distanceSq);
        }

        protected void checkAndPerformAttack(LivingEntity target, double distanceSq) {
            double reach = this.getAttackReachSqr(target);
            if (distanceSq <= reach && this.ticksUntilNextAttack <= 0) {
                this.ticksUntilNextAttack = 20;
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);

                // Apply knockback
                if (this.mob.isStormPowered()) {
                    Vec3 knockback = target.position().subtract(this.mob.position())
                            .normalize()
                            .scale(0.5D)
                            .add(0.0D, 0.3D, 0.0D);

                    target.push(knockback.x, knockback.y, knockback.z);
                    target.hurtMarked = true;
                }
            }
        }

        protected double getAttackReachSqr(LivingEntity target) {
            return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
        }
    }

    static class GustAttackGoal extends Goal {
        private final WindriderEntity windrider;
        private int chargeTime;

        public GustAttackGoal(WindriderEntity windrider) {
            this.windrider = windrider;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.windrider.canGustAttack();
        }

        @Override
        public boolean canContinueToUse() {
            return this.chargeTime > 0 && this.windrider.getTarget() != null && this.windrider.getTarget().isAlive();
        }

        @Override
        public void start() {
            this.chargeTime = 20 + this.windrider.getRandom().nextInt(10);
            this.windrider.setGliding(true);
            this.windrider.getNavigation().stop();
        }

        @Override
        public void stop() {
            this.chargeTime = 0;
            this.windrider.setGliding(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.windrider.getTarget();
            if (target == null)
                return;

            // Look at target
            this.windrider.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (this.chargeTime > 0) {
                // Charge up
                this.chargeTime--;

                // Visual effect
                if (this.windrider.level() instanceof ServerLevel serverLevel) {
                    Vec3 pos = this.windrider.position();
                    serverLevel.sendParticles(
                            ParticleTypes.CLOUD,
                            pos.x,
                            pos.y + this.windrider.getBbHeight() * 0.5,
                            pos.z,
                            5,
                            0.5, 0.5, 0.5,
                            0.1);
                }

                // Perform gust attack at the end of charge
                if (this.chargeTime == 0) {
                    this.windrider.performGustAttack();
                }
            }
        }
    }
}
