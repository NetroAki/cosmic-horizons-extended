package com.netroaki.chex.network;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/** Network handler for CHEX mod client-server communication */
public class CHEXNetwork {

  private static final String PROTOCOL_VERSION = "1";
  public static final SimpleChannel INSTANCE =
      NetworkRegistry.newSimpleChannel(
          ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "main"),
          () -> PROTOCOL_VERSION,
          PROTOCOL_VERSION::equals,
          PROTOCOL_VERSION::equals);

  private static int packetId = 0;

  public static void register() {
    CHEX.LOGGER.info("Registering CHEX network messages...");

    // Register capability sync message
    INSTANCE.registerMessage(
        packetId++,
        PlayerTierSyncMessage.class,
        PlayerTierSyncMessage::encode,
        PlayerTierSyncMessage::decode,
        PlayerTierSyncMessage::handle);

    // Register progression update message
    INSTANCE.registerMessage(
        packetId++,
        ProgressionUpdateMessage.class,
        ProgressionUpdateMessage::encode,
        ProgressionUpdateMessage::decode,
        ProgressionUpdateMessage::handle);

    // Register launch deny message
    INSTANCE.registerMessage(
        packetId++,
        LaunchDenyMessage.class,
        LaunchDenyMessage::encode,
        LaunchDenyMessage::decode,
        LaunchDenyMessage::handle);

    // Register lore knowledge sync message
    INSTANCE.registerMessage(
        packetId++,
        LoreKnowledgeSyncPacket.class,
        LoreKnowledgeSyncPacket::encode,
        LoreKnowledgeSyncPacket::new,
        LoreKnowledgeSyncPacket::handle);

    CHEX.LOGGER.info("CHEX network messages registered successfully");
  }

  public static void sendTo(net.minecraft.server.level.ServerPlayer player, Object message) {
    INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
  }

  /** Send player tier data to client */
  public static void sendPlayerTierToClient(ServerPlayer player) {
    player
        .getCapability(PlayerTierCapability.INSTANCE)
        .ifPresent(
            capability -> {
              PlayerTierSyncMessage message =
                  new PlayerTierSyncMessage(player.getUUID(), capability);
              INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
            });
  }

  /** Send progression update to all players */
  public static void broadcastProgressionUpdate(
      ServerPlayer player, String updateType, String data) {
    ProgressionUpdateMessage message =
        new ProgressionUpdateMessage(player.getUUID(), updateType, data);
    INSTANCE.send(PacketDistributor.ALL.noArg(), message);
  }
}
