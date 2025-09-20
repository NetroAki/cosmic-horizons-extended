package com.netroaki.chex.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = com.netroaki.chex.CHEX.MOD_ID, value = Dist.CLIENT)
public final class RingParallaxRenderer {
    private static final ResourceLocation RING_TEX = ResourceLocation.fromNamespaceAndPath(
            com.netroaki.chex.CHEX.MOD_ID, "textures/sky/ring_arc.png");

    @SubscribeEvent
    public static void onRenderSky(RenderLevelStageEvent e) {
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY)
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;
        if (!mc.level.dimension().location().equals(ResourceLocation.fromNamespaceAndPath(
                com.netroaki.chex.CHEX.MOD_ID, "aurelia_ringworld")))
            return;

        PoseStack ps = e.getPoseStack();
        Camera cam = mc.gameRenderer.getMainCamera();
        float yaw = cam.getYRot();
        float pitch = cam.getXRot();
        float uShift = yaw * 0.0015f;
        float vShift = pitch * 0.0010f;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, RING_TEX);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tesselator t = Tesselator.getInstance();
        BufferBuilder b = t.getBuilder();

        ps.pushPose();
        ps.mulPose(Axis.XP.rotationDegrees(0));

        float S = 3000f;
        b.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v(b, ps, -S, S, -S, 0.0f + uShift, 1.0f + vShift);
        v(b, ps, S, S, -S, 1.0f + uShift, 1.0f + vShift);
        v(b, ps, S, -S, -S, 1.0f + uShift, 0.0f + vShift);
        v(b, ps, -S, -S, -S, 0.0f + uShift, 0.0f + vShift);
        t.end();

        RenderSystem.setShaderTexture(0, RING_TEX);
        b.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        float u2 = yaw * 0.0008f, v2 = pitch * 0.0005f;
        float S2 = 4000f;
        v(b, ps, -S2, S2, -S2, 0.1f + u2, 1.1f + v2);
        v(b, ps, S2, S2, -S2, 1.1f + u2, 1.1f + v2);
        v(b, ps, S2, -S2, -S2, 1.1f + u2, 0.1f + v2);
        v(b, ps, -S2, -S2, -S2, 0.1f + u2, 0.1f + v2);
        t.end();

        RenderSystem.disableBlend();
        ps.popPose();
    }

    private static void v(BufferBuilder b, PoseStack ps, float x, float y, float z, float u, float v) {
        var mat = ps.last().pose();
        b.vertex(mat, x, y, z).uv(u, v).endVertex();
    }
}
