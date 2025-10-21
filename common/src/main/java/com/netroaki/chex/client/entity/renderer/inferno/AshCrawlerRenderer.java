package com.netroaki.chex.client.entity.renderer.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.entity.inferno.AshCrawlerEntity;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AshCrawlerRenderer
    extends MobRenderer<AshCrawlerEntity, SpiderModel<AshCrawlerEntity>> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/ash_crawler.png");
  private static final ResourceLocation EMISSIVE_TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/ash_crawler_emissive.png");

  public AshCrawlerRenderer(EntityRendererProvider.Context context) {
    super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.8F);
    this.addLayer(
        new InfernoEmissiveLayer<>(this, EMISSIVE_TEXTURE) {
          @Override
          public void render(
              @NotNull PoseStack poseStack,
              @NotNull MultiBufferSource buffer,
              int packedLight,
              @NotNull AshCrawlerEntity entity,
              float limbSwing,
              float limbSwingAmount,
              float partialTicks,
              float ageInTicks,
              float netHeadYaw,
              float headPitch) {
            // Only render emissive layer when not burrowed
            if (!entity.isBurrowed()) {
              super.render(
                  poseStack,
                  buffer,
                  packedLight,
                  entity,
                  limbSwing,
                  limbSwingAmount,
                  partialTicks,
                  ageInTicks,
                  netHeadYaw,
                  headPitch);
            }
          }
        });
  }

  @Override
  protected float getFlipDegrees(AshCrawlerEntity entity) {
    return 180.0F;
  }

  @Override
  protected void scale(AshCrawlerEntity entity, PoseStack poseStack, float partialTicks) {
    // Make the crawler appear more menacing when attacking
    float attackScale = entity.getAttackAnimationScale(partialTicks);
    if (attackScale > 0.0F) {
      float scale = 1.0F + Mth.sin(attackScale * 100.0F) * attackScale * 0.3F;
      poseStack.scale(scale, 1.0F / scale, scale);
    }

    // Shrink when burrowing
    if (entity.isBurrowed()) {
      float progress = entity.getBurrowProgress(partialTicks);
      float scale = 0.5F + 0.5F * (1.0F - progress);
      poseStack.scale(scale, scale, scale);

      // Lower into the ground
      poseStack.translate(0, progress * 0.5, 0);
    }
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(@NotNull AshCrawlerEntity entity) {
    return TEXTURE;
  }
}
