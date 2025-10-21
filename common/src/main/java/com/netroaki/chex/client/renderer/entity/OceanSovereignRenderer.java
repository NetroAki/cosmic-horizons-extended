package com.netroaki.chex.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.client.model.entity.OceanSovereignModel;
import com.netroaki.chex.entity.aqua_mundus.OceanSovereignEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class OceanSovereignRenderer extends MobRenderer<OceanSovereignEntity, OceanSovereignModel> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "textures/entity/ocean_sovereign.png");
  private static final ResourceLocation GLOW_LAYER =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/ocean_sovereign_glow.png");
  private static final RenderType GLOW_RENDER_TYPE = RenderType.eyes(GLOW_LAYER);

  public OceanSovereignRenderer(EntityRendererProvider.Context context) {
    super(
        context,
        new OceanSovereignModel(context.bakeLayer(OceanSovereignModel.LAYER_LOCATION)),
        1.0F);
    this.addLayer(new GlowLayer(this));
  }

  @Override
  public void render(
      OceanSovereignEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Store the current pose for later use
    poseStack.pushPose();

    // Scale the entity
    float scale = 2.0F;
    poseStack.scale(scale, scale, scale);

    // Apply entity rotation
    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));

    // Bob up and down in water
    if (entity.isInWater()) {
      float f = (float) entity.tickCount + partialTicks;
      poseStack.translate(0.0F, Mth.sin(f * 0.05F) * 0.1F, 0.0F);
    }

    // Render the main model
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    // Render additional effects based on state
    float pulse = entity.getPulseIntensity(partialTicks);
    if (pulse > 0) {
      this.renderPulseEffect(poseStack, buffer, packedLight, pulse);
    }

    // Render whirlpool effect if active
    if (entity.isPerformingWhirlpool() && entity.getWhirlpoolCenter() != null) {
      float whirlpoolIntensity = entity.getWhirlpoolAnimation(partialTicks);
      if (whirlpoolIntensity > 0) {
        this.renderWhirlpoolEffect(
            poseStack, buffer, packedLight, entity, whirlpoolIntensity, partialTicks);
      }
    }

    poseStack.popPose();
  }

  private void renderPulseEffect(
      PoseStack poseStack, MultiBufferSource buffer, int packedLight, float intensity) {
    poseStack.pushPose();

    // Create a pulsing effect that expands outward
    float scale = 1.0F + intensity * 0.5F;
    poseStack.scale(scale, scale, scale);

    // Make the pulse effect face the camera
    poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

    // Create a simple quad for the pulse effect
    VertexConsumer vertexBuilder =
        buffer.getBuffer(RenderType.entityTranslucentEmissive(GetTextureLocation(null)));
    float alpha = 0.5F * intensity;

    // Draw a simple quad
    Matrix4f matrix = poseStack.last().pose();
    vertexBuilder
        .vertex(matrix, -1.0F, -1.0F, 0.0F)
        .color(0.2F, 0.6F, 1.0F, alpha)
        .uv(0.0F, 1.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix, 1.0F, -1.0F, 0.0F)
        .color(0.2F, 0.6F, 1.0F, alpha)
        .uv(1.0F, 1.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix, 1.0F, 1.0F, 0.0F)
        .color(0.2F, 0.6F, 1.0F, alpha)
        .uv(1.0F, 0.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix, -1.0F, 1.0F, 0.0F)
        .color(0.2F, 0.6F, 1.0F, alpha)
        .uv(0.0F, 0.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();

    poseStack.popPose();
  }

  @Override
  protected void scale(OceanSovereignEntity entity, PoseStack poseStack, float partialTickTime) {
    // Apply any additional scaling here if needed
    float scale = 1.0F;
    poseStack.scale(scale, scale, scale);
  }

  private void renderWhirlpoolEffect(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      OceanSovereignEntity entity,
      float intensity,
      float partialTicks) {
    poseStack.pushPose();

    // Get whirlpool center position
    BlockPos center = entity.getWhirlpoolCenter();
    if (center == null) {
      poseStack.popPose();
      return;
    }

    // Calculate position relative to the entity
    double dx = center.getX() + 0.5 - entity.getX();
    double dy = center.getY() - entity.getY();
    double dz = center.getZ() + 0.5 - entity.getZ();

    // Move to whirlpool position
    poseStack.translate(dx, dy, dz);

    // Get time-based animation values
    float time = (entity.tickCount + partialTicks) * 0.5f;
    float radius = 8.0f * intensity;
    float height = 4.0f * intensity;

    // Get vertex builder for translucent rendering
    VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
    Matrix4f matrix = poseStack.last().pose();

    // Draw the whirlpool surface
    int segments = 32;
    float angleStep = (float) (2 * Math.PI / segments);

    // Base color (deep blue with transparency)
    int r = 20;
    int g = 60;
    int b = 120;
    int a = (int) (150 * intensity);

    // Draw the spiral
    for (int i = 0; i < segments; i++) {
      float angle1 = i * angleStep + time * 0.1f;
      float angle2 = (i + 1) * angleStep + time * 0.1f;

      float x1 = Mth.cos(angle1) * radius;
      float z1 = Mth.sin(angle1) * radius;
      float x2 = Mth.cos(angle2) * radius;
      float z2 = Mth.sin(angle2) * radius;

      // Inner points (slightly raised)
      float innerHeight = 0.2f * intensity;
      float innerRadius = 0.5f;

      // Draw triangle fan from center to outer edge
      // Center vertex
      vertexBuilder
          .vertex(matrix, 0, innerHeight, 0)
          .color(r, g, b, a)
          .uv(0.5f, 0.5f)
          .overlayCoords(OverlayTexture.NO_OVERLAY)
          .uv2(packedLight)
          .normal(0, 1, 0)
          .endVertex();

      // First outer vertex
      vertexBuilder
          .vertex(matrix, x1, 0, z1)
          .color(r, g, b, a / 2)
          .uv(0.5f + 0.5f * Mth.cos(angle1), 0.5f + 0.5f * Mth.sin(angle1))
          .overlayCoords(OverlayTexture.NO_OVERLAY)
          .uv2(packedLight)
          .normal(0, 1, 0)
          .endVertex();

      // Second outer vertex
      vertexBuilder
          .vertex(matrix, x2, 0, z2)
          .color(r, g, b, a / 2)
          .uv(0.5f + 0.5f * Mth.cos(angle2), 0.5f + 0.5f * Mth.sin(angle2))
          .overlayCoords(OverlayTexture.NO_OVERLAY)
          .uv2(packedLight)
          .normal(0, 1, 0)
          .endVertex();

      // Draw the sides of the whirlpool
      if (i % 2 == 0) {
        // Add some vertical movement to the sides
        float wave1 = Mth.sin(angle1 * 2 + time * 0.5f) * 0.2f * intensity;
        float wave2 = Mth.sin(angle2 * 2 + time * 0.5f) * 0.2f * intensity;

        // Outer edge vertices
        vertexBuilder
            .vertex(matrix, x1, -height + wave1, z1)
            .color(10, 30, 60, a)
            .uv(0, 0)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(Mth.cos(angle1), 0, Mth.sin(angle1))
            .endVertex();

        vertexBuilder
            .vertex(matrix, x2, -height + wave2, z2)
            .color(10, 30, 60, a)
            .uv(1, 0)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(Mth.cos(angle2), 0, Mth.sin(angle2))
            .endVertex();

        vertexBuilder
            .vertex(matrix, x2, 0, z2)
            .color(10, 30, 60, a / 2)
            .uv(1, 1)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(Mth.cos(angle2), 0, Mth.sin(angle2))
            .endVertex();

        vertexBuilder
            .vertex(matrix, x1, 0, z1)
            .color(10, 30, 60, a / 2)
            .uv(0, 1)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(Mth.cos(angle1), 0, Mth.sin(angle1))
            .endVertex();
      }
    }

    poseStack.popPose();
  }

  @Override
  public ResourceLocation getTextureLocation(OceanSovereignEntity entity) {
    return TEXTURE;
  }

  // Glow layer for the bioluminescent parts
  private static class GlowLayer extends EyesLayer<OceanSovereignEntity, OceanSovereignModel> {
    private static final RenderType GLOW =
        RenderType.eyes(
            new ResourceLocation(
                CosmicHorizonsExpanded.MOD_ID, "textures/entity/ocean_sovereign_glow.png"));

    public GlowLayer(OceanSovereignRenderer renderer) {
      super(renderer);
    }

    @Override
    public void render(
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        OceanSovereignEntity entity,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks,
        float ageInTicks,
        float netHeadYaw,
        float headPitch) {
      // Only render glow if not in shadow phase
      if (entity.getPhase() < 2) {
        VertexConsumer vertexconsumer = buffer.getBuffer(this.renderType());
        this.getParentModel()
            .renderToBuffer(
                poseStack,
                vertexconsumer,
                15728640, // Full brightness
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                entity.getPulseIntensity(partialTicks) * 0.5F + 0.5F);
      }
    }

    @Override
    public RenderType renderType() {
      return GLOW;
    }
  }
}
