package mod.azure.azurelib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

/**
 * Compatibility shim for older AzureLib code paths that still reference the legacy GeoLayerRenderer
 * class. The modern library exposes {@link GeoRenderLayer}; this adapter preserves the old API
 * surface so existing render layers continue compiling.
 */
public abstract class GeoLayerRenderer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

  protected GeoLayerRenderer(GeoRenderer<T> renderer) {
    super(renderer);
  }

  public abstract void render(
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      int packedLight,
      T animatable,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch);

  @Override
  public void render(
      PoseStack poseStack,
      T animatable,
      BakedGeoModel model,
      RenderType renderType,
      MultiBufferSource bufferSource,
      VertexConsumer buffer,
      float partialTick,
      int packedLight,
      int packedOverlay) {
    render(
        poseStack,
        bufferSource,
        packedLight,
        animatable,
        0.0F,
        0.0F,
        partialTick,
        0.0F,
        0.0F,
        0.0F);
  }
}
