package com.netroaki.chex.client.screen;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.CHEXMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class Screens {
  @SubscribeEvent
  public static void clientSetup(FMLClientSetupEvent event) {
    // Register screen factories
    event.enqueueWork(
        () -> {
          MenuScreens.register(CHEXMenuTypes.LIBRARY_BOOK.get(), LibraryBookScreen::new);
        });
  }
}
