package com.netroaki.chex.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.netroaki.chex.entity.aqua.LuminfishEntity;
import net.minecraft.client.model.CodModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class LuminfishRenderer extends MobRenderer<LuminfishEntity, CodModel<LuminfishEntity>> {
    private static final ResourceLocation LUMINFISH_TEXTURE = new ResourceLocation("chex", "textures/entity/luminfish.png");
    private static final ResourceLocation LUMINFISH_GLOW = new ResourceLocation("chex", "textures/entity/luminfish_glow.png");

    public LuminfishRenderer(EntityRendererProvider.Context context) {
        super(context, new CodModel<>(context.bakeLayer(ModelLayers.COD)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(LuminfishEntity entity) {
        return LUMINFISH_TEXTURE;
    }

    @Override
    protected void setupRotations(LuminfishEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * Mth.sin(0.6F * ageInTicks);
        poseStack.mulPose(Axis.YP.rotationDegrees(f));
        
        if (!entity.isInWater()) {
            poseStack.translate(0.1F, 0.1F, -0.1F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }

    @Override
    protected void scale(LuminfishEntity entity, PoseStack poseStack, float partialTickTime) {
        // Slightly smaller than a regular fish
        float scale = 0.8F;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    protected float getBob(LuminfishEntity entity, float partialTicks) {
        return entity.isInWater() ? 
            super.getBob(entity, partialTicks) * 1.2f : 
            super.getBob(entity, partialTicks);
    }
}
