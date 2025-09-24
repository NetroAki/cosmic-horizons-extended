package com.netroaki.chex.entity;

import com.netroaki.chex.registry.CHEXEntities;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class Sporefly extends Animal implements FlyingAnimal, AbilityRegistry.IAbilityUser {
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Sporefly.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_SWARM_SIZE = SynchedEntityData.defineId(Sporefly.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_LEADER = SynchedEntityData.defineId(Sporefly.class,
            EntityDataSerializers.BOOLEAN);

    private static final Ingredient FOOD_ITEMS = Ingredient.of(CHEXItems.LUMINOUS_DUST.get());

    @Nullable
    private Sporefly swarmLeader;
    private int swarmSize = 1;
    private Vec3 swarmCenter = Vec3.ZERO;
    private int timeUntilNewTarget = 0;

    public Sporefly(EntityType<? extends Sporefly> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);

        // Random color on spawn
        if (!level.isClientSide) {
            this.setColor(0xFF55FF + this.random.nextInt(0x55) * 0x100);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SporeflyJoinSwarmGoal(this));
        this.goalSelector.addGoal(2, new SporeflySwarmGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new SporeflyWanderGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR, 0xFF55FF);
        this.entityData.define(DATA_SWARM_SIZE, 1);
        this.entityData.define(DATA_IS_LEADER, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Color", this.getColor());
        tag.putInt("SwarmSize", this.getSwarmSize());
        tag.putBoolean("IsLeader", this.isLeader());
        this.getAbilityManager().save(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Color")) {
            this.setColor(tag.getInt("Color"));
        }
        if (tag.contains("SwarmSize")) {
            this.setSwarmSize(tag.getInt("SwarmSize"));
        }
        if (tag.contains("IsLeader")) {
            this.setLeader(tag.getBoolean("IsLeader"));
        }
        this.getAbilityManager().load(tag);
    }

    // Ability system integration
    private AbilityManager abilityManager;

    @Override
    public AbilityManager getAbilityManager() {
        if (this.abilityManager == null) {
            this.abilityManager = new AbilityManager(this);
            this.abilityManager.registerAbility(SporeCloudAbility.class);
        }
        return this.abilityManager;
    }

    @Override
    public void setAbilityManager(AbilityManager manager) {
        this.abilityManager = manager;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Update abilities
        if (!this.level().isClientSide) {
            this.getAbilityManager().tick();

            // Server-side updates
            if (this.swarmLeader == null || !this.swarmLeader.isAlive()
                    || this.swarmLeader.distanceToSqr(this) > 256.0D) {
                this.setSwarmLeader(null);
            }

            // Randomly change color slightly over time
            if (this.random.nextInt(100) == 0) {
                int color = this.getColor();
                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int b = color & 0xFF;

                // Slightly adjust one color channel
                switch (this.random.nextInt(3)) {
                    case 0 -> r = Mth.clamp(r + (this.random.nextBoolean() ? 5 : -5), 0, 255);
                    case 1 -> g = Mth.clamp(g + (this.random.nextBoolean() ? 5 : -5), 0, 255);
                    case 2 -> b = Mth.clamp(b + (this.random.nextBoolean() ? 5 : -5), 0, 255);
                }

                this.setColor((r << 16) | (g << 8) | b);
            }
        } else {
            // Client-side effects
            this.spawnParticles();
        }
    }

    private void spawnParticles() {
        if (this.random.nextFloat() < 0.3F) {
            int color = this.getColor();
            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;

            this.level().addParticle(
                    ParticleTypes.ENTITY_EFFECT,
                    this.getRandomX(0.5D),
                    this.getRandomY() - 0.25D,
                    this.getRandomZ(0.5D),
                    r, g, b);
        }
    }

    // Swarm behavior methods
    public boolean hasSwarmLeader() {
        return this.swarmLeader != null && this.swarmLeader.isAlive();
    }

    public Sporefly getSwarmLeader() {
        return this.swarmLeader;
    }

    public void setSwarmLeader(@Nullable Sporefly leader) {
        this.swarmLeader = leader;
        if (leader != null) {
            leader.addToSwarm();
        }
    }

    public void addToSwarm() {
        this.setSwarmSize(this.getSwarmSize() + 1);
    }

    public void removeFromSwarm() {
        this.setSwarmSize(Math.max(0, this.getSwarmSize() - 1));
    }

    public int getSwarmSize() {
        return this.entityData.get(DATA_SWARM_SIZE);
    }

    public void setSwarmSize(int size) {
        this.entityData.set(DATA_SWARM_SIZE, size);
    }

    public boolean isLeader() {
        return this.entityData.get(DATA_IS_LEADER);
    }

    public void setLeader(boolean leader) {
        this.entityData.set(DATA_IS_LEADER, leader);
    }

    public int getColor() {
        return this.entityData.get(DATA_COLOR);
    }

    public void setColor(int color) {
        this.entityData.set(DATA_COLOR, color);
    }

    public Vec3 getSwarmCenter() {
        return this.swarmCenter;
    }

    public void setSwarmCenter(Vec3 center) {
        this.swarmCenter = center;
    }

    public int getTimeUntilNewTarget() {
        return this.timeUntilNewTarget;
    }

    public void setTimeUntilNewTarget(int time) {
        this.timeUntilNewTarget = time;
    }

    // Custom goals for swarm behavior
    static class SporeflyJoinSwarmGoal extends Goal {
        private final Sporefly sporefly;
        private int timeToRecalcPath;

        public SporeflyJoinSwarmGoal(Sporefly sporefly) {
            this.sporefly = sporefly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.sporefly.hasSwarmLeader()) {
                return false;
            } else if (this.sporefly.isLeader()) {
                return false;
            } else if (this.sporefly.getRandom().nextInt(10) != 0) {
                return false;
            } else {
                List<? extends Sporefly> list = this.sporefly.level().getEntitiesOfClass(
                        Sporefly.class,
                        this.sporefly.getBoundingBox().inflate(8.0D, 8.0D, 8.0D),
                        (p_25255_) -> p_25255_.canBeFollowed() || !p_25255_.hasSwarmLeader());

                Sporefly leader = list.stream()
                        .filter(Sporefly::canBeFollowed)
                        .findAny()
                        .orElse(null);

                if (leader == null) {
                    return false;
                } else {
                    this.sporefly.setSwarmLeader(leader);
                    return true;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.sporefly.hasSwarmLeader() && this.sporefly.getSwarmLeader().isAlive();
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void stop() {
            this.sporefly.setSwarmLeader(null);
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                Sporefly leader = this.sporefly.getSwarmLeader();
                if (leader != null) {
                    double distanceSq = this.sporefly.distanceToSqr(leader);

                    if (distanceSq > 16.0D) {
                        // Move closer to the leader
                        this.sporefly.getNavigation().moveTo(leader, 1.0D);
                    } else if (distanceSq < 4.0D) {
                        // Move away if too close
                        Vec3 awayVec = this.sporefly.position()
                                .subtract(leader.position())
                                .normalize()
                                .scale(2.0D)
                                .add(leader.position());

                        this.sporefly.getNavigation().moveTo(
                                awayVec.x,
                                awayVec.y,
                                awayVec.z,
                                1.0D);
                    }
                }
            }
        }
    }

    static class SporeflySwarmGoal extends Goal {
        private final Sporefly sporefly;
        private int timeToRecalcPath;

        public SporeflySwarmGoal(Sporefly sporefly) {
            this.sporefly = sporefly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.sporefly.isLeader() && this.sporefly.getSwarmSize() > 1;
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                if (this.sporefly.getTimeUntilNewTarget() <= 0) {
                    // Find a new target position
                    double angle = this.sporefly.getRandom().nextDouble() * Math.PI * 2.0D;
                    double distance = 5.0D + this.sporefly.getRandom().nextDouble() * 5.0D;

                    Vec3 newPos = new Vec3(
                            this.sporefly.getX() + Math.cos(angle) * distance,
                            this.sporefly.getY() + (this.sporefly.getRandom().nextDouble() * 4.0D) - 2.0D,
                            this.sporefly.getZ() + Math.sin(angle) * distance);

                    // Ensure the target is within world bounds and not in a solid block
                    BlockPos pos = BlockPos.containing(newPos);
                    if (this.sporefly.level().getBlockState(pos).isAir()) {
                        this.sporefly.setSwarmCenter(newPos);
                        this.sporefly.setTimeUntilNewTarget(100 + this.sporefly.getRandom().nextInt(100));
                    }
                } else {
                    this.sporefly.setTimeUntilNewTarget(this.sporefly.getTimeUntilNewTarget() - 1);
                }

                // Update swarm members
                if (this.sporefly.getSwarmSize() > 1) {
                    List<Sporefly> swarm = this.sporefly.level().getEntitiesOfClass(
                            Sporefly.class,
                            this.sporefly.getBoundingBox().inflate(16.0D, 8.0D, 16.0D),
                            (entity) -> entity != this.sporefly && entity.getSwarmLeader() == this.sporefly);

                    Vec3 center = this.sporefly.getSwarmCenter();
                    double spread = 2.0D + Math.min(swarm.size() * 0.5, 8.0D);

                    for (int i = 0; i < swarm.size(); ++i) {
                        Sporefly member = swarm.get(i);
                        double angle = (Math.PI * 2.0D * i) / swarm.size();
                        double x = center.x + Math.cos(angle) * spread;
                        double z = center.z + Math.sin(angle) * spread;

                        if (member.distanceToSqr(x, center.y, z) > 1.0D) {
                            member.getNavigation().moveTo(x, center.y, z, 1.0D);
                        }
                    }
                }
            }
        }
    }

    static class SporeflyWanderGoal extends Goal {
        private final Sporefly sporefly;

        public SporeflyWanderGoal(Sporefly sporefly) {
            this.sporefly = sporefly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.sporefly.hasSwarmLeader() && this.sporefly.getNavigation().isDone();
        }

        @Override
        public void tick() {
            if (this.sporefly.getRandom().nextFloat() < 0.03F) {
                double x = this.sporefly.getX() + (this.sporefly.getRandom().nextDouble() - 0.5) * 4.0D;
                double y = Mth.clamp(
                        this.sporefly.getY() + (this.sporefly.getRandom().nextInt(6) - 3),
                        this.sporefly.level().getMinBuildHeight() + 1,
                        this.sporefly.level().getMaxBuildHeight() - 1);
                double z = this.sporefly.getZ() + (this.sporefly.getRandom().nextDouble() - 0.5) * 4.0D;

                this.sporefly.getNavigation().moveTo(x, y, z, 1.0D);
            }
        }
    }

    public boolean canBeFollowed() {
        return this.isAlive() && this.getSwarmSize() < 12;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // No fall damage
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BEE_LOOP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public Sporefly getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        Sporefly baby = CHEXEntities.SPOREFLY.get().create(level);
        if (baby != null) {
            // Mix the colors of the parents
            int color1 = this.getColor();
            int color2 = otherParent instanceof Sporefly ? ((Sporefly) otherParent).getColor() : 0xFF55FF;

            int r = (((color1 >> 16) & 0xFF) + ((color2 >> 16) & 0xFF)) / 2;
            int g = (((color1 >> 8) & 0xFF) + ((color2 >> 8) & 0xFF)) / 2;
            int b = ((color1 & 0xFF) + (color2 & 0xFF)) / 2;

            // Add some random variation
            r = Mth.clamp(r + this.random.nextInt(21) - 10, 0, 255);
            g = Mth.clamp(g + this.random.nextInt(21) - 10, 0, 255);
            b = Mth.clamp(b + this.random.nextInt(21) - 10, 0, 255);

            baby.setColor((r << 16) | (g << 8) | b);
        }
        return baby;
    }
}
