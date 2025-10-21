package com.netroaki.chex.menu;

import com.netroaki.chex.CHEX;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CHEXMenuTypes {
  public static final DeferredRegister<MenuType<?>> MENUS =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, CHEX.MOD_ID);

  // TODO: Fix when menu system is properly implemented
  // public static final RegistryObject<MenuType<LibraryBookMenu>> LIBRARY_BOOK =
  // registerMenuType(
  // "library_book", (windowId, inv, data) -> new LibraryBookMenu(windowId, inv,
  // data));

  // private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>>
  // registerMenuType(
  // String name, MenuType.MenuSupplier<T> factory) {
  // return MENUS.register(name, () -> IForgeMenuType.create(factory::create));
  // }
}
