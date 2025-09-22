package com.netroaki.chex.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.netroaki.chex.world.hazards.AlphaCentauriHazardHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SolarFlareRenderer {
    private static final float[] FLARE_COLORS = new float[4];
    
    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) {
            return;
        }
        
        if (!AlphaCentauriHazardHandler.isFlareActive()) {
            return;
        }
        
        float intensity = AlphaCentauriHazardHandler.getFlareIntensity();
        if (intensity <= 0) {
            return;
        }
        
        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        ClientLevel level = (ClientLevel) event.getLevel();
        
        // Set up rendering
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        
        // Calculate sun position (simplified)
        float time = level.getTimeOfDay(event.getPartialTick());
        float sunAngle = time * ((float)Math.PI * 2F);
        float sunX = Mth.cos(sunAngle) * 100.0F;
        float sunY = Mth.sin(sunAngle) * 100.0F;
        
        // Draw flare effect
        drawFlareEffect(poseStack, camera, sunX, sunY, intensity);
        
        // Reset render state
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
    }
    
    private static void drawFlareEffect(PoseStack poseStack, Camera camera, float sunX, float sunY, float intensity) {
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        Matrix4f matrix = poseStack.last().pose();
        
        // Base flare color (white-hot)
        float r = 1.0f;
        float g = 0.8f;
        float b = 0.6f;
        float a = 0.8f * intensity;
        
        // Draw central flare
        int segments = 16;
        float radius = 10.0f * (1.0f + intensity * 2.0f);
        
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, sunX, sunY, -90.0F).color(r, g, b, a).endVertex();
        
        for(int i = 0; i <= segments; ++i) {
            float angle = (float)i * ((float)Math.PI * 2F) / (float)segments;
            float dx = Mth.cos(angle) * radius;
            float dy = Mth.sin(angle) * radius;
            buffer.vertex(matrix, sunX + dx, sunY + dy, -90.0F)
                .color(r, g, b, 0.0F)
                .endVertex();
        }
        
        BufferUploader.drawWithShader(buffer.end());
        
        // Add corona streamers (simplified)
        if (intensity > 0.5f) {
            Random random = new Random(31100L);
            for(int i = 0; i < 8; ++i) {
                float angle = random.nextFloat() * (float)Math.PI * 2.0F;
                float length = 50.0f * intensity;
                float width = 5.0f * intensity;
                
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                
                float x1 = sunX + Mth.cos(angle) * radius;
                float y1 = sunY + Mth.sin(angle) * radius;
                float x2 = x1 + Mth.cos(angle) * length;
                float y2 = y1 + Mth.sin(angle) * length;
                
                float dx = Mth.cos(angle + (float)Math.PI / 2) * width;
                float dy = Mth.sin(angle + (float)Math.PI / 2) * width;
                
                buffer.vertex(matrix, x1 - dx, y1 - dy, -90.0F).color(r, g, b, a).endVertex();
                buffer.vertex(matrix, x1 + dx, y1 + dy, -90.0F).color(r, g, b, a).endVertex();
                buffer.vertex(matrix, x2 + dx, y2 + dy, -90.0F).color(r, g, b, 0.0F).endVertex();
                buffer.vertex(matrix, x2 - dx, y2 - dy, -90.0F).color(r, g, b, 0.0F).endVertex();
                
                BufferUploader.drawWithShader(buffer.end());
            }
        }
    }
}
