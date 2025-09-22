package com.netroaki.chex.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.netroaki.chex.world.environment.ArrakisEnvironmentHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArrakisSkyRenderer extends DimensionSpecialEffects {
    private static final float[] SUNSET_COLORS = new float[4];
    private static final ArrakisSkyRenderer INSTANCE = new ArrakisSkyRenderer();
    
    public ArrakisSkyRenderer() {
        super(Float.NaN, true, SkyType.NORMAL, false, false);
    }
    
    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        // TODO: Replace with actual Arrakis dimension ID
        // event.register(new ResourceLocation(CHEX.MOD_ID, "arrakis"), INSTANCE);
    }
    
    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) {
        // Make the fog more orange/red
        return biomeFogColor.multiply(
            daylight * 0.94F + 0.06F,
            daylight * 0.94F + 0.06F,
            daylight * 0.91F + 0.09F
        );
    }
    
    @Override
    public boolean isFoggyAt(int x, int y) {
        return ArrakisEnvironmentHandler.isDustStormActive();
    }
    
    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        if (isFoggy) {
            return false;
        }
        
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        
        // Calculate time of day (0-1)
        float time = level.getTimeOfDay(partialTick);
        float timeWeight = Mth.cos(time * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        timeWeight = Mth.clamp(timeWeight, 0.0F, 1.0F);
        
        // Base sky color (reddish-orange)
        float r = 0.9F;
        float g = 0.5F;
        float b = 0.3F;
        
        // Adjust for time of day
        float dayNightFactor = 1.0F - (Mth.cos(time * ((float)Math.PI * 2F)) * 2.0F + 0.2F);
        dayNightFactor = Mth.clamp(dayNightFactor, 0.0F, 1.0F);
        
        // Apply dust storm effects
        float dustStormFactor = ArrakisEnvironmentHandler.getDustStormIntensity();
        if (dustStormFactor > 0) {
            // Make sky darker during dust storms
            float dustDarken = 1.0F - dustStormFactor * 0.7F;
            r *= dustDarken;
            g *= dustDarken * 0.8F;
            b *= dustDarken * 0.6F;
        }
        
        // Draw sky gradient
        RenderSystem.setShaderColor(r, g * dayNightFactor, b * dayNightFactor, 1.0F);
        
        // Render sky box (simplified for example)
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        
        // Draw sky gradient
        // (Implementation depends on your rendering needs)
        
        // Draw sun (larger and more intense)
        if (time < 0.5F) {
            float sunSize = 30.0F * (1.0F + dustStormFactor * 0.5F);
            // Draw sun implementation
        }
        
        BufferUploader.drawWithShader(bufferbuilder.end());
        
        // Reset render state
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        
        return true;
    }
    
    @Override
    public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f projectionMatrix) {
        // Custom cloud rendering for Arrakis
        // Can be used to make dust clouds during storms
        return false;
    }
}
