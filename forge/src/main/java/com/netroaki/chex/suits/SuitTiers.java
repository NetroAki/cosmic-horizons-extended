package com.netroaki.chex.suits;

import com.netroaki.chex.CHEX;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Defines the 5 suit tiers for CHEX hazard protection system Based on the checklist: Suits I..V
 * enforce hazards (vacuum, acid, radiation, cryogenic)
 */
public enum SuitTiers {
  SUIT_I("suit1", 1, "Basic Space Suit", EnumSet.of(HazardType.VACUUM), "chex:suits/suit1"),
  SUIT_II(
      "suit2",
      2,
      "Advanced Space Suit",
      EnumSet.of(HazardType.VACUUM, HazardType.CRYOGENIC),
      "chex:suits/suit2"),
  SUIT_III(
      "suit3",
      3,
      "Enhanced Space Suit",
      EnumSet.of(HazardType.VACUUM, HazardType.CRYOGENIC, HazardType.ACID),
      "chex:suits/suit3"),
  SUIT_IV(
      "suit4",
      4,
      "Superior Space Suit",
      EnumSet.of(HazardType.VACUUM, HazardType.CRYOGENIC, HazardType.ACID, HazardType.RADIATION),
      "chex:suits/suit4"),
  SUIT_V(
      "suit5",
      5,
      "Ultimate Space Suit",
      EnumSet.of(
          HazardType.VACUUM,
          HazardType.CRYOGENIC,
          HazardType.ACID,
          HazardType.RADIATION,
          HazardType.EXOTIC),
      "chex:suits/suit5");

  private final String id;
  private final int tier;
  private final String displayName;
  private final Set<HazardType> protectedHazards;
  private final String requiredTagString;
  private TagKey<Item> requiredTag;

  SuitTiers(
      String id,
      int tier,
      String displayName,
      Set<HazardType> protectedHazards,
      String requiredTagString) {
    this.id = id;
    this.tier = tier;
    this.displayName = displayName;
    this.protectedHazards = protectedHazards;
    this.requiredTagString = requiredTagString;
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

  public Set<HazardType> getProtectedHazards() {
    return protectedHazards;
  }

  public TagKey<Item> getRequiredTag() {
    return requiredTag;
  }

  public String getRequiredTagString() {
    return requiredTagString;
  }

  /** Check if this suit protects against a specific hazard */
  public boolean protectsAgainst(HazardType hazard) {
    return protectedHazards.contains(hazard);
  }

  /** Check if this suit can handle all hazards in a set */
  public boolean canHandleHazards(Set<HazardType> hazards) {
    return protectedHazards.containsAll(hazards);
  }

  /** Get the minimum suit tier required to handle a set of hazards */
  public static SuitTiers getMinimumSuitForHazards(Set<HazardType> hazards) {
    for (SuitTiers suit : values()) {
      if (suit.canHandleHazards(hazards)) {
        return suit;
      }
    }
    return SUIT_V; // Fallback to highest tier
  }

  /** Initialize the suit tiers system */
  public static void initialize() {
    CHEX.LOGGER.info("Initializing suit tiers system...");

    for (SuitTiers suit : values()) {
      // Create the suit tag for this tier
      suit.requiredTag =
          TagKey.create(
              ForgeRegistries.ITEMS.getRegistryKey(),
              ResourceLocation.parse(suit.requiredTagString));

      CHEX.LOGGER.debug(
          "Initialized {} - {} (protects against: {})",
          suit.id,
          suit.displayName,
          suit.protectedHazards);
    }

    CHEX.LOGGER.info("Suit tiers system initialized with {} tiers", values().length);
  }

  /** Get a suit tier by its tier number */
  public static SuitTiers getByTier(int tier) {
    for (SuitTiers suit : values()) {
      if (suit.getTier() == tier) {
        return suit;
      }
    }
    return null;
  }

  /** Get a suit tier by its ID */
  public static SuitTiers getById(String id) {
    for (SuitTiers suit : values()) {
      if (suit.getId().equals(id)) {
        return suit;
      }
    }
    return null;
  }

  /** Hazard types that suits can protect against */
  public enum HazardType {
    VACUUM("vacuum", "Vacuum"),
    CRYOGENIC("cryogenic", "Cryogenic"),
    ACID("acid", "Acid"),
    RADIATION("radiation", "Radiation"),
    EXOTIC("exotic", "Exotic");

    private final String id;
    private final String displayName;

    HazardType(String id, String displayName) {
      this.id = id;
      this.displayName = displayName;
    }

    public String getId() {
      return id;
    }

    public String getDisplayName() {
      return displayName;
    }
  }
}
