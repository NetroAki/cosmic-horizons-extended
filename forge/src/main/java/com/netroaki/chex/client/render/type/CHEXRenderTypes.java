package com.netroaki.chex.client.render.type;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.netroaki.chex.CHEX;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CHEXRenderTypes extends RenderType {
  // Prevent instantiation
  private CHEXRenderTypes(
      String name,
      VertexFormat format,
      VertexFormat.Mode mode,
      int bufferSize,
      boolean affectsCrumbling,
      boolean sortOnUpload,
      Runnable setupState,
      Runnable clearState) {
    super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    throw new UnsupportedOperationException("This class should not be instantiated");
  }

  // Custom render type for crystal blocks with refraction
  public static final RenderType CRYSTAL_RENDER_TYPE =
      createCrystalRenderType("crystal_render_type");

  private static RenderType createCrystalRenderType(String name) {
    RenderType.CompositeState state =
        RenderType.CompositeState.builder()
            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
            .setTextureState(
                new RenderStateShard.TextureStateShard(
                    new ResourceLocation(CHEX.MOD_ID, "textures/block/crystal_refraction.png"),
                    false, // blur
                    true // mipmap
                    ))
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setCullState(NO_CULL)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .setWriteMaskState(COLOR_WRITE)
            .createCompositeState(true);

    return RenderType.create(
        name, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, state);
  }
}
