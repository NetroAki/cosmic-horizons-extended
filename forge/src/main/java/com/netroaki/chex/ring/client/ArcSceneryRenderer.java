package com.netroaki.chex.ring.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.ring.blockentity.ArcSceneryBlockEntity;
import com.netroaki.chex.ring.util.ArcTessellation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class ArcSceneryRenderer implements BlockEntityRenderer<ArcSceneryBlockEntity> {
  private static final ResourceLocation DEFAULT_TEX =
      new ResourceLocation("cosmic_horizons_extended", "textures/ring/arc_segment.png");

  public ArcSceneryRenderer(BlockEntityRendererProvider.Context ctx) {}

  @Override
  public void render(
      ArcSceneryBlockEntity be,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    Level level = be.getLevel();
    if (level == null) return;

    Minecraft mc = Minecraft.getInstance();
    Camera cam = mc.gameRenderer.getMainCamera();
    Vec3 camPos = cam.getPosition();
    Vec3 world = Vec3.atCenterOf(be.getBlockPos());
    double d = camPos.distanceTo(world);

    double start = be.fadeStart;
    double end = be.fadeEnd;
    if (d <= start) return;
    float t = (float) Mth.clamp((d - start) / (end - start), 0.0, 1.0);
    float alpha = t;

    int argb = be.colorARGB;
    float a = ((argb >>> 24) & 0xFF) / 255f * alpha;
    if (a <= 0.01f) return;
    float r = ((argb >>> 16) & 0xFF) / 255f;
    float g = ((argb >>> 8) & 0xFF) / 255f;
    float b = (argb & 0xFF) / 255f;

    poseStack.pushPose();
    poseStack.translate(world.x - camPos.x, world.y - camPos.y, world.z - camPos.z);
    // Apply yaw (Y axis) then tilt (X axis)
    poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(be.yawDeg));
    poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(be.tiltDeg));

    var tex = be.texture != null ? be.texture : DEFAULT_TEX;
    RenderType type = RenderType.entityTranslucent(tex);
    RenderSystem.enableBlend();
    RenderSystem.depthMask(false);

    VertexConsumer vc = buffer.getBuffer(type);
    Matrix4f mat = poseStack.last().pose();
    ArcTessellation.buildArc(
        mat,
        vc,
        be.radius,
        be.thickness,
        be.arcAngleDeg,
        Math.max(12, (int) (be.arcAngleDeg / 2)),
        r,
        g,
        b,
        a);

    RenderSystem.depthMask(true);
    RenderSystem.disableBlend();
    poseStack.popPose();
  }

  @Override
  public boolean shouldRenderOffScreen(ArcSceneryBlockEntity be) {
    return true;
  }

  @Override
  public int getViewDistance() {
    return 512;
  }
}
