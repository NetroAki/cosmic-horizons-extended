package com.netroaki.chex.client.render.kepler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.model.kepler.VerdantColossusModel;
import com.netroaki.chex.entity.kepler.VerdantColossus;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VerdantColossusRenderer extends GeoEntityRenderer<VerdantColossus> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CHEX.MOD_ID, "textures/entity/verdant_colossus.png");
    private static final ResourceLocation ENRAGED_TEXTURE = new ResourceLocation(CHEX.MOD_ID, "textures/entity/verdant_colossus_enraged.png");
    
    public VerdantColossusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VerdantColossusModel());
        this.shadowRadius = 1.2f;
    }
    
    @Override
    public ResourceLocation getTextureLocation(VerdantColossus entity) {
        return entity.isEnraged() ? ENRAGED_TEXTURE : TEXTURE;
    }
    
    @Override
    public void render(VerdantColossus entity, float entityYaw, float partialTick, 
                      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Scale up the model
        float scale = 2.0f;
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        
        // Add floating animation
        float yOffset = (float) Math.sin((entity.tickCount + partialTick) * 0.1F) * 0.1F;
        poseStack.translate(0, yOffset, 0);
        
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        
        // Add glowing effect when enraged
        if (entity.isEnraged()) {
            float glowIntensity = 0.5f + 0.5f * (float) Math.sin((entity.tickCount + partialTick) * 0.5f);
            VertexConsumer glowBuffer = bufferSource.getBuffer(RenderType.eyes(ENRAGED_TEXTURE));
            
            // Get the model and render with glow effect
            BakedGeoModel model = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity));
            this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity));
            
            // Render with additive blending for glow effect
            this.actuallyRender(
                poseStack, entity, model, 
                RenderType.eyes(ENRAGED_TEXTURE), 
                bufferSource, glowBuffer, 
                false, partialTick, 
                packedLight, 0xFFFFFFFF
            );
        }
        
        poseStack.popPose();
    }
    
    @Override
    protected int getBlockLightLevel(VerdantColossus entity, BlockPos pos) {
        // Make the boss glow slightly
        return 15;
    }
}
