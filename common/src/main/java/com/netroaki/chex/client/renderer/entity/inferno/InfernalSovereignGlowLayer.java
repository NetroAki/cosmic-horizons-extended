package com.netroaki.chex.client.renderer.entity.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.client.model.entity.inferno.InfernalSovereignModel;
import com.netroaki.chex.entity.inferno.InfernalSovereignEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class InfernalSovereignGlowLayer
    extends RenderLayer<InfernalSovereignEntity, InfernalSovereignModel> {
  private static final ResourceLocation GLOW_TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/infernal_sovereign_glow.png");
  private static final ResourceLocation PHASE_2_GLOW =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID,
          "textures/entity/inferno/infernal_sovereign_phase2_glow.png");
  private static final ResourceLocation PHASE_3_GLOW =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID,
          "textures/entity/inferno/infernal_sovereign_phase3_glow.png");

  public InfernalSovereignGlowLayer(
      RenderLayerParent<InfernalSovereignEntity, InfernalSovereignModel> renderer) {
    super(renderer);
  }

  @Override
  public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      InfernalSovereignEntity entity,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Only render if not invisible
    if (entity.isInvisible()) {
      return;
    }

    // Get the appropriate glow texture based on phase
    ResourceLocation texture =
        switch (entity.getPhase()) {
          case 3 -> PHASE_3_GLOW;
          case 2 -> PHASE_2_GLOW;
          default -> GLOW_TEXTURE;
        };

    // Calculate pulse effect based on phase and health
    float pulse = 1.0F;
    if (entity.getPhase() >= 2) {
      float healthRatio = entity.getHealth() / entity.getMaxHealth();
      pulse = 0.8F + Mth.sin(ageInTicks * 0.2F) * 0.2F;

      // More intense pulsing at lower health
      if (healthRatio < 0.3F) {
        pulse += Mth.sin(ageInTicks * 0.5F) * 0.2F;
      }
    }

    // Render the glow layer
    VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.eyes(texture));
    this.getParentModel()
        .renderToBuffer(
            poseStack,
            vertexConsumer,
            15728640, // Full brightness
            OverlayTexture.NO_OVERLAY,
            pulse,
            pulse,
            pulse,
            1.0F);

    // Add additional effects for phase 3
    if (entity.getPhase() >= 3) {
      renderPhase3Effects(poseStack, buffer, entity, ageInTicks, partialTicks);
    }
  }

  private void renderPhase3Effects(
      PoseStack poseStack,
      MultiBufferSource buffer,
      InfernalSovereignEntity entity,
      float ageInTicks,
      float partialTicks) {
    // Add floating ember particles around the boss
    poseStack.pushPose();

    // Position at the boss's location
    poseStack.translate(0.0, 2.0, 0.0);

    // Create a ring of particles around the boss
    int particleCount = 12;
    float radius = 2.5F;
    float height = 1.5F;

    for (int i = 0; i < particleCount; i++) {
      float angle = (ageInTicks * 5.0F + (i * (360.0F / particleCount))) * ((float) Math.PI / 180F);
      float x = Mth.cos(angle) * radius;
      float z = Mth.sin(angle) * radius;
      float y = Mth.sin(ageInTicks * 0.2F + i) * height * 0.5F;

      // Draw a small quad for each particle
      Matrix4f matrix = poseStack.last().pose();
      VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lightning());

      float size = 0.2F;
      float r = 1.0F;
      float g = 0.4F + Mth.sin(ageInTicks * 0.2F + i) * 0.1F;
      float b = 0.1F;
      float a = 0.8F;

      // Draw a small quad
      vertexConsumer
          .vertex(matrix, x - size, y - size, z)
          .color(r, g, b, a)
          .uv2(15728880)
          .endVertex();
      vertexConsumer
          .vertex(matrix, x - size, y + size, z)
          .color(r, g, b, a)
          .uv2(15728880)
          .endVertex();
      vertexConsumer
          .vertex(matrix, x + size, y + size, z)
          .color(r, g, b, a)
          .uv2(15728880)
          .endVertex();
      vertexConsumer
          .vertex(matrix, x + size, y - size, z)
          .color(r, g, b, a)
          .uv2(15728880)
          .endVertex();
    }

    poseStack.popPose();
  }
}
