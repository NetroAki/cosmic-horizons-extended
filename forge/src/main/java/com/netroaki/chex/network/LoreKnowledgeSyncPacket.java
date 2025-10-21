package com.netroaki.chex.network;

import com.netroaki.chex.capability.ILoreKnowledge;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

/** Packet for syncing lore knowledge data between server and client */
public class LoreKnowledgeSyncPacket {
  private final CompoundTag nbt;

  public LoreKnowledgeSyncPacket(ILoreKnowledge knowledge) {
    this.nbt = knowledge.serializeNBT();
  }

  public LoreKnowledgeSyncPacket(FriendlyByteBuf buf) {
    this.nbt = buf.readNbt();
  }

  public static void encode(LoreKnowledgeSyncPacket msg, FriendlyByteBuf buf) {
    buf.writeNbt(msg.nbt);
  }

  public static LoreKnowledgeSyncPacket decode(FriendlyByteBuf buf) {
    return new LoreKnowledgeSyncPacket(buf);
  }

  public static void handle(LoreKnowledgeSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get()
        .enqueueWork(
            () -> {
              // Client-side handling
              Player player = ctx.get().getSender();
              if (player != null) {
                player
                    .getCapability(
                        com.netroaki.chex.capability.LoreKnowledgeProvider
                            .LORE_KNOWLEDGE_CAPABILITY)
                    .ifPresent(cap -> cap.deserializeNBT(msg.nbt));
              }
            });
    ctx.get().setPacketHandled(true);
  }
}
