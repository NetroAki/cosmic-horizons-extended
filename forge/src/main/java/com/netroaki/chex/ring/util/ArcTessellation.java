package com.netroaki.chex.ring.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public final class ArcTessellation {
  private ArcTessellation() {}

  public static void buildArc(
      Matrix4f matrix,
      VertexConsumer consumer,
      float radius,
      float thickness,
      float arcDegrees,
      int segments,
      float r,
      float g,
      float b,
      float a) {
    float inner = radius - thickness * 0.5f;
    float outer = radius + thickness * 0.5f;
    float radians = (float) Math.toRadians(arcDegrees);
    float step = radians / (float) Math.max(1, segments);
    float start = -radians * 0.5f;

    for (int i = 0; i < segments; i++) {
      float a0 = start + i * step;
      float a1 = a0 + step;
      float c0 = Mth.cos(a0), s0 = Mth.sin(a0);
      float c1 = Mth.cos(a1), s1 = Mth.sin(a1);

      put(consumer, matrix, outer * c0, outer * s0, 0, r, g, b, a);
      put(consumer, matrix, inner * c0, inner * s0, 0, r, g, b, a);
      put(consumer, matrix, inner * c1, inner * s1, 0, r, g, b, a);
      put(consumer, matrix, outer * c1, outer * s1, 0, r, g, b, a);
    }
  }

  private static void put(
      VertexConsumer v, Matrix4f m, float x, float y, float z, float r, float g, float b, float a) {
    v.vertex(m, x, y, z)
        .color(r, g, b, a)
        .uv(0.0f, 0.0f)
        .overlayCoords(0)
        .uv2(0xF000F0)
        .normal(0, 0, 1)
        .endVertex();
  }
}
