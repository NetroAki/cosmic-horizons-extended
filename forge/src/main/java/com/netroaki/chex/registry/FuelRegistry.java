package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

/** Fuel registry system for nodule tiers */
public class FuelRegistry {

  private static final Map<Integer, FuelRequirement> TIER_FUEL_REQUIREMENTS = new HashMap<>();
  private static final Map<Integer, Integer> TIER_FUEL_VOLUMES = new HashMap<>();

  public static void initialize() {
    CHEX.LOGGER.info("Initializing fuel registry...");

    // Check if GTCEu is present
    boolean gtceuPresent = net.minecraftforge.fml.ModList.get().isLoaded("gtceu");
    String modId = gtceuPresent ? "gtceu" : "chex";
    
    CHEX.LOGGER.info("Using {} as the primary fluid source", modId);

    // T1: Basic Nodule - Kerosene
    registerFuel(1, 
        "gtceu:kerosene", 1000,
        "chex:kerosene"
    );

    // T2: Advanced Nodule - RP-1
    registerFuel(2, 
        "gtceu:rocket_fuel", 1500,
        "chex:rp1"
    );

    // T3: Improved Nodule - LOX
    registerFuel(3, 
        "gtceu:liquid_oxygen", 2000,
        "chex:lox"
    );

    // T4: Enhanced Nodule - LH2
    registerFuel(4, 
        "gtceu:hydrogen", 2500,
        "chex:lh2"
    );

    // T5: Superior Nodule - DT Mix
    registerFuel(5, 
        "gtceu:deuterium", 3000,
        "chex:dt_mix"
    );

    // T6: Elite Nodule - He3 Blend
    registerFuel(6, 
        "gtceu:helium_3", 3500,
        "chex:he3_blend"
    );

    // T7: Master Nodule - Exotic Mix
    registerFuel(7, 
        "gtceu:naquadria", 4000,
        "chex:exotic_mix"
    );

    // T8-T10: Use exotic mix for highest tiers with increasing efficiency
    registerFuel(8, 
        "gtceu:naquadria", 4500,
        "chex:exotic_mix"
    );
    registerFuel(9, 
        "gtceu:naquadria", 5000,
        "chex:exotic_mix"
    );
    registerFuel(10, 
        "gtceu:naquadria", 6000,
        "chex:exotic_mix"
    );

    CHEX.LOGGER.info("Fuel registry initialized with {} fuel types", TIER_FUEL_REQUIREMENTS.size());
    
    // Log registered fuels for debugging
    for (Map.Entry<Integer, FuelRequirement> entry : TIER_FUEL_REQUIREMENTS.entrySet()) {
      CHEX.LOGGER.debug("Tier {}: {} ({} mB)", 
          entry.getKey(), 
          entry.getValue().getFluidId(),
          TIER_FUEL_VOLUMES.get(entry.getKey()));
    }
  }

  public static void registerFuel(int tier, String fluidId, int volumeMb) {
    ResourceLocation fluidResource = ResourceLocation.parse(fluidId);
    Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidResource);

    if (fluid != null) {
      TIER_FUEL_REQUIREMENTS.put(tier, new FuelRequirement(fluid, fluidResource));
      TIER_FUEL_VOLUMES.put(tier, volumeMb);
      CHEX.LOGGER.debug("Registered fuel for T{}: {} ({}mB)", tier, fluidId, volumeMb);
    } else {
      CHEX.LOGGER.warn("Failed to register fuel for T{}: {} not found", tier, fluidId);
    }
  }

  public static Optional<FuelRequirement> getFuelRequirement(int tier) {
    return Optional.ofNullable(TIER_FUEL_REQUIREMENTS.get(tier));
  }

  public static int getFuelVolume(int tier) {
    return TIER_FUEL_VOLUMES.getOrDefault(tier, 0);
  }

  /**
   * Checks if the given fluid and amount is sufficient for the specified tier.
   * 
   * @param tier The tier of the fuel being checked
   * @param fluidId The ID of the fluid to check
   * @param availableMb The amount of fluid available in millibuckets
   * @return true if the fluid and amount are sufficient, false otherwise
   */
  public static boolean isFuelSufficientForTier(int tier, ResourceLocation fluidId, int availableMb) {
    // If no requirement for this tier, allow any fuel
    var reqOpt = getFuelRequirement(tier);
    if (reqOpt.isEmpty()) {
      return true;
    }
    
    // Check if we have enough of the exact fluid
    var req = reqOpt.get();
    int required = getFuelVolume(tier);
    
    if (availableMb < required) {
      return false;
    }
    
    // Exact match always works
    if (req.getFluidId().equals(fluidId)) {
      return true;
    }
    
    // Check if we accept higher tier fuels
    if (FuelConfig.SERVER.acceptHigherTierFuel.get()) {
      // Check if this is one of our known fuel types
      String ns = fluidId.getNamespace();
      if (ns.equals("gtceu") || ns.equals(com.netroaki.chex.CHEX.MOD_ID)) {
        // Get the tier of the provided fluid
        Optional<Integer> fluidTier = getTierForFluid(fluidId);
        
        // If we can't determine the tier, only allow exact matches
        if (fluidTier.isEmpty()) {
          return false;
        }
        
        // Allow higher or equal tier fuels
        return fluidTier.get() >= tier;
      }
    }
    
    return false;
  }

  /**
   * Gets the tier for a given fluid ID, if it's registered as a fuel.
   * 
   * @param fluidId The ID of the fluid to check
   * @return An Optional containing the tier if found, or empty if not a registered fuel
   */
  public static Optional<Integer> getTierForFluid(ResourceLocation fluidId) {
    // Create a reverse mapping of fluid IDs to tiers
    Map<ResourceLocation, Integer> fluidToTier = new HashMap<>();
    for (Map.Entry<Integer, FuelRequirement> entry : TIER_FUEL_REQUIREMENTS.entrySet()) {
      fluidToTier.put(entry.getValue().getFluidId(), entry.getKey());
    }
    
    return Optional.ofNullable(fluidToTier.get(fluidId));
  }
  
  /**
   * Gets all registered fuel tiers and their requirements.
   * 
   * @return An unmodifiable map of tier to fuel requirement
   */
  public static Map<Integer, FuelRequirement> getAllFuelRequirements() {
    return Collections.unmodifiableMap(TIER_FUEL_REQUIREMENTS);
  }
  
  /**
   * Gets all registered fuel volumes.
   * 
   * @return An unmodifiable map of tier to fuel volume in millibuckets
   */
  public static Map<Integer, Integer> getAllFuelVolumes() {
    return Collections.unmodifiableMap(TIER_FUEL_VOLUMES);
  }
  
  public static boolean isValidFuel(int tier, Fluid fluid) {
    return getFuelRequirement(tier).map(req -> req.getFluid() == fluid).orElse(false);
  }

  public static class FuelRequirement {
    private final Fluid fluid;
    private final ResourceLocation fluidId;

    public FuelRequirement(Fluid fluid, ResourceLocation fluidId) {
      this.fluid = fluid;
      this.fluidId = fluidId;
    }

    public Fluid getFluid() {
      return fluid;
    }

    public ResourceLocation getFluidId() {
      return fluidId;
    }
  }
}
