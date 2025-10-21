package com.netroaki.chex.client.entity.renderer.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

/** Base renderer for Inferno Prime entities with common rendering logic. */
public abstract class InfernoEntityRenderer<T extends Entity> extends EntityRenderer<T> {
  protected InfernoEntityRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(
      @NotNull T entity,
      float entityYaw,
      float partialTicks,
      @NotNull PoseStack poseStack,
      @NotNull MultiBufferSource buffer,
      int packedLight) {
    // Apply fire effect if entity is on fire
    if (entity.isOnFire()) {
      poseStack.pushPose();
      float scale = entity.getBbWidth() * 1.4f;
      poseStack.scale(scale, scale, scale);
      float yOffset = entity.getBbHeight() / (2.0f * scale);
      poseStack.translate(0.0D, yOffset, 0.0D);
      super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
      poseStack.popPose();
    }

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  @Override
  protected boolean shouldShowName(T entity) {
    double d0 = this.entityRenderDispatcher.distanceToSqr(entity);
    float f = entity.isDiscrete() ? 0.25F : 0.5F;
    return d0 >= (double) (f * f) && entity.isAlive() && !entity.isInvisible();
  }
}
