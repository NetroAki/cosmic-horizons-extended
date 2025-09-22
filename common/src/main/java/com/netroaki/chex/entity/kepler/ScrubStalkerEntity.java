package com.netroaki.chex.entity.kepler;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ScrubStalkerEntity extends KeplerCreatureEntity {
    private static final int CAMOUFLAGE_COOLDOWN = 200; // 10 seconds at 20 ticks/second
    private int camouflageCooldown = 0;
    private boolean isCamouflaged = false;

    public ScrubStalkerEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return KeplerCreatureEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ARMOR, 4.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new StalkPreyGoal(this, 1.0D, 32.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Rabbit.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        
        // Update camouflage state
        if (!this.level().isClientSide) {
            if (this.camouflageCooldown > 0) {
                this.camouflageCooldown--;
            } else if (this.random.nextInt(100) == 0) {
                this.toggleCamouflage();
            }
        }
        
        // Visual effect when camouflaged
        if (this.isCamouflaged && this.random.nextInt(20) == 0) {
            this.level().addPicle(
                net.minecraft.core.particles.ParticleTypes.ASH,
                this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                this.getY() + this.random.nextDouble() * this.getBbHeight(),
                this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                0, 0, 0
            );
        }
    }

    private void toggleCamouflage() {
        this.isCamouflaged = !this.isCamouflaged;
        this.camouflageCooldown = CAMOUFLAGE_COOLDOWN;
        
        if (this.isCamouflaged) {
            // When camouflaging, reduce visibility
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D); // More damage when attacking from stealth
        } else {
            // When visible, normal stats
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        }
    }

    @Override
    public boolean isInvisible() {
        return this.isCamouflaged || super.isInvisible();
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        if (this.isCamouflaged) {
            // Players can see through camouflage when very close
            return this.distanceToSqr(player) > 16.0D; // 4 blocks away
        }
        return super.isInvisibleTo(player);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (this.isCamouflaged) {
            // Silent steps when camouflaged
            this.playSound(SoundEvents.FOX_STEP, 0.05F, 1.5F);
        } else {
            this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isCamouflaged ? null : SoundEvents.FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        this.isCamouflaged = false; // Reveal when hurt
        return SoundEvents.FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.isBaby() 
            ? EntityDimensions.fixed(0.6F, 0.6F) 
            : EntityDimensions.fixed(1.2F, 1.5F);
    }

    // Custom goal for stalking behavior
    static class StalkPreyGoal extends Goal {
        private final ScrubStalkerEntity stalker;
        private final double speedModifier;
        private final float maxDistanceSquared;
        @Nullable
        private LivingEntity target;

        public StalkPreyGoal(ScrubStalkerEntity stalker, double speedModifier, float maxDistance) {
            this.stalker = stalker;
            this.speedModifier = speedModifier;
            this.maxDistanceSquared = maxDistance * maxDistance;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (stalker.isCamouflaged) {
                this.target = stalker.level().getNearestPlayer(stalker.getX(), stalker.getY(), stalker.getZ(), 
                    Math.sqrt(maxDistanceSquared) * 0.8, 
                    entity -> entity != null && entity.distanceToSqr(stalker) < maxDistanceSquared);
                return this.target != null && stalker.random.nextFloat() < 0.1F;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && 
                   this.target.isAlive() && 
                   stalker.distanceToSqr(this.target) <= this.maxDistanceSquared &&
                   stalker.isCamouflaged;
        }

        @Override
        public void start() {
            if (this.target != null) {
                stalker.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            }
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target != null && !stalker.getNavigation().isDone()) {
                if (stalker.distanceToSqr(this.target) < 16.0D) {
                    // Pounce when close enough
                    Vec3 vec3 = this.target.getDeltaMovement();
                    double d0 = this.target.getX() - stalker.getX();
                    double d1 = this.target.getZ() - stalker.getZ();
                    double d2 = Math.sqrt(d0 * d0 + d1 * d1);
                    
                    stalker.setDeltaMovement(
                        vec3.x + d0 / d2 * 0.5D * 0.8D + 0.1D,
                        0.4D,
                        vec3.z + d1 / d2 * 0.5D * 0.8D + 0.1D
                    );
                    
                    stalker.doHurtTarget(this.target);
                    stalker.isCamouflaged = false; // Reveal after attacking
                } else {
                    // Move silently towards target
                    stalker.getNavigation().moveTo(this.target, this.speedModifier);
                }
            }
        }
    }
}
