package com.netroaki.chex.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

public class LaunchDenyMessage {
  public enum Code {
    FUEL,
    TIER,
    SUIT,
    DISCOVERY
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
              var mc = Minecraft.getInstance();
              if (mc.player != null) {
                String prefix = "§c[CHEX] Launch denied: ";
                mc.player.displayClientMessage(
                    Component.literal(prefix + msg.code + " - " + msg.detail), true);
                // Toast notification
                SystemToast toast =
                    new SystemToast(
                        SystemToast.SystemToastIds.PACK_LOAD_FAILURE,
                        Component.literal("Launch Denied"),
                        Component.literal(msg.code + ": " + msg.detail));
                mc.getToasts().addToast(toast);
              }
            });
    ctx.get().setPacketHandled(true);
  }
}
