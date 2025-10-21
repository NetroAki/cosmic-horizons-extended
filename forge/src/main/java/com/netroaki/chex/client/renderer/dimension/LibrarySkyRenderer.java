package com.netroaki.chex.client.renderer.dimension;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.netroaki.chex.CHEX;
import java.util.Random;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class LibrarySkyRenderer extends DimensionSpecialEffects {
  private static final ResourceLocation STAR_TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/environment/library_sky.png");
  private static final ResourceLocation FOG_TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/environment/library_fog.png");

  private static final float[] SUNRISE_COLORS = new float[4];
  private static final Random RANDOM = new Random(0);

  private final float[] starX = new float[2000];
  private final float[] starY = new float[2000];
  private final float[] starZ = new float[2000];
  private final float[] starSize = new float[2000];

  public LibrarySkyRenderer() {
    super(Float.NaN, false, SkyType.NORMAL, false, true);

    // Initialize star positions
    for (int i = 0; i < starX.length; i++) {
      float f = RANDOM.nextFloat() * 2.0F - 1.0F;
      float f1 = RANDOM.nextFloat() * 2.0F - 1.0F;
      float f2 = RANDOM.nextFloat() * 2.0F - 1.0F;
      float f3 = 0.15F + RANDOM.nextFloat() * 0.1F;
      float f4 = f * f + f1 * f1 + f2 * f2;

      if (f4 < 1.0F && f4 > 0.01F) {
        f4 = 1.0F / (float) Math.sqrt(f4);
        f *= f4;
        f1 *= f4;
        f2 *= f4;

        float f5 = f * 100.0F;
        float f6 = f1 * 100.0F;
        float f7 = f2 * 100.0F;

        starX[i] = f5;
        starY[i] = f6;
        starZ[i] = f7;
        starSize[i] = f3;
      } else {
        i--;
      }
    }
  }

  @Override
  public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
    // Custom fog color that shifts between deep blue and purple
    float f = (float) Math.cos(brightness * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
    f = Mth.clamp(f, 0.0F, 1.0F);
    float f1 = 0.1F;
    float f2 = 0.1F;
    float f3 = 0.3F;
    f1 = f1 * (f * 0.65F + 0.35F);
    f2 = f2 * (f * 0.65F + 0.35F);
    f3 = f3 * (f * 0.65F + 0.35F);

    return new Vec3(f1, f2, f3);
  }

  @Override
  public boolean isFoggyAt(int x, int y) {
    return true;
  }

  @Override
  public boolean renderSky(
      ClientLevel level,
      int ticks,
      float partialTick,
      PoseStack poseStack,
      Camera camera,
      Matrix4f projectionMatrix,
      boolean isFoggy,
      Runnable setupFog) {
    RenderSystem.disableTexture();
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    // Setup fog
    RenderSystem.setShaderFogStart(0.0F);
    RenderSystem.setShaderFogEnd(100.0F);

    // Draw stars
    drawStars(poseStack);

    // Draw custom sky texture
    drawSkyTexture(poseStack, partialTick);

    // Reset fog
    RenderSystem.setShaderFogStart(100.0F);
    RenderSystem.setShaderFogEnd(1000.0F);

    RenderSystem.disableBlend();
    RenderSystem.enableTexture();

    return true;
  }

  private void drawStars(PoseStack poseStack) {
    long i = Util.getMillis() * 3L % 60000L;
    float f = (float) i / 1000.0F;

    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder bufferbuilder = tesselator.getBuilder();

    RenderSystem.setShader(GameRenderer::getPositionShader);
    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

    for (int j = 0; j < starX.length; ++j) {
      float f1 = starX[j];
      float f2 = starY[j];
      float f3 = starZ[j];

      // Make stars twinkle
      float f4 = 0.1F + starSize[j] * 0.5F;
      float f5 =
          (float) (Math.cos((double) (f * 0.2F + (float) j * 0.1F) * (Math.PI * 2D)) * 0.2D + 0.8F);

      // Set star color (slightly blue-tinted white)
      float f6 = 0.9F * f5;
      float f7 = 0.9F * f5;
      float f8 = 1.0F * f5;

      // Draw star
      bufferbuilder.vertex(poseStack.last().pose(), f1, f2, f3).color(f6, f7, f8, 1.0F).endVertex();
      bufferbuilder
          .vertex(poseStack.last().pose(), f1, f2 + f4, f3)
          .color(f6, f7, f8, 1.0F)
          .endVertex();
      bufferbuilder
          .vertex(poseStack.last().pose(), f1 + f4, f2 + f4, f3 + f4)
          .color(f6, f7, f8, 1.0F)
          .endVertex();
      bufferbuilder
          .vertex(poseStack.last().pose(), f1 + f4, f2, f3 + f4)
          .color(f6, f7, f8, 1.0F)
          .endVertex();
    }

    tesselator.end();
  }

  private void drawSkyTexture(PoseStack poseStack, float partialTick) {
    // Draw a custom sky texture that gives an "infinite library" feel
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, STAR_TEXTURE);

    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder bufferbuilder = tesselator.getBuilder();

    // Draw multiple layers of scrolling clouds/void patterns
    for (int i = 1; i <= 3; ++i) {
      poseStack.pushPose();
      float f = 1.0F / (float) i;
      poseStack.mulPose(Axis.YP.rotationDegrees((float) i * 30.0F));

      for (int j = 0; j < 6; ++j) {
        poseStack.pushPose();

        if (j == 1) {
          poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        }

        if (j == 2) {
          poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        }

        if (j == 3) {
          poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        }

        if (j == 4) {
          poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }

        if (j == 5) {
          poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
        }

        Matrix4f matrix4f = poseStack.last().pose();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        float f1 = (float) ((j % 2) * 2 - 1) * 0.5F + 0.5F;
        float f2 = 0.0F;
        float f3 = 1.0F;
        float f4 = -100.0F;
        float f5 = -100.0F;
        float f6 = -100.0F;

        // Scroll the texture based on time
        float f7 = (float) Util.getMillis() * 0.0001F * (float) i * (j % 2 * 2 - 1);
        float f8 = (float) Util.getMillis() * 0.0001F * (float) i * (j % 2 * 2 - 1);

        bufferbuilder
            .vertex(matrix4f, -100.0F, -100.0F, -100.0F)
            .uv(f1 + f7, f2 + f8)
            .color(1.0F, 1.0F, 1.0F, 0.5F)
            .endVertex();
        bufferbuilder
            .vertex(matrix4f, -100.0F, -100.0F, 100.0F)
            .uv(f1 + f7, f3 + f8)
            .color(1.0F, 1.0F, 1.0F, 0.5F)
            .endVertex();
        bufferbuilder
            .vertex(matrix4f, 100.0F, -100.0F, 100.0F)
            .uv(f3 + f7, f3 + f8)
            .color(1.0F, 1.0F, 1.0F, 0.5F)
            .endVertex();
        bufferbuilder
            .vertex(matrix4f, 100.0F, -100.0F, -100.0F)
            .uv(f3 + f7, f2 + f8)
            .color(1.0F, 1.0F, 1.0F, 0.5F)
            .endVertex();

        tesselator.end();
        poseStack.popPose();
      }

      poseStack.popPose();
    }
  }

  @Override
  public boolean renderClouds(
      ClientLevel level,
      int ticks,
      float partialTick,
      PoseStack poseStack,
      double camX,
      double camY,
      double camZ,
      Matrix4f projectionMatrix) {
    // Custom cloud rendering for the library dimension
    return true;
  }

  @Override
  public boolean renderSnowAndRain(
      ClientLevel level,
      int ticks,
      float partialTick,
      LightTexture lightTexture,
      double camX,
      double camY,
      double camZ) {
    // No weather in the library
    return false;
  }

  @Override
  public boolean tickRain(ClientLevel level, int ticks, Camera camera) {
    // No rain in the library
    return false;
  }

  @Override
  public float[] getSunriseColor(float timeOfDay, float partialTicks) {
    // Custom sunrise/sunset colors for the library
    float f = 0.4F;
    float f1 = Mth.cos(timeOfDay * ((float) Math.PI * 2F)) - 0.0F;
    float f2 = -0.0F;

    if (f1 >= f2 - f && f1 <= f2 + f) {
      float f3 = (f1 - f2) / f * 0.5F + 0.5F;
      float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float) Math.PI)) * 0.99F;
      f4 = f4 * f4;
      SUNRISE_COLORS[0] = f3 * 0.3F + 0.1F;
      SUNRISE_COLORS[1] = f3 * f3 * 0.7F + 0.2F;
      SUNRISE_COLORS[2] = f3 * f3 * 0.7F + 0.2F;
      SUNRISE_COLORS[3] = f4;
      return SUNRISE_COLORS;
    } else {
      return null;
    }
  }
}
