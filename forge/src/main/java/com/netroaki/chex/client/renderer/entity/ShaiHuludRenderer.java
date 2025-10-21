package com.netroaki.chex.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.netroaki.chex.entity.arrakis.ShaiHuludEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShaiHuludRenderer
    extends MobRenderer<ShaiHuludEntity, ShaiHuludRenderer.ShaiHuludModel> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation("chex", "textures/entity/arrakis/shai_hulud.png");

  public ShaiHuludRenderer(EntityRendererProvider.Context context) {
    super(context, new ShaiHuludModel(context.bakeLayer(ShaiHuludModel.LAYER_LOCATION)), 2.0F);
  }

  @Override
  public ResourceLocation getTextureLocation(ShaiHuludEntity entity) {
    return TEXTURE;
  }

  @Override
  protected void scale(ShaiHuludEntity entity, PoseStack poseStack, float partialTickTime) {
    // Scale up the model
    float scale = 5.0F;
    poseStack.scale(scale, scale, scale);

    // Make the model horizontal
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

    // Add some subtle animation
    float animation = (entity.tickCount + partialTickTime) * 0.1F;
    float wave = Mth.sin(animation) * 0.1F;
    poseStack.mulPose(Axis.ZP.rotation(wave));
  }

  @Override
  public void render(
      ShaiHuludEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Don't render if burrowed and not emerging/submerging
    if (entity.isInvisible() && !entity.isEmerging() && !entity.isSubmerging()) {
      return;
    }

    // Handle emergence/submersion animation
    if (entity.isEmerging() || entity.isSubmerging()) {
      float progress = (entity.tickCount % 40) / 40.0F;
      if (entity.isSubmerging()) {
        progress = 1.0F - progress;
      }
      poseStack.translate(0, -2.0F * (1.0F - progress), 0);
    }

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  // Simple model class for Shai-Hulud
  public static class ShaiHuludModel extends EntityModel<ShaiHuludEntity> {
    public static final ResourceLocation LAYER_LOCATION =
        new ResourceLocation("chex", "shai_hulud");

    public ShaiHuludModel(EntityRendererProvider.Context context) {
      super(context.bakeLayer(ModelLayers.ENDER_DRAGON)); // Using ender dragon layer as base
    }

    @Override
    public void setupAnim(
        ShaiHuludEntity entity,
        float limbSwing,
        float limbSwingAmount,
        float ageInTicks,
        float netHeadYaw,
        float headPitch) {
      // Animation logic here
    }

    @Override
    public void renderToBuffer(
        PoseStack poseStack,
        VertexConsumer buffer,
        int packedLight,
        int packedOverlay,
        float red,
        float green,
        float blue,
        float alpha) {
      // Render a simple worm-like shape
      // This is a placeholder - in a real implementation, you'd want to create a proper model
      // using Blockbench and load it with GeckoLib or similar

      // For now, we'll just render a simple box as a placeholder
      poseStack.pushPose();
      poseStack.scale(2.0F, 0.5F, 8.0F);
      poseStack.translate(0.0F, 0.5F, 0.0F);

      // Render multiple segments for a worm-like appearance
      for (int i = 0; i < 5; i++) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, i * 2.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(i * 10.0F));
        // This is where you'd render your actual model
        // For now, we'll just render a simple box
        poseStack.popPose();
      }

      poseStack.popPose();
    }
  }
}
