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

public class GlowbeastModel<T extends Entity> extends EntityModel<T> {
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("chex", "glowbeast"), "main");

  private final ModelPart body;
  private final ModelPart head;
  private final ModelPart tail;
  private final ModelPart leftWing;
  private final ModelPart rightWing;

  public GlowbeastModel(ModelPart root) {
    this.body = root.getChild("body");
    this.head = this.body.getChild("head");
    this.tail = this.body.getChild("tail");
    this.leftWing = this.body.getChild("left_wing");
    this.rightWing = this.body.getChild("right_wing");
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
                .addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 16.0F, 0.0F));

    // Head
    body.addOrReplaceChild(
        "head",
        CubeListBuilder.create()
            .texOffs(0, 16)
            .addBox(-2.0F, -3.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
            .texOffs(16, 16)
            .addBox(-1.5F, -1.0F, -6.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, -2.0F, -3.0F));

    // Tail
    body.addOrReplaceChild(
        "tail",
        CubeListBuilder.create()
            .texOffs(24, 0)
            .addBox(-1.5F, -1.0F, 0.0F, 3.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, -1.0F, 3.0F));

    // Left Wing
    body.addOrReplaceChild(
        "left_wing",
        CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0F, -1.0F, -1.5F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offset(3.0F, 0.0F, 0.0F));

    // Right Wing
    body.addOrReplaceChild(
        "right_wing",
        CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-8.0F, -1.0F, -1.5F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
            .mirror(false),
        PartPose.offset(-3.0F, 0.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 64, 64);
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
    this.head.xRot = headPitch * ((float) Math.PI / 180F);
    this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);

    // Wing flapping
    float wingFlap = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.8F * limbSwingAmount;
    this.leftWing.zRot = wingFlap;
    this.rightWing.zRot = -wingFlap;

    // Tail movement
    this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;

    // Body bobbing when moving
    this.body.y = 16.0F + Mth.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
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
