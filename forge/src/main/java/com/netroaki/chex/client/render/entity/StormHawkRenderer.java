package com.netroaki.chex.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.model.StormHawkModel;
import com.netroaki.chex.entity.arrakis.StormHawkEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class StormHawkRenderer extends MobRenderer<StormHawkEntity, StormHawkModel> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/entity/arrakis/storm_hawk.png");

  public StormHawkRenderer(EntityRendererProvider.Context context) {
    super(context, new StormHawkModel(context.bakeLayer(StormHawkModel.LAYER_LOCATION)), 0.5F);
  }

  @Override
  public ResourceLocation getTextureLocation(StormHawkEntity entity) {
    return TEXTURE;
  }

  @Override
  public void render(
      StormHawkEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Add wing flapping animation
    float flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
    float flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
    float wingRotation = Mth.sin(flap) * flapSpeed * 0.5f;

    // Apply wing rotation to the model
    this.getModel()
        .setupAnim(entity, 0, 0, entity.tickCount + partialTicks, entityYaw, entity.getXRot());
    this.getModel().setupWingRotation(wingRotation);

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }
}
