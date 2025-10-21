package com.netroaki.chex.client.entity.renderer.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.entity.inferno.MagmaHopperEntity;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class MagmaHopperRenderer
    extends MobRenderer<MagmaHopperEntity, LavaSlimeModel<MagmaHopperEntity>> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/magma_hopper.png");
  private static final ResourceLocation EMISSIVE_TEXTURE =
      new ResourceLocation(
          CosmicHorizonsExpanded.MOD_ID, "textures/entity/inferno/magma_hopper_emissive.png");

  public MagmaHopperRenderer(EntityRendererProvider.Context context) {
    super(context, new LavaSlimeModel<>(context.bakeLayer(ModelLayers.MAGMA_CUBE)), 0.25F);
    this.addLayer(
        new InfernoEmissiveLayer<>(this, EMISSIVE_TEXTURE) {
          @Override
          public void render(
              @NotNull PoseStack poseStack,
              @NotNull MultiBufferSource buffer,
              int packedLight,
              @NotNull MagmaHopperEntity entity,
              float limbSwing,
              float limbSwingAmount,
              float partialTicks,
              float ageInTicks,
              float netHeadYaw,
              float headPitch) {
            // Pulsing emissive effect that intensifies before jumping
            float pulse = 0.7F + (float) Math.sin(ageInTicks * 0.2F) * 0.3F;
            if (entity.getJumpCooldown() < 10) {
              pulse += (10 - entity.getJumpCooldown()) * 0.1F;
            }
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
  protected void scale(MagmaHopperEntity entity, PoseStack poseStack, float partialTicks) {
    // Slightly smaller than a magma cube
    float scale = 0.8F;

    // Squish when landing
    if (entity.isOnGround() && !entity.wasOnGround) {
      float squish = 0.8F;
      poseStack.scale(1.0F / (squish * squish), squish, 1.0F / (squish * squish));
    }

    // Wobble effect when preparing to jump
    if (entity.getJumpCooldown() < 20) {
      float intensity = (20 - entity.getJumpCooldown() + partialTicks) / 20.0F;
      float wobble = Mth.sin(intensity * (float) Math.PI) * 0.2F;
      poseStack.scale(1.0F + wobble, 1.0F - wobble * 0.5F, 1.0F + wobble);
    }

    poseStack.scale(scale, scale, scale);

    // Tilt forward when moving
    if (entity.getDeltaMovement().lengthSqr() > 0.01) {
      Vec3 motion = entity.getDeltaMovement().normalize();
      float angle = (float) Math.atan2(motion.x, motion.z);
      poseStack.mulPose(Axis.YP.rotation(angle));

      // Tilt forward based on speed
      float speed = (float) motion.length();
      float tilt = Mth.clamp(speed * 10.0F, 0.0F, 0.5F);
      poseStack.mulPose(Axis.XP.rotation(-tilt));
    }
  }

  @Override
  public @NotNull ResourceLocation getTextureLocation(@NotNull MagmaHopperEntity entity) {
    return TEXTURE;
  }
}
