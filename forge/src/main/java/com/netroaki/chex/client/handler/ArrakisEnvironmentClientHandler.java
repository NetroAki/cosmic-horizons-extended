package com.netroaki.chex.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.environment.ArrakisEnvironmentHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, value = Dist.CLIENT)
public class ArrakisEnvironmentClientHandler {
  private static final ResourceLocation DUST_STORM_TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/environment/dust_storm.png");
  private static float dustStormIntensity = 0.0F;
  private static float lastDustStormIntensity = 0.0F;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    if (mc.level == null || mc.player == null) return;

    // Only process in Arrakis dimension
    if (!mc.level.dimension().location().getPath().equals("arrakis")) {
      dustStormIntensity = 0.0F;
      return;
    }

    // Smoothly transition dust storm intensity
    lastDustStormIntensity = dustStormIntensity;
    float targetIntensity = ArrakisEnvironmentHandler.isDustStormActive() ? 1.0F : 0.0F;
    dustStormIntensity += (targetIntensity - dustStormIntensity) * 0.02F;
  }

  @SubscribeEvent
  public static void onRenderFog(ViewportEvent.RenderFog event) {
    if (dustStormIntensity <= 0.0F) return;

    // Apply dust storm fog effect
    float density = 0.1F * dustStormIntensity;

    // Use exponential fog for more realistic dust storm appearance
    RenderSystem.setShaderFogStart(8.0F);
    RenderSystem.setShaderFogEnd(24.0F);
    RenderSystem.setShaderFogShape(FogRenderer.FogShape.SPHERE);

    // Set fog color to sand color
    RenderSystem.setShaderFogColor(0.9F, 0.8F, 0.6F, 1.0F);
  }

  @SubscribeEvent
  public static void onRenderFogColors(ViewportEvent.ComputeFogColor event) {
    if (dustStormIntensity <= 0.0F) return;

    // Shift colors towards orange/brown during dust storm
    float intensity = dustStormIntensity * 0.7F;
    event.setRed(event.getRed() * (1.0F - intensity) + 0.8F * intensity);
    event.setGreen(event.getGreen() * (1.0F - intensity) + 0.6F * intensity);
    event.setBlue(event.getBlue() * (1.0F - intensity * 0.5F));
  }

  @SubscribeEvent
  public static void onRenderLevelStage(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY || dustStormIntensity <= 0.0F) {
      return;
    }

    // TODO: Implement dust storm rendering
    // This would include rendering dust particles and overlays
  }

  @SubscribeEvent
  public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event) {
    if (dustStormIntensity <= 0.0F || event.isCanceled()) return;

    // TODO: Add dust storm overlay effect
    // This would include screen distortion and dust particles on the HUD
  }

  public static float getDustStormIntensity(float partialTicks) {
    return lastDustStormIntensity + (dustStormIntensity - lastDustStormIntensity) * partialTicks;
  }
}
