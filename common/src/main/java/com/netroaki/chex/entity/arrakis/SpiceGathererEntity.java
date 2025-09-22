package com.netroaki.chex.entity.arrakis;

import com.netroaki.chex.registry.CHEXItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SpiceGathererEntity extends AbstractVillager {
    private static final EntityDataAccessor<Boolean> HAS_SPICE = SynchedEntityData.defineId(SpiceGathererEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_AGGRESSIVE = SynchedEntityData.defineId(SpiceGathererEntity.class, EntityDataSerializers.BOOLEAN);
    
    public SpiceGathererEntity(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);
        this.setCanPickUpLoot(true);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 30.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            .add(Attributes.ATTACK_DAMAGE, 4.0D)
            .add(Attributes.FOLLOW_RANGE, 24.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(2, new MoveToGoal(this, 2.0D, 0.35D));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new DefendVillageTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_SPICE, false);
        this.entityData.define(IS_AGGRESSIVE, false);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasSpice", this.hasSpice());
        tag.putBoolean("IsAggressive", this.isAggressive());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHasSpice(tag.getBoolean("HasSpice"));
        this.setAggressive(tag.getBoolean("IsAggressive"));
    }
    
    @Override
    protected void updateTrades() {
        // Add custom trades here
        MerchantOffers offers = this.getOffers();
        
        // Basic spice trade
        offers.add(new MerchantOffer(
            new ItemStack(Items.EMERALD, 3),
            new ItemStack(CHEXItems.SPICE_MELANGE.get(), 1),
            8, 2, 0.05F
        ));
        
        // More trades can be added here
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        
        if (!this.level().isClientSide) {
            // Update spice status based on time of day
            long dayTime = this.level().getDayTime() % 24000;
            boolean isNight = dayTime >= 13000 && dayTime <= 23000;
            
            if (isNight && !this.hasSpice() && this.random.nextInt(100) == 0) {
                this.setHasSpice(true);
            } else if (!isNight && this.hasSpice() && this.random.nextInt(200) == 0) {
                this.setHasSpice(false);
            }
            
            // Randomly become aggressive if player has spice
            if (this.getTarget() instanceof Player player) {
                if (this.isAggressive() && this.random.nextInt(100) == 0) {
                    this.setAggressive(false);
                }
            }
        }
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        // Check if player is offering spice
        if (itemstack.is(CHEXItems.SPICE_MELANGE.get())) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            
            // Make friendly and trade
            this.setAggressive(false);
            this.setTarget(null);
            
            if (!this.level().isClientSide) {
                this.updateTrades();
                this.startTrading(player);
            }
            
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        
        return super.mobInteract(player, hand);
    }
    
    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
    
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        
        // Drop spice on death if carrying any
        if (this.hasSpice() && !this.level().isClientSide) {
            this.spawnAtLocation(new ItemStack(CHEXItems.SPICE_MELANGE.get(), this.random.nextInt(3) + 1));
        }
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        RandomSource random = level.getRandom();
        this.populateDefaultEquipmentSlots(random, difficulty);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        // Equip with basic gear
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(CHEXItems.CRYSKNIFE.get()));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(CHEXItems.STILLSUIT_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(CHEXItems.STILLSUIT_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(CHEXItems.STILLSUIT_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(CHEXItems.STILLSUIT_BOOTS.get()));
    }
    
    public boolean hasSpice() {
        return this.entityData.get(HAS_SPICE);
    }
    
    public void setHasSpice(boolean hasSpice) {
        this.entityData.set(HAS_SPICE, hasSpice);
    }
    
    public boolean isAggressive() {
        return this.entityData.get(IS_AGGRESSIVE);
    }
    
    public void setAggressive(boolean aggressive) {
        this.entityData.set(IS_AGGRESSIVE, aggressive);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }
    
    @Override
    public void playCelebrateSound() {
        this.playSound(SoundEvents.VILLAGER_YES, this.getSoundVolume(), this.getVoicePitch());
    }
}
