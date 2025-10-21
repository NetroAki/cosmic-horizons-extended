package com.netroaki.chex.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.netroaki.chex.config.CrystalisHazardsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CrystalisAuroraRenderer {

  private static final ResourceLocation AURORA_TEX =
      new ResourceLocation("minecraft", "textures/environment/clouds.png");

  @SubscribeEvent
  public static void onRenderLevel(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
    if (!CrystalisHazardsConfig.AURORA_ENABLED.get()) return;
    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;
    if (player == null) return;
    Level level = player.level();

    // Only show at night with snow precipitation biomes
    Biome biome = level.getBiome(player.blockPosition()).value();
    boolean isCold = biome.getPrecipitation() == Biome.Precipitation.SNOW;
    float dayTime = level.getTimeOfDay(1.0f);
    boolean isNight = dayTime < 0.20f || dayTime > 0.80f;
    if (!isCold || !isNight) return;

    float intensity = auroraIntensity(level);
    if (intensity <= 0.02f) return;

    PoseStack poseStack = event.getPoseStack();
    poseStack.pushPose();

    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    RenderSystem.setShaderTexture(0, AURORA_TEX);

    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder buffer = tesselator.getBuilder();
    Matrix4f mat = poseStack.last().pose();

    // Draw a gentle curved band above horizon
    float height = 140f; // world y for sky dome
    float radius = 200f;
    float width = 60f;

    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

    // Simple straight band toward north; keep cheap
    float r = Mth.lerp(intensity, 0.3f, 0.8f);
    float g = Mth.lerp(intensity, 0.6f, 1.0f);
    float b = 1.0f;
    float a = 0.6f * intensity;

    // Quad
    buffer.vertex(mat, -radius, height, -radius).uv(0f, 0f).color(r, g, b, a).endVertex();
    buffer.vertex(mat, radius, height, -radius).uv(1f, 0f).color(r, g, b, a).endVertex();
    buffer
        .vertex(mat, radius, height + width, -radius)
        .uv(1f, 1f)
        .color(r, g, b, a * 0.8f)
        .endVertex();
    buffer
        .vertex(mat, -radius, height + width, -radius)
        .uv(0f, 1f)
        .color(r, g, b, a * 0.8f)
        .endVertex();

    tesselator.end();

    RenderSystem.disableBlend();
    poseStack.popPose();
  }

  private static float auroraIntensity(Level level) {
    // Basic variation based on time and weather
    float t = (level.getGameTime() % 24000) / 24000.0f;
    float base = 0.5f + 0.5f * Mth.cos(t * Mth.TWO_PI * 2.0f);
    if (level.isRaining()) base *= 0.8f; // dim during snowfall
    return Mth.clamp(base, 0.1f, 1.0f);
  }
}
