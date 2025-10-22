package com.netroaki.chex.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/**
 * Ultra-simple renderer that draws a single textured cube for any entity. Used as a placeholder so
 * we can spawn and debug entities before bespoke models/animations are ready.
 */
public class PlaceholderCubeRenderer<T extends Entity> extends EntityRenderer<T> {

  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(
          new ResourceLocation(CHEX.MOD_ID, "placeholder_cube"), "main");

  private final ModelPart cube;
  private final ResourceLocation texture;
  private final float baseScale;

  public PlaceholderCubeRenderer(
      EntityRendererProvider.Context context, ResourceLocation texture) {
    this(context, texture, 1.0F);
  }

  public PlaceholderCubeRenderer(
      EntityRendererProvider.Context context, ResourceLocation texture, float baseScale) {
    super(context);
    this.cube = context.bakeLayer(LAYER_LOCATION);
    this.texture = texture;
    this.baseScale = baseScale;
    this.shadowRadius = 0.2F * baseScale;
  }

  public static LayerDefinition createLayer() {
    MeshDefinition mesh = new MeshDefinition();
    PartDefinition root = mesh.getRoot();
    root.addOrReplaceChild(
        "cube",
        CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
        PartPose.ZERO);
    return LayerDefinition.create(mesh, 16, 16);
  }

  @Override
  public ResourceLocation getTextureLocation(T entity) {
    return texture;
  }

  @Override
  public void render(
      T entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    poseStack.pushPose();

    float width = Mth.clamp(entity.getBbWidth(), 0.25F, 3.0F);
    float height = Mth.clamp(entity.getBbHeight(), 0.25F, 3.0F);
    float scale = Math.max(width, height) * baseScale;
    poseStack.scale(scale, scale, scale);

    VertexConsumer consumer =
        buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
    cube.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

    poseStack.popPose();
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }
}
