package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXMenuTypes {
  public static final DeferredRegister<MenuType<?>> MENU_TYPES =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, CHEX.MOD_ID);

  // Library UI temporarily disabled pending implementation

  private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
      String name, MenuType.MenuSupplier<T> factory) {
    return MENU_TYPES.register(name, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
  }

  public static void register() {
    // This method is called to register the menu types
  }
}
