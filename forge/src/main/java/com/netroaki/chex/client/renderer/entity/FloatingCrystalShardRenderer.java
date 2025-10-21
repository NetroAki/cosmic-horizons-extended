package com.netroaki.chex.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.netroaki.chex.entity.crystal.FloatingCrystalShard;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class FloatingCrystalShardRenderer extends EntityRenderer<FloatingCrystalShard> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation("chex", "textures/entity/crystal_shard.png");

  public FloatingCrystalShardRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.shadowRadius = 0.15F;
    this.shadowStrength = 0.5F;
  }

  @Override
  public void render(
      FloatingCrystalShard entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    poseStack.pushPose();

    // Position and rotate the shard
    float yOffset = Mth.sin((entity.tickCount + partialTicks) * 0.1F) * 0.1F + 0.1F;
    poseStack.translate(0.0D, 0.3F + yOffset, 0.0D);
    poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
    poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getRotation() + partialTicks * 2.0F));

    // Scale based on crystal type
    float scale = 0.5F + entity.getCrystalType() * 0.1F;
    poseStack.scale(scale, scale, scale);

    // Get the buffer to render the shard
    VertexConsumer vertexBuilder =
        buffer.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity)));

    // Render the shard
    Matrix4f matrix4f = poseStack.last().pose();
    Matrix3f normal = poseStack.last().normal();

    // Front face
    vertexBuilder
        .vertex(matrix4f, -0.5F, -0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(0.0F, 1.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, 1.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix4f, 0.5F, -0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(1.0F, 1.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, 1.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix4f, 0.5F, 0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(1.0F, 0.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, 1.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix4f, -0.5F, 0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(0.0F, 0.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, 1.0F)
        .endVertex();

    // Back face (same as front but with inverted normals)
    vertexBuilder
        .vertex(matrix4f, -0.5F, -0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(0.0F, 1.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, -1.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix4f, -0.5F, 0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(1.0F, 1.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, -1.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix4f, 0.5F, 0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(1.0F, 0.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, -1.0F)
        .endVertex();
    vertexBuilder
        .vertex(matrix4f, 0.5F, -0.5F, 0.0F)
        .color(1.0F, 1.0F, 1.0F, 0.8F)
        .uv(0.0F, 0.0F)
        .overlayCoords(OverlayTexture.NO_OVERLAY)
        .uv2(packedLight)
        .normal(normal, 0.0F, 0.0F, -1.0F)
        .endVertex();

    poseStack.popPose();

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  @Override
  public ResourceLocation getTextureLocation(FloatingCrystalShard entity) {
    return TEXTURE;
  }
}
