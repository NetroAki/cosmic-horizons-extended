package com.netroaki.chex.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(
    modid = CosmicHorizonsExpanded.MOD_ID,
    value = Dist.CLIENT,
    bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArrakisSkyRenderer {
  private static final ResourceLocation SUN_TEXTURE =
      new ResourceLocation("textures/environment/sun.png");
  private static final ResourceLocation MOON_PHASES =
      new ResourceLocation("textures/environment/moon_phases.png");

  // Arrakis-specific colors
  private static final Vector3f DAWN_COLOR = new Vector3f(0.9f, 0.4f, 0.1f); // Deep orange-red
  private static final Vector3f NOON_COLOR = new Vector3f(0.8f, 0.5f, 0.3f); // Sandy orange
  private static final Vector3f SUNSET_COLOR = new Vector3f(0.9f, 0.3f, 0.1f); // Intense red-orange
  private static final Vector3f NIGHT_COLOR = new Vector3f(0.2f, 0.1f, 0.15f); // Deep maroon

  private static float[] sunriseCol = new float[4];
  private static boolean isSandstorm = false;

  @SubscribeEvent
  public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
    // Register custom sky renderer for Arrakis dimension
    event.register(
        new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "arrakis"),
        new DimensionSpecialEffects(
            256.0F, // Cloud level
            true, // Alternate sky color
            DimensionSpecialEffects.SkyType.NORMAL,
            false, // Force bright lightmap
            false // Tweak lightmap
            ) {
          @Override
          public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) {
            // Use custom fog color based on time of day
            return biomeFogColor.multiply(
                daylight * 0.94F + 0.06F, daylight * 0.94F + 0.06F, daylight * 0.91F + 0.09F);
          }

          @Override
          public boolean isFoggyAt(int x, int y) {
            // Make fog more intense during sandstorms
            return isSandstorm;
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
            renderArrakisSky(level, partialTick, poseStack, camera, projectionMatrix, setupFog);
            return true;
          }
        });
  }

  @SubscribeEvent
  public static void onFogColors(ViewportEvent.ComputeFogColor event) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null
        || !minecraft.level.dimension().location().getPath().equals("arrakis")) {
      return;
    }

    // Get time of day (0.0 to 1.0)
    float timeOfDay = minecraft.level.getTimeOfDay(event.getPartialTick());

    // Calculate sun position in the sky (0.0 to 1.0, where 0.5 is noon)
    float sunPosition = (timeOfDay + 0.25F) % 1.0F;

    // Calculate sun intensity (0.0 at night to 1.0 at noon)
    float sunIntensity = 1.0F - (float) ((Math.cos(timeOfDay * Math.PI * 2.0D) * 2.0D + 0.5D));
    sunIntensity = Mth.clamp(sunIntensity, 0.0F, 1.0F);

    // Get base colors based on time of day
    Vector3f skyColor;
    if (sunPosition < 0.25F) {
      // Night to dawn
      float t = sunPosition / 0.25F;
      skyColor = lerpColor(NIGHT_COLOR, DAWN_COLOR, t * t);
    } else if (sunPosition < 0.35F) {
      // Dawn to day
      float t = (sunPosition - 0.25F) / 0.1F;
      skyColor = lerpColor(DAWN_COLOR, NOON_COLOR, t);
    } else if (sunPosition < 0.65F) {
      // Day
      skyColor = NOON_COLOR;
    } else if (sunPosition < 0.75F) {
      // Day to sunset
      float t = (sunPosition - 0.65F) / 0.1F;
      skyColor = lerpColor(NOON_COLOR, SUNSET_COLOR, t);
    } else {
      // Sunset to night
      float t = (sunPosition - 0.75F) / 0.25F;
      skyColor = lerpColor(SUNSET_COLOR, NIGHT_COLOR, t * t);
    }

    // Apply sandstorm effect if active
    if (isSandstorm) {
      float stormFactor = 0.7f; // How much the sandstorm affects the sky color
      Vector3f stormColor = new Vector3f(0.8f, 0.6f, 0.4f); // Sandy color
      skyColor.lerp(stormColor, stormFactor);
    }

    // Set the fog color
    event.setRed(skyColor.x());
    event.setGreen(skyColor.y());
    event.setBlue(skyColor.z());
  }

  private static void renderArrakisSky(
      ClientLevel level,
      float partialTick,
      PoseStack poseStack,
      Camera camera,
      Matrix4f projectionMatrix,
      Runnable setupFog) {
    RenderSystem.depthMask(false);
    RenderSystem.setShader(GameRenderer::getPositionColorShader);

    // Get time of day (0.0 to 1.0)
    float timeOfDay = level.getTimeOfDay(partialTick);

    // Calculate sun position in the sky (0.0 to 1.0, where 0.5 is noon)
    float sunPosition = (timeOfDay + 0.25F) % 1.0F;

    // Calculate sun intensity (0.0 at night to 1.0 at noon)
    float sunIntensity = 1.0F - (float) ((Math.cos(timeOfDay * Math.PI * 2.0D) * 2.0D + 0.5D));
    sunIntensity = Mth.clamp(sunIntensity, 0.0F, 1.0F);

    // Get base colors based on time of day
    Vector3f skyColor;
    if (sunPosition < 0.25F) {
      // Night to dawn
      float t = sunPosition / 0.25F;
      skyColor = lerpColor(NIGHT_COLOR, DAWN_COLOR, t * t);
    } else if (sunPosition < 0.35F) {
      // Dawn to day
      float t = (sunPosition - 0.25F) / 0.1F;
      skyColor = lerpColor(DAWN_COLOR, NOON_COLOR, t);
    } else if (sunPosition < 0.65F) {
      // Day
      skyColor = NOON_COLOR;
    } else if (sunPosition < 0.75F) {
      // Day to sunset
      float t = (sunPosition - 0.65F) / 0.1F;
      skyColor = lerpColor(NOON_COLOR, SUNSET_COLOR, t);
    } else {
      // Sunset to night
      float t = (sunPosition - 0.75F) / 0.25F;
      skyColor = lerpColor(SUNSET_COLOR, NIGHT_COLOR, t * t);
    }

    // Apply sandstorm effect if active
    if (isSandstorm) {
      float stormFactor = 0.7f; // How much the sandstorm affects the sky color
      Vector3f stormColor = new Vector3f(0.8f, 0.6f, 0.4f); // Sandy color
      skyColor.lerp(stormColor, stormFactor);
    }

    // Render sky gradient
    renderSkyGradient(poseStack, skyColor, sunIntensity);

    // Render sun and moon
    if (sunIntensity > 0.0F) {
      // Only render sun during the day
      renderSun(poseStack, level, partialTick, sunPosition, sunIntensity);
    } else {
      // Only render moon at night
      renderMoon(poseStack, level, partialTick, sunPosition);
    }

    // Render stars (only visible at night)
    if (sunIntensity < 0.5F) {
      renderStars(poseStack, projectionMatrix, partialTick, 1.0F - sunIntensity * 2.0F);
    }

    RenderSystem.depthMask(true);
  }

  private static void renderSkyGradient(PoseStack poseStack, Vector3f skyColor, float intensity) {
    RenderSystem.setShader(GameRenderer::getPositionColorShader);

    // Sky gradient colors
    float r = skyColor.x();
    float g = skyColor.y();
    float b = skyColor.z();

    // Horizon colors (slightly brighter)
    float hr = Math.min(1.0F, r * 1.2F);
    float hg = Math.min(1.0F, g * 1.1F);
    float hb = Math.min(1.0F, b * 1.05F);

    // Zenith color (darker)
    float zr = r * 0.7F;
    float zg = g * 0.7F;
    float zb = b * 0.7F;

    // Draw sky gradient
    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    poseStack.pushPose();
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

    float f = Math.signum(-1.0F) * 0.95F + 0.05F;
    float f1 = -1.0F;
    float f2 = 100.0F;

    bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
    bufferbuilder.vertex(0.0D, 100.0D, 0.0D).color(zr, zg, zb, 1.0F).endVertex();

    for (int i = -180; i <= 180; i += 30) {
      float f3 = (float) i * ((float) Math.PI / 180F);
      float f4 = Mth.sin(f3);
      float f5 = Mth.cos(f3);
      bufferbuilder
          .vertex(
              (double) (f4 * 120.0F * f1),
              (double) (f5 * 120.0F * f1),
              (double) (-f5 * 40.0F * f * f2))
          .color(hr, hg, hb, 1.0F)
          .endVertex();
    }

    BufferUploader.drawWithShader(bufferbuilder.end());
    poseStack.popPose();
    RenderSystem.disableBlend();
  }

  private static void renderSun(
      PoseStack poseStack,
      ClientLevel level,
      float partialTick,
      float sunPosition,
      float intensity) {
    float sunAngle = (sunPosition - 0.5F) * ((float) Math.PI * 2.0F);
    float sunSize = 30.0F;

    // Calculate sun color (more intense at noon)
    float sunIntensity = Mth.clamp((intensity - 0.7F) * 3.0F, 0.0F, 1.0F);
    float r = 1.0F;
    float g = 0.9F - sunIntensity * 0.3F;
    float b = 0.7F - sunIntensity * 0.5F;

    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, SUN_TEXTURE);
    RenderSystem.setShaderColor(r, g, b, 1.0F);

    poseStack.pushPose();
    poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
    poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));

    // Draw sun
    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

    bufferbuilder.vertex(-sunSize, 100.0D, -sunSize).uv(0.0F, 0.0F).endVertex();
    bufferbuilder.vertex(sunSize, 100.0D, -sunSize).uv(1.0F, 0.0F).endVertex();
    bufferbuilder.vertex(sunSize, 100.0D, sunSize).uv(1.0F, 1.0F).endVertex();
    bufferbuilder.vertex(-sunSize, 100.0D, sunSize).uv(0.0F, 1.0F).endVertex();

    BufferUploader.drawWithShader(bufferbuilder.end());
    poseStack.popPose();

    // Add sun glare/halo effect
    if (intensity > 0.8F) {
      float glareIntensity = (intensity - 0.8F) * 5.0F;
      RenderSystem.disableTexture();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);

      poseStack.pushPose();
      poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
      poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));

      float glareSize = sunSize * 3.0F;
      bufferbuilder = Tesselator.getInstance().getBuilder();
      bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

      bufferbuilder.vertex(0.0D, 100.0D, 0.0D).color(r, g, b, glareIntensity).endVertex();

      for (int i = 0; i <= 360; i += 10) {
        float rad = (float) Math.toRadians(i);
        float x = Mth.cos(rad) * glareSize;
        float z = Mth.sin(rad) * glareSize;
        bufferbuilder.vertex(x, 100.0D, z).color(r, g, b, 0.0F).endVertex();
      }

      BufferUploader.drawWithShader(bufferbuilder.end());
      poseStack.popPose();
      RenderSystem.enableTexture();
    }

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  private static void renderMoon(
      PoseStack poseStack, ClientLevel level, float partialTick, float moonPosition) {
    // Similar to sun but darker and with moon phases
    float moonAngle = (moonPosition - 0.5F) * ((float) Math.PI * 2.0F);
    float moonSize = 20.0F;

    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, MOON_PHASES);
    RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);

    // Calculate moon phase (0-7)
    int moonPhase = level.getMoonPhase();
    int xOffset = moonPhase % 4;
    int yOffset = moonPhase / 4 % 2;
    float uMin = xOffset * 0.25F;
    float uMax = (xOffset + 1) * 0.25F;
    float vMin = yOffset * 0.5F;
    float vMax = (yOffset + 1) * 0.5F;

    poseStack.pushPose();
    poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
    poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));

    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

    bufferbuilder.vertex(-moonSize, -100.0D, -moonSize).uv(uMax, vMin).endVertex();
    bufferbuilder.vertex(moonSize, -100.0D, -moonSize).uv(uMin, vMin).endVertex();
    bufferbuilder.vertex(moonSize, -100.0D, moonSize).uv(uMin, vMax).endVertex();
    bufferbuilder.vertex(-moonSize, -100.0D, moonSize).uv(uMax, vMax).endVertex();

    BufferUploader.drawWithShader(bufferbuilder.end());
    poseStack.popPose();

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  private static void renderStars(
      PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, float intensity) {
    // Only render stars at night
    if (intensity <= 0.0F) {
      return;
    }

    RenderSystem.setShader(GameRenderer::getPositionColorShader);

    // Random star positions (seeded for consistency)
    Random random = new Random(10842L);

    BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

    // Generate random stars
    for (int i = 0; i < 1500; ++i) {
      double d0 = random.nextFloat() * 2.0F - 1.0F;
      double d1 = random.nextFloat() * 2.0F - 1.0F;
      double d2 = random.nextFloat() * 2.0F - 1.0F;
      double d3 = 0.15F + random.nextFloat() * 0.1F;
      double d4 = d0 * d0 + d1 * d1 + d2 * d2;

      if (d4 < 1.0D && d4 > 0.01D) {
        d4 = 1.0D / Math.sqrt(d4);
        d0 *= d4;
        d1 *= d4;
        d2 *= d4;

        double d5 = d0 * 100.0D;
        double d6 = d1 * 100.0D;
        double d7 = d2 * 100.0D;

        // Random star color (slightly blueish)
        float f = 0.8F + random.nextFloat() * 0.2F;
        float f1 = 0.8F + random.nextFloat() * 0.2F;
        float f2 = 0.9F + random.nextFloat() * 0.1F;

        float f3 = (float) (Math.sin(random.nextDouble() * Math.PI * 2.0D) * 0.2D);
        float f4 = (float) (Math.sin(random.nextDouble() * Math.PI * 2.0D) * 0.2D);

        // Add some twinkling
        double d8 = random.nextDouble() * Math.PI * 2.0D;
        double d9 = 0.1D + random.nextDouble() * 0.05D;
        double d10 = Math.cos(d8) * d9;
        double d11 = Math.sin(d8) * d9;

        for (int j = 0; j < 4; ++j) {
          double d12 = 0.0D;
          double d13 = ((j & 2) - 1) * d3;
          double d14 = ((j + 1 & 2) - 1) * d3;

          double d15 = d5 + d13;
          double d16 = d6 + d14;
          double d17 = d7 + d13;

          // Apply twinkling
          d15 += d10;
          d16 += d11;

          // Add some variation to star brightness
          float f5 = (float) Math.sin((level.getGameTime() + partialTick) * 0.1F + i) * 0.1F + 0.9F;

          bufferbuilder
              .vertex(poseStack.last().pose(), (float) d15, (float) d16, (float) d17)
              .color(f * f5 * intensity, f1 * f5 * intensity, f2 * f5 * intensity, 1.0F)
              .endVertex();
        }
      }
    }

    // Draw all stars in one batch
    BufferUploader.drawWithShader(bufferbuilder.end());
  }

  private static Vector3f lerpColor(Vector3f start, Vector3f end, float t) {
    return new Vector3f(
        Mth.lerp(t, start.x(), end.x()),
        Mth.lerp(t, start.y(), end.y()),
        Mth.lerp(t, start.z(), end.z()));
  }

  public static void updateSandstormState(boolean isActive) {
    isSandstorm = isActive;
  }
}
