package com.netroaki.chex.entity.kepler;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MeadowFlutterwingEntity extends KeplerCreatureEntity implements FlyingAnimal {
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(Items.HONEYCOMB, Items.GLOW_BERRIES);
    
    public MeadowFlutterwingEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return KeplerCreatureEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.FLYING_SPEED, 0.4D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.8D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        
        // Custom flying behavior
        this.goalSelector.addGoal(6, new Goal() {
            private double targetX;
            private double targetY;
            private double targetZ;
            private int idleTime;

            @Override
            public boolean canUse() {
                if (MeadowFlutterwingEntity.this.random.nextInt(50) != 0) {
                    return false;
                } else {
                    Vec3 vec3 = this.findRandomLocation();
                    if (vec3 == null) {
                        return false;
                    } else {
                        this.targetX = vec3.x;
                        this.targetY = vec3.y;
                        this.targetZ = vec3.z;
                        return true;
                    }
                }
            }

            @Nullable
            private Vec3 findRandomLocation() {
                RandomSource random = MeadowFlutterwingEntity.this.random;
                Vec3 vec3 = MeadowFlutterwingEntity.this.getViewVector(0.0F);
                
                int i = 8;
                double d0 = MeadowFlutterwingEntity.this.getX() + (random.nextFloat() * 2.0F - 1.0F) * 8.0F;
                double d1 = MeadowFlutterwingEntity.this.getY() + (random.nextFloat() * 2.0F - 1.0F) * 4.0F;
                double d2 = MeadowFlutterwingEntity.this.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 8.0F;
                
                return new Vec3(d0, d1, d2);
            }

            @Override
            public boolean canContinueToUse() {
                return !MeadowFlutterwingEntity.this.getNavigation().isDone() && this.idleTime < 100;
            }

            @Override
            public void start() {
                MeadowFlutterwingEntity.this.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, 1.0D);
                this.idleTime = 0;
            }

            @Override
            public void tick() {
                ++this.idleTime;
                if (MeadowFlutterwingEntity.this.random.nextFloat() < 0.05F) {
                    MeadowFlutterwingEntity.this.getLookControl().setLookAt(
                        this.targetX + (MeadowFlutterwingEntity.this.random.nextFloat() * 2.0F - 1.0F) * 2.0F,
                        this.targetY + (MeadowFlutterwingEntity.this.random.nextFloat() * 2.0F - 1.0F) * 2.0F,
                        this.targetZ + (MeadowFlutterwingEntity.this.random.nextFloat() * 2.0F - 1.0F) * 2.0F,
                        180.0F, 20.0F);
                }
            }
        });
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return TEMPTATION_ITEMS.test(stack);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.5F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.isBaby() 
            ? EntityDimensions.fixed(0.3F, 0.3F) 
            : EntityDimensions.fixed(0.6F, 0.6F);
    }
}
