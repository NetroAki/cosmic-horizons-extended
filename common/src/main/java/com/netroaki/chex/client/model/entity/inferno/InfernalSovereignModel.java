package com.netroaki.chex.client.model.entity.inferno;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.entity.inferno.InfernalSovereignEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class InfernalSovereignModel extends EntityModel<InfernalSovereignEntity> {
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("chex", "infernal_sovereign"), "main");

  private final ModelPart body;
  private final ModelPart head;
  private final ModelPart rightArm;
  private final ModelPart leftArm;
  private final ModelPart rightLeg;
  private final ModelPart leftLeg;
  private final ModelPart wingLeft;
  private final ModelPart wingRight;
  private final ModelPart[] horns = new ModelPart[4];

  public InfernalSovereignModel(ModelPart root) {
    this.body = root.getChild("body");
    this.head = body.getChild("head");
    this.rightArm = body.getChild("right_arm");
    this.leftArm = body.getChild("left_arm");
    this.rightLeg = body.getChild("right_leg");
    this.leftLeg = body.getChild("left_leg");
    this.wingLeft = body.getChild("wing_left");
    this.wingRight = body.getChild("wing_right");

    // Initialize horns
    for (int i = 0; i < horns.length; i++) {
      this.horns[i] = head.getChild("horn_" + i);
    }
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body =
        partdefinition.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 12.0F, 0.0F));

    PartDefinition head =
        body.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -12.0F, 0.0F));

    // Add horns
    head.addOrReplaceChild(
        "horn_0",
        CubeListBuilder.create()
            .texOffs(24, 0)
            .addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-3.0F, -6.0F, 0.0F, -0.5236F, -0.5236F, 0.0F));

    head.addOrReplaceChild(
        "horn_1",
        CubeListBuilder.create()
            .texOffs(24, 0)
            .addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(3.0F, -6.0F, 0.0F, -0.5236F, 0.5236F, 0.0F));

    head.addOrReplaceChild(
        "horn_2",
        CubeListBuilder.create()
            .texOffs(24, 0)
            .addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
        PartPose.offset(-4.0F, -4.0F, 0.0F));

    head.addOrReplaceChild(
        "horn_3",
        CubeListBuilder.create()
            .texOffs(24, 0)
            .addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
        PartPose.offset(4.0F, -4.0F, 0.0F));

    body.addOrReplaceChild(
        "right_arm",
        CubeListBuilder.create()
            .texOffs(16, 16)
            .addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(-5.0F, -10.0F, 0.0F));

    body.addOrReplaceChild(
        "left_arm",
        CubeListBuilder.create()
            .mirror()
            .texOffs(16, 16)
            .addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offset(5.0F, -10.0F, 0.0F));

    body.addOrReplaceChild(
        "right_leg",
        CubeListBuilder.create()
            .texOffs(0, 16)
            .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(-2.0F, 0.0F, 0.0F));

    body.addOrReplaceChild(
        "left_leg",
        CubeListBuilder.create()
            .mirror()
            .texOffs(0, 16)
            .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offset(2.0F, 0.0F, 0.0F));

    // Add wings
    body.addOrReplaceChild(
        "wing_left",
        CubeListBuilder.create()
            .texOffs(32, 0)
            .addBox(0.0F, -6.0F, 0.0F, 16.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offset(4.0F, -10.0F, 2.0F));

    body.addOrReplaceChild(
        "wing_right",
        CubeListBuilder.create()
            .texOffs(32, 0)
            .mirror()
            .addBox(-16.0F, -6.0F, 0.0F, 16.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offset(-4.0F, -10.0F, 2.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  @Override
  public void setupAnim(
      InfernalSovereignEntity entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Head movement
    this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
    this.head.xRot = headPitch * ((float) Math.PI / 180F);

    // Arm and leg movement
    this.rightArm.xRot =
        Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
    this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
    this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

    // Wing flapping
    float wingAngle = Mth.cos(ageInTicks * 0.3F) * 0.4F;
    this.wingLeft.zRot = wingAngle + 0.2F;
    this.wingRight.zRot = -wingAngle - 0.2F;

    // Horn movement (subtle)
    for (int i = 0; i < horns.length; i++) {
      horns[i].xRot = Mth.cos(ageInTicks * 0.2F + i) * 0.1F;
    }

    // Phase-based animations
    if (entity.getPhase() >= 2) {
      // More aggressive animations in later phases
      float phaseFactor = entity.getPhase() == 3 ? 2.0F : 1.5F;
      this.rightArm.xRot *= phaseFactor;
      this.leftArm.xRot *= phaseFactor;
      this.wingLeft.zRot *= 1.5F;
      this.wingRight.zRot *= 1.5F;
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
