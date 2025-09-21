package com.netroaki.chex.events;

import com.netroaki.chex.CHEX;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GTCEuIntegrationEvent {

  @SubscribeEvent
  public static void onCommonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(
        () -> {
          CHEX.LOGGER.info(
              "GTCEu integration ready - ore generation will be handled by"
                  + " GTCEuWorldGenIntegration");
        });
  }
}
