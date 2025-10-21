package com.netroaki.chex.client.entity.renderer.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.entity.inferno.FireWraithEntity;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FireWraithRenderer
    extends MobRenderer<FireWraithEntity, BlazeModel<FireWraithEntity>> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/fire_wraith.png");
  private static final ResourceLocation EMISSIVE_TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/fire_wraith_emissive.png");

  public FireWraithRenderer(EntityRendererProvider.Context context) {
    super(context, new BlazeModel<>(context.bakeLayer(ModelLayers.BLAZE)), 0.5F);
    this.addLayer(
        new InfernoEmissiveLayer<>(this, EMISSIVE_TEXTURE) {
          @Override
          public void render(
              @NotNull PoseStack poseStack,
              @NotNull MultiBufferSource buffer,
              int packedLight,
              @NotNull FireWraithEntity entity,
              float limbSwing,
              float limbSwingAmount,
              float partialTicks,
              float ageInTicks,
              float netHeadYaw,
              float headPitch) {
            // Pulsing emissive effect
            float pulse = (Mth.sin(ageInTicks * 0.1F) * 0.5F + 0.5F) * 0.4F + 0.6F;
            poseStack.pushPose();
            poseStack.scale(pulse, pulse, pulse);
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
            poseStack.popPose();
          }
        });
  }

  @Override
  protected void scale(FireWraithEntity entity, PoseStack poseStack, float partialTicks) {
    // Slightly larger than a blaze
    float scale = 1.2F;
    poseStack.scale(scale, scale, scale);

    // Bob up and down
    float yOffset = Mth.sin(entity.tickCount * 0.1F) * 0.1F;
    poseStack.translate(0, yOffset, 0);

    // Sway side to side
    if (!entity.onGround()) {
      float sway = Mth.sin(entity.tickCount * 0.3F) * 0.1F;
      poseStack.mulPose(Axis.YP.rotation(sway));
    }
  }

  @Override
  public void render(
      FireWraithEntity entity,
      float entityYaw,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    // Add flame particles when moving or attacking
    if (entity.getDeltaMovement().lengthSqr() > 0.01
        || entity.getAttackAnimationScale(partialTicks) > 0) {
      for (int i = 0; i < 2; ++i) {
        double dx = (entity.getRandom().nextDouble() - 0.5) * 0.5;
        double dy = (entity.getRandom().nextDouble() - 0.5) * 0.5;
        double dz = (entity.getRandom().nextDouble() - 0.5) * 0.5;

        entity
            .level()
            .addParticle(
                ParticleTypes.FLAME,
                entity.getX() + dx,
                entity.getY() + entity.getBbHeight() * 0.5 + dy,
                entity.getZ() + dz,
                0,
                0,
                0);
      }
    }
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(@NotNull FireWraithEntity entity) {
    return TEXTURE;
  }
}
