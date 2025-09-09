package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Defines the 10 rocket tiers for CHEX progression system
 */
public enum RocketTiers {
    T1("T1", 1, "Basic Rocket", "chex:suits/suit1"),
    T2("T2", 2, "Advanced Rocket", "chex:suits/suit1"),
    T3("T3", 3, "Improved Rocket", "chex:suits/suit2"),
    T4("T4", 4, "Enhanced Rocket", "chex:suits/suit2"),
    T5("T5", 5, "Superior Rocket", "chex:suits/suit3"),
    T6("T6", 6, "Elite Rocket", "chex:suits/suit3"),
    T7("T7", 7, "Master Rocket", "chex:suits/suit4"),
    T8("T8", 8, "Legendary Rocket", "chex:suits/suit4"),
    T9("T9", 9, "Mythic Rocket", "chex:suits/suit5"),
    T10("T10", 10, "Cosmic Rocket", "chex:suits/suit5");

    private final String id;
    private final int tier;
    private final String displayName;
    private final String requiredSuitTag;
    private TagKey<Item> suitTag;

    RocketTiers(String id, int tier, String displayName, String requiredSuitTag) {
        this.id = id;
        this.tier = tier;
        this.displayName = displayName;
        this.requiredSuitTag = requiredSuitTag;
    }

    public String getId() {
        return id;
    }

    public int getTier() {
        return tier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TagKey<Item> getRequiredSuitTag() {
        return suitTag;
    }

    public String getRequiredSuitTagString() {
        return requiredSuitTag;
    }

    /**
     * Initialize the rocket tiers system
     */
    public static void initialize() {
        CHEX.LOGGER.info("Initializing rocket tiers system...");

        for (RocketTiers tier : values()) {
            // Create the suit tag for this tier
            tier.suitTag = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),
                    ResourceLocation.parse(tier.requiredSuitTag));

            CHEX.LOGGER.debug("Initialized {} - {} (requires {})",
                    tier.id, tier.displayName, tier.requiredSuitTag);
        }

        CHEX.LOGGER.info("Rocket tiers system initialized with {} tiers", values().length);
    }

    /**
     * Get a rocket tier by its tier number
     */
    public static RocketTiers getByTier(int tier) {
        for (RocketTiers rocketTier : values()) {
            if (rocketTier.getTier() == tier) {
                return rocketTier;
            }
        }
        return null;
    }

    /**
     * Get a rocket tier by its ID
     */
    public static RocketTiers getById(String id) {
        for (RocketTiers rocketTier : values()) {
            if (rocketTier.getId().equals(id)) {
                return rocketTier;
            }
        }
        return null;
    }

    /**
     * Get a rocket tier by its tier number (alias for getByTier)
     */
    public static RocketTiers fromLevel(int tier) {
        return getByTier(tier);
    }

    /**
     * Check if this tier is higher than another tier
     */
    public boolean isHigherThan(RocketTiers other) {
        return this.tier > other.tier;
    }

    /**
     * Check if this tier is lower than another tier
     */
    public boolean isLowerThan(RocketTiers other) {
        return this.tier < other.tier;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", displayName, id);
    }
}