package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class RingworldWrapHooks {
  private static final double RING_CIRCUMFERENCE = 2000.0; // wrap between -1000 and +1000
  private static final double HALF = RING_CIRCUMFERENCE / 2.0;

  private static final ResourceKey<Level> AURELIA_RINGWORLD =
      ResourceKey.create(Registries.DIMENSION, CHEX.id("aurelia_ringworld"));

  public static void register() {
    MinecraftForge.EVENT_BUS.register(new RingworldWrapHooks());
  }

  @SubscribeEvent
  public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    var player = event.player;
    if (player == null || player.level().isClientSide) return;
    if (!(player instanceof ServerPlayer serverPlayer)) return;

    var level = serverPlayer.serverLevel();
    if (!level.dimension().location().equals(AURELIA_RINGWORLD.location())) return;

    double x = serverPlayer.getX();
    double y = serverPlayer.getY();
    double z = serverPlayer.getZ();

    // Map x into canonical range [-HALF, HALF)
    double canonical =
        ((x + HALF) % RING_CIRCUMFERENCE + RING_CIRCUMFERENCE) % RING_CIRCUMFERENCE - HALF;
    if (canonical != x) {
      serverPlayer.teleportTo(
          level,
          canonical,
          y,
          z,
          Set.copyOf(EnumSet.noneOf(RelativeMovement.class)),
          serverPlayer.getYRot(),
          serverPlayer.getXRot());
    }
  }
}
