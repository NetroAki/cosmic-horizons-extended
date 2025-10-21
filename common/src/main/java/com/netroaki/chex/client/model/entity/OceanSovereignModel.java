package com.netroaki.chex.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.entity.aqua_mundus.OceanSovereignEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class OceanSovereignModel extends EntityModel<OceanSovereignEntity> {
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("chex", "ocean_sovereign"), "main");

  private final ModelPart body;
  private final ModelPart[] heads;
  private final ModelPart tail;
  private final ModelPart[] tentacles;

  public OceanSovereignModel(ModelPart root) {
    this.body = root.getChild("body");

    // Initialize heads (main head + 2 additional heads)
    this.heads = new ModelPart[3];
    this.heads[0] = this.body.getChild("head");
    this.heads[1] = this.body.getChild("head_1");
    this.heads[2] = this.body.getChild("head_2");

    this.tail = this.body.getChild("tail");

    // Initialize tentacles (8 tentacles)
    this.tentacles = new ModelPart[8];
    for (int i = 0; i < this.tentacles.length; i++) {
      this.tentacles[i] = this.body.getChild("tentacle_" + i);
    }
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    // Main body
    PartDefinition body =
        partdefinition.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 16.0F, 0.0F));

    // Main head
    body.addOrReplaceChild(
        "head",
        CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 6.0F),
        PartPose.offset(0.0F, -4.0F, -4.0F));

    // Additional heads
    body.addOrReplaceChild(
        "head_1",
        CubeListBuilder.create().texOffs(32, 12).addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F),
        PartPose.offset(5.0F, -3.0F, 2.0F));

    body.addOrReplaceChild(
        "head_2",
        CubeListBuilder.create()
            .texOffs(32, 12)
            .mirror()
            .addBox(-2.5F, -2.5F, -5.0F, 5.0F, 5.0F, 5.0F),
        PartPose.offset(-5.0F, -3.0F, 2.0F));

    // Tail
    body.addOrReplaceChild(
        "tail",
        CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.0F),
        PartPose.offset(0.0F, -2.0F, 4.0F));

    // Tentacles (8 tentacles in a circle)
    for (int i = 0; i < 8; i++) {
      float angle = (float) (i * (Math.PI * 2 / 8));
      float x = Mth.cos(angle) * 3.0F;
      float z = Mth.sin(angle) * 3.0F;

      body.addOrReplaceChild(
          "tentacle_" + i,
          CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
          PartPose.offset(x, 0.0F, z));
    }

    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  @Override
  public void setupAnim(
      OceanSovereignEntity entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Body rotation
    this.body.yRot = netHeadYaw * ((float) Math.PI / 180F);
    this.body.xRot = headPitch * ((float) Math.PI / 180F);

    // Head rotations (main head looks at target)
    if (entity.getTarget() != null) {
      this.heads[0].yRot = netHeadYaw * ((float) Math.PI / 180F);
      this.heads[0].xRot = headPitch * ((float) Math.PI / 180F);
    } else {
      this.heads[0].yRot = 0.0F;
      this.heads[0].xRot = 0.0F;
    }

    // Additional heads sway gently
    float time = ageInTicks * 0.1F;
    this.heads[1].yRot = Mth.cos(time) * 0.5F;
    this.heads[1].xRot = Mth.sin(time * 0.5F) * 0.5F;

    this.heads[2].yRot = Mth.sin(time) * 0.5F;
    this.heads[2].xRot = -Mth.cos(time * 0.5F) * 0.5F;

    // Tail movement
    this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
    this.tail.xRot = Mth.sin(ageInTicks * 0.05F) * 0.1F;

    // Tentacle movement
    for (int i = 0; i < this.tentacles.length; i++) {
      float tentacleTime = ageInTicks * 0.1F + i * 0.25F;
      this.tentacles[i].xRot = Mth.sin(tentacleTime) * 0.3F;
      this.tentacles[i].yRot =
          (float) (i * (Math.PI * 2 / 8)) + Mth.cos(tentacleTime * 0.5F) * 0.2F;
    }

    // Apply tentacle movement from entity
    float tentacleMovement = entity.getTentacleMovement(1.0F);
    for (ModelPart tentacle : this.tentacles) {
      tentacle.xRot += tentacleMovement * 0.5F;
    }
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
    // Render all parts
    this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

    // If we want to render additional effects, we can do it here
    if (this.young) {
      poseStack.pushPose();
      poseStack.scale(0.5F, 0.5F, 0.5F);
      poseStack.translate(0.0D, 1.5D, 0.0D);
      this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
      poseStack.popPose();
    }
  }
}
