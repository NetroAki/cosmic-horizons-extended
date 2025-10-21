package com.netroaki.chex.client.renderer.entity.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.client.model.entity.inferno.InfernalSovereignModel;
import com.netroaki.chex.entity.inferno.InfernalSovereignEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class InfernalSovereignRenderer
    extends MobRenderer<InfernalSovereignEntity, InfernalSovereignModel> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/infernal_sovereign.png");
  private static final ResourceLocation PHASE_2_TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/infernal_sovereign_phase2.png");
  private static final ResourceLocation PHASE_3_TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/infernal_sovereign_phase3.png");

  public InfernalSovereignRenderer(EntityRendererProvider.Context context) {
    super(
        context,
        new InfernalSovereignModel(context.bakeLayer(InfernalSovereignModel.LAYER_LOCATION)),
        0.7F);
    this.addLayer(new InfernalSovereignGlowLayer(this));
  }

  @Override
  public ResourceLocation getTextureLocation(InfernalSovereignEntity entity) {
    return switch (entity.getPhase()) {
      case 3 -> PHASE_3_TEXTURE;
      case 2 -> PHASE_2_TEXTURE;
      default -> TEXTURE;
    };
  }

  @Override
  public void render(
      InfernalSovereignEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Scale up the boss
    float scale = 2.0F;
    poseStack.pushPose();
    poseStack.scale(scale, scale, scale);

    // Add bobbing effect
    if (entity.isAlive()) {
      float f = (float) entity.tickCount + partialTicks;
      poseStack.translate(0.0F, Mth.sin(f * 0.03F) * 0.1F, 0.0F);
    }

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    // Render fire rain particles when in phase 2 or 3
    if (entity.getPhase() >= 2) {
      renderFireRain(entity, partialTicks, poseStack, buffer, packedLight);
    }

    poseStack.popPose();
  }

  private void renderFireRain(
      InfernalSovereignEntity entity,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Only render fire rain when the boss is using that ability
    if (entity.getFireRainCooldown() > 0) {
      VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lightning());
      poseStack.pushPose();

      // Position the effect above the boss
      poseStack.translate(0.0, 3.0, 0.0);

      // Create a circular pattern of fire particles
      for (int i = 0; i < 16; i++) {
        float angle = (entity.tickCount + partialTicks) * 5.0F + i * 22.5F;
        float radius = 3.0F + Mth.sin((entity.tickCount + partialTicks) * 0.2F) * 0.5F;

        float x = Mth.cos(angle * ((float) Math.PI / 180F)) * radius;
        float z = Mth.sin(angle * ((float) Math.PI / 180F)) * radius;

        // Draw a line from the circle down to the ground
        drawParticleLine(poseStack, vertexConsumer, x, 0, z, 0, -10, 0, 0.1F);
      }

      poseStack.popPose();
    }
  }

  private void drawParticleLine(
      PoseStack.Pose pose,
      VertexConsumer buffer,
      float x1,
      float y1,
      float z1,
      float x2,
      float y2,
      float z2,
      float width) {
    float red = 1.0F;
    float green = 0.6F;
    float blue = 0.1F;
    float alpha = 0.5F;

    Vec3 normal = new Vec3(x2 - x1, y2 - y1, z2 - z1).normalize();
    Vec3 cross = new Vec3(0, 1, 0).cross(normal).normalize();

    Matrix4f matrix = pose.pose();

    // Draw a thick line using quads
    buffer
        .vertex(
            matrix,
            (float) (x1 + cross.x * width),
            (float) (y1 + cross.y * width),
            (float) (z1 + cross.z * width))
        .color(red, green, blue, alpha)
        .uv2(15728880)
        .endVertex();
    buffer
        .vertex(
            matrix,
            (float) (x1 - cross.x * width),
            (float) (y1 - cross.y * width),
            (float) (z1 - cross.z * width))
        .color(red, green, blue, alpha)
        .uv2(15728880)
        .endVertex();
    buffer
        .vertex(
            matrix,
            (float) (x2 - cross.x * width),
            (float) (y2 - cross.y * width),
            (float) (z2 - cross.z * width))
        .color(red, green, blue, alpha)
        .uv2(15728880)
        .endVertex();
    buffer
        .vertex(
            matrix,
            (float) (x2 + cross.x * width),
            (float) (y2 + cross.y * width),
            (float) (z2 + cross.z * width))
        .color(red, green, blue, alpha)
        .uv2(15728880)
        .endVertex();
  }

  @Override
  protected void scale(InfernalSovereignEntity entity, PoseStack poseStack, float partialTicks) {
    // Add pulsing effect when charging
    if (entity.isCharging()) {
      float f = (entity.tickCount + partialTicks) * 0.1F;
      float scale = 1.0F + Mth.sin(f) * 0.1F;
      poseStack.scale(scale, scale, scale);
    }
  }
}
