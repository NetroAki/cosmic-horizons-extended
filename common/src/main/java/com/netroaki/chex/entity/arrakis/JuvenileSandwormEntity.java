package com.netroaki.chex.entity.arrakis;

import com.netroaki.chex.registry.CHEXItems;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class JuvenileSandwormEntity extends ArrakisCreatureEntity {
    private static final EntityDataAccessor<Boolean> IS_BURROWED = SynchedEntityData.defineId(JuvenileSandwormEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BURROW_TIME = SynchedEntityData.defineId(JuvenileSandwormEntity.class, EntityDataSerializers.INT);
    
    private int surfaceTimer;
    private int burrowCooldown;
    private Vec3 burrowPos;
    
    public JuvenileSandwormEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new SandwormMoveControl(this);
        this.setMaxUpStep(1.0F);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 50.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.ATTACK_DAMAGE, 6.0D)
            .add(Attributes.ARMOR, 4.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_BURROWED, false);
        this.entityData.define(BURROW_TIME, 0);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsBurrowed", this.isBurrowed());
        tag.putInt("BurrowTime", this.getBurrowTime());
        tag.putInt("SurfaceTimer", this.surfaceTimer);
        tag.putInt("BurrowCooldown", this.burrowCooldown);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBurrowed(tag.getBoolean("IsBurrowed"));
        this.setBurrowTime(tag.getInt("BurrowTime"));
        this.surfaceTimer = tag.getInt("SurfaceTimer");
        this.burrowCooldown = tag.getInt("BurrowCooldown");
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.setBurrowed(this.random.nextFloat() < 0.7F);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        
        if (!this.level().isClientSide) {
            // Handle burrowing logic
            if (this.isBurrowed()) {
                this.setBurrowTime(this.getBurrowTime() + 1);
                
                // Surface after random time or if target is nearby
                if (this.getTarget() != null || this.getBurrowTime() > 600) {
                    this.setBurrowed(false);
                    this.setBurrowTime(0);
                    this.burrowCooldown = 200 + this.random.nextInt(400);
                }
                
                // Create sand particles
                if (this.tickCount % 5 == 0) {
                    ((ServerLevel)this.level()).sendParticles(
                        ParticleTypes.CLOUD,
                        this.getX(), this.getY() + 0.1D, this.getZ(),
                        2, 0.2D, 0.0D, 0.2D, 0.0D
                    );
                }
            } else {
                // Surface behavior
                this.surfaceTimer++;
                
                // Consider burrowing if no target and cooldown is over
                if (this.getTarget() == null && this.burrowCooldown <= 0 && this.surfaceTimer > 200) {
                    if (this.random.nextFloat() < 0.02F) {
                        this.setBurrowed(true);
                        this.surfaceTimer = 0;
                    }
                }
                
                // Decrease cooldown
                if (this.burrowCooldown > 0) {
                    this.burrowCooldown--;
                }
            }
            
            // Update burrow position
            if (this.isBurrowed()) {
                this.burrowPos = this.position();
            }
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Reduce damage when burrowed
        if (this.isBurrowed() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            amount *= 0.5F;
            
            // Chance to surface when hurt
            if (this.random.nextFloat() < 0.3F) {
                this.setBurrowed(false);
            }
        }
        
        return super.hurt(source, amount);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Update movement based on burrow state
        if (this.isBurrowed()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setYRot(this.yRotO);
            this.setXRot(0.0F);
            this.yHeadRot = this.getYRot();
            this.yBodyRot = this.getYRot();
            
            // Stay at burrow position
            if (this.burrowPos != null) {
                this.setPos(this.burrowPos.x, this.burrowPos.y, this.burrowPos.z);
            }
        }
    }
    
    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }
    
    @Override
    public boolean isPushable() {
        return !this.isBurrowed() && super.isPushable();
    }
    
    @Override
    public boolean isPushedByFluid() {
        return !this.isBurrowed();
    }
    
    @Override
    protected boolean isAffectedByFluids() {
        return !this.isBurrowed();
    }
    
    @Override
    public boolean isNoGravity() {
        return this.isBurrowed();
    }
    
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean playerKill) {
        super.dropCustomDeathLoot(source, looting, playerKill);
        
        // Drop sandworm scales
        int scaleCount = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        this.spawnAtLocation(new ItemStack(CHEXItems.SANDWORM_SCALE.get(), scaleCount));
        
        // Drop spice essence
        if (this.random.nextFloat() < 0.3F + looting * 0.1F) {
            this.spawnAtLocation(new ItemStack(CHEXItems.SPICE_ESSENCE.get()));
        }
    }
    
    public boolean isBurrowed() {
        return this.entityData.get(IS_BURROWED);
    }
    
    public void setBurrowed(boolean burrowed) {
        this.entityData.set(IS_BURROWED, burrowed);
        
        if (burrowed) {
            this.burrowPos = this.position();
        }
    }
    
    public int getBurrowTime() {
        return this.entityData.get(BURROW_TIME);
    }
    
    public void setBurrowTime(int time) {
        this.entityData.set(BURROW_TIME, time);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBurrowed() ? null : SoundEvents.HOGLIN_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.HOGLIN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOGLIN_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.isBurrowed()) {
            this.playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
        }
    }
    
    private static class SandwormMoveControl extends MoveControl {
        private final JuvenileSandwormEntity sandworm;
        
        public SandwormMoveControl(JuvenileSandwormEntity sandworm) {
            super(sandworm);
            this.sandworm = sandworm;
        }
        
        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO && !this.sandworm.isBurrowed()) {
                Vec3 vec3 = new Vec3(this.wantedX - this.sandworm.getX(), 
                                   this.wantedY - this.sandworm.getY(), 
                                   this.wantedZ - this.sandworm.getZ());
                double d0 = vec3.length();
                
                if (d0 < 2.5E-7) {
                    this.mob.setZza(0.0F);
                    return;
                }
                
                float f = (float)(Mth.atan2(vec3.z, vec3.x) * (180F / (float)Math.PI)) - 90.0F;
                this.sandworm.setYRot(this.rotlerp(this.sandworm.getYRot(), f, 90.0F));
                this.sandworm.yBodyRot = this.sandworm.getYRot();
                
                float f1 = (float)(this.speedModifier * this.sandworm.getAttributeValue(Attributes.MOVEMENT_SPEED));
                this.sandworm.setSpeed(Mth.lerp(0.125F, this.sandworm.getSpeed(), f1));
                
                double d1 = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
                if (Math.abs(vec3.y) > 1.0E-5F || Math.abs(d1) > 1.0E-5F) {
                    float f2 = -(float)(Mth.atan2(vec3.y, d1) * (180F / (float)Math.PI));
                    this.sandworm.setXRot(this.rotlerp(this.sandworm.getXRot(), f2, 5.0F));
                }
                
            } else {
                this.sandworm.setSpeed(0.0F);
            }
        }
    }
    
    public static boolean checkSandwormSpawnRules(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(Blocks.SAND) || level.getBlockState(pos.below()).is(Blocks.RED_SAND);
    }
}
