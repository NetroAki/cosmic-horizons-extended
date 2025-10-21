package com.netroaki.chex.network;

import com.netroaki.chex.capabilities.PlayerTierCapability;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

/** Network message for syncing player tier capabilities between client and server */
public class PlayerTierSyncMessage {

  private final UUID playerId;
  private final PlayerTierCapability capability;

  public PlayerTierSyncMessage(UUID playerId, PlayerTierCapability capability) {
    this.playerId = playerId;
    this.capability = capability;
  }

  public static void encode(PlayerTierSyncMessage message, FriendlyByteBuf buffer) {
    buffer.writeUUID(message.playerId);
    buffer.writeNbt(message.capability.serializeNBT());
  }

  public static PlayerTierSyncMessage decode(FriendlyByteBuf buffer) {
    UUID playerId = buffer.readUUID();
    PlayerTierCapability capability = new PlayerTierCapability();
    capability.deserializeNBT(buffer.readNbt());
    return new PlayerTierSyncMessage(playerId, capability);
  }

  public static void handle(
      PlayerTierSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
    NetworkEvent.Context context = contextSupplier.get();
    context.enqueueWork(
        () -> {
          if (context.getDirection().getReceptionSide().isClient()) {
            // On client, update a client-side cache safely without touching server-only classes
            DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () ->
                    () ->
                        com.netroaki.chex.network.client.ClientTierCache.update(
                            message.playerId, message.capability));
          } else {
            // Handle on server side
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null && serverPlayer.getUUID().equals(message.playerId)) {
              serverPlayer
                  .getCapability(PlayerTierCapability.INSTANCE)
                  .ifPresent(
                      capability -> {
                        capability.deserializeNBT(message.capability.serializeNBT());
                      });
            }
          }
        });
    context.setPacketHandled(true);
  }
}
