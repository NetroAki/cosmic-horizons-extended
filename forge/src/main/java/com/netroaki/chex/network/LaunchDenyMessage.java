package com.netroaki.chex.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LaunchDenyMessage {
  public enum Code {
    FUEL,
    TIER,
    SUIT,
    DISCOVERY,
    DESTINATION
  }

  private final Code code;
  private final String detail;

  public LaunchDenyMessage(Code code, String detail) {
    this.code = code;
    this.detail = detail;
  }

  public static void encode(LaunchDenyMessage msg, FriendlyByteBuf buf) {
    buf.writeEnum(msg.code);
    buf.writeUtf(msg.detail);
  }

  public static LaunchDenyMessage decode(FriendlyByteBuf buf) {
    Code code = buf.readEnum(Code.class);
    String detail = buf.readUtf();
    return new LaunchDenyMessage(code, detail);
  }

  public static void handle(LaunchDenyMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get()
        .enqueueWork(
            () -> {
              // Handle on client side - use proper client-side player access
              // Note: This will be handled by client-side code when implemented
              // For now, we'll skip client-side handling to avoid server crashes
            });
    ctx.get().setPacketHandled(true);
  }
}
