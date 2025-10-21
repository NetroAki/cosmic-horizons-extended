package com.netroaki.chex.client;

import com.netroaki.chex.client.render.BlizzardOverlay;
import com.netroaki.chex.client.render.FrostOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrystalisVisibilityHandler {
  @SubscribeEvent
  public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
    event.registerAbove(
        VanillaGuiOverlay.HOTBAR.id(), "blizzard", BlizzardOverlay.BLIZZARD_OVERLAY_EFFECT);
    event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "frost", FrostOverlay.FROST_OVERLAY);
  }
}
