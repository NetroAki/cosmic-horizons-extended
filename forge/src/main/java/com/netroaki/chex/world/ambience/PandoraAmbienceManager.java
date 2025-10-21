package com.netroaki.chex.world.ambience;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client ambience hooks are temporarily disabled while the Pandora biome set is under
 * reconstruction. The handler remains so existing event subscriptions compile without the full
 * implementation.
 */
@Mod.EventBusSubscriber
public final class PandoraAmbienceManager {
  private PandoraAmbienceManager() {}

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    // Intentionally left blank.
  }
}
