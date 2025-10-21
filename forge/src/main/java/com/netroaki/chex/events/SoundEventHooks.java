package com.netroaki.chex.events;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.sounds.CHEXSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Basic sound event hooks for hazard warnings and ambient audio. Provides minimal implementation
 * for T-026 sound system.
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoundEventHooks {

  /** Play hazard warning sound when player enters a hazardous dimension. */
  @SubscribeEvent
  public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      String dimension = event.getTo().location().toString();

      // Play appropriate warning sound based on dimension
      if (dimension.contains("crystalis")) {
        player.playNotifySound(CHEXSounds.FROSTBITE_WARNING.get(), SoundSource.AMBIENT, 0.5f, 1.0f);
      } else if (dimension.contains("arrakis")) {
        player.playNotifySound(CHEXSounds.HEAT_WARNING.get(), SoundSource.AMBIENT, 0.5f, 1.0f);
      } else if (dimension.contains("pandora")) {
        player.playNotifySound(CHEXSounds.PANDORA_AMBIENT.get(), SoundSource.AMBIENT, 0.3f, 1.0f);
      }
    }
  }

  /** Play ambient sound when player spawns in a custom dimension. */
  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      String dimension = player.level().dimension().location().toString();

      if (dimension.contains("pandora")) {
        player.playNotifySound(CHEXSounds.PANDORA_AMBIENT.get(), SoundSource.AMBIENT, 0.2f, 1.0f);
      }
    }
  }
}
