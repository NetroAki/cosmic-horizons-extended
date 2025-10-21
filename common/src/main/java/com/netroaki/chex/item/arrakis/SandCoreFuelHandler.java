package com.netroaki.chex.item.arrakis;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

/** Handles the Sand Core's functionality as a T4 fuel source. */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SandCoreFuelHandler {
  // Burn time for the Sand Core (1 hour of real time = 20 * 60 * 60 = 72000
  // ticks)
  public static final int SAND_CORE_BURN_TIME = 72000;

  // T4 fuel type that this item represents
  public static final int TIER = 4;
  public static final ResourceLocation SAND_CORE_ID =
      new ResourceLocation(CHEX.MOD_ID, "sand_core");

  /** Registers the Sand Core as a fuel item. Should be called during mod initialization. */
  public static void register() {
    // Register with our custom fuel system
    FuelRegistry.registerFuelItem(TIER, SAND_CORE_ID, SAND_CORE_BURN_TIME);

    CHEX.LOGGER.info(
        "Registered Sand Core as T{} fuel with {} ticks burn time", TIER, SAND_CORE_BURN_TIME);
  }

  /** Handles the burn time for the Sand Core in furnaces and other fuel-using blocks. */
  @SubscribeEvent
  public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
    if (isSandCore(event.getItemStack())) {
      event.setBurnTime(SAND_CORE_BURN_TIME);
    }
  }

  /** Checks if an ItemStack is a Sand Core. */
  public static boolean isSandCore(ItemStack stack) {
    return !stack.isEmpty() && stack.getItem() instanceof SandCoreItem;
  }

  /** Gets the burn time for a Sand Core. */
  public static int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
    return isSandCore(stack) ? SAND_CORE_BURN_TIME : 0;
  }

  /** Checks if an item is valid fuel for the specified tier. */
  public static boolean isFuelForTier(ItemStack stack, int tier) {
    return tier == TIER && isSandCore(stack);
  }
}
