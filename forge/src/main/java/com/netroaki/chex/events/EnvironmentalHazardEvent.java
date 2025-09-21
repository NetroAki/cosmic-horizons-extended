package com.netroaki.chex.events;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.hazards.EnvironmentalHazardManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnvironmentalHazardEvent {

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase == TickEvent.Phase.END && event.level != null) {
      EnvironmentalHazardManager.tick(event.level);
    }
  }
}
