package com.netroaki.chex;

import com.netroaki.chex.client.sound.DesertAmbienceHandler;
import com.netroaki.chex.client.sound.DesertMusicHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class CHEXClient {
  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    // Client-side initialization
    event.enqueueWork(
        () -> {
          // Register client-side only initialization here
        });
  }

  @SubscribeEvent
  public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
    // Register client-side resource reload listeners here
  }

  // The following static initializers ensure the handlers are registered
  static {
    // These will be initialized when the class is loaded
    new DesertAmbienceHandler();
    new DesertMusicHandler();
  }
}
