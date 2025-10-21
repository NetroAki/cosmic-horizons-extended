package com.netroaki.chex.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.netroaki.chex.config.CrystalisHazardsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FrostOverlay {
  private static final ResourceLocation FROST_TEX =
      new ResourceLocation("minecraft", "textures/environment/clouds.png");

  public static final IGuiOverlay FROST_OVERLAY =
      (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        if (!CrystalisHazardsConfig.FROST_OVERLAY_ENABLED.get()) return;
        // Check for any frostbite-like effects by name to avoid hard dep on registry here
        MobEffectInstance eff =
            player.getActiveEffects().stream()
                .filter(e -> e.getEffect().getDescriptionId().contains("frost"))
                .findFirst()
                .orElse(null);
        if (eff == null) return;

        float amp = 0.25f + eff.getAmplifier() * 0.2f;
        float alpha = Mth.clamp(0.2f + amp * 0.4f, 0.2f, 0.7f);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, FROST_TEX);
        RenderSystem.setShaderColor(0.8f, 0.95f, 1.0f, alpha);

        blitTiled(gui, poseStack, screenWidth, screenHeight, 256, 0, 0);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
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
}
