package com.netroaki.chex.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class SporeflyModel<T extends Entity> extends EntityModel<T> {
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("chex", "sporefly"), "main");

  private final ModelPart body;
  private final ModelPart leftWing;
  private final ModelPart rightWing;
  private final ModelPart antennaLeft;
  private final ModelPart antennaRight;

  public SporeflyModel(ModelPart root) {
    this.body = root.getChild("body");
    this.leftWing = this.body.getChild("left_wing");
    this.rightWing = this.body.getChild("right_wing");
    this.antennaLeft = this.body.getChild("antenna_left");
    this.antennaRight = this.body.getChild("antenna_right");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    // Body
    PartDefinition body =
        partdefinition.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 21.0F, 0.0F));

    // Left Wing
    body.addOrReplaceChild(
        "left_wing",
        CubeListBuilder.create()
            .texOffs(0, 8)
            .addBox(0.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(1.5F, -0.5F, 0.0F));

    // Right Wing
    body.addOrReplaceChild(
        "right_wing",
        CubeListBuilder.create()
            .texOffs(0, 8)
            .mirror()
            .addBox(-6.0F, -1.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offset(-1.5F, -0.5F, 0.0F));

    // Left Antenna
    body.addOrReplaceChild(
        "antenna_left",
        CubeListBuilder.create()
            .texOffs(14, 0)
            .addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
        PartPose.offset(1.5F, -1.5F, -2.0F));

    // Right Antenna
    body.addOrReplaceChild(
        "antenna_right",
        CubeListBuilder.create()
            .texOffs(14, 0)
            .mirror()
            .addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offset(-1.5F, -1.5F, -2.0F));

    return LayerDefinition.create(meshdefinition, 32, 32);
  }

  @Override
  public void setupAnim(
      T entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Wing flapping animation
    float wingFlap = Mth.cos(ageInTicks * 2.0F) * 0.5F * limbSwingAmount;
    this.leftWing.zRot = wingFlap;
    this.rightWing.zRot = -wingFlap;

    // Antenna movement
    float antennaMove = Mth.cos(ageInTicks * 0.5F) * 0.2F;
    this.antennaLeft.xRot = antennaMove;
    this.antennaRight.xRot = -antennaMove;

    // Body bobbing
    this.body.y = 21.0F + Mth.cos(limbSwing * 0.6662F) * 0.2F * limbSwingAmount;
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
    this.body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
  }

  public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
    modelRenderer.xRot = x;
    modelRenderer.yRot = y;
    modelRenderer.zRot = z;
  }
}
