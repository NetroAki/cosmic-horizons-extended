package com.netroaki.chex.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.model.entity.SandEmperorModel;
import com.netroaki.chex.entity.arrakis.SandEmperorEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class SandEmperorRenderer
    extends MobRenderer<SandEmperorEntity, SandEmperorModel<SandEmperorEntity>> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/entity/arrakis/sand_emperor.png");
  private static final ResourceLocation GLOW_LAYER =
      new ResourceLocation(CHEX.MOD_ID, "textures/entity/arrakis/sand_emperor_glow.png");

  public SandEmperorRenderer(EntityRendererProvider.Context context) {
    super(
        context, new SandEmperorModel<>(context.bakeLayer(SandEmperorModel.LAYER_LOCATION)), 1.5F);
    this.addLayer(new SandEmperorGlowLayer<>(this));
    this.addLayer(new SandDustLayer<>(this));
  }

  @Override
  public ResourceLocation getTextureLocation(SandEmperorEntity entity) {
    return TEXTURE;
  }

  @Override
  protected void scale(SandEmperorEntity entity, PoseStack poseStack, float partialTick) {
    float scale = 2.0F; // Larger than the juvenile sandworm
    if (entity.isBurrowing() || entity.isBurrowed()) {
      float progress = entity.getBurrowProgress(partialTick);
      scale *= (1.0F - progress * 0.3F); // Shrink slightly when burrowing
    }

    // Add pulsing effect during phase transitions
    if (entity.getPhaseTransitionTicks() > 0) {
      float phaseProgress = entity.getPhaseTransitionProgress();
      scale += Mth.sin((entity.tickCount + partialTick) * 0.5F) * 0.1F * phaseProgress;
    }

    poseStack.scale(scale, scale, scale);
  }

  @Override
  protected float getFlipDegrees(SandEmperorEntity entity) {
    return 0; // Prevent model from flipping upside down when hurt
  }

  @Override
  public void render(
      SandEmperorEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Add subtle rotation based on movement for more dynamic appearance
    if (entity.getDeltaMovement().lengthSqr() > 1.0E-7D) {
      Vec3 motion = entity.getDeltaMovement();
      float yRot = (float) Mth.atan2(motion.x, motion.z) * (180F / (float) Math.PI);
      entityYaw += Mth.wrapDegrees(yRot - entity.getYRot());
    }

    // Add slight bobbing motion when not burrowed
    if (!entity.isBurrowed()) {
      float bob = Mth.sin((entity.tickCount + partialTicks) * 0.1F) * 0.1F;
      poseStack.translate(0, bob, 0);
    }

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  // Custom layer for emissive texture
  private static class SandEmperorGlowLayer<
          T extends SandEmperorEntity, M extends SandEmperorModel<T>>
      extends net.minecraft.client.renderer.entity.layers.RenderLayer<T, M> {
    public SandEmperorGlowLayer(SandEmperorRenderer renderer) {
      super(renderer);
    }

    @Override
    public void render(
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks,
        float ageInTicks,
        float netHeadYaw,
        float headPitch) {
      VertexConsumer vertexconsumer =
          buffer.getBuffer(net.minecraft.client.renderer.RenderType.eyes(GLOW_LAYER));
      float alpha = 0.8f;

      // Pulsing effect for the glow
      if (entity.getPhaseTransitionTicks() > 0) {
        float pulse = Mth.sin((entity.tickCount + partialTicks) * 0.5F) * 0.1F + 0.9F;
        alpha *= pulse;
      }

      this.getParentModel()
          .renderToBuffer(
              poseStack, vertexconsumer, 15728640, packedLight, 1.0F, 1.0F, 1.0F, alpha);
    }
  }

  // Custom layer for sand particles/dust
  private static class SandDustLayer<T extends SandEmperorEntity, M extends SandEmperorModel<T>>
      extends net.minecraft.client.renderer.entity.layers.RenderLayer<T, M> {
    private static final ResourceLocation SAND_DUST =
        new ResourceLocation(CHEX.MOD_ID, "textures/particle/sand_dust.png");

    public SandDustLayer(SandEmperorRenderer renderer) {
      super(renderer);
    }

    @Override
    public void render(
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float partialTicks,
        float ageInTicks,
        float netHeadYaw,
        float headPitch) {
      if (entity.isBurrowed() || entity.isBurrowing()) {
        float progress = entity.getBurrowProgress(partialTicks);
        if (progress > 0) {
          VertexConsumer vertexconsumer =
              buffer.getBuffer(
                  net.minecraft.client.renderer.RenderType.entityTranslucent(SAND_DUST));

          poseStack.pushPose();
          poseStack.translate(0, 0.1, 0);

          // Create a circular cloud of dust
          int segments = 8;
          float radius = 2.0F * progress;
          float alpha = 0.7F * progress;

          for (int i = 0; i < segments; i++) {
            float angle = (i / (float) segments) * Mth.TWO_PI;
            float x = Mth.cos(angle) * radius;
            float z = Mth.sin(angle) * radius;

            poseStack.pushPose();
            poseStack.translate(x, 0, z);

            Matrix4f pose = poseStack.last().pose();
            Matrix4f normal = new Matrix4f(pose).invert().transpose();

            // Render a billboarded quad
            float size = 0.5F * progress;
            vertexConsumer(
                vertexconsumer, pose, normal, -size, 0, -size, 0, 0, 1, 0, 1, 1, 1, alpha);
            vertexConsumer(
                vertexconsumer, pose, normal, size, 0, -size, 1, 0, 1, 0, 1, 1, 1, alpha);
            vertexConsumer(vertexconsumer, pose, normal, size, 0, size, 1, 1, 1, 0, 1, 1, 1, alpha);
            vertexConsumer(
                vertexconsumer, pose, normal, -size, 0, size, 0, 1, 1, 0, 1, 1, 1, alpha);

            poseStack.popPose();
          }

          poseStack.popPose();
        }
      }
    }

    private void vertexConsumer(
        VertexConsumer consumer,
        Matrix4f pose,
        Matrix4f normal,
        float x,
        float y,
        float z,
        float u,
        float v,
        float r,
        float g,
        float b,
        float nx,
        float ny,
        float nz,
        float a) {
      consumer
          .vertex(pose, x, y, z)
          .color(r, g, b, a)
          .uv(u, v)
          .overlayCoords(0, 10)
          .uv2(15728880)
          .normal(normal, nx, ny, nz)
          .endVertex();
    }
  }
}
