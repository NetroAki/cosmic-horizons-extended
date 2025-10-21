package com.netroaki.chex.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.model.SpiceGathererModel;
import com.netroaki.chex.entity.arrakis.SpiceGathererEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class SpiceGathererRenderer
    extends MobRenderer<SpiceGathererEntity, SpiceGathererModel<SpiceGathererEntity>> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/entity/arrakis/spice_gatherer.png");

  public SpiceGathererRenderer(EntityRendererProvider.Context context) {
    this(context, new SpiceGathererModel<>(context.bakeLayer(ModelLayers.VILLAGER)), 0.5F);
    this.addLayer(
        new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
  }

  public SpiceGathererRenderer(
      EntityRendererProvider.Context context,
      SpiceGathererModel<SpiceGathererEntity> model,
      float shadowRadius) {
    super(context, model, shadowRadius);
  }

  @Override
  public ResourceLocation getTextureLocation(SpiceGathererEntity entity) {
    return TEXTURE;
  }

  @Override
  protected void scale(SpiceGathererEntity entity, PoseStack poseStack, float partialTickTime) {
    // Slightly smaller than a regular villager
    float scale = 0.9375F;
    poseStack.scale(scale, scale, scale);
  }
}
