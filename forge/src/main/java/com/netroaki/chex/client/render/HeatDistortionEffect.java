package com.netroaki.chex.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "cosmic_horizons_extended", value = Dist.CLIENT)
public class HeatDistortionEffect {
  private static PostChain heatDistortionChain;
  private static RenderTarget heatDistortionTarget;
  private static float time = 0.0F;
  private static float intensity = 0.0F;
  private static float targetIntensity = 0.0F;
  private static boolean initialized = false;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null || minecraft.player == null) {
      intensity = 0.0F;
      return;
    }

    // Only apply in Arrakis dimension
    if (!minecraft.level.dimension().location().getPath().equals("arrakis")) {
      intensity = 0.0F;
      return;
    }

    Player player = minecraft.player;
    Level level = minecraft.level;

    // Update time for animation
    time += 0.05F;
    if (time > 10000.0F) {
      time = 0.0F;
    }

    // Calculate heat intensity based on biome and time of day
    Biome biome = level.getBiome(player.blockPosition()).value();
    float temperature = biome.getBaseTemperature();

    // Increase effect during the day
    float timeOfDay = level.getTimeOfDay(1.0F);
    float dayFactor = 1.0F - Math.abs(0.5F - timeOfDay) * 2.0F; // 0 at night, 1 at noon

    // Increase effect in desert biomes
    float biomeFactor = temperature > 1.0F ? 1.0F : temperature;

    // Random variation
    float randomFactor = 0.9F + (Mth.sin(time * 0.1F) * 0.5F + 0.5F) * 0.2F;

    // Calculate target intensity
    targetIntensity = dayFactor * biomeFactor * randomFactor * 0.7F;

    // Smoothly transition to target intensity
    intensity = Mth.lerp(0.02F, intensity, targetIntensity);
  }

  @SubscribeEvent
  public static void onRenderLevelLast(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) {
      return;
    }

    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null || minecraft.player == null || intensity < 0.01F) {
      return;
    }

    // Initialize the effect if needed
    if (!initialized) {
      try {
        initializeEffect(minecraft);
      } catch (IOException e) {
        // Log error and disable effect
        e.printStackTrace();
        initialized = false;
        return;
      }
    }

    // Skip if not initialized or disabled
    if (!initialized || heatDistortionChain == null) {
      return;
    }

    // Update shader uniforms
    updateShaderUniforms();

    // Apply the effect
    try {
      heatDistortionChain.process(event.getPartialTick());
    } catch (Exception e) {
      // Log error and disable effect
      e.printStackTrace();
      cleanup();
      return;
    }

    // Draw the result to the main framebuffer
    RenderSystem.disableBlend();
    RenderSystem.disableDepthTest();
    RenderSystem.depthMask(false);

    minecraft.getMainRenderTarget().bindWrite(false);

    // Draw the heat distortion effect
    heatDistortionTarget.blitToScreen(
        minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), false);

    // Reset render state
    RenderSystem.depthMask(true);
    RenderSystem.enableDepthTest();
    RenderSystem.enableBlend();
  }

  private static void initializeEffect(Minecraft minecraft) throws IOException {
    if (initialized) {
      return;
    }

    try {
      // Create a custom shader effect
      heatDistortionChain =
          new PostChain(
              minecraft.getTextureManager(),
              minecraft.getResourceManager(),
              minecraft.getMainRenderTarget(),
              new ResourceLocation(
                  "cosmic_horizons_extended", "shaders/post/heat_distortion.json"));

      // Create a render target for the effect
      int width = minecraft.getWindow().getWidth();
      int height = minecraft.getWindow().getHeight();
      heatDistortionTarget = new TextureTarget(width, height, true, minecraft.is64Bit());

      initialized = true;
    } catch (Exception e) {
      cleanup();
      throw e;
    }
  }

  private static void updateShaderUniforms() {
    if (heatDistortionChain == null) {
      return;
    }

    // Update shader uniforms
    for (PostPass pass : heatDistortionChain.passes) {
      EffectInstance effect = pass.getEffect();
      if (effect != null) {
        // Set time for animation
        effect.safeGetUniform("Time").set(time);

        // Set intensity based on heat
        effect.safeGetUniform("Intensity").set(intensity);

        // Add some variation
        effect.safeGetUniform("Speed").set(0.5F);
        effect.safeGetUniform("Distortion").set(0.02F * intensity);
        effect.safeGetUniform("Rise").set(0.005F * intensity);
      }
    }
  }

  private static void cleanup() {
    if (heatDistortionChain != null) {
      heatDistortionChain.close();
      heatDistortionChain = null;
    }

    if (heatDistortionTarget != null) {
      heatDistortionTarget.destroyBuffers();
      heatDistortionTarget = null;
    }

    initialized = false;
  }

  public static void onResourceReload() {
    // Re-initialize the effect when resources are reloaded
    cleanup();

    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level != null) {
      try {
        initializeEffect(minecraft);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Call this from your resource reload listener
  public static void onResourceManagerReload() {
    onResourceReload();
  }
}
