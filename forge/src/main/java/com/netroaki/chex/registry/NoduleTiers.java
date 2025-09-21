package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/** Defines the 10 nodule tiers for CHEX progression system */
public enum NoduleTiers {
  T1("T1", 1, "Basic Nodule", "chex:suits/suit1", "iron"),
  T2("T2", 2, "Advanced Nodule", "chex:suits/suit1", "nickel"),
  T3("T3", 3, "Improved Nodule", "chex:suits/suit2", "titanium"),
  T4("T4", 4, "Enhanced Nodule", "chex:suits/suit2", "tungsten"),
  T5("T5", 5, "Superior Nodule", "chex:suits/suit3", "platinum"),
  T6("T6", 6, "Elite Nodule", "chex:suits/suit3", "spice_melange"),
  T7("T7", 7, "Master Nodule", "chex:suits/suit4", "uranium"),
  T8("T8", 8, "Legendary Nodule", "chex:suits/suit4", "beryllium"),
  T9("T9", 9, "Mythic Nodule", "chex:suits/suit5", "diamond_core"),
  T10("T10", 10, "Cosmic Nodule", "chex:suits/suit5", "mythic_alloy");

  private final String id;
  private final int tier;
  private final String displayName;
  private final String requiredSuitTag;
  private final String materialName;
  private TagKey<Item> suitTag;

  NoduleTiers(
      String id, int tier, String displayName, String requiredSuitTag, String materialName) {
    this.id = id;
    this.tier = tier;
    this.displayName = displayName;
    this.requiredSuitTag = requiredSuitTag;
    this.materialName = materialName;
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

  public String getMaterialName() {
    return materialName;
  }

  /** Initialize the nodule tiers system */
  public static void initialize() {
    CHEX.LOGGER.info("Initializing nodule tiers system...");

    for (NoduleTiers tier : values()) {
      // Create the suit tag for this tier
      tier.suitTag =
          TagKey.create(
              ForgeRegistries.ITEMS.getRegistryKey(), ResourceLocation.parse(tier.requiredSuitTag));

      CHEX.LOGGER.debug(
          "Initialized {} - {} (requires {})", tier.id, tier.displayName, tier.requiredSuitTag);
    }

    CHEX.LOGGER.info("Nodule tiers system initialized with {} tiers", values().length);
  }

  /** Get a nodule tier by its tier number */
  public static NoduleTiers getByTier(int tier) {
    for (NoduleTiers noduleTier : values()) {
      if (noduleTier.getTier() == tier) {
        return noduleTier;
      }
    }
    return null;
  }

  /** Get a nodule tier by its ID */
  public static NoduleTiers getById(String id) {
    for (NoduleTiers noduleTier : values()) {
      if (noduleTier.getId().equals(id)) {
        return noduleTier;
      }
    }
    return null;
  }

  /** Get a nodule tier by its tier number (alias for getByTier) */
  public static NoduleTiers fromLevel(int tier) {
    return getByTier(tier);
  }

  /** Check if this tier is higher than another tier */
  public boolean isHigherThan(NoduleTiers other) {
    return this.tier > other.tier;
  }

  /** Check if this tier is lower than another tier */
  public boolean isLowerThan(NoduleTiers other) {
    return this.tier < other.tier;
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", displayName, id);
  }
}
