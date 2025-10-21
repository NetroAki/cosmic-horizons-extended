package com.netroaki.chex.client;

import com.netroaki.chex.client.render.SandstormOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SandstormVisibilityHandler {

  private static float currentFogDensity = 0.0f;
  private static final float FOG_CHANGE_RATE = 0.01f;

  @SubscribeEvent
  public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
    // Register the sandstorm overlay to render above the hotbar but below vignette
    event.registerAbove(
        VanillaGuiOverlay.HOTBAR.id(), "sandstorm", SandstormOverlay.SANDSTORM_OVERLAY_EFFECT);
  }

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    if (player == null) return;

    Level level = player.level();

    // Only update in Arrakis dimension
    if (!level.dimension().location().getPath().equals("arrakis")) {
      currentFogDensity = 0.0f;
      return;
    }

    // Calculate target fog density based on sandstorm intensity
    float targetFogDensity = calculateSandstormIntensity(level, player);

    // Smoothly transition fog density
    if (currentFogDensity < targetFogDensity) {
      currentFogDensity = Math.min(currentFogDensity + FOG_CHANGE_RATE, targetFogDensity);
    } else if (currentFogDensity > targetFogDensity) {
      currentFogDensity = Math.max(currentFogDensity - FOG_CHANGE_RATE, targetFogDensity);
    }

    // Apply fog effect if needed
    if (currentFogDensity > 0) {
      // This will be used by the fog renderer
      // The actual fog rendering is handled by the render type in the client setup
    }
  }

  private static float calculateSandstormIntensity(Level level, Player player) {
    // Check if it's raining (or your custom sandstorm weather)
    if (!level.isRaining()) {
      return 0.0f;
    }

    // Base intensity based on biome and weather
    float intensity = 0.7f;

    // Reduce intensity when under cover or underground
    if (player.isInWaterOrRain() || player.isInWater()) {
      intensity *= 0.5f;
    }

    if (player.getY() < level.getSeaLevel() - 20) {
      intensity *= 0.3f;
    }

    return Math.min(1.0f, Math.max(0.0f, intensity));
  }

  public static float getFogDensity() {
    return currentFogDensity;
  }

  public static float getVisibilityRange() {
    // Reduce visibility based on fog density
    // Minimum 16 blocks, maximum 96 blocks
    return 16.0f + (1.0f - currentFogDensity) * 80.0f;
  }
}
