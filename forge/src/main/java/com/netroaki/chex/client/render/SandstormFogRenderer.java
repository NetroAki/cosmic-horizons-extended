package com.netroaki.chex.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.netroaki.chex.client.SandstormVisibilityHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class SandstormFogRenderer {

  @SubscribeEvent
  public static void onRenderFog(ViewportEvent.RenderFog event) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.getCameraEntity();

    if (entity == null || !entity.level().dimension().location().getPath().equals("arrakis")) {
      return;
    }

    float fogDensity = SandstormVisibilityHandler.getFogDensity();
    if (fogDensity <= 0.0f) {
      return;
    }

    // Get camera and player info
    Camera camera = event.getCamera();
    FogType fluidState = camera.getFluidInCamera();

    // Don't override water/lava fog
    if (fluidState != FogType.NONE) {
      return;
    }

    // Calculate fog parameters based on sandstorm intensity
    float startDistance = 8.0f;
    float endDistance = SandstormVisibilityHandler.getVisibilityRange();

    // Apply fog settings
    if (event.getMode() == FogRenderer.FogMode.FOG_SKY) {
      // Sky fog
      RenderSystem.setShaderFogStart(startDistance * 0.8f);
      RenderSystem.setShaderFogEnd(endDistance * 0.8f);
    } else {
      // Terrain fog
      RenderSystem.setShaderFogStart(startDistance);
      RenderSystem.setShaderFogEnd(endDistance);

      // Add density for a more intense fog effect
      float density = fogDensity * 0.1f;
      RenderSystem.setShaderFogDensity(density);

      // Tint the fog with a sand color
      float r = 0.9f;
      float g = 0.8f;
      float b = 0.6f;
      float a = 0.8f * fogDensity;

      // This is a workaround since Forge's RenderFogEvent doesn't support color
      // We'll handle the actual fog rendering in the render level last event
    }
  }

  @SubscribeEvent
  public static void onFogColors(ViewportEvent.ComputeFogColor event) {
    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.getCameraEntity();

    if (entity == null || !entity.level().dimension().location().getPath().equals("arrakis")) {
      return;
    }

    float fogDensity = SandstormVisibilityHandler.getFogDensity();
    if (fogDensity <= 0.0f) {
      return;
    }

    // Tint the fog with a sand color during sandstorms
    float[] sandColor = {0.9f, 0.8f, 0.6f}; // RGB values for sand color
    float lerpFactor = fogDensity * 0.7f; // How much to blend with the sand color

    // Blend the original fog color with the sand color
    float r = Mth.lerp(lerpFactor, event.getRed(), sandColor[0]);
    float g = Mth.lerp(lerpFactor, event.getGreen(), sandColor[1]);
    float b = Mth.lerp(lerpFactor, event.getBlue(), sandColor[2]);

    event.setRed(r);
    event.setGreen(g);
    event.setBlue(b);
  }

  @SubscribeEvent
  public static void onRenderLevelLast(
      net.minecraftforge.client.event.RenderLevelStageEvent event) {
    if (event.getStage() != net.minecraftforge.client.event.RenderLevelStageEvent.Stage.AFTER_SKY) {
      return;
    }

    Minecraft minecraft = Minecraft.getInstance();
    Entity entity = minecraft.getCameraEntity();

    if (entity == null || !entity.level().dimension().location().getPath().equals("arrakis")) {
      return;
    }

    float fogDensity = SandstormVisibilityHandler.getFogDensity();
    if (fogDensity <= 0.0f) {
      return;
    }

    // Render additional fog pass for better sandstorm effect
    renderSandstormFog(event.getPoseStack(), minecraft, fogDensity);
  }

  private static void renderSandstormFog(
      com.mojang.blaze3d.vertex.PoseStack poseStack, Minecraft minecraft, float intensity) {
    // Set up rendering
    RenderSystem.disableTexture();
    RenderSystem.depthMask(false);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    // Get the camera position
    Camera camera = minecraft.gameRenderer.getMainCamera();
    Vec3 cameraPos = camera.getPosition();

    // Calculate fog parameters
    float fogStart = 8.0f;
    float fogEnd = SandstormVisibilityHandler.getVisibilityRange();

    // Set up the shader
    RenderSystem.setShader(GameRenderer::getPositionColorShader);

    // Create the fog quad
    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder buffer = tesselator.getBuilder();

    // Draw the fog
    float alpha = intensity * 0.8f;
    int color = (int) (alpha * 255.0F) << 24 | 0x404040;

    // Draw a full-screen quad with distance-based alpha
    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

    // Far plane vertices
    addFogQuad(buffer, poseStack.last().pose(), -1, -1, 1, 1, fogStart, fogEnd, color);

    tesselator.end();

    // Reset render state
    RenderSystem.depthMask(true);
    RenderSystem.enableTexture();
    RenderSystem.disableBlend();
  }

  private static void addFogQuad(
      BufferBuilder buffer,
      Matrix4f matrix,
      float x1,
      float y1,
      float x2,
      float y2,
      float near,
      float far,
      int color) {
    // Convert normalized device coordinates to world space
    Vector3f[] vertices = new Vector3f[4];
    vertices[0] = new Vector3f(x1, y1, 0).mulProject(matrix);
    vertices[1] = new Vector3f(x2, y1, 0).mulProject(matrix);
    vertices[2] = new Vector3f(x2, y2, 0).mulProject(matrix);
    vertices[3] = new Vector3f(x1, y2, 0).mulProject(matrix);

    // Add vertices to form two triangles (a quad)
    buffer.vertex(vertices[0].x, vertices[0].y, vertices[0].z).color(color).endVertex();
    buffer.vertex(vertices[1].x, vertices[1].y, vertices[1].z).color(color).endVertex();
    buffer.vertex(vertices[2].x, vertices[2].y, vertices[2].z).color(color).endVertex();
    buffer.vertex(vertices[3].x, vertices[3].y, vertices[3].z).color(color).endVertex();
  }
}
