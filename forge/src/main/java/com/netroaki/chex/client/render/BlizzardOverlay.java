package com.netroaki.chex.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.netroaki.chex.config.CrystalisHazardsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class BlizzardOverlay {
  private static final ResourceLocation BLIZZARD_OVERLAY =
      new ResourceLocation("minecraft", "textures/environment/clouds.png");

  private static float globalTime = 0;
  private static float lastPartialTick = 0;

  public static final IGuiOverlay BLIZZARD_OVERLAY_EFFECT =
      (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        if (!CrystalisHazardsConfig.BLIZZARD_OVERLAY_ENABLED.get()) return;
        Level level = player.level();

        // Only show in cold/snowy precipitation
        Biome biome = level.getBiome(player.blockPosition()).value();
        boolean isSnowing =
            level.isRaining() && biome.getPrecipitation() == Biome.Precipitation.SNOW;
        if (!isSnowing) return;

        // Update animation time
        if (partialTick < lastPartialTick) {
          globalTime += 0.05f;
        }
        lastPartialTick = partialTick;

        float intensity = getBlizzardIntensity(level, player);
        if (intensity <= 0.01f) return;

        // Render tinted scrolling overlay
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BLIZZARD_OVERLAY);

        float time = globalTime + partialTick * 0.05f;
        float r = 0.85f, g = 0.95f, b = 1.0f;
        float a = Mth.clamp(intensity * 0.9f, 0.1f, 0.95f);
        RenderSystem.setShaderColor(r, g, b, a);

        // Slight parallax layers
        for (int i = 0; i < 2; i++) {
          float scrollX = (float) Math.sin(time * (0.3f + i * 0.2f)) * (3 + i * 2);
          float scrollY = (float) Math.cos(time * (0.25f + i * 0.15f)) * (3 + i * 2);
          blitTiled(gui, poseStack, screenWidth, screenHeight, 256, (int) scrollX, (int) scrollY);
        }

        // Edge vignette for harsh weather
        if (intensity > 0.3f) {
          float vignette = (intensity - 0.3f) * 0.7f;
          int border = (int) (screenWidth * 0.1f);
          gui.fill(poseStack, 0, 0, screenWidth, border, packColor(0f, 0f, 0f, vignette));
          gui.fill(
              poseStack,
              0,
              screenHeight - border,
              screenWidth,
              screenHeight,
              packColor(0f, 0f, 0f, vignette));
          gui.fill(
              poseStack, 0, border, border, screenHeight - border, packColor(0f, 0f, 0f, vignette));
          gui.fill(
              poseStack,
              screenWidth - border,
              border,
              screenWidth,
              screenHeight - border,
              packColor(0f, 0f, 0f, vignette));
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      };

  private static void blitTiled(
      GuiGraphics gui,
      com.mojang.blaze3d.vertex.PoseStack poseStack,
      int w,
      int h,
      int tile,
      int offX,
      int offY) {
    int tilesX = (int) Math.ceil(w / (float) tile) + 1;
    int tilesY = (int) Math.ceil(h / (float) tile) + 1;
    for (int tx = -1; tx < tilesX; tx++) {
      for (int ty = -1; ty < tilesY; ty++) {
        int x = tx * tile + (offX % tile);
        int y = ty * tile + (offY % tile);
        gui.blit(poseStack, x, y, x + tile, y + tile, 0, 0, tile, tile, tile, tile);
      }
    }
  }

  private static int packColor(float r, float g, float b, float a) {
    int ia = (int) (a * 255) & 0xFF;
    int ir = (int) (r * 255) & 0xFF;
    int ig = (int) (g * 255) & 0xFF;
    int ib = (int) (b * 255) & 0xFF;
    return (ia << 24) | (ir << 16) | (ig << 8) | ib;
  }

  private static float getBlizzardIntensity(Level level, Player player) {
    if (!level.isRaining()) return 0.0f;
    float base = 0.6f;
    if (player.isInWaterOrRain() || player.isInWater()) base *= 0.7f;
    if (player.getY() < level.getSeaLevel() - 20) base *= 0.5f;
    return Mth.clamp(base, 0f, 1f);
  }
}
