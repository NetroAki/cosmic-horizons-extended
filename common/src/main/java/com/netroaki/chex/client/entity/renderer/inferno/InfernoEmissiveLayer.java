package com.netroaki.chex.client.entity.renderer.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Base emissive layer for Inferno Prime entities. Handles the glowing/emissive effect for eyes and
 * other glowing parts.
 */
public class InfernoEmissiveLayer<T extends LivingEntity, M extends EntityModel<T>>
    extends EyesLayer<T, M> {
  private final RenderType renderType;

  public InfernoEmissiveLayer(RenderLayerParent<T, M> parent, ResourceLocation texture) {
    super(parent);
    this.renderType = RenderType.eyes(texture);
  }

  @Override
  public void render(
      @NotNull PoseStack poseStack,
      @NotNull MultiBufferSource buffer,
      int packedLight,
      @NotNull T entity,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Only render if the entity is alive and not invisible
    if (entity.isAlive() && !entity.isInvisible()) {
      VertexConsumer vertexConsumer = buffer.getBuffer(this.renderType());
      this.getParentModel()
          .renderToBuffer(
              poseStack,
              vertexConsumer,
              15728640, // Full brightness for emissive effect
              OverlayTexture.NO_OVERLAY,
              1.0F,
              1.0F,
              1.0F,
              1.0F);
    }
  }

  @Override
  public @NotNull RenderType renderType() {
    return this.renderType;
  }
}
