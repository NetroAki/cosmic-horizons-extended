package com.netroaki.chex.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.boss.SporeTyrantEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SporeTyrantModel<T extends SporeTyrantEntity> extends EntityModel<T> {
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation(CHEX.MOD_ID, "spore_tyrant"), "main");

  private final ModelPart body;
  private final ModelPart head;
  private final ModelPart leftArm;
  private final ModelPart rightArm;
  private final ModelPart leftLeg;
  private final ModelPart rightLeg;
  private final ModelPart[] tendrils = new ModelPart[6];
  private final ModelPart[] spores = new ModelPart[8];

  public SporeTyrantModel(ModelPart root) {
    this.body = root.getChild("body");
    this.head = this.body.getChild("head");
    this.leftArm = this.body.getChild("left_arm");
    this.rightArm = this.body.getChild("right_arm");
    this.leftLeg = this.body.getChild("left_leg");
    this.rightLeg = this.body.getChild("right_leg");

    // Initialize tendrils
    for (int i = 0; i < this.tendrils.length; i++) {
      this.tendrils[i] = this.body.getChild("tendril_" + i);
    }

    // Initialize floating spores
    for (int i = 0; i < this.spores.length; i++) {
      this.spores[i] = this.body.getChild("spore_" + i);
    }
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
                .addBox(-8.0F, -12.0F, -4.0F, 16.0F, 24.0F, 8.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 0.0F, 0.0F));

    // Head
    body.addOrReplaceChild(
        "head",
        CubeListBuilder.create()
            .texOffs(0, 32)
            .addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, -12.0F, 0.0F));

    // Arms
    body.addOrReplaceChild(
        "left_arm",
        CubeListBuilder.create()
            .texOffs(48, 32)
            .addBox(-1.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(8.0F, -8.0F, 0.0F));

    body.addOrReplaceChild(
        "right_arm",
        CubeListBuilder.create()
            .mirror()
            .texOffs(48, 32)
            .addBox(-3.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(-8.0F, -8.0F, 0.0F));

    // Legs
    body.addOrReplaceChild(
        "left_leg",
        CubeListBuilder.create()
            .texOffs(0, 56)
            .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(4.0F, 12.0F, 0.0F));

    body.addOrReplaceChild(
        "right_leg",
        CubeListBuilder.create()
            .texOffs(0, 56)
            .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offset(-4.0F, 12.0F, 0.0F));

    // Tendrils (floating appendages)
    for (int i = 0; i < 6; i++) {
      float angle = (float) (i * (Math.PI * 2 / 6));
      float x = Mth.cos(angle) * 8.0F;
      float z = Mth.sin(angle) * 8.0F;

      body.addOrReplaceChild(
          "tendril_" + i,
          CubeListBuilder.create()
              .texOffs(64, 0)
              .addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)),
          PartPose.offsetAndRotation(x, 0.0F, z, 0.0F, angle, 0.0F));
    }

    // Floating spores
    for (int i = 0; i < 8; i++) {
      float angle = (float) (i * (Math.PI * 2 / 8));
      float x = Mth.cos(angle) * 12.0F;
      float y = -8.0F + (i % 3) * 8.0F;
      float z = Mth.sin(angle) * 12.0F;

      body.addOrReplaceChild(
          "spore_" + i,
          CubeListBuilder.create()
              .texOffs(32, 56)
              .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
          PartPose.offset(x, y, z));
    }

    return LayerDefinition.create(meshdefinition, 128, 128);
  }

  @Override
  public void setupAnim(
      T entity,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch) {
    // Head movement
    this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
    this.head.xRot = headPitch * ((float) Math.PI / 180F);

    // Arm swing animation
    float f = Mth.sin(ageInTicks * 0.1F) * 0.1F + 0.1F;
    this.leftArm.xRot = (float) (Math.PI * 0.25F) + f;
    this.rightArm.xRot = (float) (Math.PI * 0.25F) - f;

    // Leg movement
    this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

    // Tendril movement
    for (int i = 0; i < this.tendrils.length; i++) {
      float angle = ageInTicks * 0.1F + (i * 0.5F);
      this.tendrils[i].yRot = angle % ((float) Math.PI * 2F);
      this.tendrils[i].xRot = Mth.sin(ageInTicks * 0.1F + i) * 0.1F;
    }

    // Floating spore movement
    for (int i = 0; i < this.spores.length; i++) {
      float offset = ageInTicks * 0.05F + (i * 0.5F);
      this.spores[i].y = -8.0F + (i % 3) * 8.0F + Mth.sin(offset) * 0.5F;
      this.spores[i].yRot = offset;
      this.spores[i].xRot = offset * 0.5F;
      this.spores[i].zRot = offset * 0.3F;
    }

    // Phase-based scaling
    float phaseScale = 1.0F;
    if (entity.getPhase() == 2) {
      phaseScale = 1.2F;
    } else if (entity.getPhase() >= 3) {
      phaseScale = 1.4F;
    }

    this.body.xScale = phaseScale;
    this.body.yScale = phaseScale;
    this.body.zScale = phaseScale;
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
}
