package com.netroaki.chex.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID, value = Dist.CLIENT)
public class InfernoSkyRenderer {
  private static final ResourceLocation INFERNO_SKY =
      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "textures/environment/inferno_sky.png");
  private static final float[] SUNSET_COLORS = new float[4];
  private static float timeInInferno = 0.0F;
  private static float lastPartialTicks = 0.0F;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      // Update timer for animations
      timeInInferno += 0.05F;
      if (timeInInferno > 2.0F * (float) Math.PI) {
        timeInInferno -= 2.0F * (float) Math.PI;
      }
    }
  }

  @SubscribeEvent
  public static void onRenderFog(ViewportEvent.RenderFog event) {
    if (event
        .getCamera()
        .getEntity()
        .level()
        .dimension()
        .location()
        .getPath()
        .equals("inferno_prime")) {
      // Custom fog for Inferno Prime
      float density = 0.1F;
      float start = event.getNearPlaneDistance();
      float end = Math.min(192.0F, event.getFarPlaneDistance() * 0.8F);

      RenderSystem.setShaderFogStart(start);
      RenderSystem.setShaderFogEnd(end);
      RenderSystem.setShaderFogShape(event.getFogShape());

      // Add pulsing effect to fog density
      float pulse = 0.5F + 0.2F * Mth.sin(timeInInferno * 0.5F);
      RenderSystem.setShaderFogColor(0.8F, 0.2F, 0.1F, pulse);
    }
  }

  @SubscribeEvent
  public static void onRenderFogColors(ViewportEvent.ComputeFogColor event) {
    if (event
        .getCamera()
        .getEntity()
        .level()
        .dimension()
        .location()
        .getPath()
        .equals("inferno_prime")) {
      // Set red/orange sky color
      float time = event.getCamera().getEntity().level().getTimeOfDay(1.0F);
      float red = 0.9F + 0.1F * Mth.sin(timeInInferno * 0.1F);
      float green = 0.3F + 0.1F * Mth.sin(timeInInferno * 0.2F);
      float blue = 0.1F + 0.05F * Mth.sin(timeInInferno * 0.3F);

      // Darken at night
      float nightFactor =
          1.0F - Mth.clamp((Mth.cos(time * ((float) Math.PI * 2F)) * 2.0F + 0.5F), 0.0F, 1.0F);
      red *= 0.7F + 0.3F * nightFactor;
      green *= 0.5F + 0.5F * nightFactor;
      blue *= 0.3F + 0.7F * nightFactor;

      event.setRed(red);
      event.setGreen(green);
      event.setBlue(blue);

      // Store sunset colors for sky rendering
      SUNSET_COLORS[0] = red;
      SUNSET_COLORS[1] = green;
      SUNSET_COLORS[2] = blue;
      SUNSET_COLORS[3] = 1.0F;
    }
  }

  public static void renderSky(
      ClientLevel level,
      float partialTicks,
      PoseStack poseStack,
      Camera camera,
      Matrix4f projectionMatrix,
      boolean isFoggy,
      Runnable setupFog) {
    if (!level.dimension().location().getPath().equals("inferno_prime")) {
      return;
    }

    // Update last partial ticks for smooth animations
    lastPartialTicks = partialTicks;

    // Setup rendering
    RenderSystem.depthMask(false);
    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    // Get view position
    Vec3 viewPos = camera.getPosition();
    float x = (float) Mth.lerp(partialTicks, camera.getEntity().xo, camera.getEntity().getX());
    float y = (float) Mth.lerp(partialTicks, camera.getEntity().yo, camera.getEntity().getY());
    float z = (float) Mth.lerp(partialTicks, camera.getEntity().zo, camera.getEntity().getZ());

    // Draw sky plane
    RenderSystem.setShaderTexture(0, INFERNO_SKY);
    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder bufferbuilder = tesselator.getBuilder();

    // Draw multiple layers for depth
    for (int k1 = 0; k1 < 6; ++k1) {
      poseStack.pushPose();
      float f3 = k1 == 1 ? -1.0F : 1.0F;

      if (k1 == 1) {
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
      } else if (k1 == 2) {
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
      } else if (k1 == 3) {
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
      } else if (k1 == 4) {
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
      } else if (k1 == 5) {
        poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
      }

      Matrix4f matrix4f = poseStack.last().pose();
      bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

      float size = 100.0F;
      float yOffset = (float) ((y - viewPos.y()) * 0.1);

      // Add pulsing effect
      float pulse = 0.8F + 0.2F * Mth.sin((timeInInferno + partialTicks * 0.05F) * 0.5F);

      // Draw the sky plane with animated texture coordinates
      for (int i = 0; i < 4; ++i) {
        float f7 = (float) ((i & 2) * 2 - 1);
        float f8 = (float) ((i + 1 & 2) * 2 - 1);
        float f9 = f7 * size;
        float f10 = f8 * size;
        float u = (f7 + 1.0F) / 2.0F;
        float v = (f8 + 1.0F) / 2.0F;

        // Add subtle movement to the sky texture
        float timeOffset = timeInInferno * 0.01F;
        u += Mth.sin(timeOffset) * 0.1F;
        v += Mth.cos(timeOffset * 0.8F) * 0.1F;

        bufferbuilder
            .vertex(matrix4f, f9, yOffset, f10)
            .uv(u, v)
            .color(
                SUNSET_COLORS[0] * pulse, SUNSET_COLORS[1] * pulse, SUNSET_COLORS[2] * pulse, 1.0F)
            .endVertex();
      }

      tesselator.end();
      poseStack.popPose();
    }

    // Draw additional effects during lava rain
    if (InfernoEnvironmentHandler.isLavaRainActive()) {
      // Add glowing embers during lava rain
      float intensity = 0.5F + 0.5F * Mth.sin(timeInInferno * 0.2F);
      RenderSystem.setShaderColor(1.0F, 0.5F, 0.2F, intensity * 0.3F);

      for (int i = 0; i < 10; ++i) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(i * 45.0F + timeInInferno * 2.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(100.0F + i * 10.0F));

        Matrix4f matrix4f1 = poseStack.last().pose();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        bufferbuilder.vertex(matrix4f1, 0.0F, 100.0F, 0.0F).endVertex();

        for (int j = 0; j <= 8; ++j) {
          float f6 = (float) j * ((float) Math.PI * 2F) / 8.0F;
          float f7 = Mth.sin(f6);
          float f8 = Mth.cos(f6);
          bufferbuilder.vertex(matrix4f1, f7 * 120.0F, f8 * 120.0F, -f8 * 40.0F * 0.0F).endVertex();
        }

        tesselator.end();
        poseStack.popPose();
      }
    }

    // Reset render state
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();
    RenderSystem.enableTexture();
    RenderSystem.depthMask(true);
  }
}
