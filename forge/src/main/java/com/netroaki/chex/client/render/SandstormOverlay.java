package com.netroaki.chex.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SandstormOverlay {
  private static final ResourceLocation SANDSTORM_OVERLAY =
      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "textures/misc/sandstorm_overlay.png");

  // Animation timers
  private static float globalTime = 0;
  private static float lastPartialTick = 0;

  public static final IGuiOverlay SANDSTORM_OVERLAY_EFFECT =
      (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        // Update animation time
        if (partialTick < lastPartialTick) { // New frame
          globalTime += 0.05f;
        }
        lastPartialTick = partialTick;

        Level level = player.level();

        // Only render in Arrakis dimension
        if (!level.dimension().location().getPath().equals("arrakis")) {
          return;
        }

        // Get sandstorm intensity
        float sandstormIntensity = getSandstormIntensity(level, player);
        if (sandstormIntensity <= 0.01f) return;

        // Calculate view bobbing effect
        float viewBobbing = 0.0f;
        if (minecraft.options.bobView().get() && minecraft.getCameraEntity() instanceof Player) {
          float f = minecraft.getFrameTime() - player.walkDistO;
          f = minecraft.isPaused() ? 0.0f : f;
          viewBobbing = Mth.lerp(partialTick, player.oBob, player.bob);
          viewBobbing = (viewBobbing + f) % ((float) Math.PI * 2.0f);
        }

        // Set up rendering
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        // Base color - sandy yellow
        float r = 0.9f;
        float g = 0.8f;
        float b = 0.6f;
        float a = sandstormIntensity * 0.8f;

        // Time-based animations
        float time = globalTime + partialTick * 0.05f;

        // Draw multiple layers with different scroll speeds and scales for depth
        for (int i = 0; i < 3; i++) {
          float layerIntensity = sandstormIntensity * (0.7f + i * 0.1f);
          float layerTime = time * (0.8f + i * 0.1f);

          // Calculate scroll offsets
          float scrollX = (float) Math.sin(layerTime * 0.5f) * 0.1f;
          float scrollY = (float) Math.cos(layerTime * 0.3f) * 0.1f;

          // Add view bobbing effect
          float bobX = (float) Math.sin(viewBobbing) * 0.5f * (i + 1) * 0.2f;
          float bobY = (float) Math.cos(viewBobbing * 2.0f) * 0.5f * (i + 1) * 0.2f;

          // Set shader parameters
          RenderSystem.setShaderColor(r, g, b, a * (0.5f + i * 0.2f));
          RenderSystem.setShaderTexture(0, SANDSTORM_OVERLAY);

          // Draw the overlay with parallax effect
          for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
              float offsetX = (scrollX + bobX + x) * (i + 1) * 10f;
              float offsetY = (scrollY + bobY + y) * (i + 1) * 10f;

              // Draw the overlay with tiling
              int tileSize = 256;
              int tilesX = (int) Math.ceil(screenWidth / (float) tileSize) + 1;
              int tilesY = (int) Math.ceil(screenHeight / (float) tileSize) + 1;

              for (int tx = 0; tx < tilesX; tx++) {
                for (int ty = 0; ty < tilesY; ty++) {
                  int posX = (int) (tx * tileSize + offsetX) % tileSize - tileSize / 2;
                  int posY = (int) (ty * tileSize + offsetY) % tileSize - tileSize / 2;

                  gui.blit(
                      poseStack,
                      posX,
                      posY,
                      posX + tileSize,
                      posY + tileSize,
                      0,
                      0,
                      tileSize,
                      tileSize,
                      tileSize,
                      tileSize);
                }
              }
            }
          }
        }

        // Add vignette effect at the edges
        if (sandstormIntensity > 0.3f) {
          float vignetteIntensity = (sandstormIntensity - 0.3f) * 0.7f;
          RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, vignetteIntensity);

          int border = (int) (screenWidth * 0.1f);
          // Top
          gui.fill(poseStack, 0, 0, screenWidth, border, 0xFF000000);
          // Bottom
          gui.fill(poseStack, 0, screenHeight - border, screenWidth, screenHeight, 0xFF000000);
          // Left
          gui.fill(poseStack, 0, border, border, screenHeight - border, 0xFF000000);
          // Right
          gui.fill(
              poseStack,
              screenWidth - border,
              border,
              screenWidth,
              screenHeight - border,
              0xFF000000);
        }

        // Reset render state
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      };

  private static float getSandstormIntensity(Level level, Player player) {
    // Check if it's raining (or your custom sandstorm weather)
    if (!level.isRaining()) {
      return 0.0f;
    }

    // Get biome for additional intensity variation
    Biome biome = level.getBiome(player.blockPosition()).value();
    float baseIntensity = 0.0f;

    // Increase intensity in desert biomes
    if (biome.getPrecipitation() == Biome.Precipitation.NONE) {
      baseIntensity = 0.7f;
    }

    // Get player's view vector for directional intensity
    Vec3 viewVector = player.getViewVector(1.0f);
    float viewAngle = (float) Math.atan2(viewVector.z, viewVector.x);

    // Get wind direction (could be based on time or other factors)
    float windAngle = (level.getGameTime() % 24000) / 24000.0f * (float) Math.PI * 2.0f;

    // Calculate dot product between view direction and wind direction
    float dot = (float) Math.cos(viewAngle - windAngle);

    // Increase intensity when looking into the wind
    baseIntensity *= 0.5f + 0.5f * (dot * 0.5f + 0.5f);

    // Reduce intensity when under cover
    if (player.isInWaterOrRain() || player.isInWater()) {
      baseIntensity *= 0.5f;
    }

    // Reduce intensity when underground
    if (player.getY() < level.getSeaLevel() - 20) {
      baseIntensity *= 0.3f;
    }

    // Add random variation
    float randomVariation = 0.9f + (float) Math.sin(level.getGameTime() * 0.1f) * 0.1f;

    return Math.min(1.0f, baseIntensity * randomVariation);
  }
}
