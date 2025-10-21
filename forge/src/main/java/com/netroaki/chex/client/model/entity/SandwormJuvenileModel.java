package com.netroaki.chex.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.arrakis.SandwormJuvenileEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SandwormJuvenileModel<T extends SandwormJuvenileEntity> extends EntityModel<T> {
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation(CHEX.MOD_ID, "sandworm_juvenile"), "main");

  private final ModelPart body;
  private final ModelPart[] segments = new ModelPart[5];
  private final ModelPart tail;

  public SandwormJuvenileModel(ModelPart root) {
    this.body = root.getChild("body");

    // Initialize segments
    for (int i = 0; i < segments.length; i++) {
      segments[i] = body.getChild("segment" + i);
    }

    this.tail = body.getChild("tail");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body =
        partdefinition.addOrReplaceChild(
            "body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

    // Main body segments
    for (int i = 0; i < 5; i++) {
      body.addOrReplaceChild(
          "segment" + i,
          CubeListBuilder.create()
              .texOffs(0, i * 8)
              .addBox(-2.0F, -3.0F, -8.0F + i * 4, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)),
          PartPose.offset(0.0F, 0.0F, 0.0F));
    }

    // Tail segment
    body.addOrReplaceChild(
        "tail",
        CubeListBuilder.create()
            .texOffs(0, 40)
            .addBox(-1.5F, -2.5F, 0.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, 0.0F, 12.0F));

    return LayerDefinition.create(meshdefinition, 32, 64);
  }

  @Override
  public void setupAnim(
      T entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Animate body segments with wave motion
    for (int i = 0; i < segments.length; i++) {
      float offset = (ageInTicks * 0.5F) + (i * 0.5F);
      float rotation = Mth.cos(offset) * 0.15F * limbSwingAmount;
      segments[i].yRot = rotation;

      // Add slight vertical movement
      segments[i].y = Mth.sin(ageInTicks * 0.1F + i * 0.2F) * 0.1F;
    }

    // Animate tail
    tail.yRot = Mth.cos(ageInTicks * 0.3F) * 0.2F * limbSwingAmount;

    // Handle burrowing animation
    if (entity.isBurrowed()) {
      float progress = Math.min(1.0F, entity.getBurrowTicks() / 20.0F);
      this.body.y = 24.0F + progress * 2.0F;

      for (ModelPart segment : segments) {
        segment.visible = progress < 0.5F;
      }
      tail.visible = progress < 0.5F;
    } else {
      this.body.y = 24.0F;
      for (ModelPart segment : segments) {
        segment.visible = true;
      }
      tail.visible = true;
    }
  }

  @Override
  public void renderToBuffer(
      PoseStack poseStack,
      VertexConsumer vertexConsumer,
      int packedLight,
      int packedOverlay,
      float red,
      float green,
      float blue,
      float alpha) {
    body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
  }
}
