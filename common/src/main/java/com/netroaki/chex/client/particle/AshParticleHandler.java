package com.netroaki.chex.client.particle;

import com.netroaki.chex.world.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class AshParticleHandler {
  private static int ashParticleTimer = 0;
  private static final int ASH_PARTICLE_DELAY = 5; // Ticks between particle spawns

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;

    if (player == null || mc.level == null) return;

    // Only handle in-game rendering
    if (mc.isPaused() || mc.options.hideGui) return;

    // Only in the Inferno Prime dimension
    if (player.level.dimension() != ModDimensions.INFERNO_PRIME) return;

    // Only in certain biomes (Ash Wastes or Basalt Flats)
    Biome biome = player.level.getBiome(player.blockPosition()).value();
    if (!biome.getRegistryName().getPath().contains("ash")
        && !biome.getRegistryName().getPath().contains("basalt")) {
      return;
    }

    // Only spawn particles occasionally
    if (ashParticleTimer++ < ASH_PARTICLE_DELAY) return;
    ashParticleTimer = 0;

    // Only spawn particles when it's "raining" (ash storm)
    if (!player.level.isRaining()) return;

    // Calculate particle position above the player
    double x = player.getX() + (Math.random() * 16.0 - 8.0);
    double y = player.getY() + 10.0 + (Math.random() * 5.0);
    double z = player.getZ() + (Math.random() * 16.0 - 8.0);

    // Spawn the particle
    ClientLevel level = (ClientLevel) player.level;
    level.addParticle(
        ModParticles.FALLING_ASH.get(),
        x,
        y,
        z,
        (Math.random() - 0.5) * 0.1,
        -0.2 - Math.random() * 0.2,
        (Math.random() - 0.5) * 0.1);

    // Occasionally play a wind sound
    if (Math.random() < 0.02) {
      level.playLocalSound(
          x,
          y,
          z,
          SoundEvents.WIND_BURST,
          SoundSource.AMBIENT,
          0.1f,
          0.8f + (float) Math.random() * 0.4f,
          false);
    }
  }

  @SubscribeEvent
  public static void onPlayerRespawn(ClientPlayerNetworkEvent.RespawnEvent event) {
    // Reset timer on respawn
    ashParticleTimer = 0;
  }
}
