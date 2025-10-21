package com.netroaki.chex.network;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/** Network message for broadcasting progression updates to all players */
public class ProgressionUpdateMessage {

  private final UUID playerId;
  private final String updateType;
  private final String data;

  public ProgressionUpdateMessage(UUID playerId, String updateType, String data) {
    this.playerId = playerId;
    this.updateType = updateType;
    this.data = data;
  }

  public static void encode(ProgressionUpdateMessage message, FriendlyByteBuf buffer) {
    buffer.writeUUID(message.playerId);
    buffer.writeUtf(message.updateType);
    buffer.writeUtf(message.data);
  }

  public static ProgressionUpdateMessage decode(FriendlyByteBuf buffer) {
    UUID playerId = buffer.readUUID();
    String updateType = buffer.readUtf();
    String data = buffer.readUtf();
    return new ProgressionUpdateMessage(playerId, updateType, data);
  }

  public static void handle(
      ProgressionUpdateMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
    NetworkEvent.Context context = contextSupplier.get();
    context.enqueueWork(
        () -> {
          if (context.getDirection().getReceptionSide().isClient()) {
            // Handle on client side - use proper client-side player access
            // Note: This will be handled by client-side code when implemented
            // For now, we'll skip client-side handling to avoid server crashes
          } else {
            // Handle on server side - log the update
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null) {
              com.netroaki.chex.CHEX.LOGGER.info(
                  "Progression update: {} - {} by {}",
                  message.updateType,
                  message.data,
                  serverPlayer.getGameProfile().getName());
            }
          }
        });
    context.setPacketHandled(true);
  }

  private static String getNotificationText(String updateType, String data) {
    return switch (updateType) {
      case "rocket_tier" -> "§a🚀 " + data + " unlocked rocket tier!";
      case "suit_tier" -> "§a👨‍🚀 " + data + " unlocked suit tier!";
      case "planet" -> "§a🌍 " + data + " discovered a new planet!";
      case "mineral" -> "§a⛏️ " + data + " discovered a new mineral!";
      default -> "§a" + data + " made progress!";
    };
  }
}
