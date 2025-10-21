package com.netroaki.chex.registry.items;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.item.arrakis.SandCoreItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArrakisItems {
  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, CHEX.MOD_ID);

  // Boss Drops
  public static final RegistryObject<Item> SAND_CORE =
      ITEMS.register(
          "sand_core",
          () -> new SandCoreItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

  // TODO: Spawn Eggs when ArrakisEntities is implemented
  // public static final RegistryObject<Item> SHAI_HULUD_SPAWN_EGG =
  // ITEMS.register(
  // "shai_hulud_spawn_egg",
  // () ->
  // new SpawnEggItem(
  // ArrakisEntities.SHAI_HULUD,
  // 0x8B4513, // Sandy brown color
  // 0x654321, // Darker brown for spots
  // new Item.Properties()));

  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
  }

  public static void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
    if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
      event.accept(SAND_CORE);
      // } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
      // event.accept(SHAI_HULUD_SPAWN_EGG);
      // }
    }
  }
}
