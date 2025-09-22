package com.netroaki.chex.entity.kepler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class VerdantColossusEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_ENRAGED = SynchedEntityData.defineId(VerdantColossusEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(VerdantColossusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ATTACK_TIMER = SynchedEntityData.defineId(VerdantColossusEntity.class, EntityDataSerializers.INT);
    
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS));
    private int summonCooldown = 0;
    private int phaseTransitionTimer = 0;
    
    public VerdantColossusEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 100;
        this.setHealth(this.getMaxHealth());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ColossusMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new ColossusSummonMinionsGoal(this));
        this.goalSelector.addGoal(3, new ColossusSlamAttackGoal(this));
        this.goalSelector.addGoal(4, new ColossusHealGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ENRAGED, false);
        this.entityData.define(DATA_PHASE, 1);
        this.entityData.define(DATA_ATTACK_TIMER, 0);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            
            // Handle phase transitions
            if (this.getHealth() < this.getMaxHealth() * 0.5 && this.getPhase() == 1) {
                this.setPhase(2);
                this.setEnraged(true);
                this.level().broadcastEntityEvent(this, (byte) 10); // Enrage animation
            }
            
            // Update attack timer
            if (this.entityData.get(DATA_ATTACK_TIMER) > 0) {
                this.entityData.set(DATA_ATTACK_TIMER, this.entityData.get(DATA_ATTACK_TIMER) - 1);
            }
            
            // Cooldown for summoning minions
            if (this.summonCooldown > 0) {
                this.summonCooldown--;
            }
            
            // Phase transition effects
            if (this.phaseTransitionTimer > 0) {
                this.phaseTransitionTimer--;
                if (this.phaseTransitionTimer % 5 == 0) {
                    this.spawnParticles(ParticleTypes.ENTITY_EFFECT, 20, 0.5);
                }
            }
            
            // Enraged aura effect
            if (this.isEnraged() && this.tickCount % 20 == 0) {
                this.spawnParticles(ParticleTypes.ANGRY_VILLAGER, 5, 0.1);
            }
        }
    }
    
    private void spawnParticles(net.minecraft.core.particles.ParticleOptions particle, int count, double radius) {
        for (int i = 0; i < count; ++i) {
            double d0 = this.random.nextGaussian() * radius;
            double d1 = this.random.nextGaussian() * radius;
            double d2 = this.random.nextGaussian() * radius;
            this.level().addParticle(particle, 
                this.getX() + d0, 
                this.getY() + 1.0D + d1, 
                this.getZ() + d2, 
                0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            // Enrage effect
            for (int i = 0; i < 50; ++i) {
                double d0 = this.random.nextGaussian() * 0.2D;
                double d1 = this.random.nextGaussian() * 0.2D;
                double d2 = this.random.nextGaussian() * 0.2D;
                this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, 
                    this.getX(), 
                    this.getY() + 2.0D, 
                    this.getZ(), 
                    d0, d1, d2);
            }
        } else {
            super.handleEntityEvent(id);
        }
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
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Enraged", this.isEnraged());
        tag.putInt("Phase", this.getPhase());
        tag.putInt("SummonCooldown", this.summonCooldown);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setEnraged(tag.getBoolean("Enraged"));
        this.setPhase(tag.getInt("Phase"));
        this.summonCooldown = tag.getInt("SummonCooldown");
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_GROWL;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 0.5F);
    }
    
    // Custom methods
    public boolean isEnraged() {
        return this.entityData.get(DATA_IS_ENRAGED);
    }
    
    public void setEnraged(boolean enraged) {
        this.entityData.set(DATA_IS_ENRAGED, enraged);
    }
    
    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }
    
    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
        this.phaseTransitionTimer = 40;
        
        // Apply phase-based modifiers
        if (phase == 2) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(16.0D);
        }
    }
    
    public int getAttackTimer() {
        return this.entityData.get(DATA_ATTACK_TIMER);
    }
    
    public void setAttackTimer(int ticks) {
        this.entityData.set(DATA_ATTACK_TIMER, ticks);
    }
    
    public boolean canSummonMinions() {
        return this.summonCooldown <= 0 && this.random.nextFloat() < 0.1F;
    }
    
    public void resetSummonCooldown() {
        this.summonCooldown = 200 + this.random.nextInt(200);
    }
    
    // Custom AI Goals
    static class ColossusMeleeAttackGoal extends MeleeAttackGoal {
        private final VerdantColossusEntity colossus;
        private int raiseArmTicks;
        
        public ColossusMeleeAttackGoal(VerdantColossusEntity colossus, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(colossus, speedModifier, followingTargetEvenIfNotSeen);
            this.colossus = colossus;
        }
        
        @Override
        public void start() {
            super.start();
            this.raiseArmTicks = 0;
        }
        
        @Override
        public void stop() {
            super.stop();
            this.colossus.setAggressive(false);
            this.colossus.setAttackTimer(0);
        }
        
        @Override
        public void tick() {
            super.tick();
            ++this.raiseArmTicks;
            
            if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
                this.colossus.setAggressive(true);
                this.colossus.setAttackTimer(10);
            } else {
                this.colossus.setAggressive(false);
                this.colossus.setAttackTimer(0);
            }
        }
    }
    
    static class ColossusSummonMinionsGoal extends Goal {
        private final VerdantColossusEntity colossus;
        private int summonAttempts;
        
        public ColossusSummonMinionsGoal(VerdantColossusEntity colossus) {
            this.colossus = colossus;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            if (!colossus.canSummonMinions()) {
                return false;
            }
            
            return colossus.getTarget() != null && 
                   colossus.distanceToSqr(colossus.getTarget()) < 100.0D &&
                   colossus.random.nextFloat() < 0.1F;
        }
        
        @Override
        public void start() {
            this.summonAttempts = 0;
            colossus.setAttackTimer(20);
        }
        
        @Override
        public void tick() {
            if (summonAttempts < 3) {
                if (colossus.getAttackTimer() <= 0) {
                    summonMinions();
                    summonAttempts++;
                    colossus.setAttackTimer(10);
                }
            } else {
                colossus.resetSummonCooldown();
            }
        }
        
        private void summonMinions() {
            if (!colossus.level().isClientSide) {
                int count = 2 + colossus.random.nextInt(3);
                
                for (int i = 0; i < count; i++) {
                    // Choose a random minion type based on phase
                    EntityType<?> minionType;
                    if (colossus.random.nextBoolean()) {
                        minionType = ForgeRegistries.ENTITY_TYPES.getValue(
                            new ResourceLocation("chex", "scrub_stalker"));
                    } else {
                        minionType = ForgeRegistries.ENTITY_TYPES.getValue(
                            new ResourceLocation("chex", "river_grazer"));
                    }
                    
                    if (minionType != null) {
                        Entity minion = minionType.create(colossus.level());
                        if (minion != null) {
                            double angle = (Math.PI * 2.0D / count) * i;
                            double x = colossus.getX() + Math.cos(angle) * 3.0D;
                            double z = colossus.getZ() + Math.sin(angle) * 3.0D;
                            
                            minion.moveTo(x, colossus.getY(), z, colossus.random.nextFloat() * 360.0F, 0.0F);
                            colossus.level().addFreshEntity(minion);
                            
                            // Visual effect
                            ((ServerLevel)colossus.level()).sendParticles(
                                ParticleTypes.POOF,
                                x, colossus.getY() + 1.0D, z,
                                10, 0.5D, 0.5D, 0.5D, 0.1D);
                        }
                    }
                }
                
                colossus.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 0.5F);
            }
        }
    }
    
    static class ColossusSlamAttackGoal extends Goal {
        private final VerdantColossusEntity colossus;
        private int cooldown;
        
        public ColossusSlamAttackGoal(VerdantColossusEntity colossus) {
            this.colossus = colossus;
            this.cooldown = 0;
        }
        
        @Override
        public boolean canUse() {
            if (cooldown > 0) {
                cooldown--;
                return false;
            }
            
            LivingEntity target = colossus.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            }
            
            double distance = colossus.distanceToSqr(target);
            return distance < 16.0D && colossus.random.nextFloat() < 0.05F;
        }
        
        @Override
        public void start() {
            colossus.setAttackTimer(20);
            colossus.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 0.5F);
        }
        
        @Override
        public void tick() {
            if (colossus.getAttackTimer() == 10) {
                // Perform slam attack
                AABB aabb = colossus.getBoundingBox().inflate(5.0D, 2.0D, 5.0D);
                List<LivingEntity> entities = colossus.level().getEntitiesOfClass(
                    LivingEntity.class, aabb, e -> e != colossus && e.isAlive());
                
                for (LivingEntity entity : entities) {
                    if (entity.hurt(colossus.damageSources().mobAttack(colossus), 
                                   (float)colossus.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5F)) {
                        entity.knockback(1.5F, 
                                       entity.getX() - colossus.getX(), 
                                       entity.getZ() - colossus.getZ());
                    }
                }
                
                // Visual effect
                if (!colossus.level().isClientSide) {
                    ((ServerLevel)colossus.level()).sendParticles(
                        ParticleTypes.EXPLOSION,
                        colossus.getX(), colossus.getY() + 1.0D, colossus.getZ(),
                        20, 2.0D, 0.5D, 2.0D, 0.1D);
                }
                
                cooldown = 100 + colossus.random.nextInt(100);
            }
        }
    }
    
    static class ColossusHealGoal extends Goal {
        private final VerdantColossusEntity colossus;
        private int healCooldown;
        
        public ColossusHealGoal(VerdantColossusEntity colossus) {
            this.colossus = colossus;
            this.healCooldown = 0;
        }
        
        @Override
        public boolean canUse() {
            if (healCooldown > 0) {
                healCooldown--;
                return false;
            }
            
            return colossus.getHealth() < colossus.getMaxHealth() * 0.5F && 
                   colossus.random.nextFloat() < 0.05F;
        }
        
        @Override
        public void start() {
            colossus.setAttackTimer(40);
            colossus.getNavigation().stop();
            
            // Visual effect
            colossus.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR, 1.0F, 0.5F);
        }
        
        @Override
        public void tick() {
            if (colossus.getAttackTimer() == 20) {
                // Heal the colossus
                colossus.heal(50.0F);
                
                // Visual effect
                if (!colossus.level().isClientSide) {
                    ((ServerLevel)colossus.level()).sendParticles(
                        ParticleTypes.HEART,
                        colossus.getX(), colossus.getY() + 2.0D, colossus.getZ(),
                        10, 1.0D, 1.0D, 1.0D, 0.1D);
                }
                
                healCooldown = 400 + colossus.random.nextInt(200);
            }
        }
    }
}
