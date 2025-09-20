package com.netroaki.chex.network;

import com.netroaki.chex.progress.PlayerTierCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class CHEXNet {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath("cosmic_horizons_extended", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(packetId++, SyncPlayerTierPacket.class,
                SyncPlayerTierPacket::encode,
                SyncPlayerTierPacket::decode,
                SyncPlayerTierPacket::handle);
    }

    public static void syncPlayerTier(ServerPlayer player) {
        player.getCapability(PlayerTierCapability.INSTANCE).ifPresent(cap -> {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                    new SyncPlayerTierPacket(cap.getRocketTier(), cap.getMilestones().toByteArray()));
        });
    }

    public static class SyncPlayerTierPacket {
        private final int rocketTier;
        private final byte[] milestoneBytes;

        public SyncPlayerTierPacket(int rocketTier, byte[] milestoneBytes) {
            this.rocketTier = rocketTier;
            this.milestoneBytes = milestoneBytes;
        }

        public static void encode(SyncPlayerTierPacket packet, FriendlyByteBuf buffer) {
            buffer.writeInt(packet.rocketTier);
            buffer.writeByteArray(packet.milestoneBytes);
        }

        public static SyncPlayerTierPacket decode(FriendlyByteBuf buffer) {
            int rocketTier = buffer.readInt();
            byte[] milestoneBytes = buffer.readByteArray();
            return new SyncPlayerTierPacket(rocketTier, milestoneBytes);
        }

        public static void handle(SyncPlayerTierPacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                Player player = context.get().getSender();
                if (player != null) {
                    player.getCapability(PlayerTierCapability.INSTANCE).ifPresent(cap -> {
                        cap.setRocketTier(packet.rocketTier);
                        // Note: We don't sync milestones to client for security
                        // Milestones are server-authoritative
                    });
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
