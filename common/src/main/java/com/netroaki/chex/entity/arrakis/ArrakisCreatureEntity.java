package com.netroaki.chex.entity.arrakis;

import com.netroaki.chex.registry.CHEXItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class ArrakisCreatureEntity extends Animal {
    private static final EntityDataAccessor<Boolean> HAS_SPICE = SynchedEntityData.defineId(ArrakisCreatureEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_STARVING = SynchedEntityData.defineId(ArrakisCreatureEntity.class, EntityDataSerializers.BOOLEAN);
    
    public ArrakisCreatureEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.setCanPickUpLoot(true);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            .add(Attributes.FOLLOW_RANGE, 16.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(CHEXItems.SPICE_MELANGE.get()), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_SPICE, false);
        this.entityData.define(IS_STARVING, false);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasSpice", this.hasSpice());
        tag.putBoolean("IsStarving", this.isStarving());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHasSpice(tag.getBoolean("HasSpice"));
        this.setStarving(tag.getBoolean("IsStarving"));
    }
    
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(CHEXItems.SPICE_MELANGE.get());
    }
    
    @Override
    public boolean canBreatheUnderwater() {
        return false;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SAND_STEP, 0.15F, 1.0F);
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.HUSK_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }
    
    protected SoundEvent getSpiceSound() {
        return SoundEvents.HUSK_CONVERTED_TO_ZOMBIE;
    }
    
    public boolean hasSpice() {
        return this.entityData.get(HAS_SPICE);
    }
    
    public void setHasSpice(boolean hasSpice) {
        this.entityData.set(HAS_SPICE, hasSpice);
    }
    
    public boolean isStarving() {
        return this.entityData.get(IS_STARVING);
    }
    
    public void setStarving(boolean starving) {
        this.entityData.set(IS_STARVING, starving);
    }
    
    @Override
    public void aiStep() {
        this.updateSwingTime();
        this.updateNoActionTime();
        
        if (!this.level().isClientSide) {
            this.updateSunBurn();
            
            // Check for spice in inventory
            if (!this.hasSpice() && this.random.nextInt(1000) == 0) {
                for (ItemStack stack : this.getArmorSlots()) {
                    if (stack.is(CHEXItems.SPICE_MELANGE.get())) {
                        this.setHasSpice(true);
                        break;
                    }
                }
            }
            
            // Starvation check
            if (this.tickCount % 600 == 0 && this.random.nextFloat() < 0.05F) {
                this.setStarving(true);
            }
        }
        
        super.aiStep();
    }
    
    protected void updateSunBurn() {
        if (this.level().isDay() && !this.level().isClientSide) {
            float f = this.getBrightness();
            BlockPos blockpos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
            
            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && 
                this.level().canSeeSky(blockpos)) {
                
                this.setSecondsOnFire(8);
            }
        }
    }
    
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.02F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            super.travel(travelVector);
        }
    }
}
