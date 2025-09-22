package com.netroaki.chex.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.alpha_centauri.SolarEngineerDrone;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SolarEngineerDroneRenderer extends GeoEntityRenderer<SolarEngineerDrone> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CHEX.MOD_ID, "textures/entity/solar_engineer_drone.png");
    
    public SolarEngineerDroneRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(CHEX.MOD_ID, "solar_engineer_drone")));
    }
    
    @Override
    public ResourceLocation getTextureLocation(SolarEngineerDrone entity) {
        return TEXTURE;
    }
    
    @Override
    public void render(SolarEngineerDrone entity, float entityYaw, float partialTick, PoseStack poseStack, 
                      MultiBufferSource bufferSource, int packedLight) {
        // Add floating animation
        poseStack.pushPose();
        float yOffset = (float) Math.sin((entity.tickCount + partialTick) * 0.1F) * 0.1F;
        poseStack.translate(0, yOffset, 0);
        
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        
        // Add glowing effect when repairing
        if (entity.isRepairing()) {
            float glowIntensity = 0.5F + 0.5F * (float) Math.sin((entity.tickCount + partialTick) * 0.5F);
            VertexConsumer glowBuffer = bufferSource.getBuffer(RenderType.eyes(TEXTURE));
            
            // Get the model and render with glow effect
            BakedGeoModel model = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity));
            this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity));
            
            // Render with additive blending for glow effect
            this.actuallyRender(
                poseStack, entity, model, 
                RenderType.eyes(TEXTURE), 
                bufferSource, glowBuffer, 
                false, partialTick, 
                packedLight, 0xFFFFFFFF
            );
        }
        
        poseStack.popPose();
    }
    
    @Override
    public void preRender(PoseStack poseStack, SolarEngineerDrone animatable, BakedGeoModel model, 
                         MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, 
                         float partialTick, int packedLight, int packedOverlay, float red, float green, 
                         float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, 
                       packedLight, packedOverlay, red, green, blue, alpha);
        
        // Add rotation animation
        float rotation = (animatable.tickCount + partialTick) * 2.0F;
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotation));
    }
}
