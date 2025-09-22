package com.netroaki.chex.entity.aqua;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class LuminfishEntity extends AquaticEntity {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(LuminfishEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SCHOOL_SIZE = SynchedEntityData.defineId(LuminfishEntity.class, EntityDataSerializers.INT);
    
    // Temptation items for breeding
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(Items.TROPICAL_FISH, Items.COD, Items.SALMON);
    
    // Glow effect data
    private float glowIntensity = 0.0F;
    private float oGlowIntensity;
    private float glowSpeed = 0.02F;
    
    public LuminfishEntity(EntityType<? extends LuminfishEntity> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 20);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.2D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(SCHOOL_SIZE, 1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(4, new LuminfishSwimGoal(this));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public void tick() {
        super.tick();
        
        // Update glow animation
        this.oGlowIntensity = this.glowIntensity;
        if (this.isInWater() && !this.level().isClientSide) {
            // Pulsing glow effect
            this.glowIntensity = (float)((Math.sin(this.tickCount * this.glowSpeed) + 1.0F) * 0.5F * 0.5F + 0.5F);
            
            // Schooling behavior
            if (this.tickCount % 10 == 0 && this.getSchoolSize() < 8) {
                this.findAndJoinSchool();
            }
        } else {
            this.glowIntensity = 0.0F;
        }
        
        // Emit light particles when glowing
        if (this.level().isClientSide && this.glowIntensity > 0.1F && this.random.nextFloat() < 0.1F) {
            this.level().addParticle(
                net.minecraft.core.particles.ParticleTypes.GLOW,
                this.getRandomX(0.5D),
                this.getRandomY(),
                this.getRandomZ(0.5D),
                0.0D, 0.0D, 0.0D
            );
        }
    }

    private void findAndJoinSchool() {
        if (this.getSchoolSize() >= 8) return;
        
        List<LuminfishEntity> nearbyFish = this.level().getEntitiesOfClass(
            LuminfishEntity.class, 
            this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D),
            fish -> fish != this && fish.getSchoolSize() < 8
        );
        
        if (!nearbyFish.isEmpty()) {
            LuminfishEntity leader = nearbyFish.get(0);
            int newSchoolSize = Math.min(8, leader.getSchoolSize() + 1);
            
            // Join the school
            this.setSchoolSize(newSchoolSize);
            for (LuminfishEntity fish : nearbyFish) {
                fish.setSchoolSize(newSchoolSize);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("SchoolSize", this.getSchoolSize());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setSchoolSize(compound.getInt("SchoolSize"));
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 3));
    }

    public int getSchoolSize() {
        return this.entityData.get(SCHOOL_SIZE);
    }

    public void setSchoolSize(int size) {
        this.entityData.set(SCHOOL_SIZE, Mth.clamp(size, 1, 8));
    }

    public float getGlowIntensity(float partialTicks) {
        return Mth.lerp(partialTicks, this.oGlowIntensity, this.glowIntensity);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        // Add custom drops here
        if (this.random.nextFloat() < 0.1F + looting * 0.05F) {
            this.spawnAtLocation(Items.GLOW_INK_SAC);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return TEMPTATION_ITEMS.test(stack);
    }

    // Custom AI for schooling behavior
    static class LuminfishSwimGoal extends Goal {
        private final LuminfishEntity fish;
        private int timeToRecalcPath;
        private int schoolSize;

        public LuminfishSwimGoal(LuminfishEntity fish) {
            this.fish = fish;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.fish.isInWater() && this.fish.getSchoolSize() > 1;
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
            this.schoolSize = this.fish.getSchoolSize();
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10 + this.fish.getRandom().nextInt(5));
                
                // Find other fish in the school
                List<LuminfishEntity> school = this.fish.level().getEntitiesOfClass(
                    LuminfishEntity.class,
                    this.fish.getBoundingBox().inflate(8.0D, 4.0D, 8.0D),
                    fish -> fish != this.fish && fish.getSchoolSize() == this.schoolSize
                );
                
                if (!school.isEmpty()) {
                    // Find center of the school
                    Vec3 center = Vec3.ZERO;
                    int count = 0;
                    
                    for (LuminfishEntity fish : school) {
                        if (!fish.isInWater()) continue;
                        center = center.add(fish.position());
                        count++;
                    }
                    
                    if (count > 0) {
                        center = center.scale(1.0D / count);
                        
                        // Add some randomness to the target position
                        double angle = this.fish.getRandom().nextDouble() * Math.PI * 2.0D;
                        double radius = 2.0D + this.fish.getRandom().nextDouble() * 2.0D;
                        double x = center.x + Math.cos(angle) * radius;
                        double y = center.y + (this.fish.getRandom().nextDouble() * 2.0D - 1.0D);
                        double z = center.z + Math.sin(angle) * radius;
                        
                        // Move towards the school
                        this.fish.getNavigation().moveTo(x, y, z, 1.0D);
                    }
                }
            }
        }
    }

    // Spawn rules
    public static boolean checkLuminfishSpawnRules(EntityType<LuminfishEntity> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getFluidState(pos).is(FluidTags.WATER) && 
               level.getBlockState(pos.above()).is(Blocks.WATER) &&
               (spawnType == MobSpawnType.SPAWNER || random.nextFloat() < 0.1F);
    }
}
