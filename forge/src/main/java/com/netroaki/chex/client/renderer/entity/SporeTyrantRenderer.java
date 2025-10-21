package com.netroaki.chex.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.model.entity.SporeTyrantModel;
import com.netroaki.chex.entity.boss.SporeTyrantEntity;
import mod.azure.azurelib.renderer.GeoLayerRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SporeTyrantRenderer
    extends MobRenderer<SporeTyrantEntity, SporeTyrantModel<SporeTyrantEntity>> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/entity/spore_tyrant.png");
  private static final ResourceLocation GLOW_LAYER =
      new ResourceLocation(CHEX.MOD_ID, "textures/entity/spore_tyrant_glow.png");

  public SporeTyrantRenderer(EntityRendererProvider.Context context) {
    super(
        context, new SporeTyrantModel<>(context.bakeLayer(SporeTyrantModel.LAYER_LOCATION)), 1.0F);
    this.addLayer(new SporeTyrantGlowLayer<>(this));
  }

  @Override
  public void render(
      SporeTyrantEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    // Scale the boss based on phase
    float scale = 1.0F;
    if (entity.getPhase() == 2) {
      scale = 1.2F;
    } else if (entity.getPhase() >= 3) {
      scale = 1.4F;
    }

    poseStack.pushPose();
    poseStack.scale(scale, scale, scale);

    // Add floating animation
    float bob = Mth.sin((entity.tickCount + partialTicks) * 0.1F) * 0.1F + 0.1F;
    poseStack.translate(0.0D, bob, 0.0D);

    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    poseStack.popPose();
  }

  @Override
  protected void scale(SporeTyrantEntity entity, PoseStack poseStack, float partialTickTime) {
    // Scale the model
    float scale = 1.0F;
    if (entity.getPhase() == 2) {
      scale = 1.2F;
    } else if (entity.getPhase() >= 3) {
      scale = 1.4F;
    }
    poseStack.scale(scale, scale, scale);
  }

  @Override
  public ResourceLocation getTextureLocation(SporeTyrantEntity entity) {
    return TEXTURE;
  }

  // Glow layer for the Spore Tyrant
  private static class SporeTyrantGlowLayer<T extends SporeTyrantEntity>
      extends GeoLayerRenderer<T> {
    private static final RenderType GLOW = RenderType.eyes(GLOW_LAYER);
    private final SporeTyrantModel<T> model;

    public SporeTyrantGlowLayer(SporeTyrantRenderer renderer) {
      super(renderer);
      this.model = renderer.getModel();
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
      // Only render glow if not in a phase transition
      if (entity.phaseTransitionTicks <= 0) {
        // Pulsing glow effect based on phase
        float pulse =
            (Mth.sin((entity.tickCount + partialTicks) * 0.1F) * 0.1F + 0.9F) * 0.5F + 0.5F;

        // More intense glow in later phases
        if (entity.getPhase() >= 2) {
          pulse *= 1.2F;
        }
        if (entity.getPhase() >= 3) {
          pulse *= 1.2F;
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(GLOW);
        this.getRenderer()
            .render(
                this.model,
                entity,
                partialTicks,
                GLOW,
                poseStack,
                buffer,
                vertexConsumer,
                15728640, // Full brightness
                OverlayTexture.NO_OVERLAY,
                pulse,
                pulse,
                pulse,
                1.0F);
      }
    }
  }
}
