package com.netroaki.chex.entity;

import com.netroaki.chex.registry.ArrakisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Map;

public class FremenVillager extends Villager {
    private static final EntityDataAccessor<Boolean> DATA_IS_ARMED = SynchedEntityData.defineId(FremenVillager.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_REPUTATION = SynchedEntityData.defineId(FremenVillager.class, EntityDataSerializers.INT);
    
    public FremenVillager(EntityType<? extends Villager> type, Level level) {
        super(type, level);
        this.setCanPickUpLoot(true);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Villager.createAttributes()
            .add(Attributes.MAX_HEALTH, 30.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            .add(Attributes.ARMOR, 4.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(2, new MoveToTargetGoal(this, 0.5D, 0.35D));
        this.goalSelector.addGoal(3, new MoveToHomeGoal(this, 0.5D, 0.35D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        // Custom goals for Fremen behavior
        this.goalSelector.addGoal(7, new FindWaterGoal(this, 0.01F));
        this.goalSelector.addGoal(8, new DefendVillageGoal(this));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_ARMED, false);
        this.entityData.define(DATA_REPUTATION, 0);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsArmed", this.isArmed());
        tag.putInt("Reputation", this.getReputation());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setArmed(tag.getBoolean("IsArmed"));
        this.setReputation(tag.getInt("Reputation"));
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, 
                                      MobSpawnType reason, @Nullable SpawnGroupData spawnData, 
                                      @Nullable CompoundTag dataTag) {
        // Randomize profession based on biome
        if (level.getBiome(this.blockPosition()).is(BiomeTags.IS_DESERT)) {
            this.setVillagerData(this.getVillagerData()
                .setProfession(this.random.nextBoolean() ? 
                    VillagerProfession.LEATHERWORKER : 
                    VillagerProfession.WEAPONSMITH));
        }
        
        // Chance to be armed based on difficulty
        if (random.nextFloat() < 0.3F * level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty()) {
            this.setArmed(true);
        }
        
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
    
    @Override
    protected void updateTrades() {
        // Custom trades for Fremen
        VillagerTrades.ItemListing[] trades = this.getVillagerData().getProfession().getTrades(1);
        if (trades != null) {
            VillagerTrades.ItemListing[] fremenTrades = new VillagerTrades.ItemListing[]{
                new ItemsForEmeraldsTrade(ArrakisItems.SPICE_CRYSTAL.get(), 1, 2, 16, 2, 0.05F),
                new ItemsForEmeraldsTrade(ArrakisItems.STILLSUIT_HELMET.get(), 10, 1, 12, 5, 0.2F),
                new ItemsForEmeraldsTrade(ArrakisItems.CRYOPHASE_BLADE.get(), 15, 1, 3, 10, 0.1F)
            };
            
            this.addOffersFromItemListings(this.getOffers(), fremenTrades, 3);
        }
    }
    
    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        // Increase reputation with successful trades
        if (offer.shouldRewardExp()) {
            this.setReputation(this.getReputation() + 1);
        }
        super.rewardTradeXp(offer);
    }
    
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        // Special interaction with water-related items
        if (itemstack.is(Items.WATER_BUCKET) && this.getReputation() < 10) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
                player.addItem(new ItemStack(Items.BUCKET));
            }
            
            // Increase reputation for water
            this.setReputation(this.getReputation() + 2);
            this.heal(10.0F);
            this.playSound(SoundEvents.VILLAGER_YES, 1.0F, 1.0F);
            
            return InteractionResult.SUCCESS;
        }
        
        return super.mobInteract(player, hand);
    }
    
    // Custom AI Goals
    static class FindWaterGoal extends MoveToBlockGoal {
        private final FremenVillager villager;
        
        public FindWaterGoal(FremenVillager villager, float probability) {
            super(villager, 1.0D, 16, 4);
            this.villager = villager;
            this.verticalSearchStart = -1;
        }
        
        @Override
        public boolean canUse() {
            // Only search for water when not in combat and during the day
            return !villager.level().isDay() && 
                   villager.getTarget() == null && 
                   super.canUse();
        }
        
        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            // Look for water sources in the desert
            return level.getBlockState(pos).is(Blocks.WATER);
        }
        
        @Override
        public void tick() {
            super.tick();
            if (this.isReachedTarget()) {
                // Drink water if found
                villager.heal(4.0F);
                villager.playSound(SoundEvents.GENERIC_DRINK, 1.0F, 1.0F);
                this.stop();
            }
        }
    }
    
    static class DefendVillageGoal extends TargetGoal {
        private final FremenVillager villager;
        private LivingEntity potentialTarget;
        
        public DefendVillageGoal(FremenVillager villager) {
            super(villager, false, true);
            this.villager = villager;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }
        
        @Override
        public boolean canUse() {
            // Only defend if armed and not in combat
            if (!villager.isArmed() || villager.getTarget() != null) {
                return false;
            }
            
            // Look for hostile mobs nearby
            List<Monster> list = villager.level().getEntitiesOfClass(
                Monster.class, 
                villager.getBoundingBox().inflate(16.0D, 4.0D, 16.0D),
                (monster) -> monster.getTarget() instanceof Villager || monster.getTarget() == null
            );
            
            if (list.isEmpty()) {
                return false;
            }
            
            // Target the closest hostile mob
            this.potentialTarget = list.get(0);
            return true;
        }
        
        @Override
        public void start() {
            villager.setTarget(this.potentialTarget);
            super.start();
        }
    }
    
    // Custom trade implementation
    static class ItemsForEmeraldsTrade implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public ItemsForEmeraldsTrade(Item item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this(new ItemStack(item), emeraldCost, numberOfItems, maxUses, villagerXp, priceMultiplier);
        }
        
        public ItemsForEmeraldsTrade(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
            this.itemStack = itemStack;
            this.emeraldCost = emeraldCost;
            this.numberOfItems = numberOfItems;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }
        
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            ItemStack itemstack = new ItemStack(Items.EMERALD, this.emeraldCost);
            ItemStack result = new ItemStack(this.itemStack.getItem(), this.numberOfItems);
            
            if (this.itemStack.hasTag()) {
                result.setTag(this.itemStack.getTag().copy());
            }
            
            return new MerchantOffer(itemstack, result, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    // Getters and setters
    public boolean isArmed() {
        return this.entityData.get(DATA_IS_ARMED);
    }
    
    public void setArmed(boolean armed) {
        this.entityData.set(DATA_IS_ARMED, armed);
    }
    
    public int getReputation() {
        return this.entityData.get(DATA_REPUTATION);
    }
    
    public void setReputation(int reputation) {
        this.entityData.set(DATA_REPUTATION, Math.min(10, Math.max(0, reputation)));
    }
    
    @Override
    protected SoundEvent getTradeUpdatedSound(boolean hasTraded) {
        return hasTraded ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
    }
}
