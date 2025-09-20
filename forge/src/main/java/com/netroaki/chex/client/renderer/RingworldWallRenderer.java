package com.netroaki.chex.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import com.netroaki.chex.blocks.RingworldWallBlock;
import com.netroaki.chex.blocks.RingworldWallBlockEntity;

/**
 * Renderer for ringworld wall blocks that displays skybox texture
 */
public class RingworldWallRenderer implements BlockEntityRenderer<RingworldWallBlockEntity> {

    private static final ResourceLocation SKYBOX_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft",
            "textures/environment/end_sky.png");

    public RingworldWallRenderer(BlockEntityRendererProvider.Context context) {
        // Constructor
    }

    @Override
    public void render(RingworldWallBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockState state = blockEntity.getBlockState();
        boolean isOuterWall = state.getValue(RingworldWallBlock.IS_OUTER_WALL);

        // Simple rendering - just render a basic cube for now
        // TODO: Implement proper skybox texture rendering
        poseStack.pushPose();

        // Scale based on wall type
        if (isOuterWall) {
            poseStack.scale(2.0f, 2.0f, 2.0f);
        }

        // Use end portal render type for the skybox effect
        RenderType renderType = RenderType.endPortal();
        VertexConsumer buffer = bufferSource.getBuffer(renderType);

        // Render basic cube
        renderBasicCube(poseStack.last().pose(), buffer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    private void renderBasicCube(Matrix4f matrix, VertexConsumer buffer, int packedLight, int packedOverlay) {
        // Render a simple cube with end portal effect
        // Front face
        buffer.vertex(matrix, 0.0f, 0.0f, 1.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 0.0f, 1.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 1.0f, 1.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 1.0f, 1.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, 1.0f).endVertex();

        // Back face
        buffer.vertex(matrix, 1.0f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 0.0f, -1.0f).endVertex();

        // Left face
        buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(-1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 0.0f, 1.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(-1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 1.0f, 1.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(-1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(-1.0f, 0.0f, 0.0f).endVertex();

        // Right face
        buffer.vertex(matrix, 1.0f, 0.0f, 1.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 1.0f, 1.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(1.0f, 0.0f, 0.0f).endVertex();

        // Top face
        buffer.vertex(matrix, 0.0f, 1.0f, 1.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 1.0f, 1.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, 1.0f, 0.0f).endVertex();

        // Bottom face
        buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 1.0f, 0.0f, 1.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex(matrix, 0.0f, 0.0f, 1.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(packedOverlay)
                .uv2(packedLight).normal(0.0f, -1.0f, 0.0f).endVertex();
    }
}
