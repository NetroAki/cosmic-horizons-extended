package com.netroaki.chex.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.arrakis.SandEmperorEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SandEmperorModel<T extends SandEmperorEntity> extends EntityModel<T> {
  public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation(CHEX.MOD_ID, "sand_emperor"), "main");

  private final ModelPart body;
  private final ModelPart head;
  private final ModelPart[] bodySegments = new ModelPart[7];
  private final ModelPart tail;
  private final ModelPart[] mandibles = new ModelPart[4];

  public SandEmperorModel(ModelPart root) {
    this.body = root.getChild("body");
    this.head = body.getChild("head");

    // Initialize body segments
    for (int i = 0; i < bodySegments.length; i++) {
      bodySegments[i] = body.getChild("segment" + i);
    }

    this.tail = body.getChild("tail");

    // Initialize mandibles
    for (int i = 0; i < mandibles.length; i++) {
      mandibles[i] = head.getChild("mandible" + i);
    }
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition body =
        partdefinition.addOrReplaceChild(
            "body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

    // Head
    PartDefinition head =
        body.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -4.0F, -12.0F, 8.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 0.0F, -16.0F));

    // Add mandibles
    for (int i = 0; i < 4; i++) {
      head.addOrReplaceChild(
          "mandible" + i,
          CubeListBuilder.create()
              .texOffs(40 + i * 6, 0)
              .addBox(-1.0F, 0.0F, -6.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)),
          PartPose.offsetAndRotation(
              (i % 2 == 0 ? -2.0F : 2.0F) * (i < 2 ? 1 : -1),
              2.0F * (i < 2 ? 1 : -1),
              -10.0F,
              Mth.PI / 6 * (i < 2 ? 1 : -1),
              0.0F,
              Mth.PI / 4 * (i % 2 == 0 ? -1 : 1)));
    }

    // Main body segments (larger and more detailed than juvenile)
    for (int i = 0; i < 7; i++) {
      body.addOrReplaceChild(
          "segment" + i,
          CubeListBuilder.create()
              .texOffs(0, 20 + i * 10)
              .addBox(-3.5F, -4.5F, -10.0F + i * 5, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)),
          PartPose.offset(0.0F, 0.0F, 0.0F));
    }

    // Tail segment
    body.addOrReplaceChild(
        "tail",
        CubeListBuilder.create()
            .texOffs(0, 90)
            .addBox(-2.5F, -3.5F, 0.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, 0.0F, 25.0F));

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

    // Animate mandibles
    float mandibleMovement = Mth.sin(ageInTicks * 0.3F) * 0.2F;
    for (int i = 0; i < mandibles.length; i++) {
      mandibles[i].xRot = (Mth.PI / 6 * (i < 2 ? 1 : -1)) + mandibleMovement;

      // Add slight twitching
      if (entity.getRandom().nextFloat() < 0.05F) {
        mandibles[i].zRot += (entity.getRandom().nextFloat() - 0.5F) * 0.1F;
      }
    }

    // Animate body segments with wave motion
    for (int i = 0; i < bodySegments.length; i++) {
      float offset = (ageInTicks * 0.4F) + (i * 0.5F);
      float rotation = Mth.cos(offset) * 0.1F * limbSwingAmount;
      bodySegments[i].yRot = rotation;

      // Add vertical movement that decreases towards the tail
      float verticalMovement =
          Mth.sin(ageInTicks * 0.1F + i * 0.2F) * 0.1F * (1.0F - (i / (float) bodySegments.length));
      bodySegments[i].y = verticalMovement;

      // Add scale variation for breathing effect
      float scale = 1.0F + Mth.sin(ageInTicks * 0.1F + i * 0.2F) * 0.02F;
      bodySegments[i].xScale = scale;
      bodySegments[i].zScale = scale;
    }

    // Animate tail with a more fluid motion
    tail.yRot = Mth.cos(ageInTicks * 0.2F) * 0.1F * limbSwingAmount;
    tail.xRot = Mth.sin(ageInTicks * 0.15F) * 0.05F;

    // Handle burrowing animation
    if (entity.isBurrowing() || entity.isBurrowed()) {
      float progress = entity.getBurrowProgress(ageInTicks);
      this.body.y = 24.0F + progress * 2.0F;

      // Hide segments during burrow
      for (ModelPart segment : bodySegments) {
        segment.visible = progress < 0.5F;
      }
      tail.visible = progress < 0.5F;
    } else {
      this.body.y = 24.0F;
      for (ModelPart segment : bodySegments) {
        segment.visible = true;
      }
      tail.visible = true;
    }

    // Phase transition effects
    if (entity.getPhaseTransitionTicks() > 0) {
      float phaseProgress = entity.getPhaseTransitionProgress();
      // Add pulsing effect during phase transition
      float pulse = Mth.sin(ageInTicks * 0.5F) * 0.1F * phaseProgress;
      for (ModelPart segment : bodySegments) {
        segment.xScale += pulse;
        segment.zScale += pulse;
      }
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
