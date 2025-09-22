package com.netroaki.chex.entity.alpha_centauri;

import com.netroaki.chex.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CoreFragmentBoss extends Monster {
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(CoreFragmentBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_SHIELDED = SynchedEntityData.defineId(CoreFragmentBoss.class, EntityDataSerializers.BOOLEAN);
    
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(
        this.getDisplayName(), 
        BossEvent.BossBarColor.YELLOW, 
        BossEvent.BossBarOverlay.PROGRESS
    )).setDarkenScreen(true);
    
    private int attackTimer = 0;
    private int phaseChangeCooldown = 0;
    private int shieldRegenCooldown = 0;
    private int[] pylonPositions = new int[6]; // Stores relative positions of pylons
    private boolean pylonsActive = false;
    
    public CoreFragmentBoss(EntityType<? extends CoreFragmentBoss> type, Level level) {
        super(type, level);
        this.xpReward = 1000;
        this.setHealth(this.getMaxHealth());
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 500.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            .add(Attributes.ATTACK_DAMAGE, 10.0D)
            .add(Attributes.ARMOR, 10.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
            .add(Attributes.FOLLOW_RANGE, 50.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CoreFragmentAttackGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 32.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PHASE, 0);
        this.entityData.define(DATA_SHIELDED, true);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel)this.level();
            
            // Update boss bar
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            
            // Handle attack cooldown
            if (this.attackTimer > 0) {
                this.attackTimer--;
            }
            
            // Handle phase change cooldown
            if (this.phaseChangeCooldown > 0) {
                this.phaseChangeCooldown--;
            }
            
            // Handle shield regeneration
            if (this.shieldRegenCooldown > 0) {
                this.shieldRegenCooldown--;
            } else if (!this.isShielded()) {
                this.setShielded(true);
                this.level().broadcastEntityEvent(this, (byte)7); // Shield reactivate animation
            }
            
            // Visual effects
            if (this.tickCount % 5 == 0) {
                // Core glow
                serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    this.getX(), this.getY() + 1.0D, this.getZ(),
                    10, 0.5D, 0.5D, 0.5D, 0.1D);
                    
                // Shield particles when active
                if (this.isShielded()) {
                    for(int i = 0; i < 5; ++i) {
                        double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 2.0D;
                        double d1 = this.getY() + this.random.nextDouble() * 3.0D;
                        double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D;
                        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, d0, d1, d2, 1, 0, 0, 0, 0.1D);
                    }
                }
            }
            
            // Phase transition logic
            float healthRatio = this.getHealth() / this.getMaxHealth();
            int phase = this.getPhase();
            
            if (healthRatio < 0.3F && phase < 3) {
                this.setPhase(3);
                this.phaseChange();
            } else if (healthRatio < 0.6F && phase < 2) {
                this.setPhase(2);
                this.phaseChange();
            } else if (healthRatio < 0.9F && phase < 1) {
                this.setPhase(1);
                this.phaseChange();
            }
        }
    }
    
    private void phaseChange() {
        this.phaseChangeCooldown = 100;
        this.attackTimer = 0;
        this.setShielded(true);
        this.shieldRegenCooldown = 200;
        
        // Spawn pylons in phase 2
        if (this.getPhase() == 2 && !this.pylonsActive) {
            this.spawnPylons();
            this.pylonsActive = true;
        }
        
        // Heal on phase change
        this.heal(this.getMaxHealth() * 0.2F);
        
        // Visual effect
        if (!this.level().isClientSide) {
            ((ServerLevel)this.level()).sendParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                this.getX(), this.getY() + 2.0D, this.getZ(),
                50, 2.0D, 2.0D, 2.0D, 0.5D);
                
            this.level().playSound(null, this.blockPosition(), 
                SoundEvents.ENDER_DRAGON_GROWL, 
                SoundSource.HOSTILE, 5.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }
    }
    
    private void spawnPylons() {
        // Create 6 pylons in a hexagon around the boss
        double radius = 8.0D;
        for (int i = 0; i < 6; i++) {
            double angle = 2.0D * Math.PI * i / 6.0D;
            double x = this.getX() + Math.cos(angle) * radius;
            double z = this.getZ() + Math.sin(angle) * radius;
            
            // Find ground position
            BlockPos pos = new BlockPos((int)x, (int)this.getY(), (int)z);
            while (pos.getY() > this.level().getMinBuildHeight() && 
                   this.level().isEmptyBlock(pos)) {
                pos = pos.below();
            }
            
            // Spawn pylon entity
            PylonEntity pylon = new PylonEntity(ModEntities.ENERGY_PYLON.get(), this.level());
            pylon.setPos(x, pos.getY() + 1, z);
            pylon.setOwner(this);
            this.level().addFreshEntity(pylon);
            
            // Store relative position for tracking
            pylonPositions[i * 2] = (int)(x - this.getX());
            pylonPositions[i * 2 + 1] = (int)(z - this.getZ());
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Ignore damage if shielded (except for specific damage types)
        if (this.isShielded() && !source.isBypassInvul() && 
            !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            // Visual effect when hit
            if (source.getEntity() instanceof Player) {
                this.level().broadcastEntityEvent(this, (byte)6); // Shield hit animation
            }
            return false;
        }
        
        // Phase-specific damage modifiers
        switch (this.getPhase()) {
            case 1: amount *= 0.9F; break; // 10% resistance in phase 1
            case 2: amount *= 0.8F; break; // 20% resistance in phase 2
            case 3: amount *= 1.2F; break; // 20% vulnerability in final phase
        }
        
        // Chance to drop shield when hit
        if (this.random.nextFloat() < 0.15F) {
            this.setShielded(false);
            this.shieldRegenCooldown = 100 + this.random.nextInt(100);
        }
        
        return super.hurt(source, amount);
    }
    
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        
        // Explosion effect
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5.0F, Level.ExplosionInteraction.MOB);
            
            // Drop loot and experience
            this.dropExperience();
        }
        
        // Clean up
        this.bossEvent.setProgress(0.0F);
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
    
    // Getters and setters
    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }
    
    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
        this.bossEvent.setName(this.getDisplayName());
    }
    
    public boolean isShielded() {
        return this.entityData.get(DATA_SHIELDED);
    }
    
    public void setShielded(boolean shielded) {
        this.entityData.set(DATA_SHIELDED, shielded);
    }
    
    // Custom attack goal for the boss
    static class CoreFragmentAttackGoal extends Goal {
        private final CoreFragmentBoss boss;
        private int attackStep;
        private int attackTime;
        private int teleportTime;
        
        public CoreFragmentAttackGoal(CoreFragmentBoss boss) {
            this.boss = boss;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            LivingEntity target = this.boss.getTarget();
            return target != null && target.isAlive() && this.boss.phaseChangeCooldown <= 0;
        }
        
        @Override
        public void start() {
            this.attackStep = 0;
            this.attackTime = 0;
            this.teleportTime = 0;
        }
        
        @Override
        public void tick() {
            if (--this.attackTime <= 0) {
                LivingEntity target = this.boss.getTarget();
                if (target == null) {
                    return;
                }
                
                double distanceSq = this.boss.distanceToSqr(target);
                
                // Randomly choose attack pattern based on phase
                int phase = this.boss.getPhase();
                int attackType = this.boss.random.nextInt(phase == 3 ? 4 : (phase == 2 ? 3 : 2));
                
                switch (attackType) {
                    case 0: // Melee attack
                        if (distanceSq < 16.0D * 16.0D) {
                            this.attackTime = 20;
                            this.boss.doHurtTarget(target);
                        } else {
                            // Move closer if too far
                            this.boss.getNavigation().moveTo(target, 1.0D);
                        }
                        break;
                        
                    case 1: // Projectile attack
                        this.attackTime = 40;
                        this.boss.lookAt(target, 30.0F, 30.0F);
                        this.shootProjectile(target, 1.0F);
                        break;
                        
                    case 2: // Multi-shot attack (phase 2+)
                        this.attackTime = 60;
                        for (int i = 0; i < 3; i++) {
                            this.boss.lookAt(target, 30.0F, 30.0F);
                            this.shootProjectile(target, 0.8F);
                        }
                        break;
                        
                    case 3: // Teleport and nova attack (phase 3)
                        if (this.teleportTime <= 0) {
                            // Teleport behind player
                            Vec3 teleportPos = target.position().add(
                                (this.boss.random.nextDouble() - 0.5) * 8.0,
                                0.0,
                                (this.boss.random.nextDouble() - 0.5) * 8.0
                            );
                            this.boss.teleportTo(teleportPos.x, teleportPos.y, teleportPos.z);
                            this.teleportTime = 40;
                        } else {
                            this.teleportTime--;
                        }
                        
                        // Nova attack
                        if (this.attackStep++ % 5 == 0) {
                            this.novaAttack();
                        }
                        this.attackTime = 5;
                        break;
                }
            }
        }
        
        private void shootProjectile(LivingEntity target, float velocity) {
            // Implementation for shooting a projectile
            // This would create and spawn a custom projectile entity
            // For example:
            /*
            EnergyOrbEntity orb = new EnergyOrbEntity(
                this.boss.level(), 
                this.boss, 
                target.getX() - this.boss.getX(), 
                target.getY(0.5D) - this.boss.getY(0.5D), 
                target.getZ() - this.boss.getZ()
            );
            orb.setPos(this.boss.getX(), this.boss.getY(0.5D), this.boss.getZ());
            this.boss.level().addFreshEntity(orb);
            */
        }
        
        private void novaAttack() {
            // Create a circular shockwave
            if (!this.boss.level().isClientSide) {
                int particles = 16;
                for (int i = 0; i < particles; i++) {
                    double angle = 2.0 * Math.PI * i / particles;
                    double dx = Math.cos(angle) * 2.0;
                    double dz = Math.sin(angle) * 2.0;
                    
                    // Spawn particles or create projectiles
                    ((ServerLevel)this.boss.level()).sendParticles(
                        ParticleTypes.FLAME,
                        this.boss.getX(), 
                        this.boss.getY() + 1.0D, 
                        this.boss.getZ(),
                        1, dx, 0.1D, dz, 0.5D
                    );
                }
                
                // Damage nearby entities
                AABB aabb = this.boss.getBoundingBox().inflate(5.0D);
                for (LivingEntity entity : this.boss.level().getEntitiesOfClass(
                    LivingEntity.class, 
                    aabb, 
                    e -> e != this.boss && e.isAlive()
                )) {
                    entity.hurt(this.boss.damageSources().mobAttack(this.boss), 5.0F);
                    entity.setSecondsOnFire(5);
                }
                
                // Play sound
                this.boss.level().playSound(
                    null, 
                    this.boss.blockPosition(), 
                    SoundEvents.GENERIC_EXPLODE, 
                    SoundSource.HOSTILE, 
                    2.0F, 
                    0.5F + this.boss.random.nextFloat() * 0.2F
                );
            }
        }
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Phase", this.getPhase());
        compound.putBoolean("Shielded", this.isShielded());
        compound.putIntArray("PylonPositions", this.pylonPositions);
        compound.putBoolean("PylonsActive", this.pylonsActive);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setPhase(compound.getInt("Phase"));
        this.setShielded(compound.getBoolean("Shielded"));
        this.pylonPositions = compound.getIntArray("PylonPositions");
        this.pylonsActive = compound.getBoolean("PylonsActive");
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
