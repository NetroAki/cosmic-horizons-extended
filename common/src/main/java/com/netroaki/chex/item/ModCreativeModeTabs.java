package com.netroaki.chex.item;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CosmicHorizonsExpanded.MOD_ID);

  public static final RegistryObject<CreativeModeTab> AQUA_MUNDUS_TAB =
      CREATIVE_MODE_TABS.register(
          "aqua_mundus_tab",
          () ->
              CreativeModeTab.builder()
                  .title(
                      Component.translatable(
                          "itemGroup." + CosmicHorizonsExpanded.MOD_ID + ".aqua_mundus"))
                  .icon(() -> new ItemStack(ModItems.AQUA_MUNDUS_CHESTPLATE.get()))
                  .displayItems(
                      (parameters, output) -> {
                        // Add all Aqua Mundus items to the tab
                        output.accept(ModItems.AQUA_MUNDUS_HELMET.get());
                        output.accept(ModItems.AQUA_MUNDUS_CHESTPLATE.get());
                        output.accept(ModItems.AQUA_MUNDUS_LEGGINGS.get());
                        output.accept(ModItems.AQUA_MUNDUS_BOOTS.get());
                      })
                  .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                  .build());

  public static void register(IEventBus eventBus) {
    CREATIVE_MODE_TABS.register(eventBus);
  }
}
