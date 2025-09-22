package com.netroaki.chex.events;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.network.CHEXNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Handles player-related events for CHEX */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class PlayerEvents {

  /** Attach capabilities to players when they join */
  @SubscribeEvent
  public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player) {
      // The capability is already attached in PlayerTierCapability class
      // This is just a placeholder for any additional capability attachment logic
    }
  }

  /** Sync capability data when player logs in */
  @SubscribeEvent
  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
      CHEX.LOGGER.info("Syncing player tier data for {}", player.getGameProfile().getName());
      CHEXNetwork.sendPlayerTierToClient(player);
    }
  }

  /** Sync capability data when player respawns */
  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
      // Small delay to ensure everything is properly initialized
      player.getServer().execute(() -> {
        CHEX.LOGGER.info("Syncing player tier data after respawn for {}", player.getGameProfile().getName());
        CHEXNetwork.sendPlayerTierToClient(player);
      });
    }
  }

  /** Sync capability data when player changes dimensions */
  @SubscribeEvent
  public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
      CHEX.LOGGER.debug("Syncing player tier data after dimension change for {}", player.getGameProfile().getName());
      CHEXNetwork.sendPlayerTierToClient(player);
    }
  }

  /** Copy capability data when player is cloned (e.g., returning from the End) */
  @SubscribeEvent
  public static void onPlayerCloned(PlayerEvent.Clone event) {
    if (!event.isWasDeath()) {
      // Player changed dimensions or respawned from bed, copy the capability data
      event.getOriginal().reviveCaps();
      
      event.getOriginal().getCapability(PlayerTierCapability.INSTANCE).ifPresent(oldCap -> {
        event.getEntity().getCapability(PlayerTierCapability.INSTANCE).ifPresent(newCap -> {
          // Copy all data from old capability to new one
          newCap.deserializeNBT(oldCap.serializeNBT());
          
          // Sync the capability to the client
          if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            CHEXNetwork.sendPlayerTierToClient(player);
          }
        });
      });
      
      event.getOriginal().invalidateCaps();
    } else {
      // Player died, don't copy the capability data
      // This allows for potential death penalties to be implemented later
      if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
        // Sync default capability to the client
        CHEXNetwork.sendPlayerTierToClient(player);
      }
    }
  }
}
