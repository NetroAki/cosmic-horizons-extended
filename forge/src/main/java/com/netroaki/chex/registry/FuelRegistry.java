package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

/** Fuel registry system for nodule tiers */
public class FuelRegistry {

  private static final Map<Integer, FuelRequirement> TIER_FUEL_REQUIREMENTS = new HashMap<>();
  private static final Map<Integer, Integer> TIER_FUEL_VOLUMES = new HashMap<>();
  private static final Map<ResourceLocation, ItemFuelInfo> ITEM_FUELS = new HashMap<>();

  public static void initialize() {
    CHEX.LOGGER.info("Initializing fuel registry...");

    // T1: Basic Nodule - Kerosene
    registerFuel(1, "cosmic_horizons_extended:kerosene", 1000);

    // T2: Advanced Nodule - RP-1
    registerFuel(2, "cosmic_horizons_extended:rp1", 1500);

    // T3: Improved Nodule - LOX
    registerFuel(3, "cosmic_horizons_extended:lox", 2000);

    // T4: Enhanced Nodule - LH2
    registerFuel(4, "cosmic_horizons_extended:lh2", 2500);

    // T5: Superior Nodule - DT Mix
    registerFuel(5, "cosmic_horizons_extended:dt_mix", 3000);

    // T6: Elite Nodule - He3 Blend
    registerFuel(6, "cosmic_horizons_extended:he3_blend", 3500);

    // T7: Master Nodule - Exotic Mix
    registerFuel(7, "cosmic_horizons_extended:exotic_mix", 4000);

    // T8-T10: Use exotic mix for highest tiers
    registerFuel(8, "cosmic_horizons_extended:exotic_mix", 4500);
    registerFuel(9, "cosmic_horizons_extended:exotic_mix", 5000);
    registerFuel(10, "cosmic_horizons_extended:exotic_mix", 6000);

    CHEX.LOGGER.info("Fuel registry initialized with {} fuel types", TIER_FUEL_REQUIREMENTS.size());
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

  public static boolean isFuelSufficientForTier(
      int tier, ResourceLocation fluidId, int availableMb) {
    var reqOpt = getFuelRequirement(tier);
    if (reqOpt.isEmpty()) return true;
    var req = reqOpt.get();
    int required = getFuelVolume(tier);
    if (availableMb < required) return false;
    // Exact match or acceptHigherTierFuel allows any registered higher tier fluid
    if (req.getFluidId().equals(fluidId)) return true;
    if (com.netroaki.chex.config.CHEXConfig.acceptHigherTierFuel()) {
      // simple heuristic: allow any of our known fluids
      String ns = fluidId.getNamespace();
      return ns.equals(com.netroaki.chex.CHEX.MOD_ID);
    }
    return false;
  }

  public static boolean isValidFuel(int tier, Fluid fluid) {
    return getFuelRequirement(tier).map(req -> req.getFluid() == fluid).orElse(false);
  }

  /**
   * Registers an item as fuel for a specific tier
   *
   * @param tier The tier this fuel is valid for
   * @param itemId The item ID to register as fuel
   * @param burnTime The burn time in ticks (1 item = 1 bucket equivalent)
   */
  public static void registerFuelItem(int tier, ResourceLocation itemId, int burnTime) {
    if (burnTime <= 0) {
      CHEX.LOGGER.warn("Invalid burn time {} for item {}", burnTime, itemId);
      return;
    }

    Item item = ForgeRegistries.ITEMS.getValue(itemId);
    if (item != null) {
      ITEM_FUELS.put(itemId, new ItemFuelInfo(tier, burnTime));
      CHEX.LOGGER.info("Registered item fuel for T{}: {} ({} ticks)", tier, itemId, burnTime);
    } else {
      CHEX.LOGGER.warn("Failed to register item fuel: {} not found", itemId);
    }
  }

  /** Gets the fuel info for an item, if it's registered as fuel */
  public static Optional<ItemFuelInfo> getItemFuelInfo(Item item) {
    ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
    return Optional.ofNullable(ITEM_FUELS.get(itemId));
  }

  /** Checks if an item is valid fuel for a specific tier */
  public static boolean isItemValidForTier(ItemStack stack, int tier) {
    return getItemFuelInfo(stack.getItem()).map(info -> info.tier() == tier).orElse(false);
  }

  /** Gets the burn time for an item if it's registered as fuel */
  public static int getItemBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
    return getItemFuelInfo(stack.getItem()).map(ItemFuelInfo::burnTime).orElse(0);
  }

  /** Information about a fuel requirement for a tier */
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

  /** Information about an item-based fuel */
  public record ItemFuelInfo(int tier, int burnTime) {
    /** Gets the number of items needed to match one bucket of fluid fuel */
    public int getItemsPerBucket() {
      // Default to 1 item = 1 bucket (1000mB)
      return Math.max(1, 1000 / (burnTime / 20)); // Convert burn time to mB equivalent
    }
  }
}
