package com.netroaki.chex.client.model.kepler;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.kepler.VerdantColossus;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class VerdantColossusModel extends GeoModel<VerdantColossus> {
    private static final ResourceLocation MODEL = new ResourceLocation(CHEX.MOD_ID, "geo/verdant_colossus.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CHEX.MOD_ID, "textures/entity/verdant_colossus.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(CHEX.MOD_ID, "animations/verdant_colossus.animation.json");
    
    @Override
    public ResourceLocation getModelResource(VerdantColossus animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(VerdantColossus animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(VerdantColossus animatable) {
        return ANIMATION;
    }
    
    @Override
    public void setCustomAnimations(VerdantColossus entity, long instanceId, AnimationState<VerdantColossus> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        
        // Get the head bone for looking at the target
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone body = this.getAnimationProcessor().getBone("body");
        
        if (head != null) {
            EntityModelData entityData = animationState.getData(EntityModelData.class);
            
            // Make the head look at the target
            head.setRotX(entityData.headPitch() * ((float)Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float)Math.PI / 180F));
            
            // Add slight body movement
            if (body != null) {
                float limbSwingAmount = entity.animationPosition - entity.animationSpeed * (1.0F - animationState.getPartialTick());
                float bodyRotation = Math.min(0.1F, Mth.cos(limbSwingAmount * 0.6662F) * 1.4F * 0.5F);
                body.setRotX(bodyRotation);
            }
        }
    }
}
