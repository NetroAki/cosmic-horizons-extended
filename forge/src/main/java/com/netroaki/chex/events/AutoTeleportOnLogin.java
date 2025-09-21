package com.netroaki.chex.events;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class AutoTeleportOnLogin {
  private static final ResourceKey<Level> AURELIA =
      ResourceKey.create(Registries.DIMENSION, CHEX.id("aurelia_ringworld"));

  public static void register() {
    MinecraftForge.EVENT_BUS.register(new AutoTeleportOnLogin());
  }

  @SubscribeEvent
  public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (!(event.getEntity() instanceof net.minecraft.server.level.ServerPlayer sp)) return;
    var level = sp.server.getLevel(AURELIA);
    if (level == null) return;
    // Only auto-tp if joining overworld (first join), to avoid moving players every
    // relog
    if (sp.level().dimension() == Level.OVERWORLD) {
      CHEX.LOGGER.info(
          "Auto-teleporting {} to Aurelia for quick test", sp.getGameProfile().getName());
      sp.teleportTo(level, 0.5, 160.0, 0.5, sp.getYRot(), sp.getXRot());
    }
  }
}
