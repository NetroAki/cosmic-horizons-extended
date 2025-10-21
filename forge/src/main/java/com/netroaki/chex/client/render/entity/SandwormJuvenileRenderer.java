package com.netroaki.chex.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.model.entity.SandwormJuvenileModel;
import com.netroaki.chex.entity.arrakis.SandwormJuvenileEntity;
import com.netroaki.chex.init.ModelLayerInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SandwormJuvenileRenderer
    extends MobRenderer<SandwormJuvenileEntity, SandwormJuvenileModel> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/entity/arrakis/sandworm_juvenile.png");

  public SandwormJuvenileRenderer(EntityRendererProvider.Context context) {
    super(
        context,
        new SandwormJuvenileModel(context.bakeLayer(ModelLayerInit.SANDWORM_JUVENILE)),
        0.5F);
  }

  @Override
  public ResourceLocation getTextureLocation(SandwormJuvenileEntity entity) {
    return TEXTURE;
  }

  @Override
  protected void scale(SandwormJuvenileEntity entity, PoseStack poseStack, float partialTickTime) {
    float scale = 1.0f;
    if (entity.isBurrowed()) {
      // Scale down when burrowing
      float progress = Math.min(1.0f, (entity.getBurrowTicks() + partialTickTime) / 20.0f);
      scale = 1.0f - (progress * 0.3f);
    }
    poseStack.scale(scale, scale, scale);
  }

  @Override
  public void render(
      SandwormJuvenileEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Add wiggling motion when moving
    if (entity.getDeltaMovement().lengthSqr() > 0.0D) {
      float f = entity.walkAnimation.position(partialTicks) * 0.5F;
      float f1 = entity.walkAnimation.speed(partialTicks);
      poseStack.translate(0.0F, -f * f1, 0.0F);
    }

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }
}
