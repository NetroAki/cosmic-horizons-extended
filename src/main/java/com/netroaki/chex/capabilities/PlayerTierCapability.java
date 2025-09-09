package com.netroaki.chex.capabilities;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.RocketTiers;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Player capability for tracking rocket and suit tier progression
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class PlayerTierCapability implements INBTSerializable<CompoundTag> {

    public static final Capability<PlayerTierCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final ResourceLocation ID = CHEX.id("player_tier");

    private int rocketTier = 1; // Start with T1 rocket
    private int suitTier = 1; // Start with T1 suit
    private final Set<String> unlockedPlanets = new HashSet<>();
    private final Set<String> discoveredMinerals = new HashSet<>();

    // Default constructor
    public PlayerTierCapability() {
        // Initialize with basic unlocks
        unlockedPlanets.add("cosmos:earth_moon"); // Always start with moon access
        discoveredMinerals.add("iron"); // Basic mineral knowledge
    }

    // Getters
    public int getRocketTier() {
        return rocketTier;
    }

    public int getSuitTier() {
        return suitTier;
    }

    public Set<String> getUnlockedPlanets() {
        return new HashSet<>(unlockedPlanets);
    }

    public Set<String> getDiscoveredMinerals() {
        return new HashSet<>(discoveredMinerals);
    }

    // Setters
    public void setRocketTier(int tier) {
        this.rocketTier = Math.max(1, Math.min(10, tier)); // Clamp between 1-10
        CHEX.LOGGER.info("Player rocket tier set to T{}", this.rocketTier);
    }

    public void setSuitTier(int tier) {
        this.suitTier = Math.max(1, Math.min(5, tier)); // Clamp between 1-5
        CHEX.LOGGER.info("Player suit tier set to T{}", this.suitTier);
    }

    // Progression methods
    public boolean unlockRocketTier(int tier) {
        if (tier > rocketTier && tier <= 10) {
            setRocketTier(tier);
            CHEX.LOGGER.info("Player unlocked rocket tier T{}", tier);
            return true;
        }
        return false;
    }

    public boolean unlockSuitTier(int tier) {
        if (tier > suitTier && tier <= 5) {
            setSuitTier(tier);
            CHEX.LOGGER.info("Player unlocked suit tier T{}", tier);
            return true;
        }
        return false;
    }

    public void unlockPlanet(String planetId) {
        if (unlockedPlanets.add(planetId)) {
            CHEX.LOGGER.info("Player unlocked planet: {}", planetId);
        }
    }

    public void discoverMineral(String mineral) {
        if (discoveredMinerals.add(mineral)) {
            CHEX.LOGGER.info("Player discovered mineral: {}", mineral);
        }
    }

    // Validation methods
    public boolean canAccessRocketTier(RocketTiers requiredTier) {
        return rocketTier >= requiredTier.getTier();
    }

    public boolean canAccessSuitTier(int requiredTier) {
        return suitTier >= requiredTier;
    }

    public boolean hasUnlockedPlanet(String planetId) {
        return unlockedPlanets.contains(planetId);
    }

    public boolean hasDiscoveredMineral(String mineral) {
        return discoveredMinerals.contains(mineral);
    }

    // NBT Serialization
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("rocketTier", rocketTier);
        tag.putInt("suitTier", suitTier);

        // Serialize unlocked planets
        CompoundTag planetsTag = new CompoundTag();
        int i = 0;
        for (String planet : unlockedPlanets) {
            planetsTag.putString("planet_" + i++, planet);
        }
        planetsTag.putInt("count", unlockedPlanets.size());
        tag.put("unlockedPlanets", planetsTag);

        // Serialize discovered minerals
        CompoundTag mineralsTag = new CompoundTag();
        i = 0;
        for (String mineral : discoveredMinerals) {
            mineralsTag.putString("mineral_" + i++, mineral);
        }
        mineralsTag.putInt("count", discoveredMinerals.size());
        tag.put("discoveredMinerals", mineralsTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        rocketTier = tag.getInt("rocketTier");
        suitTier = tag.getInt("suitTier");

        // Deserialize unlocked planets
        unlockedPlanets.clear();
        if (tag.contains("unlockedPlanets")) {
            CompoundTag planetsTag = tag.getCompound("unlockedPlanets");
            int count = planetsTag.getInt("count");
            for (int i = 0; i < count; i++) {
                unlockedPlanets.add(planetsTag.getString("planet_" + i));
            }
        }

        // Deserialize discovered minerals
        discoveredMinerals.clear();
        if (tag.contains("discoveredMinerals")) {
            CompoundTag mineralsTag = tag.getCompound("discoveredMinerals");
            int count = mineralsTag.getInt("count");
            for (int i = 0; i < count; i++) {
                discoveredMinerals.add(mineralsTag.getString("mineral_" + i));
            }
        }

        // Ensure we have basic unlocks
        if (unlockedPlanets.isEmpty()) {
            unlockedPlanets.add("cosmos:earth_moon");
        }
        if (discoveredMinerals.isEmpty()) {
            discoveredMinerals.add("iron");
        }
    }

    // Capability Provider
    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final PlayerTierCapability capability = new PlayerTierCapability();
        private final LazyOptional<PlayerTierCapability> optional = LazyOptional.of(() -> capability);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return INSTANCE.orEmpty(cap, optional);
        }

        @Override
        public CompoundTag serializeNBT() {
            return capability.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            capability.deserializeNBT(nbt);
        }
    }

    // Event handler to attach capability to players
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (event.getObject() instanceof net.minecraft.world.entity.player.Player) {
            event.addCapability(ID, new Provider());
        }
    }
}
